package com.hongguo.read.mvp.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.Group;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hongguo.common.utils.EditWatcher;
import com.hongguo.read.R;
import com.hongguo.read.adapter.SearchAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.search.SearchContractor;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.model.search.SearchBean;
import com.hongguo.read.mvp.presenter.search.SearchPresenter;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.AutoLinefeedLayout;
import com.hongguo.read.widget.CommonThreeItem;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.utils.InputMethodUtils;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/11.
 */
@Route(path = AppRouter.BOOK_SEARCH)
public class SearchActivity extends ActivityEx implements SearchContractor.IView, AutoLinefeedLayout.LabelClickListener, LoadingView.LoadingViewClickListener, CommonThreeItem.ItemClickListener {

    @Inject
    SearchPresenter           mSearchPresenter;
    @BindView(R.id.host_search)
    AutoLinefeedLayout        mHostSearch;
    @BindView(R.id.suggest_books)
    CommonThreeItem           mSuggestBooks;
    @BindView(R.id.design_refresh)
    DesignRefreshRecyclerView mDesignRefresh;
    @BindView(R.id.search_text)
    EditText                  mSearchText;
    @BindView(R.id.search_suggest_layer)
    ScrollView                mSearchSuggestLayer;
    @BindView(R.id.suggest_group)
    Group                     mSuggestGroup;
    @BindView(R.id.search_history_group)
    Group                     mSearchHistoryGroup;
    @BindView(R.id.search_history)
    AutoLinefeedLayout        mSearchHistory;


    private List<SearchBean.DataBean> mItems;
    private SearchAdapter             mSearchAdapter;
    private List<RankBean.DataBean> mRankData;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        getSupportActionBar().hide();
        mToolLayer.setVisibility(View.GONE);
        mSuggestBooks.setLines(1);
        mSuggestBooks.setItemClickListener(this);

        mItems = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(mContext, mItems);
        mDesignRefresh.setAdapter(mSearchAdapter);
        mDesignRefresh.setCanRefresh(false);
        bindRefresh(mDesignRefresh);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("暂无搜索结果，换一个试试看", "");
        loadingHelper.setResultNullVisible(false);
        bindLoadingView(loadingHelper, mDesignRefresh, 1);
        loadingHelper.setStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS);

        mHostSearch.setLabelClickListener(this);
        mSearchHistory.setLabelClickListener(this);

        mSearchPresenter.bingView(this);
        mSearchPresenter.loading();

        initEdit();
    }

    private void initEdit() {
        mSearchText.addTextChangedListener(new EditWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(mSearchText.getText().toString())) {
                    mSearchSuggestLayer.setVisibility(View.VISIBLE);
                    mSearchPresenter.queryHistory();
                } else {
                    mSearchSuggestLayer.setVisibility(View.GONE);
                }
            }
        });
    }

    @ViewMethod
    public void setHotSearch(String[] hots) {
        mHostSearch.setLables(hots, true);
    }

    @ViewMethod
    public void setHistory(String[] history) {
        mSearchHistory.setLables(history);
    }

    @ViewMethod
    public void setSuggestBooks(List<RankBean.DataBean> dataBeans) {
        mRankData = dataBeans;
        mSuggestGroup.setVisibility(View.VISIBLE);
        mSuggestBooks.setLoadImage(ImageLoadUtils::loadUrl);
        for (int i = 0; i < 3; i++) {
            RankBean.DataBean dataBean = dataBeans.get(i);
            mSuggestBooks.setImageUrl(i, dataBean.cover);
            mSuggestBooks.setName(i, dataBean.name);
        }
    }

    @ViewMethod
    public void setSearchResult(List<SearchBean.DataBean> dataBeans) {
        mSearchAdapter.setDataBeans(dataBeans);
        mSearchAdapter.notifyChange();
    }

    @OnClick(R.id.change_hots)
    void changeHots() {
        mSearchPresenter.querySuggestHots();
    }

    @OnClick(R.id.search)
    void searchBook() {
        InputMethodUtils.hideInputMethod(this);
        mSearchPresenter.searchBook(mSearchText.getText().toString());
    }

    @OnClick(R.id.view_finish)
    void viewFinish() {
        finish();
    }

    @OnClick(R.id.delete_history)
    void deleteHistory(){
        mSearchPresenter.deleteSearchHistory();
    }

    @ViewMethod
    public void setHistoryGroupVisiable(boolean visiable){
        mSearchHistoryGroup.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    /**
     * 搜索热词点击回调
     *
     * @param view
     * @param labelName
     */
    @Override
    public void labelClick(View view, String labelName) {
        mSearchText.setText(labelName);
        searchBook();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {

    }

    @Override
    public void click(int position) {
        RankBean.DataBean dataBean = mRankData.get(position);
        BookDetailActivity.toActivity(mContext, dataBean.iD, dataBean.bookFrom);
    }
}
