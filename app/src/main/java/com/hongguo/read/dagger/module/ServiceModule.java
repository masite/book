package com.hongguo.read.dagger.module;

import android.app.Service;


import com.hongguo.common.dagger.scope.ServiceScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by losg on 2016/11/5.
 */

@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @ServiceScope
    public Service provideService(){
        return mService;
    }
}
