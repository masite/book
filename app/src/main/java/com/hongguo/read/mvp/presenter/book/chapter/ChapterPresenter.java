package com.hongguo.read.mvp.presenter.book.chapter;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.Chapter;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ChapterPresenter extends BaseImpPresenter<ChapterContractor.IView> implements ChapterContractor.IPresenter {

    /**
     * 列表形式   查询本地 -->  存在  去本地数据  --> 检测更新 --> 更新本地
     * 不存在 查询服务器 --> 更新本地
     * <p>
     * 阅读页     查询本地 --> 存在   取本地数据  --> 检测更新 --> 更新本地
     * 不存在 查询服务器(分页 第一页数据) -->查询服务器 -->更新本地
     *
     * @param apiService
     */

    private String                       mBookId;
    private String                       mBookName;
    private String                       mBookBookCover;
    private int                          mBookFrom;
    private ChapterContractor.IPresenter mIPresenter;

    @Inject
    public ChapterPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @PresenterMethod
    public void init(String bookId, int bookType, String bookName, String bookCover) {
        mBookFrom = bookType;
        mBookId = bookId;
        mBookName = bookName;
        mBookBookCover = bookCover;
        if (mBookFrom == Constants.BOOK_FROM.FROM_SLEF) {
            mIPresenter = new SelfChapterPresenter(mApiService);
        } else {
            mIPresenter = new BaiduChapterPresenter(mApiService);
        }
        mIPresenter.bingView(mView);
        checkNative(bookId, mBookFrom);
    }

    @PresenterMethod
    public void checkNative(String bookId, int bookType) {
        mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LOADING, 1);

        Observable.create(new ObservableOnSubscribe<List<ChapterBean.Chapters>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ChapterBean.Chapters>> emitter) throws Exception {
                List<Chapter> chapters = ChapterRepertory.queryAllChapterByBookId(bookId, bookType);
                emitter.onNext(ChapterRepertory.parse(chapters, mBookId, mBookFrom));
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(exist -> {
            mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 1);
            mView.nativeChapters(exist);
        });
    }

    @PresenterMethod
    public void checkUpate(String bookId, String bookName, String addChapterTime, String bookCover) {
        mIPresenter.checkUpate(bookId, bookName, addChapterTime, bookCover);
    }

    @PresenterMethod
    public void queryChapter(String bookid) {
        mIPresenter.queryChapter(bookid);
    }

    @PresenterMethod
    public void queryAllChapter(String bookId, String bookName, String bookCover, String updateTime, boolean needLoading) {
        mIPresenter.queryAllChapter(bookId, bookName, bookCover, updateTime, needLoading);
    }

    @PresenterMethod
    public void refreshChapterLimitType(String bookId, int bookType, int limitType){
        RxJavaUtils.backgroundDeal(new RxJavaUtils.BackgroundDealListener() {
            @Override
            public void run() {
                ChapterRepertory.setChapterLimitType(bookId, bookType,limitType);
            }
        });
    }

    @Override
    public void loading() {

    }

}