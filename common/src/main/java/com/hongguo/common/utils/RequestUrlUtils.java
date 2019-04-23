package com.hongguo.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.hongguo.common.repertory.share.UserRepertory;
import com.losg.library.utils.AppUtils;
import com.losg.library.utils.MD5;

import okhttp3.FormBody;

/**
 * Created time 2017/12/7.
 *
 * @author losg
 */

public class RequestUrlUtils {

    private static final int    TIME_OUT = 10 * 60;
    private static final String KEY      = "6066e91f93ae69d30bdaf717e5cb3313";

    public static FormBody signBody(Context context, FormBody body, String url) {
        String state = "";
        String page = "";
        String count = "";
        String userId = "";
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (int i = 0; i < body.size(); i++) {
            String name = body.name(i);
            String value = body.value(i);
            formBuilder.add(name, value);
            if (name.equals("state")) {
                state = value;
            } else if (name.equals("page")) {
                page = value;
            } else if (name.equals("count")) {
                count = value;
            } else if (name.equals("userId")) {
                userId = value;
            }
        }
        int end = url.length();
        if (url.contains("?")) {
            end = url.indexOf("?");
        }
        String action = url.substring(url.lastIndexOf("/") + 1, end);

        //当前时间(秒)
        int second = (int) (System.currentTimeMillis() / 1000);
        //到期时间 10分钟过期
        second += TIME_OUT;
        //时间转成十六进制
        String hex = Integer.toHexString(second);

        if (TextUtils.isEmpty(state)) {
            state = MD5.md5(System.currentTimeMillis() + "").substring(25).toUpperCase();
            formBuilder.add("state", state);
        }

        String signUrl = action;

        if (!TextUtils.isEmpty(page)) {
            signUrl += "?count=" + count;
            signUrl += "&page=" + page;
            signUrl += "&state=" + state;
            signUrl += "&key=" + KEY + hex;
        } else {
            //没有直接用state 加密
            signUrl += "?state=" + state;
            signUrl += "&key=" + KEY + hex;
        }

        //每个接口添加userid 和 token信息
        if (!TextUtils.isEmpty(UserRepertory.getToken()) && !action.equals("UserOauth")) {
            if (TextUtils.isEmpty(userId))
                formBuilder.add("userId", UserRepertory.getUserID());
            formBuilder.add("token", UserRepertory.getToken());
        }

        formBuilder.add("ver", AppUtils.getVersionName(context));
        formBuilder.add("upf", "21");

        String sign = MD5.md5(signUrl).toLowerCase().substring(16) + hex;
        formBuilder.add("sign", sign);
        return formBuilder.build();
    }

}
