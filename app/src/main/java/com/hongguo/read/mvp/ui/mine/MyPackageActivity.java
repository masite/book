package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.mypackage.MyPackageContractor;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.mine.MyPackagePresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.ConsumeActivity;
import com.hongguo.read.mvp.ui.mine.mypackage.RechargeHistoryActivity;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class MyPackageActivity extends MineInfoActivity implements MyPackageContractor.IView{

    @Inject
    MyPackagePresenter mMyPackagePresenter;

    @Inject
    MinePresenter mMinePresenter;

    @BindView(R.id.current_coin)
    TextView mCurrentCoin;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MyPackageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_my_package;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("钱包");
        mMyPackagePresenter.bingView(this);
        mMyPackagePresenter.loading();
        mMinePresenter.bingView(this);
        mMinePresenter.loading();
    }

    @OnClick(R.id.recharge)
    void recharge() {
        StatisticsUtils.collect("充值", "点击充值");
        RechargeActivity.toActivity(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.consume_history)
    void consume() {
        ConsumeActivity.toActivity(mContext);
        StatisticsUtils.collect("我的", "钱包-消费记录");
    }

    @OnClick(R.id.recharge_histroy)
    void rechargeHistory() {
        RechargeHistoryActivity.toActivity(mContext);
        StatisticsUtils.collect("我的", "钱包-充值记录");
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {
        mCurrentCoin.setText(currentCoin + " (+" + giveCoin + ")");

    }
}
