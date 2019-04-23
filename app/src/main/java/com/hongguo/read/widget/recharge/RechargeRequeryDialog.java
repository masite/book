package com.hongguo.read.widget.recharge;

import android.content.Context;
import android.view.View;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/1/6.
 */

public class RechargeRequeryDialog extends BaAnimDialog implements View.OnClickListener {

    private RechargeRequeryListener mRechargeRequeryListener;

    public RechargeRequeryDialog(Context context) {
        super(context, R.style.RechargeDialog);
        initView();
    }

    private void initView() {
        setCancelable(false);

        findViewById(R.id.order_requery).setOnClickListener(this);
        findViewById(R.id.pay_request).setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_recharge_requery;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_requery:
                requery();
                break;
            case R.id.pay_request:
                postRequest();
                break;
        }
    }

    private void requery() {
        dismissWithoutAnim();
        if(mRechargeRequeryListener != null){
            mRechargeRequeryListener.requeryOrder();
        }
    }

    private void postRequest() {
        dismissWithoutAnim();
        if(mRechargeRequeryListener != null){
            mRechargeRequeryListener.postRequestion();
        }
    }

    public void setRechargeRequeryListener(RechargeRequeryListener rechargeRequeryListener) {
        mRechargeRequeryListener = rechargeRequeryListener;
    }

    public interface RechargeRequeryListener{
        void requeryOrder();
        void postRequestion();
    }
}
