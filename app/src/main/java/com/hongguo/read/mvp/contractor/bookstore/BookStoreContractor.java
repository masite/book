package com.hongguo.read.mvp.contractor.bookstore;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.CmsBannerBean;

import java.util.List;

public class BookStoreContractor {

	public interface IView extends BaseViewEx {
		void setBannerUrls(List<CmsBannerBean.DataBean> banners) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}