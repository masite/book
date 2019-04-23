package com.hongguo.read.dagger.module;

import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.AppScope;
import com.hongguo.common.retrofit.convert.StringConverterFactory;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.retrofit.api.ApiService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by losg on 2016/10/29.
 */

@Module
public class ApiModule {

    @AppScope
    @Provides
    @ApiLife(value = ApiLife.ApiFrom.STRING)
    public ApiService provideStringApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(new StringConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.Request.HOST)
                .client(okHttpClient).build();
        return retrofit.create(ApiService.class);
    }

    @AppScope
    @Provides
    @ApiLife(value = ApiLife.ApiFrom.SELFT)
    public ApiService provideSelfApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.Request.HOST)
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiService.class);
    }

}
