package com.hongguo.read.mvp.presenter.mine;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.common.utils.UmengAuthorHelper;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.base.UserApiPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.BindAccountContractor;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import javax.inject.Inject;

public class BindAccountPresenter extends UserApiPresenter<BindAccountContractor.IView> implements BindAccountContractor.IPresenter {

    @Inject
    public BindAccountPresenter(UserApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryBindInfo();
    }

    @PresenterMethod
    public void queryBindInfo() {
        mApiService.getUserBindInfo(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).commonDeal(userBindInfo -> {
            boolean qqBind = (userBindInfo.data.hgread.getQQAuthorInfo() != null);
            boolean weixinBind = (userBindInfo.data.hgread.getWeiXinAuthorInfo() != null);
            mView.setQQBind(qqBind);
            mView.setWeiXinBind(weixinBind);
        }));
    }

    @PresenterMethod
    public void authorLogin(int authorType, UmengAuthorHelper.AuthorResult authorResult) {
        if (authorType == UmengAuthorHelper.AUTHOR_QQ) {
            bindQQ(authorResult);
        } else {
            bindWeiXin(authorResult);
        }
    }

    private void bindQQ(UmengAuthorHelper.AuthorResult authorResult) {
        mApiService.bindQQ(authorResult.openid, authorResult.name, authorResult.iconurl, authorResult).compose(RxJavaResponseDeal.create(this).widthDialog("正在绑定用户").commonDeal(response -> {
            if (response.code == 0) {
                mView.setQQBind(true);
                mView.toastMessage("绑定成功");
            } else {
                mView.setQQBind(false);
                mView.toastMessage("该QQ号已经绑定过账号，请直接用QQ登录");
            }
        }));
    }

    private void bindWeiXin(UmengAuthorHelper.AuthorResult authorResult) {
        mApiService.bindWeiXin(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN, authorResult.openid, authorResult.unionid,authorResult.name, authorResult.iconurl, authorResult).compose(RxJavaResponseDeal.create(this).widthDialog("正在绑定用户").commonDeal(response -> {
            if (response.code == 0) {
                mView.setWeiXinBind(true);
                mView.toastMessage("绑定成功");
            } else {
                mView.setWeiXinBind(false);
                mView.toastMessage("该微信号已经绑定过账号，请直接用微信登录");
            }
        }));
    }

    @PresenterMethod
    public void unbindQQ() {
        mApiService.unbindQQ(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).widthDialog("正在解绑").commonDeal(result -> {
            if (result.code == 0) {
                mView.toastMessage("解绑成功");
                mView.setQQBind(false);
                return;
            }
            mView.toastMessage("解绑失败");
        }));

    }

    @PresenterMethod
    public void unbindWeiXin() {
        mApiService.unbindQWeiXin(com.hongguo.common.constants.Constants.AUHOR_TYPE.WEIXIN).compose(RxJavaResponseDeal.create(this).widthDialog("正在解绑").commonDeal(result -> {
            if (result.code == 0) {
                mView.toastMessage("解绑成功");
                mView.setWeiXinBind(false);
                return;
            }
            mView.toastMessage("解绑失败");
        }));
    }
}