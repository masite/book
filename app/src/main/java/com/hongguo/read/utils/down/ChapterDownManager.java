package com.hongguo.read.utils.down;

import com.hongguo.read.constants.Constants;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.ChapterContentBean;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.utils.down.exception.CoinNotEnoughException;
import com.hongguo.read.utils.down.exception.NetException;
import com.hongguo.common.utils.rxjava.SubscriberImp;

/**
 * Created by losg on 2018/1/17.
 */

public class ChapterDownManager implements IChapterDown {

    @Override
    public HgReadDownManager.DownResult downFreeChapter(ApiService apiService, ChapterBean.Chapters chapter) {
        return downChapter(apiService, chapter, false);
    }

    @Override
    public HgReadDownManager.DownResult downAndBuyChapter(ApiService apiService, ChapterBean.Chapters chapter) {
        return downChapter(apiService, chapter, true);
    }

    private HgReadDownManager.DownResult downChapter(ApiService apiService, ChapterBean.Chapters chapter, boolean buy) {
        HgReadDownManager.DownResult downResult = new HgReadDownManager.DownResult();
        apiService.downChapter(chapter.bookid, chapter.chapterId, "10", buy ? "true" : "").subscribe(new DownSubscriber(downResult, buy, chapter));
        return downResult;
    }


    private class DownSubscriber extends SubscriberImp<ChapterContentBean> {

        private HgReadDownManager.DownResult mDownResult;
        private boolean                      mBuy;
        private ChapterBean.Chapters         mChapter;

        public DownSubscriber(HgReadDownManager.DownResult downResult, boolean buy, ChapterBean.Chapters chapter) {
            mDownResult = downResult;
            mBuy = buy;
            mChapter = chapter;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mDownResult.mIsSuccess = false;
            mDownResult.mException = new NetException();
        }

        @Override
        public void onNext(ChapterContentBean chapterContentBean) {
            super.onNext(chapterContentBean);
            //下载结果是简介
            if (chapterContentBean.data.notRead) {
                LogUtils.log("下载的是简介:" + mChapter.chapterName + "----" + mBuy);
                String bookDownDescribePath = FileManager.getBookDownDescribePath(Constants.BOOK_FROM.FROM_SLEF, mChapter.bookid, mChapter.chapterId);
                FileUtils.writeFile(bookDownDescribePath, chapterContentBean.data.textContent);
                //是购买章节(说明红果币不够了)
                if (mBuy) {
                    mDownResult.mException = new CoinNotEnoughException();
                    mDownResult.mIsSuccess = false;
                } else {
                    mDownResult.mIsSuccess = true;
                }
            } else {
                mChapter.hasDownTotal = true;
                //本章节并没有购买
                if (chapterContentBean.data.notBuy) {
                    mChapter.mTimeFree = true;
                } else {
                    mChapter.mTimeFree = false;
                }
                LogUtils.log("下载成功:" + mChapter.chapterName + "----" + mBuy);
                mDownResult.mIsSuccess = true;
                String bookChapterDownPath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), Constants.BOOK_FROM.FROM_SLEF, mChapter.bookid, mChapter.chapterId);
                FileUtils.writeFile(bookChapterDownPath, chapterContentBean.data.textContent);
            }
        }
    }

}
