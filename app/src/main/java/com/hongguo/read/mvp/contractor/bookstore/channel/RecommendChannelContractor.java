package com.hongguo.read.mvp.contractor.bookstore.channel;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;

public class RecommendChannelContractor {

    public interface IView extends BaseViewEx {

		void setBookFavor(RecommendItemBean.ObjBean objBean);

		void setHotsRecommend(RecommendItemBean.ObjBean objBean) ;

		void setHotsBook(RecommendItemBean.ObjBean objBean) ;

		void setNewBooks(RecommendItemBean.ObjBean objBean) ;

		void setRecomend(RecommendItemBean.ObjBean objBean) ;

	}

    public interface IPresenter extends BasePresenter {
    }
}