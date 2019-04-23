package com.hongguo.read.mvp.contractor.bookstore.channel;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.bookstore.channel.FreeChannelBean;

public class FreeChannelContractor {

	public interface IView extends BaseViewEx {
		void baiduFree(FreeChannelBean freeChannelBean);

	}

	public interface IPresenter extends BasePresenter {
	}
}