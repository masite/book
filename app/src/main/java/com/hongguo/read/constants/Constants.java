package com.hongguo.read.constants;

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

        /**
         * loading 背景图片
         */
        public static String LOADING_HOST = "http://x1.hgread.com/qidong/QD.json";

        /**
         * 常见问题
         */
        public static String COMMON_QUESTION = "http://x1.hgread.com/faq/faq.json";

    }

    /**
     * 支付方式
     */
    public static final class PAY_TYPE {
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


    /**
     * 性别
     */
    public static final class USER_SEX {
        /**
         * 男
         */
        public static final String BOY  = "0";
        /**
         * 女
         */
        public static final String GIRL = "1";
    }

    /**
     * 更新方式
     */
    public static final class UPDATE_TYPE {
        /**
         * 强制更新
         */
        public static final int FORCE_UPDATE  = 0;
        /**
         * 默认更新
         */
        public static final int NORMAL_UPDATE = 1;
    }

    /**
     * 书本免费说明
     */
    public static class BOOK_FREE_TYPE {
        /**
         * 限免图书
         */
        public static final int BOOK_LIMIT_FREE     = 3030;
        /**
         * VIP限免图书
         */
        public static final int BOOK_VIP_LIMIT_FREE = 3230;

        /**
         * SVIP限免图书
         */
        public static final int BOOK_SVIP_LIMIT_FREE = 3330;
    }


    /**
     * 书籍来源
     */
    public static class BOOK_FROM {
        /**
         * 我们自己的书籍
         */
        public static final int FROM_SLEF  = 0;
        /**
         * 百度书籍
         */
        public static final int FROM_BAIDU = 1;
    }

    /**
     * 专题分类
     */
    public static class TOPIC_TYPE {
        /**
         * 男生专题
         */
        public static final int TOPIC_BOY   = 0;
        /**
         * 女生专题
         */
        public static final int TOPIC_GIRLS = 1;
    }


    /**
     * 统计支付类型
     */
    public static class STATISTICS_PAY_TYPE {
        public static final int RECHARGE        = 0;
        public static final int VIP_RECHARGE    = 1;
        public static final int SVIP_RECHARGE   = 2;
        public static final int REWARD_RECHARGE = 3;

    }
}
