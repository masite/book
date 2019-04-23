package com.hongguo.read.mvp.presenter.vip;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.RechageKey;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.vip.VipContractor;
import com.hongguo.read.mvp.model.mine.recharge.RechargeTypeBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class VipPresenter extends BaseImpPresenter<VipContractor.IView> implements VipContractor.IPresenter {

    private boolean mVipListFirstInit = true;

    @Inject
    public VipPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @PresenterMethod
    public void openVip(int currentIndex) {
        String rechargeKey = "";
        if (UserRepertory.userIsSVip() || UserRepertory.userIsVip()) {
            rechargeKey = RechageKey.SVIP_RECHARGE;
        } else {
            if (currentIndex == 0) {
                rechargeKey = RechageKey.SVIP_RECHARGE;
            } else {
                rechargeKey = RechageKey.VIP_RECHARGE;
            }
        }
        queryPayList(rechargeKey);
        queryPayTypeInfo();
    }

    /**
     * 300 是 dialog 动画时间，防止页面大小变化造成 dialog首次打开时动画跳动
     *
     * @param rechargeKey
     */
    private void queryPayList(String rechargeKey) {
        int delayTime = mVipListFirstInit ? 300 : 0;
        mApiService.queryRechargeList(rechargeKey).delay(delayTime, TimeUnit.MILLISECONDS).compose(RxJavaResponseDeal.create(this).commonDeal(rechargeList -> {
            mView.setVipPayDialogInfo(rechargeList.data);
            mVipListFirstInit = false;
        }));


    }

    private void queryPayTypeInfo() {
        mApiService.getTopicInfo(CmsTopicInfo.RECHARGE_TYPE).compose(CmsTopicDeal.cmsTopDeal(RechargeTypeBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(rechargeType -> {
            mView.setPayDialogPayType(rechargeType.paySupport.weixin, rechargeType.paySupport.alipay);
        }));
    }
}