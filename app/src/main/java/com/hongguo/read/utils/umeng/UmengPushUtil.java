package com.hongguo.read.utils.umeng;

import android.content.Context;

import com.hongguo.common.constants.Constants;
import com.hongguo.common.utils.LogUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;

/**
 * Created by losg on 2018/5/2.
 */

public class UmengPushUtil {

    public static void init(Context context){
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, Constants.UMENG.APP_SECRET);
        PushAgent pushAgent = PushAgent.getInstance(context);
        pushAgent.setMessageHandler(new UmengMessageHandler());
        pushAgent.setNotificationClickHandler(new CustomNotificationHandler());
        //注册推送服务，每次调用register方法都会回调该接口
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.log(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.log("推送启动失败" + s + s1);
            }
        });
    }
}
