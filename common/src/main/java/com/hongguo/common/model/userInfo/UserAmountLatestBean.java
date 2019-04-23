package com.hongguo.common.model.userInfo;


public class UserAmountLatestBean {


    /**
     * data : {"userId":207443,"sumAmount":1}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {
        /**
         * userId : 207443
         * sumAmount : 1
         * giveAmount:0
         * loseTime :/Date(1501058393100)/
         */

        public String userId;
        public String sumAmount;
        public String giveAmount;
        public String loseTime;

    }
}
