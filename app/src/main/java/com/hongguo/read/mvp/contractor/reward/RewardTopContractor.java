package com.hongguo.read.mvp.contractor.reward;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;

import java.util.List;

public class RewardTopContractor {

	public interface IView extends BaseViewEx {
		void setRankTops(List<AwardRankBean.DataBean> dataBeans) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}