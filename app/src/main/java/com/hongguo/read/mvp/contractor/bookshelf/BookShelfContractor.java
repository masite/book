package com.hongguo.read.mvp.contractor.bookshelf;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.repertory.db.read.BookReader;

import java.util.List;

public class BookShelfContractor {

	public interface IView extends BaseViewEx {
		void setBookShelfBooks(List<BookReader> books) ;

		void initShelfHeader(String bookCover, String bookName, String readProcess);

        void setShelfHeaderBookIdInfo(String bookId, int bookType);
    	void setSignInfo(String signMessage, boolean sign);

        void showSignSuccessInfo(int gval, String signNumber);
    }

	public interface IPresenter extends BasePresenter {
	}
}