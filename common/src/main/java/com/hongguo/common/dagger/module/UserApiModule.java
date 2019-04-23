package com.hongguo.common.dagger.module;

import com.hongguo.common.constants.Constants;
import com.hongguo.common.dagger.scope.AppScope;
import com.hongguo.common.retrofit.api.UserApiService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by losg on 2018/3/6.
 */

@Module
public class UserApiModule {

    public UserApiModule() {
    }

    @AppScope
    @Provides
    public UserApiService provideSelfApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.Request.HOST)
                .client(okHttpClient)
                .build();
        return retrofit.create(UserApiService.class);
    }

}
