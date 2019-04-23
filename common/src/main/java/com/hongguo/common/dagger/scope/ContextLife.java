package com.hongguo.common.dagger.scope;

import javax.inject.Qualifier;

/**
 * Created by losg on 2016/10/29.
 */
@Qualifier
public @interface ContextLife {

    enum Life{
        Application,
        Activity
    }

    Life value() default Life.Application;

}
