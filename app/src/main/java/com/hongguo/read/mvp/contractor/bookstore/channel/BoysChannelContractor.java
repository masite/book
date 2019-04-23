package com.hongguo.read.mvp.contractor.bookstore.channel;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.model.topic.TopicBean;

public class BoysChannelContractor {

    public interface IView extends BaseViewEx {
        void setRecommendBook(RecommendItemBean.ObjBean recommendItem) ;

        void setNewBook(RecommendItemBean.ObjBean newbook) ;

        void setFinishBook(RecommendItemBean.ObjBean finishBook) ;

        void setFantasyItem(RecommendItemBean.ObjBean romanticItem);

        void setUrbanItem(RecommendItemBean.ObjBean ancientItem);

    }

    public interface IPresenter extends BasePresenter {
    }
}