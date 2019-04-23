package com.hongguo.read.dagger.module;

import android.content.Context;


import com.hongguo.common.dagger.scope.AppScope;
import com.hongguo.common.dagger.scope.ContextLife;

import dagger.Module;
import dagger.Provides;

/**
 * Created by losg on 2016/10/28.
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @AppScope
    @ContextLife(value = ContextLife.Life.Application)
    public Context provoideContext() {
        return mContext;
    }


}
