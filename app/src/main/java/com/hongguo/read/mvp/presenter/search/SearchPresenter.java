package com.hongguo.read.mvp.presenter.search;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.mvp.contractor.search.SearchContractor;
import com.hongguo.read.mvp.model.search.SearchBean;
import com.hongguo.read.mvp.model.search.SearchHistoryBean;
import com.hongguo.read.repertory.data.HotsSearchRepertory;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.SearchRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.read.utils.convert.BeanConvert;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class SearchPresenter extends BaseImpPresenter<SearchContractor.IView> implements SearchContractor.IPresenter {

    private int mSelfPageNumber = 0;
    private List<SearchBean.DataBean> mItems;
    private String mSearchKey = "";
    private SearchHistoryBean mSearchHistoryBean;

    @Inject
    public SearchPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        mItems = new ArrayList<>();
        queryRankSuggest();
        querySuggestHots();

        initHistory();
        queryHistory();
    }

    private void initHistory() {
        mSearchHistoryBean = SearchRepertory.getHistory();
    }

    @PresenterMethod
    public void queryHistory() {
        if (mSearchHistoryBean.mHistory.size() == 0) {
            mView.setHistoryGroupVisiable(false);
        } else {
            mView.setHistoryGroupVisiable(true);
        }
        mView.setHistory(mSearchHistoryBean.mHistory.toArray(new String[mSearchHistoryBean.mHistory.size()]));
    }

    @PresenterMethod
    public void deleteSearchHistory() {
        mView.setHistoryGroupVisiable(false);
        mSearchHistoryBean.mHistory.clear();
    }

    @PresenterMethod
    public void querySuggestHots() {

        Disposable subscribe = Observable.create((ObservableOnSubscribe<String[]>) emitter -> emitter.onNext(HotsSearchRepertory.getRandomBooks()))
                .compose(RxJavaUtils.androidTranformer()).subscribe(books -> {
                    mView.setHotSearch(books);
                });
        addSubscriptions(subscribe);
    }

    @PresenterMethod
    public void queryRankSuggest() {
        mApiService.queryRank(RankType.RANK_SUGGEST).compose(RxJavaResponseDeal.create(this).commonDeal(ranks -> {
            mView.setSuggestBooks(ranks.data);
        }));
    }

    @PresenterMethod
    public void searchBook(String key) {
        if (textEmpty(key)) {
            mView.toastMessage("搜索内容不能未空~");
            return;
        }
        StatisticsUtils.search(key);
        mSearchHistoryBean.addHistory(key);
        mView.setSearchResult(new ArrayList<>());
        mView.refreshStatus(BaseView.RefreshStatus.REFRESH_SUCCESS, null);
        mCurrentPage = 1;
        mSelfPageNumber = 0;
        doSearch(key);
    }

    private void doSearch(String key) {
        mSearchKey = key;
        if (mSelfPageNumber != 0 && mCurrentPage > mSelfPageNumber) {
            searchBaiduBooks(key, false);
            return;
        }
        mApiService.searchBooks(key, mCurrentPage, CommonBean.PAGE_SIZE).compose(RxJavaResponseDeal.create(this).withLoading(mCurrentPage == 1).loadingTag(1).commonDeal(books -> {

            mSelfPageNumber = books.pager.total / CommonBean.PAGE_SIZE;
            mSelfPageNumber = (books.pager.total % CommonBean.PAGE_SIZE == 0 ? mSelfPageNumber : mSelfPageNumber + 1);
            mSelfPageNumber = mSelfPageNumber == 0 ? 1 : mSelfPageNumber;

            if (mCurrentPage == 1) {
                mItems.clear();
            }
            mItems.addAll(books.data);
            if (!books.hasMore() && books.data.size() <= CommonBean.PAGE_SIZE && mCurrentPage == 1) {
                mCurrentPage++;
                searchBaiduBooks(key, true);
                return;
            } else {
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_SUCCESS, null);
            }
            mView.setSearchResult(mItems);
        }, false));
    }

    private void searchBaiduBooks(String key, boolean firstPage) {
        if(AppRepertory.getBaiduTimeout()){
            mView.setSearchResult(mItems);
            if(firstPage && mItems.size() == 0){
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 1);
            }else{
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
            return;
        }
        mApiService.searchBaiduBooks(BaiduShuChengUrls.getBaiDuSearchUrl(key, mCurrentPage - mSelfPageNumber, CommonBean.PAGE_SIZE)).compose(RxJavaResponseDeal.create(this).withLoading(firstPage).loadingTag(1).commonDeal(books -> {
            SearchBean searchBooks = new SearchBean();
            searchBooks.currentPage = mCurrentPage - mSelfPageNumber;
            BeanConvert.convertBean(books.result, searchBooks);
            mItems.addAll(searchBooks.data);
            if (!searchBooks.hasMore()) {
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
            if (mCurrentPage - mSelfPageNumber == 1 && mItems.size() == 0) {
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 1);
            }
            mView.setSearchResult(mItems);
        }));
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        doSearch(mSearchKey);
    }

    @Override
    public void reLoad() {
        super.reLoad();
        doSearch(mSearchKey);
    }

    @Override
    public void unBindView() {
        super.unBindView();
        SearchRepertory.setSearchHistory(mSearchHistoryBean);
    }
}