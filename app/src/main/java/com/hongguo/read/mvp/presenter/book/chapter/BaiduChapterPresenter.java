package com.hongguo.read.mvp.presenter.book.chapter;

import android.text.TextUtils;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.base.CommonBean;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.constants.FileManager;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.BaiduBookUpdateBean;
import com.hongguo.read.mvp.model.book.chapter.BaiduCapterBean;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.Chapter;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class BaiduChapterPresenter extends BaseImpPresenter<ChapterContractor.IView> implements ChapterContractor.IPresenter {

    @Inject
    public BaiduChapterPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @Override
    public void checkUpate(String bookId, String bookName, String addChapterTime, String bookCover) {
        LogUtils.log("baidu---checkUpate--"+bookId);
        //本地记录与服务器更新时间对比，如果服务器上更新过章节信息 更新本地的章节信息
        String baiduBookUpate = BaiduShuChengUrls.getBaiduBookUpate(bookId);
        mApiService.queryBaiduUpdate(baiduBookUpate).flatMap(booksUpDataInfoBean -> {
            BaiduBookUpdateBean.ResultBean.PandaChapterInfoListBean pandaChapterInfoListBean = booksUpDataInfoBean.result.PandaChapterInfoList.get(0);
            String updateTime = booksUpDataInfoBean.result.PandaChapterInfoList.get(0).LastUpdateTime;
            boolean update;
            if (TextUtils.isEmpty(addChapterTime)) {
                update = true;
            } else {
                update = addChapterTime.compareTo(updateTime) < 0;
            }
            LogUtils.log("baidu---checkUpate-- 是否需要更新"+update);
            if (update)
                queryAllChapter(bookId, bookName, bookCover, updateTime, false);

            return Observable.just(true);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(needUpdate -> {

        }));
    }

    @Override
    public void queryAllChapter(String bookId, String bookName, String bookCover, String updateTime, boolean needLoading) {
        LogUtils.log("baidu---queryAllChapter-- 是否需要更新");
        mApiService.queryBaiduChapter(BaiduShuChengUrls.getBaiDuBookChapterListUrl(bookId, 1, Integer.MAX_VALUE)).flatMap(chapterBean -> {
            List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
            parseChapter(bookId, chapterBean, chaptersList);
            //保存数据库
            BookReader bookReader = new BookReader();
            bookReader.bookId = bookId;
            bookReader.bookName = bookName;
            bookReader.bookType = Constants.BOOK_FROM.FROM_BAIDU;
            bookReader.coverPicture = bookCover;
            bookReader.lastUpdateTime = updateTime;
            //添加章节信息
            List<Chapter> chapters = new ArrayList<>();
            for (ChapterBean.Chapters datum : chaptersList) {
                Chapter chapter = new Chapter();
                chapter.chapterId = datum.chapterId;
                chapter.chapterName = datum.chapterName;
                chapter.coin = datum.coin;
                chapter.isbuy = datum.isBuy == 1;
                chapter.updateTime = updateTime;
                chapters.add(chapter);
                datum.hasDownTotal = FileUtils.fileExist(FileManager.getBookChapterDownPath(UserRepertory.getUserID(), Constants.BOOK_FROM.FROM_SLEF, bookId, datum.chapterId));
            }
            LogUtils.log("更新数据");
            BookReaderRepertory.insertOrUpdateReader(bookReader);
            ChapterRepertory.insertChapters(chapters, bookId, Constants.BOOK_FROM.FROM_BAIDU);
            return Observable.just(chaptersList);
        }).compose(RxJavaResponseDeal.create(this).loadingTag(1).withLoading(needLoading).commonDeal(chapter -> {
            mView.setChapters(chapter);
            if (TextUtils.isEmpty(updateTime)) {
                LogUtils.log("网络获取最新时间");
                updateTimeFromNet(bookId);
            }
        }));
    }

    private void updateTimeFromNet(String bookId) {
        String baiduBookUpate = BaiduShuChengUrls.getBaiduBookUpate(bookId);
        mApiService.queryBaiduUpdate(baiduBookUpate).flatMap(booksUpDataInfoBean -> {
            BaiduBookUpdateBean.ResultBean.PandaChapterInfoListBean pandaChapterInfoListBean = booksUpDataInfoBean.result.PandaChapterInfoList.get(0);
            String updateTime = booksUpDataInfoBean.result.PandaChapterInfoList.get(0).LastUpdateTime;
            ChapterRepertory.updateChapterTime(bookId, Constants.BOOK_FROM.FROM_BAIDU, updateTime);
            LogUtils.log("更新章节时间");
            return Observable.just(true);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(needUpdate -> {

        }));
    }

    @Override
    public void queryChapter(String bookid) {
        LogUtils.log("queryChapter");
        mApiService.queryBaiduChapter(BaiduShuChengUrls.getBaiDuBookChapterListUrl(bookid, mCurrentPage, CommonBean.PAGE_SIZE)).flatMap(baiduCapterBean -> {
            List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
            parseChapter(bookid, baiduCapterBean, chaptersList);
            return Observable.just(chaptersList);
        }).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(chaptersList -> {
            mView.setPageChapters(chaptersList);
        }));
    }

    private void parseChapter(String bookId, BaiduCapterBean baiduChapter, List<ChapterBean.Chapters> chaptersList) {
        for (BaiduCapterBean.ResultBean.PageListBean pageListBean : baiduChapter.result.pageList) {
            ChapterBean.Chapters chapters = new ChapterBean.Chapters();
            chapters.chapterId = pageListBean.chapter_id;
            chapters.chapterName = pageListBean.chapter_name;
            chapters.from = Constants.BOOK_FROM.FROM_BAIDU;
            chapters.isBuy = !TextUtils.isEmpty(pageListBean.zip_url) && pageListBean.coin != 0 ? 1 : 0;
            chapters.coin = pageListBean.coin;
            chaptersList.add(chapters);
        }
    }


}