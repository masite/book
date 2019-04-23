package com.hongguo.read.mvp.contractor.mine;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.mine.SkinBean;

import java.util.List;

public class SkinContractor {

    public interface IView extends BaseViewEx {
        void setCurrentSelected(String skinName);

        void setSkinList(List<SkinBean.SkinlistBean> skinList);
    }

    public interface IPresenter extends BasePresenter {
    }
}