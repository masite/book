package com.hongguo.read.widget.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/19.
 */

public class ChapterPagerInfo {

    //当前章节页面信息(章节的所有文本信息)
    public List<PagerInfo>      mPageLines;
    //当前章节加载状态
    public BaseChapter.PageLoad pageLoadingStatus;

    public ChapterPagerInfo() {
        mPageLines = new ArrayList<>();
    }

    public static class PagerInfo {
        public int            pageNumber;
        public int            totalSize;
        public List<LineInfo> lines;

        public PagerInfo() {
            lines = new ArrayList<>();
        }

        public static class LineInfo {
            public boolean isNewLine;
            public String  line;
            public int  currentY;
        }
    }
}
