package com.hongguo.read.mvp.model.bookshelf;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class BooksUpDataInfoBean {

    public int            code;
    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        public String bookId;
        public String latestId;
        public String latesName;
        public String latestTime;
    }
}
