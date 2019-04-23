package com.hongguo.read.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/3/28.
 */

public class SignDialog extends BaAnimDialog implements View.OnClickListener {

    private TextView mGiveCoin;
    private TextView mSignNumber;

    public SignDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    private void initView() {
        mGiveCoin = findViewById(R.id.give_coin);
        mSignNumber = findViewById(R.id.sign_number);
        findViewById(R.id.dialog_content).setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_sign_success;
    }

    public void setSignNumber(String signNumber) {
        mSignNumber.setText("- 您已连续签到" + signNumber + "天 -");
    }

    public void setGiveCoin(String giveCoin) {
        mGiveCoin.setText(giveCoin + "红果币");
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
