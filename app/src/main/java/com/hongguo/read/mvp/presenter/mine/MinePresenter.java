package com.hongguo.read.mvp.presenter.mine;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.model.userInfo.UserAmountLatestBean;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.TimeUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.UserApiPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.contractor.mine.MineContractor;
import com.hongguo.read.mvp.model.mine.WebCookieUserInfo;
import com.hongguo.read.repertory.share.UserRepertory;

import java.net.URLEncoder;

import javax.inject.Inject;

public class MinePresenter extends UserApiPresenter<MineContractor.IView> implements MineContractor.IPresenter {

    @Inject
    @ContextLife
    Context mContext;

    @Inject
    public MinePresenter(UserApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        if(!TextUtils.isEmpty(UserRepertory.getWebCookieUserInfo())){
            syncCookie(UserRepertory.getWebCookieUserInfo());
        }
        queryUserInfo();
        queryUserCoin();
    }

    @PresenterMethod
    public void queryUserInfo() {
        mApiService.queryUserInfo(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).commonDeal(userInfo -> {
            if (userInfo.data.isSVip) {
                mView.setUserIsSVip(TimeUtils.parseCTime2Day(userInfo.data.sVipLostTime));
            } else if (userInfo.data.isVip) {
                mView.setUserIsVip(TimeUtils.parseCTime2Day(userInfo.data.vipLoseTime));
            } else {
                mView.setUserNormal();
            }
            userInfo.data.genderStr = userInfo.data.gender.equals(Constants.USER_SEX.BOY) ? "男" : "女";
            saveUserInfo(userInfo);
            mView.setUserInfo(userInfo.data);
        }));
    }

    private void saveUserInfo(UserInfoBean userInfo) {

        String loginType = UserRepertory.getLoginType();
        //QQ登录 和 微信  登录默认使用qq和微信的头像和用户名称
        if (loginType.equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.QQ) || loginType.equals(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN)) {
            userInfo.data.nickName = TextUtils.isEmpty(UserRepertory.getAuthorUserNmae()) ? userInfo.data.nickName : UserRepertory.getAuthorUserNmae();
            userInfo.data.avatar = TextUtils.isEmpty(UserRepertory.getAuhorAvatar()) ? userInfo.data.avatar : UserRepertory.getAuhorAvatar();
        }

        UserRepertory.setUserName(userInfo.data.nickName);
        UserRepertory.setUserAvatar(userInfo.data.avatar);
        UserRepertory.setUserIsSVip(userInfo.data.isSVip);
        UserRepertory.setUserIsVip(userInfo.data.isVip);
        UserRepertory.setUserDiscount(userInfo.data.vipDiscount);

        WebCookieUserInfo eventUserInfo = new WebCookieUserInfo();
        eventUserInfo.UserId = userInfo.data.userId;
        eventUserInfo.AccessToken = UserRepertory.getToken();
        eventUserInfo.NickName = userInfo.data.nickName;
        eventUserInfo.IsSVip = userInfo.data.isSVip;
        eventUserInfo.IsVip = userInfo.data.isVip;
        eventUserInfo.HeadImgUrl = userInfo.data.avatar;
        eventUserInfo.RefreshToken = UserRepertory.getRefreshToken();
        UserRepertory.setWebCookieUserInfo(eventUserInfo);
        syncCookie(UserRepertory.getWebCookieUserInfo());
    }

    private void syncCookie(String userUrl) {
        try {
            CookieSyncManager.createInstance(mContext);//创建一个cookie管理器
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除以前的cookie
            cookieManager.removeAllCookie();
            cookieManager.setCookie(".hgread.com", "UserItem_HGREAD_7040="+ URLEncoder.encode(userUrl));//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
            CookieSyncManager.getInstance().sync();//同步cookie
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PresenterMethod
    public void queryUserCoin() {
        mApiService.queryUserCurrentAmount(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<UserAmountLatestBean>() {
            @Override
            public void failure(Throwable e) {
                mView.dismissWaitDialog();
            }

            @Override
            public void netError() {
                super.netError();
                mView.dismissWaitDialog();
            }

            @Override
            public void success(UserAmountLatestBean userCoin) {
                mView.setCoins(MathTypeParseUtils.string2Int(userCoin.data.sumAmount), MathTypeParseUtils.string2Int(userCoin.data.giveAmount));
            }
        }));
    }

}