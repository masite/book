package com.hongguo.read.mvp.contractor.bookstore.channel;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.model.topic.TopicBean;

public class GirlsChannelContractor {

	public interface IView extends BaseViewEx {

		void setRomanticItem(RecommendItemBean.ObjBean romanticItem) ;

		void setAncientItem(RecommendItemBean.ObjBean ancientItem) ;

		void setRecommendBook(RecommendItemBean.ObjBean recommendItem) ;

		void setNewBook(RecommendItemBean.ObjBean newbook) ;

		void setFinishBook(RecommendItemBean.ObjBean finishBook) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}