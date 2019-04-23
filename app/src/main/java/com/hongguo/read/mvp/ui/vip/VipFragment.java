package com.hongguo.read.mvp.ui.vip;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.TabLayoutUtils;
import com.hongguo.common.widget.refresh.DesignRefreshLayout;
import com.hongguo.common.widget.skin.ISkinUpdate;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.CommonPagerAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.PayCheckEvent;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.eventbus.VIPStoreRefreshEvent;
import com.hongguo.read.eventbus.VIPStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.mine.MineContractor;
import com.hongguo.read.mvp.contractor.mine.recharge.RechargeContractor;
import com.hongguo.read.mvp.contractor.vip.VipContractor;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.mine.recharge.RechargePresenter;
import com.hongguo.read.mvp.presenter.vip.VipPresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.read.mvp.ui.mine.recharge.PayUtils;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.read.widget.paydialog.VipPayDialog;
import com.hongguo.read.widget.recharge.RechargeConfirmDialog;
import com.hongguo.read.widget.recharge.RechargeRequeryDialog;
import com.hongguo.common.widget.skin.loader.SkinManager;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class VipFragment extends FragmentEx implements VipContractor.IView, MineContractor.IView, RechargeContractor.IView, VipPayDialog.VipDialogistener, RechargeRequeryDialog.RechargeRequeryListener, RechargeConfirmDialog.RechargeConfirmListener, ISkinUpdate, LoadingView.LoadingViewClickListener {

    private static final String INTENT_SHOW_TITLE = "intent_show_title";

    @Inject
    VipPresenter      mVipPresenter;
    @Inject
    MinePresenter     mMinePresenter;
    @Inject
    RechargePresenter mRechargePresenter;

    @BindView(R.id.user_avatar)
    ImageView           mUserAvatar;
    @BindView(R.id.vip_avatar)
    ImageView           mVipAvatar;
    @BindView(R.id.nick_name)
    TextView            mNickName;
    @BindView(R.id.vip_describe)
    TextView            mVipDescribe;
    @BindView(R.id.open_vip)
    TextView            mOpenVip;
    @BindView(R.id.vip_tab)
    TabLayout           mVipTab;
    @BindView(R.id.vip_pager)
    ViewPager           mVipPager;
    @BindView(R.id.design_refresh)
    DesignRefreshLayout mDesignRefresh;

    @BindView(R.id.vip_toolbar)
    ConstraintLayout mVipTitle;

    private VipPayDialog          mVipPayDialog;
    private boolean               mCurrentUserIsVip;
    private boolean               mCurrentUserIsSvip;
    private String                mTimeOut;
    private RechargeRequeryDialog mRechargeRequeryDialog;
    private RechargeConfirmDialog mRechargeConfirmDialog;
    private CommonPagerAdapter    mCommonPagerAdapter;
    private PayUtils mPayUtils;

    public static VipFragment getInstance(boolean showTitle) {
        VipFragment vipFragment = new VipFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(INTENT_SHOW_TITLE, showTitle);
        vipFragment.setArguments(arguments);
        return vipFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_vip;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        if (getArguments() != null) {
            boolean showTitle = getArguments().getBoolean(INTENT_SHOW_TITLE, true);
            if (!showTitle) {
                mVipTitle.setVisibility(View.GONE);
            }
        }
        mCommonPagerAdapter = new CommonPagerAdapter(getChildFragmentManager(), SuperVipStoreFragment.class, NormalVipStoreFragment.class);
        mVipPager.setAdapter(mCommonPagerAdapter);

        mVipTab.setupWithViewPager(mVipPager);
        mVipTab.getTabAt(1).setText("包月会员");
        mVipTab.getTabAt(0).setText("超级会员");
        TabLayoutUtils.setTabLine(mVipTab, 32, 32);

        mVipPayDialog = new VipPayDialog(mContext);
        mVipPayDialog.setVipDialogistener(this);
        mRechargeRequeryDialog = new RechargeRequeryDialog(mContext);
        mRechargeRequeryDialog.setRechargeRequeryListener(this);
        mRechargeConfirmDialog = new RechargeConfirmDialog(mContext);
        mRechargeConfirmDialog.setRechargeConfirmListener(this);
        mDesignRefresh.setOnRefreshListener(this);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mDesignRefresh, 0);

        mVipPresenter.bingView(this);
        mVipPresenter.loading();
        mMinePresenter.bingView(this);
        mMinePresenter.loading();
        mRechargePresenter.bingView(this);
        mRechargePresenter.loading();

        initPage();

        SkinManager.getInstance().attach(this);

        //手动更改个别皮肤信息
        onThemeUpdate();
    }

    private void initPage() {
        mVipPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int page) {
                changeVipInfo();
            }
        });
    }


    /**************************支付对话框相关**************************************/
    @OnClick(R.id.open_vip)
    void openVip() {
        mVipPayDialog.show();
        mVipPresenter.openVip(mVipPager.getCurrentItem());
    }

    @ViewMethod
    public void setVipPayDialogInfo(List<RechargeBean.DataBean> dialogInfo) {
        mVipPayDialog.setVipPayList(dialogInfo);
    }

    @ViewMethod
    public void setPayDialogPayType(boolean weixin, boolean alipay) {
        mVipPayDialog.setPayTypeInfo(weixin, alipay);
    }

    /**********************用户信息 start*********************************/
    @Override
    public void setUserInfo(UserInfoBean.DataBean userInfo) {
        ImageLoadUtils.loadCircleUrl(mUserAvatar, userInfo.avatar);
        mNickName.setText(userInfo.nickName);
    }

    @Override
    public void setUserIsVip(String timeout) {
        mVipAvatar.setVisibility(View.VISIBLE);
        mCurrentUserIsSvip = false;
        mCurrentUserIsVip = true;
        mTimeOut = timeout;
        changeVipInfo();
    }

    @Override
    public void setUserIsSVip(String timeout) {
        mVipAvatar.setVisibility(View.VISIBLE);
        mCurrentUserIsSvip = true;
        mTimeOut = timeout;
        changeVipInfo();
    }

    @Override
    public void setUserNormal() {
        mVipAvatar.setVisibility(View.GONE);
        mCurrentUserIsSvip = false;
        mCurrentUserIsVip = false;
        changeVipInfo();
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {

    }

    /**********************用户信息 end*********************************/
    /**
     * {@link com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter}
     * {@link VipFragment}
     *
     * @param updateUserInfo
     */
    @Subscribe
    public void onEvent(UpdateUserInfo updateUserInfo) {
        mMinePresenter.loading();
    }

    /**
     * 更改vip状态
     */
    private void changeVipInfo() {
        int position = mVipPager.getCurrentItem();
        if (mCurrentUserIsSvip) {
            svipDescribe();
        } else if (mCurrentUserIsVip) {
            vipDescribe();
        } else {
            normalDiscribe(position);
        }
    }

    private void svipDescribe() {
        Drawable rightDrawable;
        mOpenVip.setText("续费");
        mVipDescribe.setText("到期时间：" + mTimeOut);
        mNickName.setTextColor(0xffc28500);
        rightDrawable = getResources().getDrawable(R.mipmap.ic_svip_mark);
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        mNickName.setCompoundDrawablesRelative(null, null, rightDrawable, null);
    }

    private void vipDescribe() {
        Drawable rightDrawable;
        mOpenVip.setText("升级为SVIP");
        mVipDescribe.setText("点亮超级会员标识，原创书籍免费看");
        mVipAvatar.setVisibility(View.VISIBLE);
        mNickName.setTextColor(0xffc28500);
        rightDrawable = getResources().getDrawable(R.mipmap.ic_vip_mark);
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        mNickName.setCompoundDrawablesRelative(null, null, rightDrawable, null);
    }

    private void normalDiscribe(int position) {
        mNickName.setCompoundDrawablesRelative(null, null, null, null);
        if (position == 0) {
            mOpenVip.setText("开通SVIP");
            mVipAvatar.setVisibility(View.GONE);
            mNickName.setTextColor(0xff666666);
            mVipDescribe.setText("点亮超级会员标识，原创书籍免费看");
        } else {
            mOpenVip.setText("开通VIP");
            mVipAvatar.setVisibility(View.GONE);
            mNickName.setTextColor(0xff666666);
            mVipDescribe.setText("点亮会员标识，即享畅阅无限");
        }
    }

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


    /******************************支付过程中回调 start*****************************************/
    /**参见 {@link com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity}**/
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
        if (mVipPager.getCurrentItem() == 1) {
            StatisticsUtils.vipRechargeClick(MathTypeParseUtils.string2Int(payType));
            mRechargePresenter.setRechargeType(Constants.STATISTICS_PAY_TYPE.VIP_RECHARGE);
        } else {
            StatisticsUtils.svipRechargeClick(MathTypeParseUtils.string2Int(payType));
            mRechargePresenter.setRechargeType(Constants.STATISTICS_PAY_TYPE.SVIP_RECHARGE);
        }
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

    /**
     * 刷新控件
     */
    @Override
    public void onRefresh() {
        Fragment fragment = mCommonPagerAdapter.getFragments().get(mVipPager.getCurrentItem());
        EventBus.getDefault().post(new VIPStoreRefreshEvent(fragment));
        mMinePresenter.loading();
    }

    /**
     * 刷新成功
     * {@link com.hongguo.read.mvp.presenter.vip.NormalVipStorePresenter}
     * {@link com.hongguo.read.mvp.presenter.vip.SuperVipStorePresenter}
     *
     * @param storeRefreshSuccessEvent
     */
    @Subscribe
    public void onEvent(VIPStoreRefreshSuccessEvent storeRefreshSuccessEvent) {
        mDesignRefresh.setRefreshing(false);
    }

    @Override
    public void onThemeUpdate() {
        if (mVipTab != null) {
            mVipTab.setSelectedTabIndicatorColor(SkinResourcesUtils.getColor(R.color.colorPrimary));
            mVipTab.setTabTextColors(0xff999999, SkinResourcesUtils.getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mVipPresenter.loading();
        mMinePresenter.loading();
        mRechargePresenter.loading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
    }

    @Override
    public boolean backPress() {
        if (mPayUtils != null && mPayUtils.onBackPress()) {
            return true;
        }
        return super.backPress();
    }
}
