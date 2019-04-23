package com.hongguo.common.eventbus;

/**
 * Created by losg on 2017/7/31.
 */

public class DataUpdateEvent {

    public Class clazz;

    public DataUpdateEvent(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isSelf(Class clazz){
        return this.clazz == clazz;
    }
}
