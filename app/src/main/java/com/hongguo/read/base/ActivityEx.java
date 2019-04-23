package com.hongguo.read.base;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hongguo.common.base.BaseActivity;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.BaApp;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.dagger.component.DaggerActivityComponent;
import com.hongguo.read.dagger.module.ActivityModule;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.mvp.ui.loading.LoadingActivity;
import com.hongguo.read.utils.AndroidInstaller;
import com.umeng.message.PushAgent;


/**
 * Created by losg on 2017/7/17.
 */

public abstract class ActivityEx extends BaseActivity implements BaseViewEx {

    private ActivityComponent mActivityComponent;
    private AndroidInstaller mAndroidInstaller;

    @Override
    protected void bindView() {
        super.bindView();
        mAndroidInstaller = new AndroidInstaller();
        mActivityComponent = DaggerActivityComponent.builder().appComponent(findApp().getAppComponent()).activityModule(new ActivityModule(this)).build();
        inject(mActivityComponent);
    }

    @Override
    protected void initView() {
        PushAgent.getInstance(this).onAppStart();
    }

    protected boolean activityAddSwipBackLayout() {
        return !(this instanceof LoadingActivity || this instanceof MainActivity || this instanceof BookReaderActivity);
    }

    protected void inject(ActivityComponent mActivityComponent) {
    }

    public BaApp findApp(){
        return (BaApp) getApplication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAndroidInstaller.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mAndroidInstaller.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void installApk(String path){
        mAndroidInstaller.intallApk(mContext, path);
    }
}
