package com.hongguo.read.dagger.component;

import android.content.Context;

import com.hongguo.common.dagger.module.BaseApiModule;
import com.hongguo.common.dagger.module.UserApiModule;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.AppScope;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.read.dagger.module.ApiModule;
import com.hongguo.read.dagger.module.AppModule;

import com.hongguo.read.retrofit.api.ApiService;

import dagger.Component;

/**
 * Created by losg on 2016/10/28.
 */
@AppScope
@Component(modules = {AppModule.class, BaseApiModule.class, ApiModule.class, UserApiModule.class})
public interface AppComponent {

    @ContextLife(value = ContextLife.Life.Application)
    Context getApplicationContext();

    @ApiLife(value = ApiLife.ApiFrom.SELFT)
    ApiService getSelfApiService();

    @ApiLife(value = ApiLife.ApiFrom.STRING)
    ApiService getStringApiService();

    UserApiService getUserApiService();

}
