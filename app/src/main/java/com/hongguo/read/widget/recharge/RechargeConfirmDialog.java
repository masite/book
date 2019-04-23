package com.hongguo.read.widget.recharge;

import android.content.Context;
import android.view.View;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/1/6.
 */

public class RechargeConfirmDialog extends BaAnimDialog implements View.OnClickListener {

    private RechargeConfirmListener mRechargeConfirmListener;

    public RechargeConfirmDialog(Context context) {
        super(context, R.style.RechargeDialog);
        initView();
    }

    private void initView() {
        setCancelable(false);
        findViewById(R.id.pay_success).setOnClickListener(this);
        findViewById(R.id.pay_cancel).setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_recharge_confirm;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_success:
                paySuccess();
                break;
            case R.id.pay_cancel:
                payCancel();
                break;
        }
    }

    private void payCancel() {
        dismissWithoutAnim();
        if(mRechargeConfirmListener != null){
            mRechargeConfirmListener.rechargeCancel();
        }
    }

    private void paySuccess() {
        dismissWithoutAnim();
        if(mRechargeConfirmListener != null){
            mRechargeConfirmListener.rechargeConfirmSuccess();
        }
    }


    public void setRechargeConfirmListener(RechargeConfirmListener rechargeConfirmListener) {
        mRechargeConfirmListener = rechargeConfirmListener;
    }

    public interface RechargeConfirmListener{
        void rechargeConfirmSuccess();
        void rechargeCancel();
    }
}
