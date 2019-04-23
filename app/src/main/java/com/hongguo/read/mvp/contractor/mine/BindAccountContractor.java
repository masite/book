package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BaseViewEx;

import com.hongguo.common.base.BasePresenter;

public class BindAccountContractor {

	public interface IView extends BaseViewEx {

		void setQQBind(boolean qqBind);

		void setWeiXinBind(boolean weixin);

	}

	public interface IPresenter extends BasePresenter {
	}
}