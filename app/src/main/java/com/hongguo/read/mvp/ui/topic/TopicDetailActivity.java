package com.hongguo.read.mvp.ui.topic;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.adapter.SearchAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.search.SearchContractor;
import com.hongguo.read.mvp.contractor.topic.TopicDetailContractor;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.model.search.SearchBean;
import com.hongguo.read.mvp.presenter.search.SearchPresenter;
import com.hongguo.read.mvp.presenter.topic.TopicDetailPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/14.
 */

public class TopicDetailActivity extends ActivityEx implements TopicDetailContractor.IView, SearchContractor.IView, LoadingView.LoadingViewClickListener {

    private static final String INTENT_TOPIC_NAME     = "topic_name";
    private static final String INTENT_TOPIC_KEYWORDS = "topic_keywords";

    @Inject
    TopicDetailPresenter mTopicDetailPresenter;

    @Inject
    SearchPresenter mSearchPresenter;

    @BindView(R.id.topic_detail_list)
    DesignRefreshRecyclerView mTopicDetailList;
    private List<SearchBean.DataBean> mItems;
    private SearchAdapter             mSearchAdapter;
    private String                    mSearchKey;

    public static void toActivity(Context context, String topicName, String topicKeyWrods) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(INTENT_TOPIC_NAME, topicName);
        intent.putExtra(INTENT_TOPIC_KEYWORDS, topicKeyWrods);
        context.startActivity(intent);
    }


    @Override
    protected int initLayout() {
        return R.layout.activity_topic_detail;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        mSearchKey = getIntent().getStringExtra(INTENT_TOPIC_KEYWORDS);
        setTitle(getIntent().getStringExtra(INTENT_TOPIC_NAME));

        mItems = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(mContext, mItems);
        mTopicDetailList.setAdapter(mSearchAdapter);
        mTopicDetailList.setCanRefresh(false);
        bindRefresh(mTopicDetailList);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mTopicDetailList, 1);

        mTopicDetailPresenter.bingView(this);
        mTopicDetailPresenter.loading();
        mSearchPresenter.bingView(this);
        mSearchPresenter.loading();

        mSearchPresenter.searchBook(mSearchKey);
    }

    @Override
    public void setHotSearch(String[] hots) {

    }

    @Override
    public void setSuggestBooks(List<RankBean.DataBean> dataBeans) {

    }

    @Override
    public void setSearchResult(List<SearchBean.DataBean> dataBeans) {
        mSearchAdapter.setDataBeans(dataBeans);
        mSearchAdapter.notifyChange();
    }

    @Override
    public void setHistory(String[] history) {

    }

    @Override
    public void setHistoryGroupVisiable(boolean visiable) {

    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mSearchPresenter.searchBook(mSearchKey);
    }
}
