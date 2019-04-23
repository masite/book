package com.hongguo.read.mvp.contractor.mine.center;

import com.hongguo.common.base.BaseViewEx;

import com.hongguo.common.base.BasePresenter;

public class UserCenterContractor {

	public interface IView extends BaseViewEx {
		void setLoginInfo(String text, boolean selected);
		void toLogin();

		void deviceLogin();

	}

	public interface IPresenter extends BasePresenter {
	}
}