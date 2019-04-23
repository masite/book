package com.hongguo.read.mvp.presenter.book;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.mvp.contractor.book.BookEndRecommendContractor;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.mvp.presenter.book.detail.BaiduBookDetailPresenter;
import com.hongguo.read.mvp.presenter.book.detail.SelfBookDetailPresenter;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.base.BaseView;

import javax.inject.Inject;

public class BookEndRecommendPresenter extends BaseImpPresenter<BookEndRecommendContractor.IView> implements BookEndRecommendContractor.IPresenter{

    SelfBookDetailPresenter         mSelfBookDetailPresenter;
    BaiduBookDetailPresenter        mBaiduBookDetailPresenter;
    BookDetailContractor.IPresenter mIPresenter;

    private String mBookId;
    private String mBookType;

    @Inject
    public BookEndRecommendPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mSelfBookDetailPresenter = new SelfBookDetailPresenter(mApiService);
        mBaiduBookDetailPresenter = new BaiduBookDetailPresenter(mApiService);
    }

    @Override
    public void bingView(BaseView view) {
        super.bingView(view);
        mSelfBookDetailPresenter.bingView(view);
        mBaiduBookDetailPresenter.bingView(view);
    }

    @PresenterMethod
    public void init(String bookId, int bookType) {
        mBookId = bookId;
        mBookType = bookType + "";
        if (bookType == 0) {
            mIPresenter = mSelfBookDetailPresenter;
        } else {
            mIPresenter = mBaiduBookDetailPresenter;
        }
    }

    @Override
    public void loading() {
        queryBookCommonInfo(mBookId);
        querySimilarBooks(mBookId);
    }

    private void queryBookCommonInfo(String bookId) {
        mIPresenter.queryBookCommonInfo(bookId);
    }

    private void querySimilarBooks(String bookId) {
        mIPresenter.querySimilarBooks(bookId);
    }

}