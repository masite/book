package com.hongguo.read.mvp.model.book;

/**
 * Created by Administrator on 2017/3/29.
 */

public class ChapterContentBean {

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {
        public String  bookId;
        public String  bookName;
        public String  chapterId;
        public String  chapterName;
        public String  wordCount;
        public String  textContent;
        public String  prevId;
        public String  prevName;
        public String  nextId;
        public String  nextName;
        public boolean isVip;
        public boolean isAutoBuy;
        public boolean notLogin;
        public boolean notBuy;
        public boolean notAmount;
        public boolean notRead;
    }
}
