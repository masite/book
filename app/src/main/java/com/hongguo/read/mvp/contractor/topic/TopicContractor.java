package com.hongguo.read.mvp.contractor.topic;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.topic.TopicBean;

import java.util.List;

public class TopicContractor {

	public interface IView extends BaseViewEx {
		void setTopic(List<TopicBean.ZtlistBean> topic) ;

	}

	public interface IPresenter extends BasePresenter {
	}
}