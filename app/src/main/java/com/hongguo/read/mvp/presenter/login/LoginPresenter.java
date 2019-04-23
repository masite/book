package com.hongguo.read.mvp.presenter.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.model.login.UserOauthBean;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.baidu.ShuChengManager;
import com.hongguo.read.eventbus.BackgroundLoginEvent;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.UserRepertory;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created time 2017/11/30.
 * 后台登录操作
 *
 * @author losg
 */

public class LoginPresenter {

    /**
     * 避免重复登录
     */
    private static boolean sLogin = false;

    private UserApiService mApiService;

    @Inject
    @ContextLife(ContextLife.Life.Application)
    Context mContext;

    @Inject
    public LoginPresenter(UserApiService apiService) {
        mApiService = apiService;
    }

    @PresenterMethod
    public void login() {
        String loginType = UserRepertory.getLoginType();
        if (loginType.equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN)) {
            weinXinAuthor();
        } else if (loginType.equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ)) {
            qqAuthor();
        } else if (loginType.equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD)) {
            nameLogin();
        } else {
            deviceLogin();
        }
    }

    /**
     * 账号密码登录
     */
    private void nameLogin() {
        String userName = UserRepertory.getLoginName();
        String password = UserRepertory.getLoginPassword();
        mApiService.loginByUserName(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD, userName, password).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<UserOauthBean>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD, null, "");
            }

            @Override
            public void onNext(UserOauthBean userOauthBean) {
                super.onNext(userOauthBean);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD, userOauthBean, "");
            }
        });
    }

    /**
     * qq登录
     */
    private void qqAuthor() {
        String openId = UserRepertory.getAuthorOpenId();
        mApiService.loginByQQ(com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ, openId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<UserOauthBean>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, null, "");
            }

            @Override
            public void onNext(UserOauthBean userOauthBean) {
                super.onNext(userOauthBean);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, userOauthBean, "");
            }
        });
    }

    /**
     * 微信登录
     */
    private void weinXinAuthor() {
        String openId = UserRepertory.getAuthorOpenId();
        String unionId = UserRepertory.getAuthorUnionId();
        mApiService.loginByWeiXin(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, openId, TextUtils.isEmpty(unionId) ? openId : unionId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<UserOauthBean>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, null, "");
            }

            @Override
            public void onNext(UserOauthBean userOauthBean) {
                super.onNext(userOauthBean);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, userOauthBean, "");
            }
        });
    }

    /**
     * 匿名登录
     */
    public void deviceLogin() {
        //检查是否获取到权限
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            sLogin = false;
            return;
        }
        if (sLogin) {
            return;
        }

        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        sLogin = true;

        final String newUserEvent = "";
//        String newUserEvent = "2146";

        mApiService.userLogin(telephonyManager.getDeviceId(), newUserEvent).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<UserOauthBean>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.DEVICE_ID, null, newUserEvent);
            }

            @Override
            public void onNext(UserOauthBean userOauthBean) {
                super.onNext(userOauthBean);
                sLogin = false;
                dealLoginResult(com.hongguo.common.constants.Constants.AUHOR_TYPE.DEVICE_ID, userOauthBean, newUserEvent);
            }
        });
    }

    /**
     * 处理登录返回数据
     *
     * @param userOauthBean
     */
    private void dealLoginResult(String authorType, UserOauthBean userOauthBean, String newUserEvent) {
        if (userOauthBean != null && userOauthBean.code == 0) {
            if (authorType != com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ && authorType != com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN) {
                UserRepertory.clearAuhorInfo();
            }
            UserRepertory.setLoginType(authorType);
            UserRepertory.setToken(userOauthBean.data.accessToken);
            UserRepertory.setRefreshToken(userOauthBean.data.refreshToken);
            UserRepertory.setUserID(userOauthBean.data.userId);
            if (!AppRepertory.isVipEventClick() && userOauthBean.data.data != null && !TextUtils.isEmpty(newUserEvent)) {
                UserRepertory.setUserIsNew(userOauthBean.data.data.newUser);
            }else{
                UserRepertory.setUserIsNew(false);
            }

            EventBus.getDefault().post(new BackgroundLoginEvent(BackgroundLoginEvent.LoginCode.SUCCESS));
            //绑定百度账号
            ShuChengManager.shuchengLogin(userOauthBean.data.userId, userOauthBean.data.accessToken, mApiService);
        } else {
            EventBus.getDefault().post(new BackgroundLoginEvent(BackgroundLoginEvent.LoginCode.FAILURE));
        }
    }

}
