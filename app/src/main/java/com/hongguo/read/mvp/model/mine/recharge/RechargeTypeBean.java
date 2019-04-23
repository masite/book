package com.hongguo.read.mvp.model.mine.recharge;

/**
 * Created by losg on 2017/12/28.
 */

public class RechargeTypeBean {

    /**
     * paySupport : {"weixin":"true","alipay":"true"}
     */

    public PaySupportBean paySupport;

    public static class PaySupportBean {
        /**
         * weixin : true
         * alipay : true
         */

        public boolean weixin;
        public boolean alipay;
    }
}
