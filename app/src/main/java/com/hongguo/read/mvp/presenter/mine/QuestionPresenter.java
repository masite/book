package com.hongguo.read.mvp.presenter.mine;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.QuestionContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import javax.inject.Inject;

public class QuestionPresenter extends BaseImpPresenter<QuestionContractor.IView> implements QuestionContractor.IPresenter{

	@Inject
	public QuestionPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
		queryQuestion();
	}

	@PresenterMethod
	public void queryQuestion(){
		mApiService.queryCommonQuestion(Constants.Request.COMMON_QUESTION).compose(RxJavaResponseDeal.create(this).commonDeal(question->{
			mView.setRequestList(question.data);
		}));
	}
}