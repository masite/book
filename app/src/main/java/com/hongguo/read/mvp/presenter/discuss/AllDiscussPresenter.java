package com.hongguo.read.mvp.presenter.discuss;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.discuss.AllDiscussContractor;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import javax.inject.Inject;

public class AllDiscussPresenter extends BaseImpPresenter<AllDiscussContractor.IView> implements AllDiscussContractor.IPresenter {

    private String          mBookId;
    private String          mBookType;
    private BookDiscussBean mBookDiscussBean;

    @Inject
    public AllDiscussPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void queryBookDiscuss(String bookid, String bookType) {
        mBookId = bookid;
        mBookType = bookType;
        mApiService.queryBookDiscuss(bookid, bookType, mCurrentPage, PAGE_SIZE).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(bookDiscussBean -> {
            if (mCurrentPage == 1) {
                mBookDiscussBean = bookDiscussBean;
            } else {
                mBookDiscussBean.data.addAll(bookDiscussBean.data);
            }

            if(mCurrentPage == 1 &&  bookDiscussBean.data.size() == 0){
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
            }

            mView.setBookDiscussInfo(mBookDiscussBean);

            if (mBookDiscussBean.data.size() >= bookDiscussBean.pager.total) {
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
        }));
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryBookDiscuss(mBookId, mBookType);
    }

    @Override
    public void refresh() {
        super.refresh();
        queryBookDiscuss(mBookId, mBookType);
    }

    @Override
    public void reLoad() {
        super.reLoad();
        queryBookDiscuss(mBookId, mBookType);
    }
}