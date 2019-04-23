package com.hongguo.read.base;

import com.hongguo.common.base.BaseViewEx;
import com.hongguo.common.base.Presenter;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.read.retrofit.api.ApiService;

/**
 * Created by losg on 2016/10/29.
 */

public abstract class UserApiPresenter<T extends BaseViewEx> extends Presenter<T> {

    protected UserApiService mApiService;


    public UserApiPresenter(UserApiService apiService) {
        super();
        mApiService = apiService;
    }
}
