package com.hongguo.read.mvp.contractor.book;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;

public class BookReaderContractor {

    public interface IView extends BaseViewEx {
        void needDown(ChapterBean.Chapters chapters, boolean total);

        void setBookReadInfo(int chapterIndex, int page, boolean bookshelfExist);

        void allDown();

        void initBookCommonInfo(int animType, int backgroundColor,int backgroundIndex,int backgroundResource, int textColor, boolean isModeNeight);

        void initBookSpecialInfo(int textSize, int lineType, int lineHeight, int paragraphHeight);

        void bookReadInfo(int speed, String speakerName);

        void clearErrorReportInfo();
    }

    public interface IPresenter extends BasePresenter {
    }
}