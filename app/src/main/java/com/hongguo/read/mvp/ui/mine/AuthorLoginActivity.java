package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.UmengAuthorHelper;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.AuthorLoginContractor;
import com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter;
import com.hongguo.common.utils.StatisticsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/9.
 */

public class AuthorLoginActivity extends ActivityEx implements AuthorLoginContractor.IView, UmengAuthorHelper.UmAuthorResponseListener {

    @Inject
    AuthorLoginPresenter mLoginPresenter;

    @BindView(R.id.user_name)
    EditText  mUserName;
    @BindView(R.id.clear_user_name)
    ImageView mClearUserName;
    @BindView(R.id.change_password_show)
    ImageView mChangePasswordShow;
    @BindView(R.id.user_password)
    EditText  mUserPassword;
    @BindView(R.id.login)
    TextView  mLogin;

    private UmengAuthorHelper mUmengAuthorHelper;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, AuthorLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        setTitle("用户登录");
        mLoginPresenter.bingView(this);
        mLoginPresenter.loading();
        mUmengAuthorHelper = new UmengAuthorHelper(this);
        mUmengAuthorHelper.setUmAuhorResponseListener(this);
    }


    @OnClick(R.id.clear_user_name)
    public void onClearUserNameClicked() {
        mUserName.setText("");
    }

    @OnClick(R.id.change_password_show)
    public void onChangePasswordShowClicked() {
        mChangePasswordShow.setSelected(!mChangePasswordShow.isSelected());
        if (mChangePasswordShow.isSelected())
            mUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else{
            mUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mUserPassword.setSelection(mUserPassword.getText().length());
    }

    @OnClick(R.id.login)
    public void onLoginClicked() {
        mLoginPresenter.loginByUserName(mUserName.getText().toString(), mUserPassword.getText().toString());
    }

    @OnClick(value = {R.id.qq_logo, R.id.qq_text})
    public void qqLogin() {
        StatisticsUtils.mine("QQ登录");
        mUmengAuthorHelper.author(UmengAuthorHelper.AUTHOR_QQ);
    }

    @OnClick(value = {R.id.weixin_logo, R.id.weixin_text})
    public void weixinLogin() {
        StatisticsUtils.mine("微信登录");
        mUmengAuthorHelper.author(UmengAuthorHelper.AUTHOR_WEIXIN);
    }


    /*************************umeng 登录相关**********************************/
    @Override
    public void authorStart() {
        showWaitDialog(true, "授权中", null);
    }

    @Override
    public void authorComplete(int authorType, UmengAuthorHelper.AuthorResult authorResult) {
        dismissWaitDialogWithoutAnim();
        mLoginPresenter.loginByAuthor(authorType, authorResult);
    }

    @Override
    public void authorError() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权失败");
    }

    @Override
    public void authorCancel() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权取消");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengAuthorHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUmengAuthorHelper.destory();
    }

    /*************************umeng 登录相关 end**********************************/

}
