package com.hongguo.read.mvp.ui.mine.center;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.BackgroundLoginEvent;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.eventbus.UploadAvatarEvent;
import com.hongguo.read.mvp.contractor.mine.center.UserCenterContractor;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.read.mvp.presenter.login.LoginPresenter;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.mine.center.UserCenterPresenter;
import com.hongguo.read.mvp.ui.mine.AuthorLoginActivity;
import com.hongguo.read.mvp.ui.mine.BindAccountActivity;
import com.hongguo.read.mvp.ui.mine.center.chooseheader.LocalImageChooseActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.SexChooseDialog;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/8.
 */

public class UserCenterActivity extends MineInfoActivity implements UserCenterContractor.IView, SexChooseDialog.SexChooseListener {

    @Inject
    MinePresenter mMinePresenter;

    @Inject
    LoginPresenter mLoginPresenter;

    @Inject
    UserCenterPresenter mUserCenterPresenter;
    @BindView(R.id.user_avatar)
    ImageView           mUserAvatar;
    @BindView(R.id.choose_avatar)
    LinearLayout        mChooseAvatar;
    @BindView(R.id.nick_name)
    EditText            mNickName;
    @BindView(R.id.sex)
    TextView            mSex;
    @BindView(R.id.sex_layer)
    LinearLayout        mSexLayer;
    @BindView(R.id.user_id)
    TextView            mUserId;
    @BindView(R.id.bind_account)
    LinearLayout        mBindAccount;
    @BindView(R.id.login_or_logout)
    TextView            mLoginOrLogout;

    private SexChooseDialog mSexChooseDialog;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_user_center;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("我的账户");
        mUserCenterPresenter.bingView(this);
        mUserCenterPresenter.loading();
        mMinePresenter.bingView(this);
        mMinePresenter.loading();

        mSexChooseDialog = new SexChooseDialog(mContext);
        mSexChooseDialog.setSexChooseListener(this);
    }

    @Override
    public void setUserInfo(UserInfoBean.DataBean userInfo) {
        super.setUserInfo(userInfo);
        ImageLoadUtils.loadCircleUrl(mUserAvatar, userInfo.avatar);
        mNickName.setText(userInfo.nickName);
        mSex.setText(userInfo.genderStr);
        mUserId.setText(userInfo.userId);
    }

    @ViewMethod
    public void setLoginInfo(String text, boolean selected) {
        mLoginOrLogout.setText(text);
        mLoginOrLogout.setSelected(selected);
    }

    /**
     * 选择头像返回
     * {@link com.hongguo.read.mvp.ui.mine.center.chooseheader.CropActivity}
     *
     * @param uploadAvatarEvent
     */
    @Subscribe
    public void onEvent(UploadAvatarEvent uploadAvatarEvent) {
        mUserCenterPresenter.upLoadAvatar(uploadAvatarEvent.filePath);
    }

    /**
     * 更新用户信息
     * {@link UserCenterPresenter}
     * {@link com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter}
     *
     * @param updateUserInfo
     */
    @Subscribe
    public void onEvent(UpdateUserInfo updateUserInfo) {
        mMinePresenter.loading();
        mUserCenterPresenter.loading();
    }

    @OnClick(R.id.choose_avatar)
    void chooseAvatar() {
        LocalImageChooseActivity.toActivity(mContext);
    }

    @OnClick(R.id.sex_layer)
    public void onSexLayerClicked() {
        mSexChooseDialog.show();
    }

    @OnClick(R.id.bind_account)
    public void onBindAccountClicked() {
        BindAccountActivity.toActivity(mContext);
    }

    @OnClick(R.id.login_or_logout)
    public void onLoginOrLogoutClicked() {
        mUserCenterPresenter.loginOrLogout();
    }

    @ViewMethod
    public void deviceLogin(){
        mLoginPresenter.deviceLogin();
    }

    /**
     * 设备号登录回调
     * {@link LoginPresenter}
     * @param backgroundLoginEvent
     */
    @Subscribe
    public void onEvent(BackgroundLoginEvent backgroundLoginEvent){
        mUserCenterPresenter.checkDeviceLoginStatus();
    }

    @ViewMethod
    public void toLogin() {
        AuthorLoginActivity.toActivity(mContext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem saveMenu = menu.add(0, 0, 0, "保存");
        saveMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            saveUserInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveUserInfo() {
        mUserCenterPresenter.saveUserInfo(mNickName.getText().toString(), mSex.getText().toString());
    }

    @Override
    public void sexChoose(String sex) {
        mSex.setText(sex);
    }

}
