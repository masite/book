package com.hongguo.read.mvp.model.vip;

/**
 * Created by losg on 17-12-13.
 */

public class SVIPShareBean {

    /**
     * sVIPShare : {"title":"超级会员上线，原创书籍免费看","des":"红果阅读，更多原创书籍更多折扣优惠","dirctUrl":"http://m.hgread.com/book/svipfree?utm_id=2143","imageUrl":"http://x1.hgread.com/hd/images/svip.png"}
     */
    public SVIPShare sVIPShare;

    public static class SVIPShare {
        /**
         * title : 超级会员上线，原创书籍免费看
         * des : 红果阅读，更多原创书籍更多折扣优惠
         * dirctUrl : http://m.hgread.com/book/svipfree?utm_id=2143
         * imageUrl : http://x1.hgread.com/hd/images/svip.png
         */

        public String title;
        public String des;
        public String dirctUrl;
        public String imageUrl;
    }
}
