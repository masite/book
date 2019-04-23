package com.hongguo.read.mvp.contractor.bookstore.channel;


import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.search.RankBean;

public class SortChannelContractor {

	public interface IView extends BaseViewEx {
		void setClickRank(RankBean rank);

		void setRecommendRank(RankBean rank);

		void setUpdateRank(RankBean rank);

		void setFavorRank(RankBean rank);

		void setRewardRank(RankBean rank);

	}

	public interface IPresenter extends BasePresenter {
	}
}