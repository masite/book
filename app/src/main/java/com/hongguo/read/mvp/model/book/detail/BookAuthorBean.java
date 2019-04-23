package com.hongguo.read.mvp.model.book.detail;

/**
 * Created by Administrator on 2017/4/11.
 */

public class BookAuthorBean {

    /**
     * data : {"viewTimes":74,"isLogined":false,"fromName":"阅庭书院","fromAuthor":"萧染","fromLink":""}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {
        /**
         * viewTimes : 74
         * isLogined : false
         * fromName : 阅庭书院
         * fromAuthor : 萧染
         * fromLink :
         *
         *
         */
        public int     viewTimes;
        public boolean isLogined;
        public boolean isVipFree;

        /**
         * 书籍免费类型
         * {@link com.hongguo.read.constants.Constants.BOOK_FREE_TYPE}
         */

        public int     limitFreeType;
        public String  fromName;
        public String  fromAuthor;
        public String  fromLink;
    }
}
