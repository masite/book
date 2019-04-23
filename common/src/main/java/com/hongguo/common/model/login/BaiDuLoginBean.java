package com.hongguo.common.model.login;

/**
 * Created by losg on 2018/3/27.
 */

public class BaiDuLoginBean {


    /**
     * msg : success
     * code : 0
     * result : {"Token":"b1faabe0b681c872274f1d0d8d99a93b","Oauthkey":"MTMMAjE0MTQ4","Login_Type":"youxun","Gender":"0","Has_Psw":1,"UID":"217482413","ChannelId":"1","AppNewer":"0","Nick":"214148","Third_Name":"214148","Balance":"0","isNew":0,"Create_Time":"2017-06-22 09:53:35"}
     */

    public String msg;
    public int        code;
    public ResultBean result;

    public static class ResultBean {
        /**
         * Token : b1faabe0b681c872274f1d0d8d99a93b
         * Oauthkey : MTMMAjE0MTQ4
         * Login_Type : youxun
         * Gender : 0
         * Has_Psw : 1
         * UID : 217482413
         * ChannelId : 1
         * AppNewer : 0
         * Nick : 214148
         * Third_Name : 214148
         * Balance : 0
         * isNew : 0
         * Create_Time : 2017-06-22 09:53:35
         */

        public String Token;
        public String Oauthkey;
        public String Login_Type;
        public String Gender;
        public int    Has_Psw;
        public String UID;
        public String ChannelId;
        public String AppNewer;
        public String Nick;
        public String Third_Name;
        public String Balance;
        public int    isNew;
        public String Create_Time;
    }
}
