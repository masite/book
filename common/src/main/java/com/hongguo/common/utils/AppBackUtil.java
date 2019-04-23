package com.hongguo.common.utils;

import android.os.Process;

import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.losg.library.base.BaActivity;

/**
 * Created by Administrator on 2018/2/12.
 */

public class AppBackUtil {

    private boolean mExit = false;
    private BaActivity mActivity;

    public AppBackUtil(BaActivity activity) {
        mActivity = activity;
    }

    public void onBackPress(){
        if(mExit){
            RxJavaUtils.delayRun(300, () -> Process.killProcess(Process.myPid()));
            mActivity.finish();
        }else{
            mExit = true;
            RxJavaUtils.delayRun(1000, ()-> mExit = false);
            mActivity.toastMessage("再按一次退出红果阅读");
        }
    }

}
