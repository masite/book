package com.hongguo.common.dagger.scope;

import javax.inject.Qualifier;

/**
 * Created by losg on 2017/7/18.
 */
@Qualifier
public @interface ApiLife {

    enum ApiFrom {
        SELFT,
        STRING
    }

    ApiFrom value() default ApiFrom.SELFT;
}
