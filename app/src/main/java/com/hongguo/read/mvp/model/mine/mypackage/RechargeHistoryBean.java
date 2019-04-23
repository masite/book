package com.hongguo.read.mvp.model.mine.mypackage;

import com.hongguo.common.base.CommonBean;

import java.util.List;

public class RechargeHistoryBean extends CommonBean{
    /**
     * data : [{"userId":"207443","payMethod":"Alipay","payAmount":1,"orderNo":"1703293151490767179207443388","orderName":"红果充值中心-红果币充值","tradeNo":"2017032921001004800270799550","callbackDate":"2017-03-29 14:02:32","createDate":"2017-03-29 14:01:37"}]
     * code : 0
     * msg : Successful
     */

    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * userId : 207443
         * payMethod : Alipay
         * payAmount : 1
         * orderNo : 1703293151490767179207443388
         * orderName : 红果充值中心-红果币充值
         * tradeNo : 2017032921001004800270799550
         * callbackDate : 2017-03-29 14:02:32
         * createDate : 2017-03-29 14:01:37
         */

        public String userId;
        public String payMethod;
        public int    payAmount;
        public String orderNo;
        public String orderName;
        public String tradeNo;
        public String callbackDate;
        public String createDate;
    }
}