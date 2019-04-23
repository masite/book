package com.hongguo.read.mvp.presenter.book.detail;

import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.model.book.detail.baidu.BaiduDetailBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.read.utils.convert.BeanConvert;

public class BaiduBookDetailPresenter extends BaseImpPresenter<BookDetailContractor.IView> implements BookDetailContractor.IPresenter {

    public BaiduBookDetailPresenter(ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {

    }

    @Override
    public void queryBookCommonInfo(String bookid) {
        mApiService.queryBaiduDetail(BaiduShuChengUrls.getBaiDuBookDetailUrl(bookid)).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(baiduBean -> {
            dealCommonInfoConvert(baiduBean);
            dealSimilarInfo(baiduBean);
        }));
    }

    private void dealCommonInfoConvert(BaiduDetailBean baiduBean) {
        BookDetailBean bookDetailBean = new BookDetailBean();
        BeanConvert.convertBean(baiduBean, bookDetailBean);

        if (bookDetailBean.data.wordsNumber.contains(".")) {
            String[] split = bookDetailBean.data.wordsNumber.split("\\.");
            String wordsNumber = split[0];
            if (wordsNumber.length() > 2)
                wordsNumber = wordsNumber.substring(0, wordsNumber.length() - 1) + "." + wordsNumber.substring(wordsNumber.length() - 1);
            bookDetailBean.data.wordsNumber = (wordsNumber + "万字");
            //百度书籍没有阅读人数，用字数充
            bookDetailBean.data.readersStr = BookNumberUtils.formartUserReads(Integer.parseInt(split[0]) * 753);
        } else {
            bookDetailBean.data.readersStr = BookNumberUtils.formartUserReads(Integer.parseInt(bookDetailBean.data.wordsNumber) * 753);
            bookDetailBean.data.wordsNumber = bookDetailBean.data.wordsNumber + "万字";
        }
        bookDetailBean.data.statusStr = bookDetailBean.data.status == 0 ? "状态：连载" : "状态：完结";
        mView.setBookDetailCommonInfo(bookDetailBean);
        dealAuthor(baiduBean);
    }

    private void dealSimilarInfo(BaiduDetailBean baiduBean) {
        SimilarBookBean similarBookBean = new SimilarBookBean();
        BeanConvert.convertBean(baiduBean.result, similarBookBean);
        mView.setSimilar(similarBookBean);
    }

    private void dealAuthor(BaiduDetailBean baiduDetailBean) {

        BookAuthorBean author = new BookAuthorBean();
        author.data = new BookAuthorBean.DataBean();
        author.code = 0;
        author.data.fromName = "纵横文学";
        mView.setAuthor(author);
    }

    @Override
    public void queryRewardInfos(String bookid) {

    }

    @Override
    public void queryBookDiscuss(String bookid, String bookType) {

    }

    @Override
    public void querySimilarBooks(String bookid) {

    }

    @Override
    public void queryBookAuthor(String bookid) {

    }
}