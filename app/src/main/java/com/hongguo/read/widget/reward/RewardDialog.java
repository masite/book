package com.hongguo.read.widget.reward;

import android.content.Context;
import android.support.constraint.Group;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.constants.Constants;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/2/8.
 */

public class RewardDialog extends BaAnimDialog implements View.OnClickListener {

    public static final int REWARD_MONEY = 0;
    public static final int REWARD_COIN  = 1;

    private int mRewardType = REWARD_MONEY;

    private LinearLayout               mPayChoose;
    private Group                      mCoinGroup;
    private String                     mMoney;
    private RewardPayTypeClickListener mRewardPayTypeClickListener;
    private TextView                   mWeixinPay;
    private TextView                   mAlipay;

    public RewardDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    private void initView() {
        findViewById(R.id.reward_confirm).setOnClickListener(this);
        findViewById(R.id.reward_cancel).setOnClickListener(this);
        mAlipay = (TextView) findViewById(R.id.alipay_pay);
        mAlipay.setOnClickListener(this);
        mWeixinPay = (TextView) findViewById(R.id.weixin_pay);
        mWeixinPay.setOnClickListener(this);

        mPayChoose = (LinearLayout) findViewById(R.id.pay_choose);
        mCoinGroup = (Group) findViewById(R.id.coin_group);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_reward;
    }

    public void setRewardType(int rewardType) {
        mRewardType = rewardType;
        if (mRewardType == REWARD_MONEY) {
            mPayChoose.setVisibility(View.VISIBLE);
            mCoinGroup.setVisibility(View.GONE);
        } else {
            mPayChoose.setVisibility(View.GONE);
            mCoinGroup.setVisibility(View.VISIBLE);
        }
    }

    public void setWeinXinCanUse(boolean canUse) {
        if (!canUse) {
            mWeixinPay.setSelected(false);
            mWeixinPay.setText("微信支付正在维护中");
            mWeixinPay.setEnabled(false);
        } else {
            mWeixinPay.setText("微信");
            mWeixinPay.setEnabled(true);
        }
    }

    public void setAlipayCanUse(boolean canUse) {
        if (!canUse) {
            mAlipay.setSelected(false);
            mAlipay.setText("支付宝支付正在维护中");
            mAlipay.setEnabled(false);
        } else {
            mAlipay.setText("支付宝");
            mAlipay.setEnabled(true);
        }
    }

    public void setMoney(String money) {
        mMoney = money;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reward_confirm:
                rewardConfirm();
                dismiss();
                break;
            case R.id.reward_cancel:
                dismiss();
                break;
            case R.id.alipay_pay:
                rewardMoney(Constants.PAY_TYPE.ALIPAY);
                dismiss();
                break;
            case R.id.weixin_pay:
                rewardMoney(Constants.PAY_TYPE.WEI_XIN);
                dismiss();
                break;
        }
    }

    private void rewardMoney(String payType) {
        if (mRewardPayTypeClickListener != null) {
            mRewardPayTypeClickListener.moneyReward(mMoney, payType);
        }
    }

    private void rewardConfirm() {
        if (mRewardPayTypeClickListener != null) {
            mRewardPayTypeClickListener.coinReward(mMoney);
        }
    }

    public void setRewardPayTypeClickListener(RewardPayTypeClickListener rewardPayTypeClickListener) {
        mRewardPayTypeClickListener = rewardPayTypeClickListener;
    }

    public interface RewardPayTypeClickListener {
        void moneyReward(String money, String payMethod);

        void coinReward(String coin);
    }
}
