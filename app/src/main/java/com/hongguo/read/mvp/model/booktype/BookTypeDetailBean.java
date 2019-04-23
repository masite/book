package com.hongguo.read.mvp.model.booktype;

import java.util.List;

public class BookTypeDetailBean {

    public PagerBean      pager;
    public int            code;
    public String         msg;
    public int            time;
    public List<DataBean> data;

    public static class PagerBean {
        /**
         * page : 1
         * size : 30
         * total : 59814
         */

        public int page;
        public int size;
        public int total;
    }

    public static class DataBean {
        /**
         * serial : 1
         * bookId : 14350497113730150
         * bookName : 徐富贵的艺术人生
         * formerName : null
         * bookType : 1
         * categoryId : 8
         * categoryName : 都市
         * author : 宅星月
         * Labels :
         * coverPicture : http://i-1.hgread.com/2017/8/10/cc84440c-a51e-4362-91c3-211ee08ddf9f.jpg
         * desc : 我是一个穷逼艺术生，在学校被人看不起还被抢了女朋友，放学后还要苦逼地去做兼职，生活在不断折磨着我
         */

        public int    serial;
        public String bookId;
        public String bookName;
        public Object formerName;
        public int    bookType;
        public int    categoryId;
        public String categoryName;
        public String author;
        public String Labels;
        public String coverPicture;
        public String desc;
    }
}