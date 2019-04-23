package com.hongguo.common.constants;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class Constants {

    public static class Request {

        /**
         * 请求主 HOST
         */
        public static String HOST = "http://m.hgread.com/";

    }


    public static class PayType {

        /**
         * 支付宝
         */
        public static final String ALIPAY  = "alipay";
        /**
         * 微信
         */
        public static final String WEI_XIN = "jtpay";

        /**
         * 未知
         */
        public static final String UNKNOW = "unknow";
    }

    public static final class AUHOR_TYPE {

        /**
         * 设备号登录(默认)
         */
        public static final String DEVICE_ID = "";

        /**
         * 微信授权登录
         */
        public static final String WEIXIN = "310";

        /**
         * QQ授权登录
         */
        public static final String QQ = "313";


        /**
         * 账号密码登录
         */
        public static final String NAME_PASSWORD = "200";
    }


    public static final class UMENG{
        public static String APP_KEY = "5a1be31fa40fa328a90000a7";
        public static String APP_SECRET = "33498552cea3a21e9bd3057f04730be9";
    }
}
