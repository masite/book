package com.hongguo.read.mvp.presenter.book;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.base.BaseView;

import javax.inject.Inject;

/**
 * Created by losg on 2018/1/2.
 */

public class BookDiscountPresenter extends BaseImpPresenter<BookDiscountContractor.IView> implements BookDiscountContractor.IPresenter {

    private BaiduDiscountPresenter            mBaiduDiscountPresenter;
    private SelfDiscountPresenter             mSelfDiscountPresenter;
    private BookDiscountContractor.IPresenter mIPresenter;

    private String mBookId;
    private String mBookType;

    @Inject
    public BookDiscountPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mBaiduDiscountPresenter = new BaiduDiscountPresenter(apiService);
        mSelfDiscountPresenter = new SelfDiscountPresenter(apiService);
    }

    @Override
    public void bingView(BaseView view) {
        super.bingView(view);
        mBaiduDiscountPresenter.bingView(view);
        mSelfDiscountPresenter.bingView(view);
    }

    public void init(String bookId, int bookType) {
        mBookId = bookId;
        mBookType = bookType + "";
        if (bookType == 0) {
            mIPresenter = mSelfDiscountPresenter;
        }else{
            mIPresenter = mBaiduDiscountPresenter;
        }
    }

    @Override
    public void loading() {
        queryBookDiscount(mBookId);
    }

    @Override
    public void queryBookDiscount(String bookid) {
        mIPresenter.queryBookDiscount(bookid);
    }

    @Override
    public String getBookDiscountDescribe(int freeType) {
        return mIPresenter.getBookDiscountDescribe(freeType);
    }

    @Override
    public String getUserBuyDiscountDescribe(int freeType) {
        return mIPresenter.getUserBuyDiscountDescribe(freeType);
    }

    @Override
    public boolean getUserCanReadByFreeType(int freeType) {
        return mIPresenter.getUserCanReadByFreeType(freeType);
    }
}
