package com.hongguo.read.mvp.contractor.book;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;

public class BookDiscountContractor {

    public interface IView extends BaseViewEx {
        void setDiscountInfo(int freeType);
    }

    public interface IPresenter extends BasePresenter {
        void queryBookDiscount(String bookid);

        String getBookDiscountDescribe(int freeType);

        String getUserBuyDiscountDescribe(int freeType);

        boolean getUserCanReadByFreeType(int freeType);
    }
}