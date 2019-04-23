package com.hongguo.read.mvp.contractor.mine.mypackage;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;

public class FeedBackContractor {

	public interface IView extends BaseViewEx {
		void setFeedBackLabels(String[] labels) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}