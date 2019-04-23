package com.hongguo.read.mvp.presenter;

import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.base.BaseImpPresenter;
import javax.inject.Inject;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.mvp.contractor.CmsBannerContractor;

public class CmsBannerPresenter extends BaseImpPresenter<CmsBannerContractor.IView> implements CmsBannerContractor.IPresenter{

	@Inject
	public CmsBannerPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}
}