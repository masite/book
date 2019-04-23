package com.hongguo.read.widget;

import android.content.Context;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/5/28.
 */

public class CouponDialog extends BaAnimDialog {

    public CouponDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_coupon;
    }

    private void initView() {
        getWindow().getAttributes().width = getContext().getResources().getDisplayMetrics().widthPixels * 4 / 5;
        findViewById(R.id.user_know).setOnClickListener(v->{
            dismiss();
        });
    }


}
