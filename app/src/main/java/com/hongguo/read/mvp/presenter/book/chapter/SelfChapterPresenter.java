package com.hongguo.read.mvp.presenter.book.chapter;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.base.CommonBean;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.Chapter;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.CheckBookUpdateUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SelfChapterPresenter extends BaseImpPresenter<ChapterContractor.IView> implements ChapterContractor.IPresenter {

    /**
     * @param apiService
     */
    @Inject
    public SelfChapterPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    public void queryChapter(String bookid) {
        mApiService.queryBookChapter(bookid, mCurrentPage, CommonBean.PAGE_SIZE).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(chapter -> {
            mView.setPageChapters(chapter.data);
        }));
    }


    public void queryAllChapter(String bookId, String bookName, String bookCover, String updateTime, boolean needLoading) {
        mApiService.queryBookChapter(bookId, 1, Integer.MAX_VALUE).flatMap(chapterBean -> {
            //保存数据库
            BookReader bookReader = new BookReader();
            bookReader.bookId = bookId;
            bookReader.bookName = bookName;
            bookReader.bookType = Constants.BOOK_FROM.FROM_SLEF;
            bookReader.coverPicture = bookCover;
            bookReader.lastUpdateTime = updateTime;

            //添加章节信息
            List<Chapter> chapters = new ArrayList<>();
            for (ChapterBean.Chapters datum : chapterBean.data) {
                datum.isBuy = datum.isBuy != 1 ? 0 : 1;
                Chapter chapter = new Chapter();
                chapter.chapterId = datum.chapterId;
                chapter.chapterName = datum.chapterName;
                chapter.coin = datum.coin;
                chapter.updateTime = updateTime;
                chapter.isbuy = datum.isBuy == 1;
                chapters.add(chapter);
            }
            BookReaderRepertory.insertOrUpdateReader(bookReader);
            ChapterRepertory.insertChapters(chapters, bookId, Constants.BOOK_FROM.FROM_SLEF);
            return Observable.just(chapterBean);
        }).compose(RxJavaResponseDeal.create(this).loadingTag(1).withLoading(needLoading).commonDeal(chapter -> {
            mView.setChapters(chapter.data);
            if (textEmpty(updateTime)) {
                upDateChapterTime(bookId);
            }
        }));
    }

    private void upDateChapterTime(String bookId) {
        mApiService.queryBookShelfUpdateTime(bookId).compose(RxJavaResponseDeal.create(this).commonDeal(booksUpDataInfoBean -> {
            ChapterRepertory.updateChapterTime(bookId, Constants.BOOK_FROM.FROM_SLEF, booksUpDataInfoBean.data.get(0).latestTime);
        }));
    }


    public void checkUpate(String bookId, String bookName, String addChapterTime, String bookCover) {
        //本地记录与服务器更新时间对比，如果服务器上更新过章节信息 更新本地的章节信息
        mApiService.queryBookShelfUpdateTime(bookId).compose(RxJavaResponseDeal.create(this).commonDeal(booksUpDataInfoBean -> {
            if(booksUpDataInfoBean.data == null || booksUpDataInfoBean.data.size() == 0) return;
            boolean udpate = CheckBookUpdateUtils.bookHasUpdate(booksUpDataInfoBean.data.get(0).latestTime, addChapterTime);
            if (udpate)
                queryAllChapter(bookId, bookName, bookCover, booksUpDataInfoBean.data.get(0).latestTime, false);
        }));
    }
}