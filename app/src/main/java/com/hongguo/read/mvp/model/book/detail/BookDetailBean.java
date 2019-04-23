package com.hongguo.read.mvp.model.book.detail;

public class BookDetailBean {


    /**
     * data : {"serial":0,"bookId":200798,"bookName":"美女如戏","formerName":"","author":"梦里寻欢","coverPicture":"http://i-1.hgread.com/2017/5/17/725b4a57-c032-40c7-9139-ae2aed8f3ef1.jpg","desc":"美女姐姐每天当着我的面，都要做那种羞羞的事，于是有一天，我彻底忍不住了......\n\n新书在红果阅读连载，搜索《欲望青春期》就能找到了，认准小欢出品！","categoryId":340,"categoryName":"青春","chapters":313,"words":1071889,"status":0,"lastUpdateChapterId":50216201,"lastUpdateChapterName":"完结感言。","lastUpdateTime":"2017-10-29 16:04:48","readers":2690112,"recommendTitle":"美女如戏","recommendImg":"http://i-1.hgread.com/2017/5/17/725b4a57-c032-40c7-9139-ae2aed8f3ef1.jpg","recommendInfo":"美女姐姐每天当着我的面，都要做那种羞羞的事，于是有一天，我彻底忍不住了......\n\n新书在红果阅读连载，搜索《欲望青春期》就能找到了，认准小欢出品！","bookMarkChapterId":0,"joinedMark":0}
     * code : 0
     * msg : Successful
     * time : 1514279042
     */

    public DataBean data;
    public int      code;
    public String   msg;
    public int      time;

    public static class DataBean {
        /**
         * serial : 0
         * bookId : 200798
         * bookName : 美女如戏
         * formerName :
         * author : 梦里寻欢
         * coverPicture : http://i-1.hgread.com/2017/5/17/725b4a57-c032-40c7-9139-ae2aed8f3ef1.jpg
         * desc : 美女姐姐每天当着我的面，都要做那种羞羞的事，于是有一天，我彻底忍不住了......
         * <p>
         * 新书在红果阅读连载，搜索《欲望青春期》就能找到了，认准小欢出品！
         * categoryId : 340
         * categoryName : 青春
         * chapters : 313
         * words : 1071889
         * status : 0
         * lastUpdateChapterId : 50216201
         * lastUpdateChapterName : 完结感言。
         * lastUpdateTime : 2017-10-29 16:04:48
         * readers : 2690112
         * recommendTitle : 美女如戏
         * recommendImg : http://i-1.hgread.com/2017/5/17/725b4a57-c032-40c7-9139-ae2aed8f3ef1.jpg
         * recommendInfo : 美女姐姐每天当着我的面，都要做那种羞羞的事，于是有一天，我彻底忍不住了......
         * <p>
         * 新书在红果阅读连载，搜索《欲望青春期》就能找到了，认准小欢出品！
         * bookMarkChapterId : 0
         * joinedMark : 0
         */

        public String serial;
        public String bookId;
        public String bookName;
        public String formerName;
        public String author;
        public String coverPicture;
        public String desc;
        public String categoryId;
        public String categoryName;
        public String chapters;
        public int    words;
        public String wordsNumber;
        public int    status;
        public String statusStr;
        public int    lastUpdateChapterId;
        public String lastUpdateChapterName;
        public String lastUpdateTime;
        public int    readers;
        public String readersStr;
        public String recommendTitle;
        public String recommendImg;
        public String recommendInfo;
        public int    bookMarkChapterId;
        public int    joinedMark;
    }
}