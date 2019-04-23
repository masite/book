package com.hongguo.read.mvp.contractor.search;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.model.search.SearchBean;

import java.util.List;

public class SearchContractor {

	public interface IView extends BaseViewEx {
		void setHotSearch(String[] hots);

		void setSuggestBooks(List<RankBean.DataBean> dataBeans);

		void setSearchResult(List<SearchBean.DataBean> dataBeans) ;

		void setHistory(String[] history) ;

		void setHistoryGroupVisiable(boolean visiable);

	}

	public interface IPresenter extends BasePresenter {
	}
}