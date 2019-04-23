package com.hongguo.read.widget;

import android.content.Context;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/5/28.
 */

public class BaiDuTimeOutDialog extends BaAnimDialog {

    public BaiDuTimeOutDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_baidu_timeout;
    }

    private void initView() {
        findViewById(R.id.time_out).setOnClickListener(v->{
            dismiss();
        });
    }


}
