package com.hongguo.read.mvp.ui.reward;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.adapter.RankTopAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.reward.RewardTopContractor;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.presenter.reward.RewardTopPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/14.
 */

public class RewardTopActivity extends ActivityEx implements RewardTopContractor.IView {

    @Inject
    RewardTopPresenter mRewardTopPresenter;

    private List<AwardRankBean.DataBean> mDataBeans;

    public static final String INTENT_BOOK_ID   = "intent_book_id";
    public static final String INTENT_RANK_TYPE = "intent_rank_type";

    @BindView(R.id.design_refresh)
    DesignRefreshRecyclerView mDesignRefresh;

    private String mBookId;
    private String mRankType;
    private RankTopAdapter mRankTopAdapter;

    public static void toActivity(Context context, String bookid, String rankType) {
        Intent intent = new Intent(context, RewardTopActivity.class);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_RANK_TYPE, rankType);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_reward_top;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("打赏榜");
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mRankType = getIntent().getStringExtra(INTENT_RANK_TYPE);

        mDataBeans = new ArrayList<>();
        mRankTopAdapter = new RankTopAdapter(mContext, mRankType,mDataBeans);
        mDesignRefresh.setAdapter(mRankTopAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setResultNullDescribe("暂无打赏记录", "");
        loadingHelper.setResultNullVisible(false);
        bindRefresh(mDesignRefresh);
        bindLoadingView(loadingHelper, mDesignRefresh, 0);

        mRewardTopPresenter.bingView(this);
        mRewardTopPresenter.loading();
        mRewardTopPresenter.queryRewardTop(mBookId, mRankType);
    }

    @ViewMethod
    public void setRankTops(List<AwardRankBean.DataBean> dataBeans) {
        mRankTopAdapter.setDataBeans(dataBeans);
        mRankTopAdapter.notifyChange();
    }

}
