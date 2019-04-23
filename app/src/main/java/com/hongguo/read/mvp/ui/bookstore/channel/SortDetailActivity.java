package com.hongguo.read.mvp.ui.bookstore.channel;

import android.content.Context;
import android.content.Intent;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.read.R;
import com.hongguo.read.adapter.RankAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.bookstore.channel.SortDetailContractor;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.SortDetailPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/5/29.
 */

public class SortDetailActivity extends ActivityEx implements SortDetailContractor.IView, LoadingView.LoadingViewClickListener {

    public static final String INTENT_SORT_TYPE  = "intent_sort_type";
    public static final String INTENT_SORT_TITLE = "intent_sort_title";


    @Inject
    SortDetailPresenter mSortDetailPresenter;

    @BindView(R.id.sort_list)
    DesignRefreshRecyclerView mSortList;

    private String                  mSortType;
    private List<RankBean.DataBean> mRanks;
    private RankAdapter             mRankAdapter;

    public static void toActivity(Context context, String sortType, String title) {
        Intent intent = new Intent(context, SortDetailActivity.class);
        intent.putExtra(INTENT_SORT_TYPE, sortType);
        intent.putExtra(INTENT_SORT_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_sort_detail;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra(INTENT_SORT_TITLE));
        mSortType = getIntent().getStringExtra(INTENT_SORT_TYPE);
        mSortList.setCanRefresh(false);
        bindRefresh(mSortList);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("暂无排行数据", "");
        bindLoadingView(loadingHelper, mSortList, 0);

        mRanks = new ArrayList<>();
        mRankAdapter = new RankAdapter(mContext, mRanks);
        mSortList.setAdapter(mRankAdapter);

        mSortDetailPresenter.bingView(this);
        mSortDetailPresenter.setRankType(mSortType);
        mSortDetailPresenter.loading();
    }

    @ViewMethod
    public void setRank(List<RankBean.DataBean> ranks, boolean clear) {
        if (clear)
            mRanks.clear();
        mRanks.addAll(ranks);
        mRankAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mSortDetailPresenter.loading();
    }
}
