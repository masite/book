package com.hongguo.common.model.userInfo;

/**
 * Created by Administrator on 2017/6/30.
 */

public class UserBindInfo {

    /**
     * data : {"baidu":{"userId":216710,"userKey":"MMNjgjE2NzEw","getTime":"/Date(1498812454520)/"},"hgread":{"userId":3914,"nickName":"书友1491374375","getTime":"/Date(1498812454520)/","uat315":{"uaType":315,"nickName":"书友1491374375","openId":"869435021800725","unionId":"869435021800725"},"uat315313":{"uaType":315,"nickName":null,"openId":"313-46D929F5D67E8233ED4B5F9A7BCAB04F","unionId":"313-46D929F5D67E8233ED4B5F9A7BCAB04F"},"uat310":{"uaType":310,"nickName":"WilliamsY","openId":"oCdQ20fYVUdRhUmVCAaGQ43kZCs8","unionId":"oCdQ20fYVUdRhUmVCAaGQ43kZCs8"}}}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {

        public HgreadBean hgread;

        public static class HgreadBean {
            /**
             * userId : 3914
             * nickName : 书友1491374375
             * getTime : /Date(1498812454520)/
             * 设备号
             * uat315 : {"uaType":315,"nickName":"书友1491374375","openId":"869435021800725","unionId":"869435021800725"}
             * QQ
             * uat315313 : {"uaType":315,"nickName":null,"openId":"313-46D929F5D67E8233ED4B5F9A7BCAB04F","unionId":"313-46D929F5D67E8233ED4B5F9A7BCAB04F"}
             * 微信
             * uat310 : {"uaType":310,"nickName":"WilliamsY","openId":"oCdQ20fYVUdRhUmVCAaGQ43kZCs8","unionId":"oCdQ20fYVUdRhUmVCAaGQ43kZCs8"}
             */

            public int        userId;
            public String     nickName;
            public String     getTime;
            public AuthorInfo uat315;
            public AuthorInfo uat315313;
            public AuthorInfo uat310;

            public static class AuthorInfo {
                /**
                 * uaType : 315
                 * nickName : 书友1491374375
                 * openId : 869435021800725
                 * unionId : 869435021800725
                 */

                public int    uaType;
                public String nickName;
                public String openId;
                public String unionId;
            }

            public AuthorInfo getQQAuthorInfo(){
                return uat315313;
            }

            public AuthorInfo getWeiXinAuthorInfo(){
                return uat310;
            }
        }
    }
}
