package com.hongguo.read.mvp.ui.mine;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.constants.Constants;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.refresh.DesignRefreshLayout;
import com.hongguo.read.BuildConfig;
import com.hongguo.read.R;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.mvp.contractor.mine.MineContractor;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.ui.error.ErrorReportActivity;
import com.hongguo.read.mvp.ui.mine.center.UserCenterActivity;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.read.mvp.ui.vip.VipActivity;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.widget.CouponDialog;
import com.losg.library.widget.TransStatusBar;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class MineFragment extends FragmentEx implements MineContractor.IView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    MinePresenter mMinePresenter;

    @BindView(R.id.transStatusBar)
    TransStatusBar      mTransStatusBar;
    @BindView(R.id.user_avatar)
    ImageView           mUserAvatar;
    @BindView(R.id.vip_avatar_mark)
    ImageView           mVipAvatarMark;
    @BindView(R.id.nickname)
    TextView            mNickname;
    @BindView(R.id.current_coin)
    TextView            mCurrentCoin;
    @BindView(R.id.current_give_coin)
    TextView            mCurrentGiveCoin;
    @BindView(R.id.user_login)
    TextView            mUserLogin;
    @BindView(R.id.user_id)
    TextView            mUserId;
    @BindView(R.id.bind_phone)
    TextView            mBindPhone;
    @BindView(R.id.vip)
    TextView            mVip;
    @BindView(R.id.vip_timeout)
    TextView            mVipTimeout;
    @BindView(R.id.my_skin)
    TextView            mMySkin;
    @BindView(R.id.feedback)
    TextView            mFeedback;
    @BindView(R.id.setting)
    TextView            mSetting;
    @BindView(R.id.error_info)
    LinearLayout        mErrorInfo;
    @BindView(R.id.content_view)
    ScrollView          mContentView;
    @BindView(R.id.design_refresh)
    DesignRefreshLayout mDesignRefresh;

    private CouponDialog      mCouponDialog;


    @Override
    protected int initLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mCouponDialog = new CouponDialog(mContext);
        bindRefreshView(mDesignRefresh);
        mDesignRefresh.setOnRefreshListener(this);
        mMinePresenter.bingView(this);
        mMinePresenter.loading();

        if (BuildConfig.DEBUG) {
            mErrorInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 涉及到金额 和 用户信息变动 主动刷新用户信息
     */
    @Override
    public void onResume() {
        super.onResume();
        mMinePresenter.loading();
    }

    /**
     * 用户切换到该页时，主动刷新用户信息
     */
    @Override
    public void showChange(boolean show) {
        //首次进来 fragment 尚未初始化
        if (mMinePresenter != null && show)
            mMinePresenter.loading();
    }

    @ViewMethod
    public void setUserInfo(UserInfoBean.DataBean userInfo) {
        ImageLoadUtils.loadCircleUrl(mUserAvatar, userInfo.avatar);
        mNickname.setText(userInfo.nickName);
        if (UserRepertory.getLoginType().equals(Constants.AUHOR_TYPE.DEVICE_ID)) {
            mUserId.setText("游客ID:" + userInfo.userId);
            mUserLogin.setVisibility(View.VISIBLE);
            mNickname.setVisibility(View.GONE);
        } else {
            mNickname.setVisibility(View.VISIBLE);
            mUserId.setText("ID:" + userInfo.userId);
            mUserLogin.setVisibility(View.GONE);
        }
    }

    @ViewMethod
    public void setUserIsVip(String timeout) {
        Drawable vipMark = getResources().getDrawable(R.mipmap.ic_vip_mark);
        vipMark.setBounds(0, 0, vipMark.getIntrinsicWidth(), vipMark.getIntrinsicHeight());
//        mNickname.setCompoundDrawablesRelative(null, null, vipMark, null);
        mNickname.setTextColor(0xffc28500);
        mVipTimeout.setTextColor(0xffc28500);
        mVipTimeout.setText(timeout);
        mVipTimeout.setBackgroundResource(R.color.base_transparent);
        mVipAvatarMark.setVisibility(View.VISIBLE);
    }

    @ViewMethod
    public void setUserIsSVip(String timeout) {
        Drawable svipMark = getResources().getDrawable(R.mipmap.ic_svip_mark);
        svipMark.setBounds(0, 0, svipMark.getIntrinsicWidth(), svipMark.getIntrinsicHeight());
//        mNickname.setCompoundDrawablesRelative(null, null, svipMark, null);
        mNickname.setTextColor(0xffc28500);
        mVipTimeout.setTextColor(0xffc28500);
        mVipTimeout.setText(timeout);
        mVipAvatarMark.setVisibility(View.VISIBLE);
        mVipTimeout.setBackgroundResource(R.color.base_transparent);
    }

    @ViewMethod
    public void setUserNormal() {
        mNickname.setCompoundDrawablesRelative(null, null, null, null);
        mNickname.setTextColor(0xffc28500);
        mVipTimeout.setTextColor(Color.WHITE);
        mVipTimeout.setText("开通");
        mVipTimeout.setBackgroundResource(R.drawable.bg_round_primary);
        mVipAvatarMark.setVisibility(View.GONE);
    }

    @ViewMethod
    public void setCoins(int currentCoin, int giveCoin) {
        mCurrentCoin.setText(currentCoin + "");
        mCurrentGiveCoin.setText(giveCoin + "");
    }

    @Override
    public void onRefresh() {
        mMinePresenter.loading();
    }

    @OnClick(R.id.my_package)
    void myPackage() {
        StatisticsUtils.mine("我的钱包");
        MyPackageActivity.toActivity(mContext);
    }


    @OnClick(R.id.recharge)
    void recharge() {
        StatisticsUtils.mine("我的充值");
        RechargeActivity.toActivity(mContext);
    }

    @OnClick(R.id.my_reward)
    void reward() {
        StatisticsUtils.mine("我的打赏");
        RewardActivity.toActivity(mContext);
    }

    @OnClick(R.id.my_skin)
    void mySkin() {
        StatisticsUtils.mine("个性皮肤");
        SkinActivity.toActivity(mContext);
    }

    @OnClick(R.id.question)
    void question() {
        StatisticsUtils.mine("常见问题");
        QuestionActivity.toActivity(mContext);
    }

    @OnClick(R.id.feedback)
    void feedBack() {
        StatisticsUtils.mine("意见反馈");
        FeedBackActivity.toActivity(mContext);
    }

    @OnClick(R.id.setting)
    void setting() {
        StatisticsUtils.mine("设置");
        SettingActivity.toActivity(mContext);
    }

    @OnClick(R.id.error_info)
    void errorInfo() {
        ErrorReportActivity.toActivity(mContext);
    }

    @OnClick(R.id.vip_timeout)
    void openVip() {
        VipActivity.toActivity(mContext);
    }

    @OnClick(R.id.user_avatar)
    public void userCenter() {
        UserCenterActivity.toActivity(mContext);
    }

    /**
     * {@link com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter}
     *
     * @param updateUserInfo
     */
    @Subscribe
    public void onEvent(UpdateUserInfo updateUserInfo) {
        mMinePresenter.loading();
    }

    @OnClick(R.id.user_login)
    public void userLogin(){
        AuthorLoginActivity.toActivity(mContext);
    }

    @OnClick(R.id.coupon_layer)
    public void couponLayerClick(){
        mCouponDialog.show();
    }
}
