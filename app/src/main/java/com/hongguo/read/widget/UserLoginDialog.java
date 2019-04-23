package com.hongguo.read.widget;

import android.content.Context;
import android.view.View;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/5/30.
 */

public class UserLoginDialog extends BaAnimDialog implements View.OnClickListener {

    private UserLoginListener mUserLoginListener;

    public UserLoginDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_user_login;
    }

    private void initView() {
        getWindow().getAttributes().width = getContext().getResources().getDisplayMetrics().widthPixels * 4 / 5;
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.login_password).setOnClickListener(this);
        findViewById(R.id.login_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.login_password:
                mUserLoginListener.loginPassword();
                dismiss();
                break;
            case R.id.login_weixin:
                mUserLoginListener.loginWeiXin();
                dismiss();
                break;
        }
    }

    public void setUserLoginListener(UserLoginListener userLoginListener) {
        mUserLoginListener = userLoginListener;
    }

    public interface UserLoginListener {
        void loginPassword();

        void loginWeiXin();
    }
}
