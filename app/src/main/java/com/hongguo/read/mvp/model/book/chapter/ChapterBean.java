package com.hongguo.read.mvp.model.book.chapter;

import com.hongguo.common.base.CommonBean;
import com.hongguo.read.widget.reader.BaseChapter;

import java.util.List;

public class ChapterBean extends CommonBean {

    public String         msg;
    public List<Chapters> data;

    public static class Chapters extends BaseChapter {

        public String chapterId;
        public int    isBuy;
        public int    coin;
        public int    id;
        public int    limitType;

        //添加的信息
        public String  updateTime;      //章节更新时间
        public boolean mTimeFree;       //判断是否为限免信息
        public boolean hasDownTotal;    //判断是否下载完成
        public String  error;           //下载是否完成

        public String  bookid;          //书本id      主要通过bookid 来查找对应的下载监听
        public int     from;            //书本来源
        public boolean isChoose;        //条目中是否被选中

        /**
         * 批量下载标志信息
         */
        public int     mulitDownIndex;          //批量下载章节索引(0 开始)
        public int     mulitDownTotalNumber;    //批量下载的总数(0 表示不是批量下载)


        @Override
        public boolean equals(Object obj) {
            return obj instanceof Chapters && chapterId.equals(((Chapters) obj).chapterId);
        }
    }
}