package com.hongguo.read.mvp.contractor.vip;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.vip.VipBean;

import java.util.List;

public class NormalVipStoreContractor {

	public interface IView extends BaseViewEx {
		void setTopBooks(List<VipBean.DataBean> items) ;

		void setVipBooks(List<VipBean.DataBean> items) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}