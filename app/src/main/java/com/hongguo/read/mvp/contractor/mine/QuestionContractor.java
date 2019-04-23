package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.QuestionBean;

import java.util.List;

public class QuestionContractor {

	public interface IView extends BaseViewEx {
		void setRequestList(List<QuestionBean.DataBean> items);

	}

	public interface IPresenter extends BasePresenter {
	}
}