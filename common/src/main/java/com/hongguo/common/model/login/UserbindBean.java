package com.hongguo.common.model.login;

/**
 * Created by losg on 2017/6/6.
 */

public class UserbindBean {

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {
        public BaiduBean baidu;

        public static class BaiduBean {
            public int    userId;
            public String userKey;
            public String getTime;
        }
    }
}
