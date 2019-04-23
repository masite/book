package com.hongguo.read.mvp.presenter.mine;

import com.hongguo.read.base.BaseImpPresenter;
import javax.inject.Inject;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.mvp.contractor.mine.mypackage.MyPackageContractor;

public class MyPackagePresenter extends BaseImpPresenter<MyPackageContractor.IView> implements MyPackageContractor.IPresenter{

	@Inject
	public MyPackagePresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}
}