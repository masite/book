package com.hongguo.read.base;

import com.hongguo.common.base.BaseViewEx;
import com.hongguo.common.base.Presenter;
import com.hongguo.read.retrofit.api.ApiService;

/**
 * Created by losg on 2016/10/29.
 */

public abstract class BaseImpPresenter<T extends BaseViewEx> extends Presenter<T> {

    protected ApiService mApiService;


    public BaseImpPresenter(ApiService apiService) {
        super();
        mApiService = apiService;
    }

}
