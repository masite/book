package com.hongguo.read.mvp.contractor.bookstore.channel;


import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.search.RankBean;

import java.util.List;

public class SortDetailContractor {

    public interface IView extends BaseViewEx {
        void setRank(List<RankBean.DataBean> ranks, boolean clear);

    }

    public interface IPresenter extends BasePresenter {
    }
}