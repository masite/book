package com.hongguo.read.eventbus;

/**
 * Created by losg on 2018/2/11.
 */

public class BookChapterSelectedEvent {

    public int mChapterIndex;

    public BookChapterSelectedEvent(int chapterIndex) {
        mChapterIndex = chapterIndex;
    }
}
