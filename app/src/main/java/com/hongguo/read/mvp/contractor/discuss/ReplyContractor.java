package com.hongguo.read.mvp.contractor.discuss;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;

public class ReplyContractor {

	public interface IView extends BaseViewEx {
		void setBookDiscussInfo(BookDiscussBean bookDiscussInfo) ;

		void repaySuccess();

	}

	public interface IPresenter extends BasePresenter {
	}
}