package com.hongguo.read.mvp.model.book.chapter;

import java.util.List;

/**
 * Created by losg on 2018/1/15.
 */

public class BaiduBookUpdateBean {

    /**
     * msg : ok
     * code : 0
     * result : {"ActionID":"","ApplicationID":"","NextUpdateTimeSpan":0,"PandaChapterInfoList":[{"BookID":605359121,"LastChapterID":419,"LastChapterName":"卷二：招兵买马_第0417章：防线测试","BookName":"仙界app","Href":"/book?bkid=605359121","IsFull":0,"LastUpdateTime":"2017-05-16 22:41:47","ChapterNum":419,"Status":1,"FreeStatus":0,"BookType":0}]}
     */

    public String     msg;
    public int        code;
    public ResultBean result;

    public static class ResultBean {
        /**
         * ActionID :
         * ApplicationID :
         * NextUpdateTimeSpan : 0
         * PandaChapterInfoList : [{"BookID":605359121,"LastChapterID":419,"LastChapterName":"卷二：招兵买马_第0417章：防线测试","BookName":"仙界app","Href":"/book?bkid=605359121","IsFull":0,"LastUpdateTime":"2017-05-16 22:41:47","ChapterNum":419,"Status":1,"FreeStatus":0,"BookType":0}]
         */

        public String                         ActionID;
        public String                         ApplicationID;
        public int                            NextUpdateTimeSpan;
        public List<PandaChapterInfoListBean> PandaChapterInfoList;

        public static class PandaChapterInfoListBean {
            /**
             * BookID : 605359121
             * LastChapterID : 419
             * LastChapterName : 卷二：招兵买马_第0417章：防线测试
             * BookName : 仙界app
             * Href : /book?bkid=605359121
             * IsFull : 0
             * LastUpdateTime : 2017-05-16 22:41:47
             * ChapterNum : 419
             * Status : 1
             * FreeStatus : 0
             * BookType : 0
             */

            public String BookID;
            public String LastChapterID;
            public String LastChapterName;
            public String BookName;
            public String Href;
            public int    IsFull;
            public String LastUpdateTime;
            public int    ChapterNum;
            public int    Status;
            public int    FreeStatus;
            public int    BookType;
        }
    }
}
