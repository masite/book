package com.hongguo.read.mvp.presenter.book;

import android.content.Context;
import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.eventbus.UpdateBookShelfEvent;
import com.hongguo.read.mvp.contractor.book.BookReaderContractor;
import com.hongguo.read.mvp.model.book.BookReaderBackground;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.ReaderLineRepertory;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.read.repertory.share.BookRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.utils.AppUtils;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class BookReaderPresenter extends BaseImpPresenter<BookReaderContractor.IView> implements BookReaderContractor.IPresenter {

    private List<ChapterBean.Chapters> mChapters;

    @Inject
    @ContextLife
    Context mContext;

    @Inject
    public BookReaderPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        initBookSpanel();
        mView.showWaitDialog(true, "正在加载", null);
    }

    /**
     * 查询阅读记录
     *
     * @param bookId
     * @param bookType
     */
    @PresenterMethod
    public void queryBookReadInfo(String bookId, int bookType) {
        Observable.create((ObservableOnSubscribe<BookReader>) subscriber -> {
            BookReader bookReader = BookReaderRepertory.queryBookById(bookId, bookType);
            subscriber.onNext(bookReader);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<BookReader>(this) {
            @Override
            public void onNext(BookReader reader) {
                super.onNext(reader);
                if (reader == null) {
                    LogUtils.log("bookshelf", "获取阅读记录" + bookId + "----" + bookId);
                    mView.setBookReadInfo(0, 0, false);
                } else {
                    mView.setBookReadInfo(MathTypeParseUtils.string2Int(reader.readChapterId), reader.readPage, reader.isInBookShelf);
                }
            }
        });
    }

    /**
     * 初始化控制页面
     */
    @PresenterMethod
    public void initBookSpanel() {
        initBookCommonInfo();
        initBookSpecialInfo();
        initReadModeInfo();
    }

    private void initReadModeInfo() {
        String speakerName = BookRepertory.getSpeakerName();
        int speed = BookRepertory.getSpeed();
        mView.bookReadInfo(speed, speakerName);
    }


    public void initBookCommonInfo() {
        int animType = BookRepertory.getAnimType();
        String readBackground = BookRepertory.getReadBackground();
        boolean modeNeight = BookRepertory.getModeNeight();
        BookReaderBackground bookReaderBackground = JsonUtils.fromJson(readBackground, BookReaderBackground.class);
        int imageSource = 0;
        if (!TextUtils.isEmpty(bookReaderBackground.backgroundResource)) {
            imageSource = mContext.getResources().getIdentifier(bookReaderBackground.backgroundResource, "mipmap", mContext.getPackageName());
        }
        if (!modeNeight) {
            mView.initBookCommonInfo(animType, bookReaderBackground.backgroundColor, bookReaderBackground.index,imageSource, bookReaderBackground.textColor, false);
        } else {
            mView.initBookCommonInfo(animType, 0xff000000, bookReaderBackground.index, 0, 0xff444444, true);
        }
    }

    public void initBookSpecialInfo() {
        int textSize = BookRepertory.getTextSize();
        int lineType = BookRepertory.getLineType();
        ReaderLineRepertory.BookLineType bookLineType = ReaderLineRepertory.getBookReaderLineTypes().get(lineType);
        mView.initBookSpecialInfo(textSize, lineType, DisplayUtil.sp2px(mContext, bookLineType.lineHeight), DisplayUtil.sp2px(mContext, bookLineType.paragraphHeight));
    }

    //更新数据库已经购买成功
    @PresenterMethod
    public void checkBookBuy(ChapterBean.Chapters chapters, String bookid, int bookType) {
        if (chapters.hasDownTotal && !chapters.mTimeFree) {
            chapters.isBuy = 1;
            ChapterRepertory.setChapterIsBuy(bookid, chapters.chapterId, bookType);
            if (mChapters != null) {
                int index = mChapters.indexOf(chapters);
                if (index != -1) {
                    chapters = mChapters.get(index);
                    chapters.hasDownTotal = true;
                    chapters.mTimeFree = false;
                    chapters.isBuy = 1;
                }
            }
        }
    }

    /**
     * 过滤掉本地存在的文件
     *
     * @param chapters
     * @param bookType
     * @param bookId
     */
    @PresenterMethod
    public void checkDown(List<ChapterBean.Chapters> chapters, int bookType, String bookId) {
        addSubscriptions(Observable.create((ObservableOnSubscribe<List<ChapterBean.Chapters>>) subscriber -> {
            List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
            for (ChapterBean.Chapters chapter : chapters) {
                String filePath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), bookType, bookId, chapter.chapterId);
                if (!FileUtils.fileExist(filePath)) {
                    chaptersList.add(chapter);
                }
            }
            for (int i = 0; i < chaptersList.size(); i++) {
                ChapterBean.Chapters chapters1 = chaptersList.get(i);
                chapters1.mulitDownTotalNumber = chaptersList.size();
                chapters1.mulitDownIndex = 0;
                mView.needDown(chapters1, true);
            }
            subscriber.onNext(chaptersList);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapter -> {
            if (chapter.size() == 0) {
                mView.toastMessage("下载完成");
                mView.allDown();
            }
        }));
    }

    /**
     * 添加书架
     *
     * @param bookId
     * @param bookType
     * @param bookName
     * @param bookCover
     */
    @PresenterMethod
    public void addBookShelf(String bookId, int bookType, String bookName, String bookCover) {
        mView.showWaitDialog(true, "正在添加", null);
        Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            BookReader bookReader = new BookReader();
            bookReader.bookId = bookId;
            bookReader.bookType = bookType;
            bookReader.bookName = bookName;
            bookReader.coverPicture = bookCover;
            BookReaderRepertory.insertOrUpdateBookShelf(bookReader);
            subscriber.onNext(true);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addSubscriptions(d);
            }

            @Override
            public void onNext(Boolean o) {
                super.onNext(o);
                mView.dismissWaitDialogWithoutAnim();
                mView.toastMessage("添加成功");
                mView.finishView();
                EventBus.getDefault().post(new UpdateBookShelfEvent());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.dismissWaitDialogWithoutAnim();
                mView.finishView();
            }
        });

        if (bookType == Constants.BOOK_FROM.FROM_SLEF)
            mApiService.addBookShelf(bookId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
    }

    @PresenterMethod
    public void setChapters(List<ChapterBean.Chapters> chapters) {
        mChapters = chapters;
    }


    /**
     * 保存阅读历史 (间隔1s后添加，防止在书籍返回动画效果卡住)
     *
     * @param bookId
     * @param bookType
     * @param chapterIndex
     * @param currentPage
     * @param readPercent
     */
    public void saveReadInfo(String bookId, int bookType, int chapterIndex, int currentPage, float readPercent, boolean updateBookShelf) {
        RxJavaUtils.delayRun(2000, () -> {
            BookReader bookReader = BookReaderRepertory.queryBookById(bookId, bookType);
            if (bookReader != null) {
                bookReader.readChapterId = chapterIndex + "";
                bookReader.readPage = currentPage;
                bookReader.readProgress = readPercent;
                BookReaderRepertory.insertOrUpdateReader(bookReader, null, updateBookShelf);
                if (updateBookShelf) {
                    EventBus.getDefault().post(new UpdateBookShelfEvent());
                }
            }
        });
    }

    @PresenterMethod
    public void saveSpeed(int speed) {
        BookRepertory.setSpeed(speed);
    }

    @PresenterMethod
    public void saveSpeakerName(String speakerName) {
        BookRepertory.setSpeakerName(speakerName);
    }

    public void changeNeightMode() {
        BookRepertory.setModeNeight(!BookRepertory.getModeNeight());
        initBookCommonInfo();
    }

    @PresenterMethod
    public void bookReport(String bookName, String chapterName, String errorInfo, String otherSuggest, String contactNumber) {

        String content = bookName + "--" + chapterName + "--" + errorInfo + "--" + otherSuggest;
        mApiService.feedBack("none", AppUtils.getVersionName(mContext), content, contactNumber).compose(RxJavaResponseDeal.create(this).widthDialog("反馈中", true).commonDeal(s -> {
            mView.toastMessage("提交成功，感谢您的反馈");
            StatisticsUtils.collect("反馈", "章节-反馈");
            mView.clearErrorReportInfo();
        }));
    }

}