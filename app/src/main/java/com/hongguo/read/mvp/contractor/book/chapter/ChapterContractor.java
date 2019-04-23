package com.hongguo.read.mvp.contractor.book.chapter;


import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;

import java.util.List;

public class ChapterContractor {

	public interface IView extends BaseViewEx {
		void setChapters(List<ChapterBean.Chapters> items) ;

		void setPageChapters(List<ChapterBean.Chapters> items);

		void nativeChapters(List<ChapterBean.Chapters> items);

	}

	public interface IPresenter extends BasePresenter {

		void checkUpate(String bookId, String bookName, String addChapterTime, String bookCover);
		void queryAllChapter(String bookId, String bookName, String bookCover, String updateTime, boolean needLoading);
		void queryChapter(String bookid);
	}
}