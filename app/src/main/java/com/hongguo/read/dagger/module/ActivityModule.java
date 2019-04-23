package com.hongguo.read.dagger.module;

import android.app.Activity;
import android.content.Context;


import com.hongguo.common.dagger.scope.ActivityScope;
import com.hongguo.common.dagger.scope.ContextLife;

import dagger.Module;
import dagger.Provides;

/**
 * Created by losg on 2016/10/28.
 */
@Module
public class ActivityModule {

    private Context mContext;

    public ActivityModule(Activity activity) {
        mContext = activity;
    }

    @ActivityScope
    @Provides
    @ContextLife(value = ContextLife.Life.Activity)
    public Context provideActivity() {
        return mContext;
    }

}
