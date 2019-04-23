package com.hongguo.read.mvp.model.vip;

import java.util.List;

public class VipDescribeBean {

    public List<SvipDesBean> svipDes;

    public static class SvipDesBean {
        /**
         * title : 开通SVIP即可免费看
         * des : 超级会员尊享包月会员的一切特权外，还享有红果阅读原创作品免费在线阅读的权益
         */

        public String title;
        public String des;
    }
}