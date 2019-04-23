package com.hongguo.read.mvp.model.mine.recharge;

/**
 * Created by losg on 2018/1/5.
 */

public class OrderInfoBean {

    /**
     * data : {"userId":207443,"orderNo":"1703293151490780667207443780","orderName":"红果充值中心-红果币充值","uaType":315,"payMethod":"alipay","payAmount":1,"payUrl":"http://m.hgread.com/opay/alipay?orderNo=1703293151490780667207443780&orderName=%e7%ba%a2%e6%9e%9c%e5%85%85%e5%80%bc%e4%b8%ad%e5%bf%83-%e7%ba%a2%e6%9e%9c%e5%b8%81%e5%85%85%e5%80%bc&payAmount=1&userId=207443&uaType=315&callbackUrl=http%3a%2f%2fm.hgread.com%2fobook%2fapi%2fhgreadapptest%2fTradeCreateReturn%3forderNo%3d1703293151490780667207443780"}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;
    public String   subcode;
    public String   subdesc;

    public static class DataBean {
        /**
         * userId : 207443
         * orderNo : 1703293151490780667207443780
         * orderName : 红果充值中心-红果币充值
         * uaType : 315
         * payMethod : alipay
         * payAmount : 1
         * payUrl : http://m.hgread.com/opay/alipay?orderNo=1703293151490780667207443780&orderName=%e7%ba%a2%e6%9e%9c%e5%85%85%e5%80%bc%e4%b8%ad%e5%bf%83-%e7%ba%a2%e6%9e%9c%e5%b8%81%e5%85%85%e5%80%bc&payAmount=1&userId=207443&uaType=315&callbackUrl=http%3a%2f%2fm.hgread.com%2fobook%2fapi%2fhgreadapptest%2fTradeCreateReturn%3forderNo%3d1703293151490780667207443780
         */

        public String userId;
        public String orderNo;
        public String orderName;
        public String uaType;
        public String payMethod;
        public String payAmount;
        public String payUrl;
    }
}
