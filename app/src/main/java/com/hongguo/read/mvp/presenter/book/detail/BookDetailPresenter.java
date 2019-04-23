package com.hongguo.read.mvp.presenter.book.detail;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.eventbus.UpdateBookShelfEvent;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.base.BaseView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class BookDetailPresenter extends BaseImpPresenter<BookDetailContractor.IView> implements BookDetailContractor.IPresenter {

    SelfBookDetailPresenter         mSelfBookDetailPresenter;
    BaiduBookDetailPresenter        mBaiduBookDetailPresenter;
    BookDetailContractor.IPresenter mIPresenter;

    private String mBookId;
    private String mBookType;

    @Inject
    public BookDetailPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mSelfBookDetailPresenter = new SelfBookDetailPresenter(mApiService);
        mBaiduBookDetailPresenter = new BaiduBookDetailPresenter(mApiService);
    }

    @Override
    public void loading() {
        queryBookShelf();
        queryBookCommonInfo(mBookId);
        queryRewardInfos(mBookId);
        queryBookDiscuss(mBookId, mBookType + "");
        querySimilarBooks(mBookId);
        queryBookAuthor(mBookId);
        queryBookReadInfo(mBookId, mBookType);
    }

    private void queryBookShelf() {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            BookReader bookReader = BookReaderRepertory.queryBookShelfById(mBookId, mBookType);
            emitter.onNext(bookReader != null);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(aBoolean -> mView.setFavorInfo(aBoolean));
        addSubscriptions(subscribe);
    }

    @PresenterMethod
    public void init(String bookId, int bookType) {
        mBookId = bookId;
        mBookType = bookType + "";
        if (bookType == 0) {
            mIPresenter = mSelfBookDetailPresenter;
        } else {
            mIPresenter = mBaiduBookDetailPresenter;
        }
    }

    @Override
    public void bingView(BaseView view) {
        super.bingView(view);
        mSelfBookDetailPresenter.bingView(view);
        mBaiduBookDetailPresenter.bingView(view);
    }

    @Override
    @PresenterMethod
    public void queryBookCommonInfo(String bookid) {
        mIPresenter.queryBookCommonInfo(bookid);
    }

    @Override
    @PresenterMethod
    public void queryRewardInfos(String bookid) {
        mIPresenter.queryRewardInfos(bookid);
    }

    @Override
    @PresenterMethod
    public void queryBookDiscuss(String bookid, String bookType) {
        mSelfBookDetailPresenter.queryBookDiscuss(bookid, bookType);
    }

    @Override
    @PresenterMethod
    public void querySimilarBooks(String bookid) {
        mIPresenter.querySimilarBooks(bookid);
    }

    @Override
    @PresenterMethod
    public void queryBookAuthor(String bookid) {
        mIPresenter.queryBookAuthor(bookid);
    }

    @PresenterMethod
    public void addBookShelf(String bookId, int bookType, BookDetailBean bookDetailBean) {
        StatisticsUtils.bookShelf(bookDetailBean.data.bookName);
        mView.showWaitDialog(true, "正在添加", null);

        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            BookReader bookReader = new BookReader();
            bookReader.bookId = bookId;
            bookReader.bookType = bookType;
            bookReader.bookAuthor = bookDetailBean.data.author;
            bookReader.bookName = bookDetailBean.data.bookName;
            bookReader.coverPicture = bookDetailBean.data.coverPicture;
            bookReader.lastUpdateTime = bookDetailBean.data.lastUpdateTime;
            BookReaderRepertory.insertOrUpdateBookShelf(bookReader);
            emitter.onNext(true);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addSubscriptions(d);
            }

            @Override
            public void onNext(Boolean o) {
                super.onNext(o);
                mView.dismissWaitDialog();
                mView.toastMessage("添加成功");
                EventBus.getDefault().post(new UpdateBookShelfEvent());

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.dismissWaitDialog();
                mView.toastMessage("添加失败");
            }
        });

        if (bookType == Constants.BOOK_FROM.FROM_SLEF)
            mApiService.addBookShelf(bookId).compose(RxJavaResponseDeal.create(this).withLoading(false).commonDeal(response -> {
            }));
    }

    @PresenterMethod
    public void queryBookReadInfo(String bookid, String booktype) {
        BookReader bookReader = BookReaderRepertory.queryBookById(bookid, MathTypeParseUtils.string2Int(booktype));
        mView.setBookHasRead(bookReader != null);
    }

}