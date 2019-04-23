package com.hongguo.read.mvp.model.bookstore.channel;

import java.util.List;

public class TypeChannelBean {


    public List<BookType> boy;
    public List<BookType> girl;
    public List<BookType> other;

    public static class BookType {
        /**
         * id : 344
         * name : 玄幻
         * image : http://x1.hgread.com/fenlei/images/001.png
         */
        public boolean selected;
        public String  bid;
        public String  id;
        public String  name;
        public String  image;
    }
}