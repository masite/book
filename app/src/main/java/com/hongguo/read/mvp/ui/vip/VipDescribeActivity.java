package com.hongguo.read.mvp.ui.vip;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hongguo.read.R;
import com.hongguo.read.adapter.SVIPDescribeAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.PayCheckEvent;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.mvp.contractor.mine.recharge.RechargeContractor;
import com.hongguo.read.mvp.contractor.vip.VipContractor;
import com.hongguo.read.mvp.contractor.vip.VipDescribeContractor;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.mvp.model.vip.SVIPDescribeBean;
import com.hongguo.read.mvp.model.vip.SVIPShareBean;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.presenter.mine.recharge.RechargePresenter;
import com.hongguo.read.mvp.presenter.vip.VipDescribePresenter;
import com.hongguo.read.mvp.presenter.vip.VipPresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.read.mvp.ui.mine.recharge.PayUtils;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.utils.UmengShareHelper;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.widget.paydialog.VipPayDialog;
import com.hongguo.read.widget.recharge.RechargeConfirmDialog;
import com.hongguo.read.widget.recharge.RechargeRequeryDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/12/7.
 *
 * @author losg
 */

@Route(path = AppRouter.VIP_DESCRIBE)
public class VipDescribeActivity extends ActivityEx implements VipDescribeContractor.IView, VipContractor.IView,RechargeContractor.IView,RechargeRequeryDialog.RechargeRequeryListener, RechargeConfirmDialog.RechargeConfirmListener, VipPayDialog.VipDialogistener {

    @Inject
    VipDescribePresenter mVipDescribePresenter;
    @Inject
    VipPresenter         mVipPresenter;
    @Inject
    RechargePresenter    mRechargePresenter;

    @BindView(R.id.vip_describe)
    RecyclerView mVipDescribe;

    private List<VipBean.DataBean> mBooks;
    private SVIPDescribeAdapter    mSvipDescribeAdapter;
    private SVIPShareBean          mSVIPShareBean;

    private RechargeRequeryDialog mRechargeRequeryDialog;
    private RechargeConfirmDialog mRechargeConfirmDialog;
    private VipPayDialog          mVipPayDialog;
    private UmengShareHelper      mUmengShareHelper;
    private PayUtils mPayUtils;

    @Override
    protected int initLayout() {
        return R.layout.activity_vip_describe;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("SVIP特权详情");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVipDescribe.setLayoutManager(linearLayoutManager);
        mBooks = new ArrayList<>();
        mSvipDescribeAdapter = new SVIPDescribeAdapter(mContext, mBooks);
        mVipDescribe.setAdapter(mSvipDescribeAdapter);

        mVipPayDialog = new VipPayDialog(mContext);
        mVipPayDialog.setVipDialogistener(this);
        mRechargeRequeryDialog = new RechargeRequeryDialog(mContext);
        mRechargeRequeryDialog.setRechargeRequeryListener(this);
        mRechargeConfirmDialog = new RechargeConfirmDialog(mContext);
        mRechargeConfirmDialog.setRechargeConfirmListener(this);

        mUmengShareHelper = new UmengShareHelper(this);

        mVipPresenter.bingView(this);
        mVipPresenter.loading();
        mVipDescribePresenter.bingView(this);
        mVipDescribePresenter.loading();
        mRechargePresenter.bingView(this);
        mRechargePresenter.loading();
    }

    @ViewMethod
    public void setSvipDesBeans(List<SVIPDescribeBean.SvipDesBean> svipDesBeans) {
        mSvipDescribeAdapter.setSvipDesBeans(svipDesBeans);
        mSvipDescribeAdapter.notifyChange();
    }

    @ViewMethod
    public void setSvipBooks(List<VipBean.DataBean> books) {
        mBooks.clear();
        mBooks.addAll(books);
        mSvipDescribeAdapter.notifyChange();
    }

    @ViewMethod
    public void setShareInfo(SVIPShareBean shareInfo){
        mSVIPShareBean = shareInfo;
    }

    /**
     * 去分享{@link SVIPDescribeAdapter}
     */
    public void share() {
        if(mSVIPShareBean == null) return;
        mUmengShareHelper.showShare(mSVIPShareBean.sVIPShare.title, mSVIPShareBean.sVIPShare.dirctUrl, mSVIPShareBean.sVIPShare.des, mSVIPShareBean.sVIPShare.imageUrl);
    }

    /**
     * 开通 svip {@link SVIPDescribeAdapter}
     */
    public void openSVip() {
        mVipPayDialog.show();
        mVipPresenter.openVip(0);
    }


    @ViewMethod
    public void setVipPayDialogInfo(List<RechargeBean.DataBean> dialogInfo) {
        mVipPayDialog.setVipPayList(dialogInfo);
    }

    @ViewMethod
    public void setPayDialogPayType(boolean weixin, boolean alipay) {
        mVipPayDialog.setPayTypeInfo(weixin, alipay);
    }

    /******************************支付过程中回调 start*****************************************/
    /**参见 {@link com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity}**/
    /**
     * 支付订单页回调支付状态
     *
     * @param payCheckEvent
     */
    @Subscribe
    public void onEvent(PayCheckEvent payCheckEvent) {
        if(!payCheckEvent.checkSelf(this.getClass())) return;
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
     * 点击支付回调
     * {@link VipPayDialog}
     *
     * @param money
     * @param payType
     * @param ruleId
     * @param sign
     */
    @Override
    public void toPay(String money, String payType, String ruleId, String sign) {
        mRechargePresenter.getPayOrder(payType, money, ruleId, ruleId);
    }

    @Override
    public void requeryOrder() {
        mRechargePresenter.queryOrderStatus(true);
    }

    @Override
    public void postRequestion() {
        FeedBackActivity.toActivity(mContext);
    }

    @Override
    public void rechargeConfirmSuccess() {
        mRechargePresenter.queryOrderStatus(true);
    }

    @Override
    public void rechargeCancel() {
        mRechargePresenter.queryOrderStatus(false);
    }
    /******************************支付过程中dialog 回调  end *****************************************/


    /*******************************RechargeContractor.IView start ***********************************************/
    @Override
    public void setRechargeList(List<RechargeBean.DataBean> rechargeList) {

    }

    @Override
    public void startToPay(String directUrl) {
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
        mPayUtils = PayUtils.startToPay(mContext, directUrl, mVipPayDialog.getPayMethod(), this.getClass());
    }

    @Override
    public void showRechargeStausInfo() {
        mRechargeRequeryDialog.show();
    }

    @Override
    public void rechargeSuccess() {
        //更新用户信息
        EventBus.getDefault().post(new UpdateUserInfo());
    }

    /*******************************RechargeContractor.IView end ***********************************************/


    /***************************分享****************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengShareHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUmengShareHelper.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        mUmengShareHelper.onDestroy();
        super.onDestroy();
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPayUtils != null && mPayUtils.onBackPress()) {
            return;
        }
    }
}
