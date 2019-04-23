package com.hongguo.read.mvp.contractor.booktype;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.repertory.data.ClassifyRepertory;

import java.util.List;

public class BookTypeDetailContractor {

	public interface IView extends BaseViewEx {
		void setType( List<ClassifyRepertory.Classify> classifies);

		void setBooks(List<BookStoreBookBean.DataBean> dataBeans);

	}

	public interface IPresenter extends BasePresenter {
	}
}