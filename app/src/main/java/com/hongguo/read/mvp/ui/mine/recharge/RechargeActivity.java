package com.hongguo.read.mvp.ui.mine.recharge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.read.R;
import com.hongguo.read.adapter.RechargeAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.PayCheckEvent;
import com.hongguo.read.mvp.contractor.mine.recharge.RechargeContractor;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.mine.recharge.RechargePresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.common.router.AppRouter;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.read.widget.recharge.RechargeConfirmDialog;
import com.hongguo.read.widget.recharge.RechargeRequeryDialog;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/5.
 */

@Route(path = AppRouter.USER_RECHAGE)
public class RechargeActivity extends MineInfoActivity implements RechargeContractor.IView, RechargeConfirmDialog.RechargeConfirmListener, RechargeRequeryDialog.RechargeRequeryListener, LoadingView.LoadingViewClickListener {

    @Inject
    RechargePresenter mRechargePresenter;
    @Inject
    MinePresenter     mMinePresenter;


    @BindView(R.id.recharge_list)
    RecyclerView mRechargeList;
    @BindView(R.id.recharge_content)
    LinearLayout mRechargeContent;

    private RechargeAdapter                       mRechargeAdapter;
    private RechargeHeaderHelper                  mRechargeHeaderHelper;
    private List<RechargeBean.DataBean.RulesBean> mItems;
    private RechargeConfirmDialog                 mRechargeConfirmDialog;
    private RechargeRequeryDialog                 mRechargeRequeryDialog;

    @Autowired(name = "key")
    String mRechargeKey;
    private PayUtils mPayUtils;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RechargeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);

        setTitle("充值中心");
        mRechargeConfirmDialog = new RechargeConfirmDialog(mContext);
        mRechargeConfirmDialog.setRechargeConfirmListener(this);
        mRechargeRequeryDialog = new RechargeRequeryDialog(mContext);
        mRechargeRequeryDialog.setRechargeRequeryListener(this);

        mRechargeHeaderHelper = new RechargeHeaderHelper(mContext);
        mRechargeList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mItems = new ArrayList<>();
        mRechargeAdapter = new RechargeAdapter(mContext, mItems);
        mRechargeAdapter.addHeader(mRechargeHeaderHelper.getView());
        mRechargeAdapter.addFooter(View.inflate(mContext, R.layout.view_recharge_tip, null));
        mRechargeList.setAdapter(mRechargeAdapter);

        mMinePresenter.bingView(this);
        mRechargePresenter.bingView(this);
        mRechargePresenter.setRechargeType(Constants.STATISTICS_PAY_TYPE.RECHARGE);
        mRechargePresenter.queryRechargeKey(mRechargeKey);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mRechargeContent, 1);

        mMinePresenter.loading();
        mRechargePresenter.loading();
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {
        mRechargeHeaderHelper.initMoney(currentCoin + "", giveCoin + "");
    }

    @Override
    public void setUserInfo(UserInfoBean.DataBean userInfo) {
        mRechargeHeaderHelper.setUserName(userInfo.nickName);
    }

    @ViewMethod
    public void setRechargeList(List<RechargeBean.DataBean> rechargeList) {
        mItems.clear();
        mItems.addAll(rechargeList.get(0).rules);
        mRechargeAdapter.notifyChange();
    }

    /**
     * 点击下订单
     */
    @OnClick(R.id.recharge_now)
    public void rechargeNow() {
        RechargeBean.DataBean.RulesBean rulesBean = mItems.get(mRechargeAdapter.getCurrentSelectedPosition());
        StatisticsUtils.rechargeClick(rulesBean.value);
        mRechargePresenter.getPayOrder(mRechargeHeaderHelper.getChoosePayMethod(), rulesBean.value + "", rulesBean.id, rulesBean.sign);
    }

    /**
     * 获取订单后跳转支付页面
     *
     * @param directUrl
     */
    @ViewMethod
    public void startToPay(String directUrl) {
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
        mPayUtils = PayUtils.startToPay(mContext, directUrl, mRechargeHeaderHelper.getChoosePayMethod(), getClass());
    }

    /**
     * 支付订单页回调支付状态
     *
     * @param payCheckEvent
     */
    @Subscribe
    public void onEvent(PayCheckEvent payCheckEvent) {
        if (!payCheckEvent.checkSelf(this.getClass())) return;
        dismissWaitDialog();
        if (TextUtils.isEmpty(payCheckEvent.mError)) {
            openPayDuration();
        } else {
            toastMessage(payCheckEvent.mError);
        }
    }

    /**
     * 打开支付后让用户手动触发是否支付完成
     */
    private void openPayDuration() {
        RxJavaUtils.delayRun(500, () -> {
            mRechargeConfirmDialog.show();
        });
    }

    /**
     * 用户点击 确认支付完成回调，查询订单状态
     */
    @Override
    public void rechargeConfirmSuccess() {
        mRechargePresenter.queryOrderStatus(true);
    }

    /**
     * 用户点击 尚未支付(防止用户误点,请求一次)
     */
    @Override
    public void rechargeCancel() {
        mRechargePresenter.queryOrderStatus(false);
    }

    /**
     * 支付成功，刷新红果币
     */
    @ViewMethod
    public void rechargeSuccess() {
        mMinePresenter.loading();
    }

    /**
     * 当没有查询到订单支付成功时，让用户手动去刷新订单信息
     */
    @ViewMethod
    public void showRechargeStausInfo() {
        mRechargeRequeryDialog.show();
    }

    /**
     * 重新请求订单状态
     */
    @Override
    public void requeryOrder() {
        mRechargePresenter.queryOrderStatus(true);
    }

    /**
     * 意见反馈
     */
    @Override
    public void postRequestion() {
        FeedBackActivity.toActivity(mContext);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRechargeHeaderHelper.destory();
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
    }


    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mMinePresenter.loading();
        mRechargePresenter.loading();
        mRechargePresenter.queryRechargeKey(mRechargeKey);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPayUtils != null && mPayUtils.onBackPress()) {
            return;
        }
    }
}
