package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;

import java.util.List;

public class RewardContractor {

	public interface IView extends BaseViewEx {
		void setRewrads(List<ConsumeBean.SellListBean.DataBean> items);

	}

	public interface IPresenter extends BasePresenter {
	}
}