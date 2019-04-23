package com.hongguo.read.mvp.model.bookstore;

import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.util.List;

/**
 * Created by losg on 2018/5/28.
 */

public class GuessFavorBean {

    public PagerBean      pager;
    public int            code;
    public String         msg;
    public int            time;
    @ListParma(paramListName = "data")
    public List<DataBean> data;

    public static class PagerBean {
        /**
         * page : 1
         * size : 20
         * total : 653
         */

        public int page;
        public int size;
        public int total;
    }

    public static class DataBean {
        /**
         * serial : 0
         * bookId : 203018
         * bookName : 和美女班主任合租情事
         * formerName : null
         * author : 大表哥
         * coverPicture : http://i-1.hgread.com/2017/12/1/b12e6d3a-5798-4bc6-827c-48469817fbde.jpg
         * desc : 美女老师住在我隔壁，每天发出一些奇怪的声音，所以我在墙上打了个洞,竟然发现.....
         * <p>
         * <p>
         * categoryId : 340
         * categoryName : 青春
         * chapters : 587
         * words : 3181
         * status : 0
         * lastUpdateChapterId : 86672801
         * lastUpdateChapterName : 第585章 女人之间的交易
         * lastUpdateTime : 2018-05-28 16:02:44
         * readers : 9777620
         * recommendTitle : 大表哥
         * recommendImg : http://i-1.hgread.com/2017/12/1/b12e6d3a-5798-4bc6-827c-48469817fbde.jpg
         * recommendInfo : 美女老师住在我隔壁，每天发出一些奇怪的声音，所以我在墙上打了个洞,竟然发现.....
         * <p>
         * <p>
         * bookMarkChapterId : 0
         * joinedMark : 0
         */

        public int    serial;
        @Parma(paramName = "bookId")
        public String bookId;
        @Parma(paramName = "bookName")
        public String bookName;
        public String formerName;
        @Parma(paramName = "author")
        public String author;
        @Parma(paramName = "coverPicture")
        public String coverPicture;
        @Parma(paramName = "desc")
        public String desc;
        public int    categoryId;
        public String categoryName;
        public int    chapters;
        @Parma(paramName = "words")
        public int    words;
        public int    status;
        public int    lastUpdateChapterId;
        public String lastUpdateChapterName;
        public String lastUpdateTime;
        public int    readers;
        public String recommendTitle;
        public String recommendImg;
        public String recommendInfo;
        public int    bookMarkChapterId;
        public int    joinedMark;
    }
}
