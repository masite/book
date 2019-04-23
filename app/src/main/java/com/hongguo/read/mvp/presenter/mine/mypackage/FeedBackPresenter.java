package com.hongguo.read.mvp.presenter.mine.mypackage;

import android.content.Context;
import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.read.mvp.contractor.mine.mypackage.FeedBackContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.utils.AppUtils;

import javax.inject.Inject;

public class FeedBackPresenter extends BaseImpPresenter<FeedBackContractor.IView> implements FeedBackContractor.IPresenter {

    private String[] mLabels = {"章节错误", "充值错误", "更新缓慢", "不流畅", "耗流量", "书籍少", "充值起点高"};

    @Inject
    @ContextLife
    Context mContext;

    @Inject
    public FeedBackPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        mView.setFeedBackLabels(mLabels);
    }

    @PresenterMethod
    public void submitFeedBack(String contact, String describe) {
        if (TextUtils.isEmpty(contact)) {
            mView.toastMessage("请输入您的联系方式");
            return;
        }
        if (TextUtils.isEmpty(describe)) {
            mView.toastMessage("我们还不知道您的意见或问题");
            return;
        }
        mApiService.feedBack("none", AppUtils.getVersionName(mContext), describe, contact).compose(RxJavaResponseDeal.create(this).widthDialog("反馈中", true).commonDeal(s -> {
            mView.toastMessage("提交成功，感谢您的反馈");
            StatisticsUtils.collect("反馈", "设置-反馈");
            mView.finishView();
        }));
    }
}