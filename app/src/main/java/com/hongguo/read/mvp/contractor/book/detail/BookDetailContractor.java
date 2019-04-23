package com.hongguo.read.mvp.contractor.book.detail;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;

public class BookDetailContractor {

    public interface IView extends BaseViewEx {
        void setBookDetailCommonInfo(BookDetailBean bookDetailCommonInfo);

        void setBookDiscussInfo(BookDiscussBean bookDiscussInfo);

        void setRewardTop(AwardRankBean awardRankBean);

        void setSimilar(SimilarBookBean similar);

        void setAuthor(BookAuthorBean author);

        void setBookVipDescribe(String vipDescribe);

        void setFavorInfo(boolean hasInSelf);

    	void setDiscountInfo(int freeType) ;

		void setBookHasRead(boolean hasRead);

	}

    public interface IPresenter extends BasePresenter {
        void queryBookCommonInfo(String bookid);

        void queryRewardInfos(String bookid);

        void queryBookDiscuss(String bookid, String bookType);

        void querySimilarBooks(String bookid);

        void queryBookAuthor(String bookid);
    }
}