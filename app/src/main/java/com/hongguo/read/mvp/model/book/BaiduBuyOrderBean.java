package com.hongguo.read.mvp.model.book;

/**
 * Created by Administrator on 2017/6/2.
 */

public class BaiduBuyOrderBean {
    /**
     * msg : ok
     * code : 0
     * result : {"order_id":"1706021000026","order_name":"购买章节","order_money":20}
     */

    public String     msg;
    public int        code;
    public ResultBean result;

    public static class ResultBean {
        /**
         * order_id : 1706021000026
         * order_name : 购买章节
         * order_money : 20
         */

        public String order_id;
        public String order_name;
        public int    order_money;
    }
}
