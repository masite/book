package com.hongguo.read.utils.down;

import android.text.TextUtils;

import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class HgReadDownManager {

    /**
     * 免费章节的下载线程(不用考虑同步问题,异步请求即可)
     */
    private static final int THREAD_FREE_NUMBER = 2;

    /**
     * 需要付费的章节下载线程 (防止并发请求，后台出现扣费异常的情况,采用一个线程去处理)
     */
    private static final int THREAD_PAY_NUMBER = 1;

    /**
     * 线程是否关闭
     */
    private boolean mCloseThread = false;

    /**
     * 信号量(来一个任务，就让各个线程去工作)
     */
    private Semaphore mFreeSemaphore = new Semaphore(0);
    private Semaphore mPaySemaphore  = new Semaphore(0);

    /**
     * 记录书籍下载信息
     */
    private List<ChapterHandler>       mChapterHandlers;
    /**
     * 免费章节下载信息
     */
    private List<ChapterBean.Chapters> mFreeChapters;
    /**
     * 需要购买的 章节信息
     */
    private List<ChapterBean.Chapters> mNeedPayChapters;

    /**
     * 正在下载中的 章节信息
     */
    private List<ChapterBean.Chapters> mRunningChapters;

    /**
     * 总 请求管理(请求下载书籍)
     */
    private ChapterDown mChapterDown;

    public HgReadDownManager(ChapterDown chapterDown) {
        mChapterHandlers = new ArrayList<>();
        mFreeChapters = new ArrayList<>();
        mNeedPayChapters = new ArrayList<>();
        mRunningChapters = new ArrayList<>();
        mChapterDown = chapterDown;
    }

    public static HgReadDownManager createDownService(ChapterDown chapterDown) {
        return new HgReadDownManager(chapterDown);
    }

    public void init() {
        for (int i = 0; i < THREAD_FREE_NUMBER; i++) {
            createDownFreeThreads();
        }
        for (int i = 0; i < THREAD_PAY_NUMBER; i++) {
            createDownPayThreads();
        }
    }

    /**
     * 添加某本书的下载监听(弱引用context 防止出现内容泄露)
     *
     * @param bookId
     * @param chapterDownListener
     * @return
     */
    public void bindChapterListener(String bookId, ChapterDownListener chapterDownListener) {
        ChapterHandler chapterHandler = findChapterHandlerById(bookId);
        if (chapterHandler != null) {
            chapterHandler.setListener(chapterDownListener);
        } else {
            chapterHandler = new ChapterHandler();
            chapterHandler.setBookId(bookId);
            chapterHandler.setListener(chapterDownListener);
            mChapterHandlers.add(chapterHandler);
        }
    }

    /**
     * 添加下载任务 (同步化)
     *
     * @param chapters     需要下载的章节
     * @param bookId       当前下载章节的 bookId
     * @param bookType     书籍的渠道(0 我们官方的书籍  1 百度书籍)
     * @param downDescribe 是否下载描述信息(false 购买章节信息并下载)
     */
    public synchronized void addChapterDownLoad(ChapterBean.Chapters chapters, String bookId, int bookType, boolean downDescribe) {

        ChapterHandler chapterHandlerById = findChapterHandlerById(bookId);
        //批量下载任务
        if (chapterHandlerById != null && chapterHandlerById.mDownloadListener.get() != null && chapters.mulitDownTotalNumber != 0) {
            if (chapters.mulitDownIndex == 0) {
                chapterHandlerById.mDownNumber = 0;
            }
        }

        chapters.bookid = bookId;
        chapters.from = bookType;
        int runIndex = mRunningChapters.indexOf(chapters);
        //正在下载中，不做处理
        if (runIndex != -1) {
            return;
        }
        //没有购买需要收费
        if (chapters.isBuy == 0 && !downDescribe) {
            int index = mNeedPayChapters.indexOf(chapters);
            //该任务不在任务中
            if (index == -1) {
                mNeedPayChapters.add(chapters);
                mPaySemaphore.release();
            }
        } else {
            //不需要收费
            int index = mFreeChapters.indexOf(chapters);
            if (index == -1) {
                mFreeChapters.add(chapters);
                mFreeSemaphore.release();
            }
        }
    }

    /**
     * 获取绑定监听
     *
     * @param bookId
     * @return
     */

    private ChapterHandler findChapterHandlerById(String bookId) {
        ChapterHandler chapterHandler = new ChapterHandler();
        chapterHandler.mBookId = bookId;
        //已经绑定过
        int taskIndex = mChapterHandlers.indexOf(chapterHandler);
        if (taskIndex != -1) {
            return mChapterHandlers.get(taskIndex);
        }
        return null;
    }

    /**
     * 获取免费书籍,先取首先压栈的
     *
     * @return
     */
    private synchronized ChapterBean.Chapters getFreeTop() {
        if (mFreeChapters.size() != 0) {
            ChapterBean.Chapters remove = mFreeChapters.remove(mFreeChapters.size() - 1);
            mRunningChapters.add(remove);
            return remove;
        }
        return null;
    }

    /**
     * 获取需要支付的书籍
     *
     * @return
     */
    private synchronized ChapterBean.Chapters getPayTop() {
        if (mNeedPayChapters.size() != 0) {
            ChapterBean.Chapters remove = mNeedPayChapters.remove(mNeedPayChapters.size() - 1);
            mRunningChapters.add(remove);
            return remove;
        }
        return null;
    }

    /**
     * 创建免费下载线程
     */
    private void createDownFreeThreads() {
        Observable.create((ObservableOnSubscribe<ChapterHandler>) emitter -> {
            while (!mCloseThread) {
                mFreeSemaphore.acquire();
                ChapterBean.Chapters freeTop = getFreeTop();
                try {
                    if (freeTop != null) {
                        doDownFree(freeTop, emitter);
                        LogUtils.log(this,"down" + freeTop.chapterName);
                    }
                } catch (Exception e) {
                } finally {
                    if (freeTop != null) {
                        LogUtils.log(this,"release " + freeTop.chapterName);
                        mRunningChapters.remove(freeTop);
                        mFreeSemaphore.release();
                    }
                }
            }
        }).delay(3000, TimeUnit.MICROSECONDS).subscribeOn(Schedulers.newThread()).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<ChapterHandler>() {
            @Override
            public void onNext(ChapterHandler chapterHandler) {
                super.onNext(chapterHandler);
                dealCommonResult(chapterHandler);
            }
        });
    }

    /**
     * 下载总实现(免费)
     *
     * @throws Exception
     */
    private void doDownFree(ChapterBean.Chapters freeTop, ObservableEmitter<ChapterHandler> emitter) throws Exception {
        //任务栈中存在任务
        ChapterHandler chapterHandlerById = findChapterHandlerById(freeTop.bookid);
        //有监听才去执行下载任务
        if (chapterHandlerById != null && chapterHandlerById.mDownloadListener.get() != null) {
            DownResult downResult = mChapterDown.downFreeChapter(freeTop);
            ChapterHandler chapterHandler = copyChapterHandler(freeTop, downResult);
            emitter.onNext(chapterHandler);
        } else {
            LogUtils.log(this, "该页面已经被销毁(免费)，过滤掉该任务--" + freeTop.bookid);
        }
    }

    private synchronized void addDownNumber(ChapterHandler chapterHandler, ChapterBean.Chapters chapters) {
        if (chapters.mulitDownTotalNumber != 0) {
            chapterHandler.mDownNumber++;
        }
    }

    /**
     * 创建需要购买的下载线程
     */
    private void createDownPayThreads() {
        Observable.create((ObservableOnSubscribe<ChapterHandler>) emitter -> {
            while (!mCloseThread) {
                mPaySemaphore.acquire();
                ChapterBean.Chapters payTop = getPayTop();
                try {
                    if (payTop != null) {
                        doDownPay(payTop, emitter);
                        LogUtils.log(this,"pay-down" + payTop.chapterName);
                    }
                } catch (Exception e) {
                } finally {
                    if (payTop != null) {
                        LogUtils.log(this,"pay-release" + payTop.chapterName);
                        mRunningChapters.remove(payTop);
                        mFreeSemaphore.release();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread()).compose(RxJavaUtils.androidTranformer()).subscribe(this::dealCommonResult);
    }

    /**
     * 下载购买章节信息实现
     *
     * @throws Exception
     */

    private void doDownPay(ChapterBean.Chapters payTop, ObservableEmitter<ChapterHandler> emitter) throws Exception {

        ChapterHandler chapterHandlerById = findChapterHandlerById(payTop.bookid);
        //有监听才去执行下载任务
        if (chapterHandlerById != null && chapterHandlerById.mDownloadListener.get() != null) {
            DownResult downResult = mChapterDown.downAndPayChapter(payTop);
            ChapterHandler chapterHandler = copyChapterHandler(payTop, downResult);
            emitter.onNext(chapterHandler);
        } else {
            LogUtils.log(this, "该页面已经被销毁(收费)，过滤掉该任务--" + payTop.bookid);
        }

    }

    /**
     * 一本书批量下载中，用一个实例会出现同步问题，备份出来返回给主线程
     *
     * @param chapters
     * @param downResult
     * @return
     */
    private synchronized ChapterHandler copyChapterHandler(ChapterBean.Chapters chapters, DownResult downResult) {
        ChapterHandler chapterHandlerById = findChapterHandlerById(chapters.bookid);
        if (chapterHandlerById == null) return null;
        addDownNumber(chapterHandlerById, chapters);
        ChapterHandler copy = new ChapterHandler();
        copy.setBookId(chapters.bookid);
        copy.setListener(chapterHandlerById.mDownloadListener.get());
        copy.mTempChapter = chapters;
        copy.mDownNumber = chapterHandlerById.mDownNumber;
        copy.mTempDownResult = downResult;
        return copy;
    }

    /**
     * 下载结果通用处理(处理在主线程中，不用考虑线程问题)
     *
     * @param result
     */
    private void dealCommonResult(ChapterHandler result) {
        try {
            ChapterDownListener chapterDownListener = result.mDownloadListener.get();
            if (chapterDownListener == null) return;
            DownResult tempDownResult = result.mTempDownResult;

            int percent = 0;
            int totalNumber = result.mTempChapter.mulitDownTotalNumber;
            if (totalNumber == 0) {
                percent = -1;
            } else {
                LogUtils.log(this, "下载  " + result.mDownNumber + "/" + totalNumber);
                percent = (int) ((float) (result.mDownNumber) / totalNumber * 100);
            }

            if (tempDownResult.mIsSuccess) {
                chapterDownListener.downSuccess(result.mTempChapter, percent);
            } else {
                chapterDownListener.downError(result.mTempChapter, percent, tempDownResult.mException);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class DownResult {
        boolean   mIsSuccess;
        Exception mException;
    }

    public static class ChapterHandler {

        private WeakReference<ChapterDownListener> mDownloadListener;
        private String                             mBookId;

        ChapterBean.Chapters mTempChapter;
        DownResult           mTempDownResult;
        int                  mDownNumber;

        public void setBookId(String bookId) {
            mBookId = bookId;
        }

        /**
         * 弱引用当前实例，在实例销毁时，下载任务直接取消掉
         *
         * @param chapterDownListener 下载任务监听
         */
        void setListener(ChapterDownListener chapterDownListener) {
            mDownloadListener = new WeakReference<>(chapterDownListener);
        }

        /**
         * 通过相同的bookId 获取 当前实例
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof ChapterHandler && !TextUtils.isEmpty(mBookId) && mBookId.equals(((ChapterHandler) obj).mBookId);
        }
    }

    public interface ChapterDownListener {

        /**
         * 章节下载失败
         *
         * @param ChapterBean  当前下载的章节信息
         * @param percent      当前下载后的进度信息
         * @param errorMessage 下载失败的原因
         */
        void downError(ChapterBean.Chapters ChapterBean, int percent, Exception errorMessage);

        /**
         * 章节下载成功
         *
         * @param ChapterBean 当前下载的章节信息
         * @param percent     当前下载后的进度信息
         */
        void downSuccess(ChapterBean.Chapters ChapterBean, int percent);
    }

}
