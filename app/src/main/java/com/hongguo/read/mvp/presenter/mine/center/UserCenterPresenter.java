package com.hongguo.read.mvp.presenter.mine.center;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.base.UserApiPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.mvp.contractor.mine.center.UserCenterContractor;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserCenterPresenter extends UserApiPresenter<UserCenterContractor.IView> implements UserCenterContractor.IPresenter {

    @Inject
    public UserCenterPresenter(UserApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryLoginInfo();
    }

    /**
     * 查询当前登录的方式
     */
    @PresenterMethod
    public void queryLoginInfo() {
        if (UserRepertory.getLoginType().equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.DEVICE_ID)) {
            mView.setLoginInfo("登录", false);
        } else {
            mView.setLoginInfo("退出登录", true);
        }
    }

    /**
     * 当前是第三方登录，退出  否则登录
     */
    @PresenterMethod
    public void loginOrLogout() {
        if (!UserRepertory.getLoginType().equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.DEVICE_ID)) {
            mView.showWaitDialog(true, "正在退出登录", null);
            mView.deviceLogin();
        } else {
            mView.toLogin();
        }
    }

    /**
     * 检查设备号登录是否成功
     */
    @PresenterMethod
    public void checkDeviceLoginStatus() {
        mView.dismissWaitDialog();
        if (!UserRepertory.getLoginType().equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.DEVICE_ID)) {
            mView.toastMessage("退出失败");
        } else {
            mView.toastMessage("成功退出");
            mView.setLoginInfo("登录", false);
            EventBus.getDefault().post(new UpdateUserInfo());
        }
    }

    /**
     * 保存用户修改的信息
     * @param userName
     * @param sex
     */
    @PresenterMethod
    public void saveUserInfo(String userName, String sex) {
        if (textEmpty(userName)) {
            mView.toastMessage("用户昵称不能未空哦~");
            return;
        }
        mApiService.updateUserInfo(userName, sex.equals("男") ? Constants.USER_SEX.BOY : Constants.USER_SEX.GIRL).compose(RxJavaResponseDeal.create(this).widthDialog("正在修改").commonDeal(response -> {
            //如果是第三方登录，本地也跟着修改
            UserRepertory.setAuthorUserName(userName);
            EventBus.getDefault().post(new UpdateUserInfo());
            mView.toastMessage("用户信息已修改");
        }));
    }


    /**
     * 上传头像
     * @param avatarPath
     */
    @PresenterMethod
    public void upLoadAvatar(String avatarPath) {
        mApiService.queryUploadPath(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).widthDialog("获取头像信息").commonDeal(uploadInfo -> {
            doUpload(uploadInfo.data.uploadUrl, avatarPath);
        }));
    }

    /**
     * 上传操作
     *
     * @param uploadUrl
     * @param avatarPath
     */
    private void doUpload(String uploadUrl, String avatarPath) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(avatarPath));
        mApiService.postImage(uploadUrl, UserRepertory.getUserID(), UserRepertory.getToken(), fileBody).compose(RxJavaResponseDeal.create(this).widthDialog("正在上传用户头像").commonDeal(response -> {
            //删除源文件
            FileUtils.deleteFile(new File(avatarPath));
            if (response.code == 0 && response.data != null) {
                String link = response.data.link;
                updateUserAvatar(link);
            }
        }));
    }

    /**
     * 更新用户头像信息
     *
     * @param link
     */
    private void updateUserAvatar(String link) {
        mApiService.updateUserAvatar("315", link).compose(RxJavaResponseDeal.create(this).widthDialog("更新用户信息").commonDeal(common -> {
            //如果是第三方登录，本地也跟着修改
            UserRepertory.setAuthorAvatar(link);
            EventBus.getDefault().post(new UpdateUserInfo());
        }));
    }


}