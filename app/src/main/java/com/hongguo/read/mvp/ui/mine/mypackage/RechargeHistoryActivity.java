package com.hongguo.read.mvp.ui.mine.mypackage;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.adapter.RechargeHistoryAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.mypackage.RechargeHistoryContractor;
import com.hongguo.read.mvp.model.mine.mypackage.RechargeHistoryBean;
import com.hongguo.read.mvp.presenter.mine.mypackage.RechargeHistoryPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RechargeHistoryActivity extends ActivityEx implements RechargeHistoryContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    RechargeHistoryPresenter mRechargeHistoryPresenter;

    @BindView(R.id.recharge_list)
    DesignRefreshRecyclerView mConsumeList;

    private RechargeHistoryAdapter             mRechargeHistoryAdapter;
    private List<RechargeHistoryBean.DataBean> mItems;
    private LoadingHelper                      mLoadingHelper;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RechargeHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_recharge_history;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("充值记录");

        mItems = new ArrayList<>();
        mRechargeHistoryAdapter = new RechargeHistoryAdapter(mContext, mItems);
        mConsumeList.setAdapter(mRechargeHistoryAdapter);
        mLoadingHelper = new LoadingHelper(mContext);
        mLoadingHelper.setLoadingViewClickListener(this);
        mLoadingHelper.setResultNullDescribe("您近期没有充值记录喔~", "去书城看看");

        bindLoadingView(mLoadingHelper, mConsumeList, 0);
        bindRefresh(mConsumeList);

        mRechargeHistoryPresenter.bingView(this);
        mRechargeHistoryPresenter.loading();
    }

    @ViewMethod
    public void setRechargeHistory(List<RechargeHistoryBean.DataBean> items){
        mRechargeHistoryAdapter.setDataBeanList(items);
        mRechargeHistoryAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        if(loadingStatus == BaLoadingView.LoadingStatus.RESULT_NULL){
            MainActivity.toActivity(mContext, 1);
            finish();
            return;
        }
        mRechargeHistoryPresenter.loading();
    }
}
