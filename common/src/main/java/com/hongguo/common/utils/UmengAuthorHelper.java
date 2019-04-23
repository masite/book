package com.hongguo.common.utils;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by losg on 2018/1/9.
 */

public class UmengAuthorHelper {

    public static final int AUTHOR_QQ     = 0;
    public static final int AUTHOR_WEIXIN = 1;

    private WeakReference<Activity>                 mActivityWeakReference;
    private WeakReference<UmAuthorResponseListener> mUmAuthorResponseListener;

    public UmengAuthorHelper(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
    }

    public void author(int authorType) {
        SHARE_MEDIA authorMedia = authorType == AUTHOR_QQ ? SHARE_MEDIA.QQ : SHARE_MEDIA.WEIXIN;
        if (mActivityWeakReference.get() == null) return;
        UMShareAPI.get(mActivityWeakReference.get()).getPlatformInfo(mActivityWeakReference.get(), authorMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                UmAuthorResponseListener umAuhorResponseListener = mUmAuthorResponseListener.get();
                if (umAuhorResponseListener == null) return;
                umAuhorResponseListener.authorStart();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                UmAuthorResponseListener umAuhorResponseListener = mUmAuthorResponseListener.get();
                Activity activity = mActivityWeakReference.get();
                if (activity == null || umAuhorResponseListener == null) return;
                AuthorResult authorResult = new AuthorResult();
                authorResult.parse(map);
                umAuhorResponseListener.authorComplete(authorType, authorResult);

                //第三方取消授权
                UMShareAPI.get(mActivityWeakReference.get()).deleteOauth(activity, authorMedia, null);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                UmAuthorResponseListener umAuhorResponseListener = mUmAuthorResponseListener.get();
                if (umAuhorResponseListener == null) return;
                umAuhorResponseListener.authorError();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                UmAuthorResponseListener umAuhorResponseListener = mUmAuthorResponseListener.get();
                if (umAuhorResponseListener == null) return;
                umAuhorResponseListener.authorCancel();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (mActivityWeakReference.get() == null) return;
        UMShareAPI.get(mActivityWeakReference.get()).onActivityResult(requestCode, resultCode, data);
    }

    public void destory() {
        if (mActivityWeakReference.get() == null) return;
        UMShareAPI.get(mActivityWeakReference.get()).release();
    }

    public void setUmAuhorResponseListener(UmAuthorResponseListener umAuthorResponseListener) {
        mUmAuthorResponseListener = new WeakReference<UmAuthorResponseListener>(umAuthorResponseListener);
    }

    public interface UmAuthorResponseListener {

        void authorStart();

        void authorComplete(int authorType, AuthorResult authorResult);

        void authorError();

        void authorCancel();
    }

    public static class AuthorResult {
        public String uid;
        public String openid;
        public String unionid;

        public String name;
        public String screen_name;
        public String iconurl;
        public String profile_image_url;
        public String gender;

        public String city;
        public String province;

        public String accessToken;
        public String access_token;
        public String refreshToken;

        public void parse(Map<String, String> map) {
            uid = map.get("uid");
            openid = map.get("openid");
            unionid = map.get("unionid");
            name = map.get("name");
            screen_name = map.get("screen_name");
            iconurl = map.get("iconurl");
            profile_image_url = map.get("profile_image_url");
            gender = map.get("gender");
            city = map.get("city");
            province = map.get("province");
            accessToken = map.get("accessToken");
            access_token = map.get("access_token");
            refreshToken = map.get("refreshToken");
        }
    }
}
