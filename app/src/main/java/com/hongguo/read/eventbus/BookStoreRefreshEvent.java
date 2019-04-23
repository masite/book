package com.hongguo.read.eventbus;

import android.support.v4.app.Fragment;

/**
 * Created by losg
 */

public class BookStoreRefreshEvent {

    public Fragment mFragment;

    public BookStoreRefreshEvent(Fragment fragment){
        mFragment = fragment;
    }
}
