package com.hongguo.read.mvp.presenter.mine;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.common.utils.UmengAuthorHelper;
import com.hongguo.read.baidu.ShuChengManager;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.base.UserApiPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.mvp.contractor.mine.AuthorLoginContractor;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class AuthorLoginPresenter extends UserApiPresenter<AuthorLoginContractor.IView> implements AuthorLoginContractor.IPresenter {

    @Inject
    public AuthorLoginPresenter(UserApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void loginByUserName(String userName, String password) {
        mApiService.loginByUserName(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD, userName, password).compose(RxJavaResponseDeal.create(this).widthDialog("正在登录", true).commonDeal(userInfo -> {
            if (userInfo != null && userInfo.code == 0) {
                UserRepertory.clearAuhorInfo();
                UserRepertory.setToken(userInfo.data.accessToken);
                UserRepertory.setUserID(userInfo.data.userId);
                UserRepertory.setLoginName(userName);
                UserRepertory.setLoginPassword(password);
                UserRepertory.setLoginType(com.hongguo.common.constants.Constants.AUHOR_TYPE.NAME_PASSWORD);
                //绑定百度账号
                ShuChengManager.shuchengLogin(userInfo.data.userId, userInfo.data.accessToken, mApiService);
                mView.toastMessage("登录成功");
                EventBus.getDefault().post(new UpdateUserInfo());
                mView.finishView();
                return;
            }
            mView.toastMessage("账号或密码错误!");
        }));
    }

    @PresenterMethod
    public void loginByAuthor(int authorType, UmengAuthorHelper.AuthorResult authorResult) {
        if (authorType == UmengAuthorHelper.AUTHOR_QQ) {
            loginByQQ(authorResult);
        } else {
            loginByWein(authorResult);
        }
    }

    /**
     * 微信登录
     *
     * @param authorResult
     */
    private void loginByWein(UmengAuthorHelper.AuthorResult authorResult) {
        mApiService.loginByWeiXin(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, authorResult.openid, textEmpty(authorResult.unionid) ? authorResult.openid : authorResult.unionid).compose(RxJavaResponseDeal.create(this).widthDialog("正在登录", true).commonDeal(userInfo -> {
            if (userInfo != null && userInfo.code == 0) {
                UserRepertory.setLoginType(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN);
                UserRepertory.setToken(userInfo.data.accessToken);
                UserRepertory.setUserID(userInfo.data.userId);
                //绑定百度账号
                ShuChengManager.shuchengLogin(userInfo.data.userId, userInfo.data.accessToken, mApiService);
                mView.toastMessage("登录成功");
                saveAuthorUserInfo(authorResult);
                EventBus.getDefault().post(new UpdateUserInfo());
                mView.finishView();
                return;
            }
            mView.toastMessage("该微信号尚未绑定账号，请先绑定后登录");
        }));
    }

    /**
     * QQ登录
     *
     * @param authorResult
     */
    private void loginByQQ(UmengAuthorHelper.AuthorResult authorResult) {
        mApiService.loginByQQ(com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ, authorResult.openid).compose(RxJavaResponseDeal.create(this).widthDialog("正在登录", true).commonDeal(userInfo -> {
            if (userInfo != null && userInfo.code == 0) {
                UserRepertory.setToken(userInfo.data.accessToken);
                UserRepertory.setUserID(userInfo.data.userId);
                //绑定百度账号
                ShuChengManager.shuchengLogin(userInfo.data.userId, userInfo.data.accessToken, mApiService);
                UserRepertory.setLoginType(com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ);
                saveAuthorUserInfo(authorResult);
                mView.toastMessage("登录成功");
                EventBus.getDefault().post(new UpdateUserInfo());
                mView.finishView();
                return;
            }
            mView.toastMessage("该QQ号尚未绑定账号，请先绑定后登录");
        }));
    }

    /**
     * 保存授权登录后的用户信息
     *
     * @param authorResult
     */
    private void saveAuthorUserInfo(UmengAuthorHelper.AuthorResult authorResult) {
        UserRepertory.setAuthorUserName(authorResult.name);
        UserRepertory.setAuthorOpenId(authorResult.openid);
        UserRepertory.setAuthorUnionId(authorResult.unionid);
        UserRepertory.setAuthorAvatar(authorResult.iconurl);
    }
}