package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BaseViewEx;

import com.hongguo.common.base.BasePresenter;

public class SettingContractor {

	public interface IView extends BaseViewEx {
		void initSetting(boolean isNeight, boolean volumePage, boolean autoUpdate) ;

		void setCache(String cache) ;

		void setVersion(String version) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}