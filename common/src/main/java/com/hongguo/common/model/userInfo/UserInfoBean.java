package com.hongguo.common.model.userInfo;


public class UserInfoBean {


    /**
     * code : 0
     * msg : Successful
     * {"data":{"userId":216710,"avatar":"","nickName":"书友1491374375","gender":"","eMail":"","qQNo":"","mphNo":"","introSelf":""},"code":0,"msg":"Successful"}
     */

    public DataBean data;
    public int      code;
    public String   msg;
    public String   subcode;
    public String   subdesc;


    public static class DataBean {
        /**
         * userId : 207443
         * nickName : 书友1490703879
         * avatar :
         * isVip:false
         */

        public String  userId;
        public String  nickName;
        public String  avatar;
        public String  gender;
        public String  genderStr;
        public String  eMail;
        public String  qQNo;
        public String  mphNo;
        public String  introSelf;
        public String  hongguoId;
        public boolean isVip;
        public boolean isSVip;
        public String  loseTime;
        public String  vipLoseTime;
        public String  sVipLostTime;
        public String  scoreLevel;
        public String  growthLevel;
        public String  vipScore;
        public String  vipGrowth;
        public String  vipDiscount;


    }
}
