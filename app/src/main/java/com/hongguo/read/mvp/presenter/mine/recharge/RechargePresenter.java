package com.hongguo.read.mvp.presenter.mine.recharge;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.recharge.RechargeContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;

import javax.inject.Inject;

public class RechargePresenter extends BaseImpPresenter<RechargeContractor.IView> implements RechargeContractor.IPresenter {

    private String mPayOrder;
    private int    mRechargeType;
    private String mPayType;
    private int    mPayMoney;

    @Inject
    public RechargePresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void queryRechargeKey(String rechargeKey) {
        if (!textEmpty(rechargeKey)) {
            rechargeList(rechargeKey);
            return;
        }
        mApiService.getTopicInfo(CmsTopicInfo.FIRST_RECHAGE).compose(CmsTopicDeal.cmsTopString()).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(key -> {
            rechargeList(key);
        }));
    }

    @PresenterMethod
    public void rechargeList(String key) {
        mApiService.queryRechargeList(key).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(rechargeList -> {
            mView.setRechargeList(rechargeList.data);
        }));
    }

    @PresenterMethod
    public void getPayOrder(String payType, String payAmount, String ruleId, String ruleSign) {
        mPayMoney = MathTypeParseUtils.string2Int(payAmount);
        mPayType = payType;
        mApiService.getPayOrder(payType, payAmount, ruleId, ruleSign).compose(RxJavaResponseDeal.create(this).widthDialog("正在获取支付信息", true).commonDeal(orderInfo -> {
            mView.startToPay(orderInfo.data.payUrl);
            mPayOrder = orderInfo.data.orderNo;
        }));
    }

    @PresenterMethod
    public void setPayOrder(String payOrder) {
        mPayOrder = payOrder;
    }

    /**
     * 查询订单状态
     *
     * @param isSuccess true 正常查询(点击支付完成)  false 点击未支付(防止用户支付完成后误点操作)
     */
    @PresenterMethod
    public void queryOrderStatus(boolean isSuccess) {
        RxJavaResponseDeal rxJavaResponseDeal = RxJavaResponseDeal.create(this);
        if (isSuccess) {
            rxJavaResponseDeal = rxJavaResponseDeal.widthDialog("正在查询订单状态");
        }
        mApiService.queryOrderStatus(mPayOrder).compose(rxJavaResponseDeal.commonDeal(orderInfo -> {
            if (orderInfo.code != 0) {
                if (isSuccess) {
                    mView.showRechargeStausInfo();
                }
                StatisticsUtils.rechargeFailure(mRechargeType, mPayType, mPayOrder, mPayMoney);
                return;
            }
            StatisticsUtils.rechargeSuccess(mRechargeType, mPayType, mPayOrder, mPayMoney);
            mView.toastMessage("支付成功");
            mView.rechargeSuccess();
        }));
    }

    @PresenterMethod
    public void setRechargeType(int rechargeType) {
        mRechargeType = rechargeType;
    }

    public void setPayType(String payType) {
        mPayType = payType;
    }

    public void setPayMoney(int payMoney) {
        mPayMoney = payMoney;
    }
}