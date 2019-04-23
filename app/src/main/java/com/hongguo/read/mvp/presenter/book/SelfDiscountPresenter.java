package com.hongguo.read.mvp.presenter.book;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

/**
 * Created by losg on 2018/1/2.
 */

public class SelfDiscountPresenter extends BaseImpPresenter<BookDiscountContractor.IView> implements BookDiscountContractor.IPresenter {

    public SelfDiscountPresenter(ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @Override
    public void queryBookDiscount(String bookid) {
        mApiService.queryAuthorBooks(bookid, "bds_forminfo").compose(RxJavaResponseDeal.create(this).commonDeal(booAuthor -> {
            int freeType = booAuthor.data.limitFreeType;
            mView.setDiscountInfo(freeType);
        }));
    }

    /**
     * 获取免费类型描述信息
     *
     * @param freeType
     * @return
     */
    @PresenterMethod
    public String getBookDiscountDescribe(int freeType) {
        if (freeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
            return "限免";
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_VIP_LIMIT_FREE) {
            return "VIP/SVIP免费";
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_SVIP_LIMIT_FREE) {
            //vip or svip 免费
            return "SVIP免费";
        } else {
            return "会员享折扣";
        }
    }

    /**
     * 用户阅读权限说明
     *
     * @param freeType
     * @return
     */
    @PresenterMethod
    public String getUserBuyDiscountDescribe(int freeType) {

        if (freeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
            return "";
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_VIP_LIMIT_FREE) {
            //非会员
            if (!(UserRepertory.userIsSVip() || UserRepertory.userIsVip())) {
                return "开通SVIP免费读";
            }
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_SVIP_LIMIT_FREE) {
            //非超级会员
            if (!UserRepertory.userIsSVip()) {
                return "开通SVIP免费读";
            }
        } else {
            //非会员
            if (!(UserRepertory.userIsSVip() || UserRepertory.userIsVip())) {
                return "";
            }
        }

        return "";
    }


    /**
     * 通过免费类型查看用户是否可以阅读
     *
     * @param freeType
     * @return
     */
    @PresenterMethod
    public boolean getUserCanReadByFreeType(int freeType) {
        if (freeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
            return true;
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_VIP_LIMIT_FREE) {
            return UserRepertory.userIsSVip() || UserRepertory.userIsVip();
        } else if (freeType == Constants.BOOK_FREE_TYPE.BOOK_SVIP_LIMIT_FREE) {
            return UserRepertory.userIsSVip();
        }
        return false;
    }
}
