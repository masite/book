package com.hongguo.read.widget.downdialog;

import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by losg on 2018/2/12.
 */

public class DownLoadAllDialogPresenter {

    private DownLoadAllDialog   mDownLoadAllDialog;
    private CompositeDisposable mSubscriptions;

    public DownLoadAllDialogPresenter(DownLoadAllDialog downLoadAllDialog) {
        mDownLoadAllDialog = downLoadAllDialog;
        mSubscriptions = new CompositeDisposable();
    }

    public void loading(String bookid, int bookType) {
        queryPayChapters(bookid, bookType);
        queryFreeChapters(bookid, bookType);
        queryPayChapterInfo(bookid, bookType);
        queryFreeChapterInfo(bookid, bookType);
    }

    public void queryPayChapters(String bookid, int bookType) {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<List<ChapterBean.Chapters>>) subscriber -> {
            subscriber.onNext(ChapterRepertory.parse(ChapterRepertory.queryAllPay(bookid, bookType), bookid, bookType));
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapters -> {
            mDownLoadAllDialog.setPayChapters(chapters);
        });
        mSubscriptions.add(subscribe);
    }

    public void queryFreeChapters(String bookid, int bookType) {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<List<ChapterBean.Chapters>>) subscriber -> {
            subscriber.onNext(ChapterRepertory.parse(ChapterRepertory.queryAllFree(bookid, bookType), bookid, bookType));
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapters -> {
            mDownLoadAllDialog.setFreeChapters(chapters);
        });
        mSubscriptions.add(subscribe);
    }

    public void queryPayChapterInfo(String bookid, int bookType) {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<ChapterRepertory.ChapterTotalInfo>) subscriber -> {
            subscriber.onNext(ChapterRepertory.queryPay(bookid, bookType));
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapters -> {
            mDownLoadAllDialog.setPayChapterInfo(chapters);
        });
        mSubscriptions.add(subscribe);
    }

    public void queryFreeChapterInfo(String bookid, int bookType) {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<ChapterRepertory.ChapterTotalInfo>) subscriber -> {
            subscriber.onNext(ChapterRepertory.queryFree(bookid, bookType));
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapters -> {
            mDownLoadAllDialog.setFreeChapterInfo(chapters);
        });
        mSubscriptions.add(subscribe);
    }
}
