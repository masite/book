package com.hongguo.read.mvp.presenter.book;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.mvp.model.book.detail.baidu.BaiduDetailBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import java.text.DecimalFormat;

/**
 * Created by losg on 2018/1/2.
 */

public class BaiduDiscountPresenter extends BaseImpPresenter<BookDiscountContractor.IView> implements BookDiscountContractor.IPresenter {

    private int mDiscountPercent = 100;


    public BaiduDiscountPresenter(ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @Override
    public void queryBookDiscount(String bookid) {

        mApiService.queryBaiduDetail(BaiduShuChengUrls.getBaiDuBookDetailUrl(bookid)).compose(RxJavaResponseDeal.create(this).withLoading(false).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<BaiduDetailBean>() {
            @Override
            public void failure(Throwable e) {
                //除网络错误外，主要是json解析异常(百度书籍在错误的时候，数据类型修改,无力吐槽)
                mView.showMessDialog("提醒", "该书因版权问题已下架", "", "确定", messageInfoDialog -> {
                    messageInfoDialog.dismissWithoutAnim();
//                    mView.finishView();
                });
            }

            @Override
            public void success(BaiduDetailBean baiduDetailBean) {
                if(baiduDetailBean.code != 0){
                    mView.showMessDialog("提醒", "该书因版权问题已下架", "", "确定", messageInfoDialog -> {
                        messageInfoDialog.dismissWithoutAnim();
//                        mView.finishView();
                    });

                    return;
                }
                int freeType = Integer.MAX_VALUE;
                mDiscountPercent = baiduDetailBean.result.discount_percent;
                if (baiduDetailBean.result.discount_percent == 0) {
                    freeType = Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE;
                } else if (baiduDetailBean.result.discount_percent != 100) {
                    freeType -= 1;
                }
                mView.setDiscountInfo(freeType);
            }
        }));
    }

    @Override
    public String getBookDiscountDescribe(int freeType) {
        if (mDiscountPercent == 0) {
            return "限免";
        } else if (mDiscountPercent != 100) {
            if (mDiscountPercent % 10 == 0) {
                return (mDiscountPercent / 10f) + "折";
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                return decimalFormat.format(mDiscountPercent / 10f);
            }
        }
        return "会员享折扣";
    }

    @Override
    public String getUserBuyDiscountDescribe(int freeType) {
        if (mDiscountPercent == 0) {
            return "";
        }
        if (!(UserRepertory.userIsSVip() || UserRepertory.userIsVip()))
            return "";

        return "";
    }

    @Override
    public boolean getUserCanReadByFreeType(int freeType) {
        if (mDiscountPercent == 0) {
            return true;
        }
        return false;
    }
}
