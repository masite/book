package com.hongguo.read.eventbus;

import android.support.v4.app.Fragment;

/**
 * Created by losg
 */

public class VIPStoreRefreshEvent {

    public Fragment mFragment;

    public VIPStoreRefreshEvent(Fragment fragment){
        mFragment = fragment;
    }
}
