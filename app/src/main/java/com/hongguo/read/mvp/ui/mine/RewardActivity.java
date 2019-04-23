package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.adapter.RewardAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.RewardContractor;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.read.mvp.presenter.mine.RewardPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/6.
 */

public class RewardActivity extends ActivityEx  implements RewardContractor.IView, LoadingView.LoadingViewClickListener {

	@Inject
    RewardPresenter mRewardPresenter;

    @BindView(R.id.design_refresh)
    DesignRefreshRecyclerView mDesignRefresh;
    private List<ConsumeBean.SellListBean.DataBean> mItems;

    private RewardAdapter mRewardAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RewardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_reward;
    }

	@Override
	protected void inject(ActivityComponent activityComponent) {
		activityComponent.inject(this);
	}

	@Override
    protected void initView()  {
        setTitle("我的打赏");

        mItems = new ArrayList<>();
        mRewardAdapter = new RewardAdapter(mContext, mItems);
        mDesignRefresh.setAdapter(mRewardAdapter);

        bindRefresh(mDesignRefresh);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("您近期没有打赏记录喔~","去书城看看");
        bindLoadingView(loadingHelper, mDesignRefresh, 0);

		mRewardPresenter.bingView(this);
		mRewardPresenter.loading();
    }

    @ViewMethod
    public void setRewrads(List<ConsumeBean.SellListBean.DataBean> items){
        mRewardAdapter.setDataBeans(items);
        mRewardAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        if(loadingStatus == BaLoadingView.LoadingStatus.RESULT_NULL){
            MainActivity.toActivity(mContext, 1);
            finish();
            return;
        }
        mRewardPresenter.loading();
    }
}
