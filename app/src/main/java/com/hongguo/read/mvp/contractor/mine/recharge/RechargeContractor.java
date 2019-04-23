package com.hongguo.read.mvp.contractor.mine.recharge;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;

import java.util.List;

public class RechargeContractor {

	public interface IView extends BaseViewEx {
		void setRechargeList(List<RechargeBean.DataBean> rechargeList);

		void startToPay(String directUrl) ;

		void showRechargeStausInfo();

		void rechargeSuccess();

	}

	public interface IPresenter extends BasePresenter {
	}
}