package com.hongguo.read.dagger.module;

import android.content.Context;
import android.support.v4.app.Fragment;


import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by losg on 2016/11/1.
 */

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @FragmentScope
    @ContextLife(value = ContextLife.Life.Activity)
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }

    @Provides
    @FragmentScope
    public Fragment provideFragment() {
        return mFragment;
    }

}
