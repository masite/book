package com.hongguo.read.mvp.contractor.loading;

import com.hongguo.common.base.BaseViewEx;

import com.hongguo.common.base.BasePresenter;

public class LoadingContractor {

	public interface IView extends BaseViewEx {
		void setLoadingImageUrl(String url) ;

		void toMain() ;

		void toWebActivity(String title, String url) ;

		void toBookReader(String bookid, int bookFrom, String bookName, String bookCover, String author) ;

		void toLogin();

		void setDelayVisiable() ;

		void setCurrentTime(int time);

	}

	public interface IPresenter extends BasePresenter {
	}
}