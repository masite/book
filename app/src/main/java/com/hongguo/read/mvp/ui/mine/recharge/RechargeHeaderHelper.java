package com.hongguo.read.mvp.ui.mine.recharge;

import android.content.Context;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.constants.Constants;
import com.losg.library.utils.stylestring.StyleString;
import com.losg.library.utils.stylestring.StyleStringBuilder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/5.
 */

public class RechargeHeaderHelper extends BaseViewHelper {

    @BindView(R.id.label_money)
    TextView mLabelMoney;
    @BindView(R.id.label_coin)
    TextView mLabelCoin;
    @BindView(R.id.label_account)
    TextView mLabelAccount;
    @BindView(R.id.account)
    TextView mAccount;
    @BindView(R.id.label_pay_type)
    TextView mLabelPayType;
    @BindView(R.id.weixin_pay)
    TextView mWeixinPay;
    @BindView(R.id.alipay_pay)
    TextView mAlipayPay;
    @BindView(R.id.label_choose_money)
    TextView mLabelChooseMoney;
    @BindView(R.id.message_tip)
    TextView mMessageTip;

    public RechargeHeaderHelper(Context context) {
        super(context);
        mWeixinPay.setSelected(true);
    }

    @Override
    protected int initLayout() {
        return R.layout.view_recharge_header;
    }

    public void initMoney(String money, String giveMoney) {
        StyleStringBuilder styleStringBuilder = new StyleStringBuilder();
        StyleString styleString = new StyleString(money + " (+" + giveMoney + ") ");
        styleString.setForegroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        styleStringBuilder.append(styleString);
        styleStringBuilder.append(" 红果币");
        mLabelCoin.setText(styleStringBuilder.build());
    }

    public void setUserName(String userName) {
        mAccount.setText(userName);
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
            mAlipayPay.setSelected(false);
            mAlipayPay.setText("支付宝支付正在维护中");
            mAlipayPay.setEnabled(false);
        } else {
            mAlipayPay.setText("支付宝");
            mAlipayPay.setEnabled(true);
        }
    }

    @OnClick(R.id.weixin_pay)
    public void chooseWeiXin() {
        mWeixinPay.setSelected(true);
        mAlipayPay.setSelected(false);
    }

    @OnClick(R.id.alipay_pay)
    public void chooseAlipay() {
        mWeixinPay.setSelected(false);
        mAlipayPay.setSelected(true);
    }

    public String getChoosePayMethod() {
        if (mWeixinPay.isSelected()) return Constants.PAY_TYPE.WEI_XIN;
        return Constants.PAY_TYPE.ALIPAY;
    }

}
