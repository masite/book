package com.hongguo.read.eventbus;

import android.text.TextUtils;

/**
 * Created by losg on 2018/1/5.
 */

public class PayCheckEvent {

    public String mError;
    public String mOwer;

    public PayCheckEvent(String error, String ower) {
        mError = error;
        mOwer = ower;
    }

    public boolean checkSelf(Class clazz){
        if(TextUtils.isEmpty(mOwer)) return false;
        return mOwer.equals(clazz.getSimpleName());
    }
}
