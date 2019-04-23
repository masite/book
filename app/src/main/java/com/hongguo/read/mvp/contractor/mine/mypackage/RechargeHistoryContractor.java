package com.hongguo.read.mvp.contractor.mine.mypackage;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.mypackage.RechargeHistoryBean;

import java.util.List;

public class RechargeHistoryContractor {

	public interface IView extends BaseViewEx {
		void setRechargeHistory(List<RechargeHistoryBean.DataBean> items);

	}

	public interface IPresenter extends BasePresenter {
	}
}