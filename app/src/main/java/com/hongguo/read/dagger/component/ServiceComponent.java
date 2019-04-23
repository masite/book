package com.hongguo.read.dagger.component;


import com.hongguo.common.dagger.scope.ServiceScope;
import com.hongguo.read.dagger.module.ServiceModule;

import dagger.Component;

/**
 * Created by losg on 2016/11/1.
 */
@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    

}
