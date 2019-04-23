package com.hongguo.read.baidu;

import android.text.TextUtils;

import com.hongguo.common.base.CommonBean;
import com.hongguo.common.model.login.BaiDuLoginBean;
import com.hongguo.common.model.login.UserbindBean;
import com.hongguo.common.retrofit.api.UserApiService;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.repertory.share.BaiduRepertory;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;

/**
 * Created time 2017/11/23.
 *
 * @author losg
 */

public class ShuChengManager {

    public static final String BAI_DU_APPID  = "10016";
    public static final String BAI_DU_SECRET = "e85ce22607a99b7c56fcab0f19f6d7f3";

    /**
     * 百度书城 绑定现有用户
     *
     * @param userId
     * @param accessToken
     * @param apiService
     */
    public static void shuchengLogin(String userId, String accessToken, UserApiService apiService) {
        queryBind(userId, accessToken, apiService);
    }

    /**
     * 获取用户百度绑定信息
     *
     * @param userId
     * @param accessToken
     */
    private static void queryBind(String userId, String accessToken, UserApiService apiService) {
        apiService.baiduBind(userId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<UserbindBean>(){
            @Override
            public void onNext(UserbindBean userbindBean) {
                super.onNext(userbindBean);
                if (userbindBean != null) {
                    //服务器没有该用户绑定的信息
                    if (userbindBean.data == null || userbindBean.data.baidu == null || TextUtils.isEmpty(userbindBean.data.baidu.userKey)) {
                        bindBaidu("", userId, accessToken, apiService);
                    } else {
                        bindBaidu(userbindBean.data.baidu.userKey, userId, accessToken, apiService);
                    }
                }
            }
        });
    }

    /**
     * 绑定百度账号
     *
     * @param key
     * @param userId
     */
    private static void bindBaidu(String key, String userId, String token, UserApiService apiService) {
        apiService.baiduLogin(BaiduShuChengUrls.getLoginUrl(userId, key)).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<BaiDuLoginBean>(){
            @Override
            public void onNext(BaiDuLoginBean userbindBean) {
                if (userbindBean.code == 0 && userbindBean.result != null) {
                    BaiduRepertory.setBaiduPayEXTRA("10016-" + userbindBean.result.UID);
                    saveKey(userId, userbindBean.result.Oauthkey, apiService);
                    BaiduRepertory.setBaiduUserInfo(userId + "@" + userbindBean.result.Token);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 将百度对应的秘钥保存在服务器上
     */
    private static void saveKey(String userId, String authKey, UserApiService apiService) {
        apiService.saveBaiduPassword(userId, authKey).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<CommonBean>());
    }

}
