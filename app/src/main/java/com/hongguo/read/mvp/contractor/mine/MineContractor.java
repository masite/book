package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.common.model.userInfo.UserInfoBean;

public class MineContractor {

	public interface IView extends BaseViewEx {
		void setUserInfo(UserInfoBean.DataBean userInfo);

		void setUserIsVip(String timeout);

		void setUserIsSVip(String timeout);

		void setUserNormal();

		void setCoins(int currentCoin, int giveCoin);

	}

	public interface IPresenter extends BasePresenter {
	}
}