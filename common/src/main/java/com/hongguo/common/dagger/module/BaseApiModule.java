package com.hongguo.common.dagger.module;

import android.content.Context;

import com.hongguo.common.dagger.scope.AppScope;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.utils.RequestUrlUtils;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by losg on 2018/3/6.
 */

@Module
public class BaseApiModule {

    private boolean mIsDebug = false;

    public BaseApiModule(boolean isDebug) {
        mIsDebug = isDebug;
    }

    @AppScope
    @Provides
    public OkHttpClient provideClient(@ContextLife(value = ContextLife.Life.Application) Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.readTimeout(1, TimeUnit.MINUTES);
        if (!mIsDebug) {
            builder.proxy(Proxy.NO_PROXY);
        }
        builder.addInterceptor(chain -> {
            Request request = chain.request();
            if (!request.method().equals("POST")) return chain.proceed(request);
            RequestBody body = request.body();
            if (body instanceof FormBody) {
                FormBody formBody = RequestUrlUtils.signBody(context, (FormBody) body, request.url().encodedPath());
                Request encodeRequest = request.newBuilder().post(formBody).build();
                return chain.proceed(encodeRequest);
            }
            return chain.proceed(request);
        });
        return builder.build();
    }
}
