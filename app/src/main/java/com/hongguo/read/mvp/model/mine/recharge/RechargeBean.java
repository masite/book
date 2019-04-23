package com.hongguo.read.mvp.model.mine.recharge;

import java.util.List;

public class RechargeBean {
    public int            code;
    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 102
         * name : 充值欢乐送
         * key : cfcd208495d565ef66e7dff9f98764da
         * summary : 充值欢乐送描述
         * link :
         * sdate : /Date(1501484882657)/
         * edate : /Date(3730345682657)/
         * rules : [{"id":100,"type":0,"name":"28元=2800红果币","tips":"","value":2800,"gvalue":0,"discount":1},{"id":102,"type":1,"name":"50元=5000红果币","tips":"+300红果币","value":5000,"gvalue":300,"discount":1},{"id":103,"type":1,"name":"100元=10000红果币","tips":"+800红果币","value":10000,"gvalue":800,"discount":1},{"id":104,"type":1,"name":"300元=30000红果币","tips":"+3000红果币","value":30000,"gvalue":3000,"discount":1}]
         */
        public String          id;
        public String          name;
        public String          key;
        public String          summary;
        public String          link;
        public String          sdate;
        public String          edate;
        public List<RulesBean> rules;

        public static class RulesBean {
            /**
             * id : 100
             * type : 0
             * name : 28元=2800红果币
             * tips :
             * value : 2800
             * gvalue : 0
             * discount : 1.0
             */
            public String id;
            public String type;
            public String name;
            public String tips;
            public int value;
            public int gvalue;
            public float  discount;
            public String sign;
        }
    }
}