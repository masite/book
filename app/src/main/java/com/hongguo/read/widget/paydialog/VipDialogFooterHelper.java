package com.hongguo.read.widget.paydialog;

import android.content.Context;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.constants.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/10.
 */

public class VipDialogFooterHelper extends BaseViewHelper {

    @BindView(R.id.vip_title)
    TextView mVipTitle;
    @BindView(R.id.weixin_pay)
    TextView mWeixinPay;
    @BindView(R.id.alipay_pay)
    TextView mAlipayPay;

    private String mPayType = Constants.PAY_TYPE.WEI_XIN;

    public VipDialogFooterHelper(Context context) {
        super(context);
        weixinPay();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_vip_pay_footer;
    }

    @OnClick(R.id.weixin_pay)
    void weixinPay() {
        mPayType = Constants.PAY_TYPE.WEI_XIN;
        mWeixinPay.setSelected(true);
        mAlipayPay.setSelected(false);
    }

    @OnClick(R.id.alipay_pay)
    void alipayPay() {
        mPayType = Constants.PAY_TYPE.ALIPAY;
        mAlipayPay.setSelected(true);
        mWeixinPay.setSelected(false);
    }

    public void setPayInfo(boolean weixinCanPay, boolean alipayCanPay) {
        mAlipayPay.setText("支付宝支付");
        mWeixinPay.setText("微信支付");
        if (!weixinCanPay) {
            mWeixinPay.setSelected(false);
            mWeixinPay.setText("微信支付(正在维护中)");
        }
        if (!alipayCanPay) {
            mAlipayPay.setSelected(false);
            mAlipayPay.setText("支付宝支付(正在维护中)");
        }
    }

    public String getPayType() {
        return mPayType;
    }
}
