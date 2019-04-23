package com.hongguo.read.utils.down;

import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.retrofit.api.ApiService;

/**
 * Created by losg on 2018/1/17.
 */

public interface IChapterDown {

    HgReadDownManager.DownResult downFreeChapter(ApiService apiService, ChapterBean.Chapters chapter);

    HgReadDownManager.DownResult downAndBuyChapter(ApiService apiService, ChapterBean.Chapters chapter);

}
