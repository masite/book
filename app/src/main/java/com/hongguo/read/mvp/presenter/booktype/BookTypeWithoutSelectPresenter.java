package com.hongguo.read.mvp.presenter.booktype;

import com.hongguo.read.base.BaseImpPresenter;
import javax.inject.Inject;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.mvp.contractor.booktype.BookTypeWithoutSelectContractor;

public class BookTypeWithoutSelectPresenter extends BaseImpPresenter<BookTypeWithoutSelectContractor.IView> implements BookTypeWithoutSelectContractor.IPresenter{

	@Inject
	public BookTypeWithoutSelectPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}
}