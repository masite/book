package com.hongguo.read.mvp.presenter.booktype;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.mvp.contractor.booktype.BookTypeDetailContractor;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BookTypeDetailPresenter extends BaseImpPresenter<BookTypeDetailContractor.IView> implements BookTypeDetailContractor.IPresenter {

    private String mTypeId;
    private String mStatus = "s9";
    private String mSort   = "s9";
    private List<BookStoreBookBean.DataBean> mItems;

    @Inject
    public BookTypeDetailPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        mItems = new ArrayList<>();
    }

    @PresenterMethod
    public void queryBookType(String typeid, String status, String sort) {
        mTypeId = typeid;
        mStatus = textEmpty(status) ? "s9" : status;
        mSort = textEmpty(sort) ? "s9" : sort;

        mView.setType(ClassifyRepertory.getSimpleClassifies());
        if(typeid.contains("status")){
            mStatus = typeid.substring(typeid.indexOf("-")+1);
            mTypeId = ClassifyRepertory.getSimpleClassifies().get(0).id;
        }else if(typeid.contains("sort")){
            mTypeId = ClassifyRepertory.getSimpleClassifies().get(0).id;
            mSort = typeid.substring(typeid.indexOf("-")+1);
        }

        queryTypeBooks(mTypeId,mStatus, mSort);
        mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 0);
    }

    @PresenterMethod
    public void queryTypeBooks(String typeid, String status, String sort) {
        mTypeId = typeid;
        mStatus = textEmpty(status) ? "s9" : status;
        mSort = textEmpty(sort) ? "s9" : sort;
        mCurrentPage = 1;
        queryBooks();
    }

    private void queryBooks() {
        mApiService.querySelfBookByType(mTypeId, mStatus, mSort, mCurrentPage, CommonBean.PAGE_SIZE)
                .compose(RxJavaResponseDeal.create(this).loadingTag(1).withLoading(mCurrentPage == 1).commonDeal(books -> {
                    if (mCurrentPage == 1) {
                        mItems.clear();
                    }
                    mItems.addAll(books.data);
                    if (!books.hasMore()) {
                        mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
                    } else {
                        mView.refreshStatus(BaseView.RefreshStatus.LOADING_SUCCESS, null);
                    }
                    mView.setBooks(mItems);
                }));
    }


    @Override
    public void loadingMore() {
        super.loadingMore();
        queryBooks();
    }

    @Override
    public void reLoad() {
        super.reLoad();
        loading();
        queryBooks();
    }
}