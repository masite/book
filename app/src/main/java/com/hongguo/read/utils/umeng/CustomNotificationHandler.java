package com.hongguo.read.utils.umeng;

import android.content.Context;

import com.hongguo.common.utils.webview.AppWebView;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

/**
 * Created by losg on 2018/4/8.
 */

public class CustomNotificationHandler extends UmengNotificationClickHandler {


    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        super.dealWithCustomAction(context, msg);
        JSONObject raw = msg.getRaw();
        try {
            JSONObject jsonObject= (JSONObject) raw.get("extra");
            if(jsonObject == null) return;
            String url = (String) jsonObject.get("jumpVC");
            AppWebView.toActivity(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
