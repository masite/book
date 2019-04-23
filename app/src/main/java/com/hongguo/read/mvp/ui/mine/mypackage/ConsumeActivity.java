package com.hongguo.read.mvp.ui.mine.mypackage;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.adapter.ConsumeAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.mypackage.ConsumeContractor;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.read.mvp.presenter.mine.mypackage.ConsumePresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ConsumeActivity extends ActivityEx implements ConsumeContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    ConsumePresenter          mConsumePresenter;

    @BindView(R.id.design_refresh)
    DesignRefreshRecyclerView mDesignRefresh;
    private List<ConsumeBean.DataBean> mItems;
    private ConsumeAdapter mConsumeAdapter;


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ConsumeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_consume;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("消费记录");
        mItems = new ArrayList<>();
        mConsumeAdapter = new ConsumeAdapter(mContext, mItems);
        mDesignRefresh.setAdapter(mConsumeAdapter);

        bindRefresh(mDesignRefresh);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("您近期没有消费记录喔~","去书城看看");
        bindLoadingView(loadingHelper, mDesignRefresh, 0);

        mConsumePresenter.bingView(this);
        mConsumePresenter.loading();
    }


    @ViewMethod
    public void setItems(List<ConsumeBean.DataBean> items) {
        mConsumeAdapter.setDataBeans(items);
        mConsumeAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        if(loadingStatus == BaLoadingView.LoadingStatus.RESULT_NULL){
            MainActivity.toActivity(mContext, 1);
            finish();
            return;
        }
        mConsumePresenter.loading();
    }
}