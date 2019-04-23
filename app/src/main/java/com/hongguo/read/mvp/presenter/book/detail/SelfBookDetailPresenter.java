package com.hongguo.read.mvp.presenter.book.detail;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

public class SelfBookDetailPresenter extends BaseImpPresenter<BookDetailContractor.IView> implements BookDetailContractor.IPresenter {

    public SelfBookDetailPresenter(ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @PresenterMethod
    public void queryBookCommonInfo(String bookid) {
        mApiService.queryBookDetail(bookid).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(bookDetail -> {
            if(bookDetail.data == null) return;
            bookDetail.data.readersStr = BookNumberUtils.formartUserReads(bookDetail.data.readers);
            bookDetail.data.wordsNumber = BookNumberUtils.formartWords(bookDetail.data.words);
            bookDetail.data.statusStr = bookDetail.data.status == 0 ? "状态：连载" : "状态：完结";
            mView.setBookDetailCommonInfo(bookDetail);
        }));
    }

    @PresenterMethod
    public void queryRewardInfos(String bookid) {
        mApiService.queryRewardRank(bookid, RankType.RANK_MONEY_REWARD, 1, 3).compose(RxJavaResponseDeal.create(this).commonDeal(rewardBean -> {
            mView.setRewardTop(rewardBean);
        }));
    }

    @PresenterMethod
    public void queryBookDiscuss(String bookid, String bookType) {
        mApiService.queryBookDiscuss(bookid, bookType, 1, 3).compose(RxJavaResponseDeal.create(this).commonDeal(bookDiscussBean -> {
            mView.setBookDiscussInfo(bookDiscussBean);
        }));
    }

    @PresenterMethod
    public void querySimilarBooks(String bookid) {
        mApiService.querySimilarBooks(bookid).compose(RxJavaResponseDeal.create(this).commonDeal(bookSimilar -> {
            mView.setSimilar(bookSimilar);
        }));
    }

    @PresenterMethod
    public void queryBookAuthor(String bookid) {
        mApiService.queryAuthorBooks(bookid, "bds_forminfo").compose(RxJavaResponseDeal.create(this).commonDeal(booAuthor -> {
            mView.setAuthor(booAuthor);
        }));
    }

}