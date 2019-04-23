package com.hongguo.read.mvp.model.book.chapter;


import java.util.List;

/**
 * Created by losg on 2017/5/31.
 */

public class BaiduCapterBean {


    public String     msg;
    public int        code;
    public ResultBean result;

    public static class ResultBean {

        public String bookid;
        public String bookname;

        public List<PageListBean> pageList;

        public static class PageListBean {
            public String chapter_id;
            public String chapter_name;
            public String zip_url;
            public int    license;
            public int    coin;
        }
    }
}
