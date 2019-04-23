package com.hongguo.read.mvp.presenter.bookshelf;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.mvp.contractor.bookshelf.BookShelfContractor;
import com.hongguo.read.mvp.model.bookshelf.BooksUpDataInfoBean;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.read.retrofit.api.ApiService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class BookShelfPresenter extends BaseImpPresenter<BookShelfContractor.IView> implements BookShelfContractor.IPresenter {

    private DecimalFormat    mDecimalFormat;
    private List<BookReader> mBookShelves;


    @Inject
    public BookShelfPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mDecimalFormat = new DecimalFormat("0.0");
    }

    @Override
    public void loading() {
        queryBookShelfFromNative();
    }

    @PresenterMethod
    public void queryUserSignInfo() {
        mApiService.sign("0", "1").compose(RxJavaResponseDeal.create(this).withLoading(false).commonDeal(signBean -> {
            mView.setSignInfo(signBean.data.gtags, signBean.data.id != 0);
        }));
    }

    @PresenterMethod
    public void toSign() {
        mApiService.sign("0", "0").compose(RxJavaResponseDeal.create(this).withLoading(false).widthDialog("签到中").commonDeal(signBean -> {
            mView.setSignInfo(signBean.data.gtags, signBean.data.id != 0);
            mView.showSignSuccessInfo(signBean.data.gval, signBean.data.snum+"");
        }));
    }

    /**
     * 查询本地书架
     */
    private void queryBookShelfFromNative() {
        Observable.create((ObservableOnSubscribe<List<BookReader>>) subscriber -> {
            List<BookReader> bookShelves = BookReaderRepertory.queryAllBookShelf();
            subscriber.onNext(bookShelves);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(books -> {
            if (books.size() == 0) {
                queryBookFromNet();
            } else {
                mBookShelves = books;
                setViewBooks(books);
                updateBookTime(books);
            }
        });
    }

    /**
     * 查询网络书架
     */
    private void queryBookFromNet() {
        mApiService.queryNetBookShelf(0).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            if (books.data == null || books.data.size() == 0) {
                insertSuggestBook();
            } else {
                mBookShelves = books.data;
                setViewBooks(books.data);
                updateBookTime(books.data);
                BookReaderRepertory.synInsertBook(books.data);
            }
        }));
    }

    /**
     * 添加推荐书籍
     */
    private void insertSuggestBook() {
        Observable.create((ObservableOnSubscribe<List<BookReader>>) subscriber -> {
            List<BookReader> bookShelves = BookReaderRepertory.createDefaultBooks();
            subscriber.onNext(bookShelves);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(books -> {
            mBookShelves = books;
            setViewBooks(books);
            updateBookTime(books);
            insertToService();
        });
    }

    private void insertToService() {
        for (BookReader bookShelf : mBookShelves) {
            if (bookShelf.bookType == 0)
                mApiService.addBookShelf(bookShelf.bookId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
        }
    }

    private void setViewBooks(List<BookReader> books) {
        if (books.size() != 0) {
            BookReader bookShelf = books.get(0);
            mView.initShelfHeader(bookShelf.coverPicture, bookShelf.bookName, "已读: " + mDecimalFormat.format(bookShelf.readProgress) + "%");
            mView.setShelfHeaderBookIdInfo(bookShelf.bookId, bookShelf.bookType);
        } else {
            mView.initShelfHeader("", "", "");
        }
        mView.setBookShelfBooks(books);
    }

    /**
     * 查询书籍是否更新
     *
     * @param bookShelves
     */
    private void updateBookTime(List<BookReader> bookShelves) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BookReader bookShelf : bookShelves) {
            //百度书籍 服务器不存在书架
            if (bookShelf.bookType == 1) continue;
            stringBuilder.append(bookShelf.bookId);
            stringBuilder.append(",");
        }
        String ids = stringBuilder.toString();
        ids = ids.length() > 0 ? ids.substring(0, ids.length() - 1) : ids;
        mApiService.queryBookShelfUpdateTime(ids).flatMap(booksUpDataInfoBean -> {
            for (BooksUpDataInfoBean.DataBean datum : booksUpDataInfoBean.data) {
                String bookid = datum.bookId;
                BookReader bookShelf = findBookShelvesFromById(bookShelves, bookid);
                if (bookShelf != null) {
                    bookShelf.netLastUpdateTime = datum.latestTime;
                }
            }
            return Observable.just(bookShelves);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            setViewBooks(books);
        }));
    }

    private BookReader findBookShelvesFromById(List<BookReader> bookShelves, String id) {
        for (BookReader bookShelf : bookShelves) {
            if (bookShelf.bookId.equals(id) && bookShelf.bookType == 0) {
                return bookShelf;
            }
        }
        return null;
    }


    @PresenterMethod
    public void addBookShelf(BookReader bookShelf) {
        if (mBookShelves == null) return;
        if (mBookShelves.contains(bookShelf)) {
            mBookShelves.remove(bookShelf);
        }
        mBookShelves.add(0, bookShelf);
        setViewBooks(mBookShelves);
        if (bookShelf.bookType == 0)
            mApiService.addBookShelf(bookShelf.bookId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
        BookReaderRepertory.synInsertBook(bookShelf);
    }


    @PresenterMethod
    public void deleteBookShelf() {
        if (mBookShelves == null) return;
        List<BookReader> deleteItems = new ArrayList<>();
        for (int i = 0; i < mBookShelves.size(); i++) {
            BookReader bookShelf = mBookShelves.get(i);
            if (bookShelf.isSelected) {
                deleteItems.add(mBookShelves.remove(i));
                mApiService.deleteBookShelf(bookShelf.bookId).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
                i--;
            }
        }
        setViewBooks(mBookShelves);
        BookReaderRepertory.synDeleteBooks(deleteItems);
    }

    @PresenterMethod
    public void clearBookShelfSelect() {
        for (BookReader bookShelf : mBookShelves) {
            bookShelf.isSelected = false;
        }
        setViewBooks(mBookShelves);
    }
}