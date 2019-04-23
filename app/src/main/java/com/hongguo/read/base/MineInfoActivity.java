package com.hongguo.read.base;

import com.hongguo.read.mvp.contractor.mine.MineContractor;
import com.hongguo.common.model.userInfo.UserInfoBean;

/**
 * Created by losg on 2018/1/4.
 */

public abstract class MineInfoActivity extends ActivityEx implements MineContractor.IView{


    @Override
    public void setUserInfo(UserInfoBean.DataBean userInfo) {

    }

    @Override
    public void setUserIsVip(String timeout) {

    }

    @Override
    public void setUserIsSVip(String timeout) {

    }

    @Override
    public void setUserNormal() {

    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {

    }
}
