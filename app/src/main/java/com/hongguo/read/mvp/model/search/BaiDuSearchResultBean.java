package com.hongguo.read.mvp.model.search;


import com.hongguo.read.constants.Constants;
import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class BaiDuSearchResultBean {

    public Result result;

    public static class Result {
        @Parma(paramName = "total_number")
        public int total_number;

        @ListParma(paramListName = "data")
        public List<Book> books;

        public static class Book {

            @Parma(paramName = "bookFrom")
            public int bookFrom = Constants.BOOK_FROM.FROM_BAIDU;

            @Parma(paramName = "readers")
            public int book_readers;

            @Parma(paramName = "desc")
            public String book_desc;

            @Parma(paramName = "coverPicture")
            public String cover_picture;

            @Parma(paramName = "bookId")
            public String book_id;

            @Parma(paramName = "bookName")
            public String book_name;

            @Parma(paramName = "author")
            public String author_name;

            @Parma(paramName = "categoryName")
            public String book_type_name;

        }
    }


}
