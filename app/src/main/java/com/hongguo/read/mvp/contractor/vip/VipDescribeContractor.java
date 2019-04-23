package com.hongguo.read.mvp.contractor.vip;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.vip.SVIPDescribeBean;
import com.hongguo.read.mvp.model.vip.SVIPShareBean;
import com.hongguo.read.mvp.model.vip.VipBean;

import java.util.List;

public class VipDescribeContractor {

	public interface IView extends BaseViewEx {
		void setSvipDesBeans(List<SVIPDescribeBean.SvipDesBean> svipDesBeans) ;

		void setSvipBooks(List<VipBean.DataBean> books);

		void setShareInfo(SVIPShareBean shareInfo);

	}

	public interface IPresenter extends BasePresenter {
	}
}