package com.hongguo.read.mvp.presenter.book.detail;

import com.hongguo.read.base.BaseImpPresenter;
import javax.inject.Inject;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.mvp.contractor.book.detail.BookReaderChapterContractor;

public class BookReaderChapterPresenter extends BaseImpPresenter<BookReaderChapterContractor.IView> implements BookReaderChapterContractor.IPresenter{

	@Inject
	public BookReaderChapterPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}
}