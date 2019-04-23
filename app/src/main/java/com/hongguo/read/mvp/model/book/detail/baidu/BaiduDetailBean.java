package com.hongguo.read.mvp.model.book.detail.baidu;


import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.util.List;

/**
 * Created by losg on 2017/5/31.
 */

public class BaiduDetailBean {

    public int    code;
    public String msg;

    @Parma(paramName = "data")
    public ResultBean result;

    public static class ResultBean {
        @Parma(paramName = "bookId")
        public String                 book_id;
        @Parma(paramName = "bookName")
        public String                 book_name;
        public String                 author_id;
        @Parma(paramName = "author")
        public String                 author_name;
        @Parma(paramName = "desc")
        public String                 book_desc;
        public String                 book_type_id;
        @Parma(paramName = "categoryName")
        public String                 book_type_name;
        @Parma(paramName = "status")
        public int                    book_status;
        public String                 contact_id;
        @Parma(paramName = "fromName")
        public String                 contact_name;
        @Parma(paramName = "wordsNumber")
        public String                 book_size;
        public String                 book_score;
        @Parma(paramName = "coverPicture")
        public String                 cover_picture;
        public String                 book_from;
        public String                 site_id;
        public String                 charge_type;
        @Parma(paramName = "lastUpdateChapterId")
        public String                 last_chapter_id;
        @Parma(paramName = "lastUpdateChapterName")
        public String                 last_chapter_name;
        @Parma(paramName = "lastUpdateTime")
        public String                 last_update_time;
        public int                    discount_percent;
        public String                 top_free;
        public String                 is_recommended;
        @Parma(paramName = "readers")
        public String                 recommendations;
        public String                 rewards;
        public String                 monthly_tickets;
        public String                 user_monthly_tickets;
        public BookPriceBean          book_price;
        public BookDiscountBean       book_discount;
        @ListParma(paramListName = "data")
        public List<SimilarBooksBean> similar_books;

        public static class BookPriceBean {
        }

        public static class BookDiscountBean {

        }

        public static class SimilarBooksBean {
            /**
             * book_id : 496249121
             * book_name : 斗战主宰
             * cover_picture : https://img.xmkanshu.com/novel/group2/M00/32/BA/CwsAiFfRJveAZK8YAABQovWZi9E371.jpg
             */

            @Parma(paramName = "bookId")
            public String book_id;
            @Parma(paramName = "bookName")
            public String book_name;
            @Parma(paramName = "coverPicture")
            public String cover_picture;
            @Parma(paramName = "bookType")
            public int type = 1;
        }
    }
}
