package com.hongguo.read.mvp.contractor.vip;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;

import java.util.List;

public class VipContractor {

	public interface IView extends BaseViewEx {
		void setVipPayDialogInfo(List<RechargeBean.DataBean> dialogInfo) ;

		void setPayDialogPayType(boolean weixin, boolean alipay) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}