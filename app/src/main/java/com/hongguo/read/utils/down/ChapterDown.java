package com.hongguo.read.utils.down;

import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.retrofit.api.ApiService;

/**
 * Created by losg on 2018/1/17.
 */

public class ChapterDown {

    private ApiService mApiService;

    public ChapterDown(ApiService apiService) {
        mApiService = apiService;
    }

    public HgReadDownManager.DownResult downFreeChapter(ChapterBean.Chapters chapter) {
        IChapterDown chapterDown;
        if (chapter.from == Constants.BOOK_FROM.FROM_SLEF) {
            chapterDown = new ChapterDownManager();
        } else {
            chapterDown = new BaiduChapterDownManager();
        }
        return chapterDown.downFreeChapter(mApiService, chapter);
    }

    public HgReadDownManager.DownResult downAndPayChapter(ChapterBean.Chapters chapter) {
        IChapterDown chapterDown;
        if (chapter.from == Constants.BOOK_FROM.FROM_SLEF) {
            chapterDown = new ChapterDownManager();
        } else {
            chapterDown = new BaiduChapterDownManager();
        }
        return chapterDown.downAndBuyChapter(mApiService, chapter);
    }

}
