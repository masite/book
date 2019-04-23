package com.hongguo.read.mvp.contractor.bookstore.channel;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.repertory.data.ClassifyRepertory;

import java.util.List;

public class TypeChannelContractor {

	public interface IView extends BaseViewEx {
		void setBookType(List<ClassifyRepertory.Classify> classifies);

	}

	public interface IPresenter extends BasePresenter {
	}
}