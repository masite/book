package com.hongguo.read.widget;

import android.content.Context;
import android.view.View;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/1/9.
 */

public class SexChooseDialog extends BaAnimDialog implements View.OnClickListener {

    private SexChooseListener mSexChooseListener;

    public SexChooseDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_choose_sex;
    }

    private void initView() {
        findViewById(R.id.sex_boy).setOnClickListener(this);
        findViewById(R.id.sex_girl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (mSexChooseListener == null) return;
        if (v.getId() == R.id.sex_boy) {
            mSexChooseListener.sexChoose("男");
        } else {
            mSexChooseListener.sexChoose("女");
        }
    }

    public void setSexChooseListener(SexChooseListener sexChooseListener) {
        mSexChooseListener = sexChooseListener;
    }

    public interface SexChooseListener {
        void sexChoose(String sex);
    }
}
