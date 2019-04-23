package com.hongguo.read.mvp.contractor.mine.mypackage;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;

import java.util.List;

public class ConsumeContractor {

	public interface IView extends BaseViewEx {
		void setItems(List<ConsumeBean.DataBean> items) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}