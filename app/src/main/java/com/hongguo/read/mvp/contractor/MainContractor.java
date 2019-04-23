package com.hongguo.read.mvp.contractor;

import com.hongguo.common.base.BaseViewEx;

import com.hongguo.common.base.BasePresenter;

public class MainContractor {

	public interface IView extends BaseViewEx {
		void setEventInfo(String littleImage, String bigImage, String direct, boolean hasEvent, boolean showEventDialog);
		void checkVersion();
	}

	public interface IPresenter extends BasePresenter {
	}
}