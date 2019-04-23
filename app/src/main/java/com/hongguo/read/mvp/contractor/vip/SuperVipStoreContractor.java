package com.hongguo.read.mvp.contractor.vip;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.read.mvp.model.vip.VipBean;

import java.util.List;

public class SuperVipStoreContractor {

	public interface IView extends BaseViewEx {
		void setBannerUrls(List<CmsBannerBean.DataBean> banners) ;

		void setVipBooks(List<VipBean.DataBean> dataBeans);
	}

	public interface IPresenter extends BasePresenter {
	}
}