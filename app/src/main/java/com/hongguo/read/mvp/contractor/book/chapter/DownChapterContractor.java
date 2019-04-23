package com.hongguo.read.mvp.contractor.book.chapter;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;

public class DownChapterContractor {

	public interface IView extends BaseViewEx {
		void setTotalPrice(int price, int selectNumber) ;

		void notifyAdapter();

		void addDownChapter(ChapterBean.Chapters chapter);

		void allDown();

	}

	public interface IPresenter extends BasePresenter {
	}
}