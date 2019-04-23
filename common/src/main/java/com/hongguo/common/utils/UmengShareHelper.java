package com.hongguo.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by losg on 17-12-13.
 */

public class UmengShareHelper {

    private Context             mContext;
    private CustomShareListener mShareListener;
    private ShareAction         mShareAction;

    public UmengShareHelper(Context context) {
        mContext = context;
    }

    public void showShare(String title, String url, String describe, String image) {
        mShareListener = new CustomShareListener(mContext);
        mShareAction = new ShareAction((Activity) mContext).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {
                    UMWeb web = new UMWeb(url);
                    web.setTitle(title);
                    web.setDescription(describe);
                    web.setThumb(new UMImage(mContext, image));
                    new ShareAction((Activity) mContext).withMedia(web)
                            .setPlatform(share_media)
                            .setCallback(mShareListener)
                            .share();
                });
        mShareAction.open();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mShareAction.close();
    }

    public void onDestroy() {
        UMShareAPI.get(mContext).release();
    }

    private static class CustomShareListener implements UMShareListener {

        private Context mContext;

        public CustomShareListener(Context context) {
            mContext = context;
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if ("WEIXIN_FAVORITE".equals(platform.name())) {
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }


}
