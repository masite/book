package com.hongguo.read.mvp.presenter.topic;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.topic.TopicContractor;
import com.hongguo.read.mvp.model.topic.TopicBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;

import javax.inject.Inject;

public class TopicPresenter extends BaseImpPresenter<TopicContractor.IView> implements TopicContractor.IPresenter {

    @Inject
    public TopicPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @PresenterMethod
    public void queryTopic(int topicType) {
        String topicToken = topicType == Constants.TOPIC_TYPE.TOPIC_BOY ? CmsTopicInfo.BOYS_TOPIC : CmsTopicInfo.GIRLS_TOPIC;
        mApiService.getTopicInfo(topicToken).compose(CmsTopicDeal.cmsTopDeal(TopicBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseListener<TopicBean>() {
            @Override
            public void success(TopicBean topicBean) {
                mView.setTopic(topicBean.ztlist);
            }
        }));
    }
}