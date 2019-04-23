package com.hongguo.read.mvp.ui.loading;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by losg on 2018/4/11.
 * <p>
 * 初始页(啥都不要做，直接用来做跳转用，因为主题设置为background 会造成有所的view的背景都是该设置的值)
 * 主要解决 华为手机存在虚拟按键，如果设置 windowBackground 会造成 APP启动后与loading页 大小不一致导致闪屏的现象
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决每次点击logo重启的问题
        if (!isTaskRoot()) {
            finish();
            return;
        }
        //直接跳转到loading 页
        LoadingActivity.toActivity(SplashActivity.this);
        finish();
    }
}
