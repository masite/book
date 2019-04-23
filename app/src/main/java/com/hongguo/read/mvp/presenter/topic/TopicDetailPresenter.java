package com.hongguo.read.mvp.presenter.topic;

import com.hongguo.read.base.BaseImpPresenter;
import javax.inject.Inject;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.mvp.contractor.topic.TopicDetailContractor;

public class TopicDetailPresenter extends BaseImpPresenter<TopicDetailContractor.IView> implements TopicDetailContractor.IPresenter{

	@Inject
	public TopicDetailPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}
}