package com.hongguo.read.mvp.ui.vip;

import android.content.Context;
import android.content.Intent;

import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.mvp.ui.MainActivity;

/**
 * Created by losg on 2018/2/12.
 */

public class VipActivity extends ActivityEx {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, VipActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        setTitle("会员中心");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.vip_content, VipFragment.getInstance(false))
                .commit();
    }
}
