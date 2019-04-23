package com.hongguo.read.mvp.presenter.book.chapter;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.contractor.book.chapter.DownChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DownChapterPresenter extends BaseImpPresenter<DownChapterContractor.IView> implements DownChapterContractor.IPresenter {

    @Inject
    public DownChapterPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void selectAll(List<ChapterBean.Chapters> mItems) {
        Observable.create(new ObservableOnSubscribe<int[]>() {
            @Override
            public void subscribe(ObservableEmitter<int[]> emitter) throws Exception {
                int[] info = new int[2];
                for (ChapterBean.Chapters mItem : mItems) {
                    mItem.isChoose = true;
                    if (mItem.isBuy != 1) {
                        info[0] += mItem.coin;
                    }
                    info[1] += 1;
                }
                emitter.onNext(info);
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(info -> {
            mView.setTotalPrice(info[0], info[1]);
            mView.notifyAdapter();
        });
    }

    @PresenterMethod
    public void selectFree(List<ChapterBean.Chapters> mItems) {
        Observable.create(new ObservableOnSubscribe<int[]>() {
            @Override
            public void subscribe(ObservableEmitter<int[]> emitter) throws Exception {
                int[] info = new int[2];
                info[0] = 0;
                for (ChapterBean.Chapters mItem : mItems) {
                    if (mItem.isBuy == 1 || mItem.coin == 0) {
                        mItem.isChoose = true;
                        info[1] += 1;
                    } else {
                        mItem.isChoose = false;
                    }
                }
                emitter.onNext(info);
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(info -> {
            mView.setTotalPrice(info[0], info[1]);
            mView.notifyAdapter();
        });
    }

    @PresenterMethod
    public void selectNeedPay(List<ChapterBean.Chapters> mItems) {

        Observable.create(new ObservableOnSubscribe<int[]>() {
            @Override
            public void subscribe(ObservableEmitter<int[]> emitter) throws Exception {
                int[] info = new int[2];
                for (ChapterBean.Chapters mItem : mItems) {
                    if (mItem.isBuy == 1 || mItem.coin == 0) {
                        mItem.isChoose = false;
                    } else {
                        mItem.isChoose = true;
                        info[0] += mItem.coin;
                        info[1] += 1;
                    }
                }
                emitter.onNext(info);
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(info -> {
            mView.setTotalPrice(info[0], info[1]);
            mView.notifyAdapter();
        });

    }

    @PresenterMethod
    public void computeMoney(List<ChapterBean.Chapters> mItems) {
        Observable.create(new ObservableOnSubscribe<int[]>() {
            @Override
            public void subscribe(ObservableEmitter<int[]> emitter) throws Exception {
                int[] info = new int[2];
                for (ChapterBean.Chapters mItem : mItems) {
                    if (mItem.isBuy == 1) continue;
                    info[0] += mItem.isChoose ? mItem.coin : 0;
                    info[1] += mItem.isChoose ? 1 : 0;
                }
                emitter.onNext(info);
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(info -> {
            mView.setTotalPrice(info[0], info[1]);
        });
    }

    //更新数据库已经购买成功
    @PresenterMethod
    public void checkBookBuy(ChapterBean.Chapters chapters, String bookid, int bookType) {
        if (chapters.hasDownTotal && !chapters.mTimeFree) {
            chapters.isBuy = 1;
            ChapterRepertory.setChapterIsBuy(bookid, chapters.chapterId, bookType);
        }
    }


    @PresenterMethod
    public void checkDown(List<ChapterBean.Chapters> chapters, int bookType, String bookId) {
        addSubscriptions(Observable.create((ObservableOnSubscribe<List<ChapterBean.Chapters>>) subscriber -> {
            List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
            for (ChapterBean.Chapters chapter : chapters) {
                if (!chapter.isChoose) continue;
                String filePath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), bookType, bookId, chapter.chapterId);
                if (!FileUtils.fileExist(filePath)) {
                    chaptersList.add(chapter);
                }
            }

            for (int i = 0; i < chaptersList.size(); i++) {
                ChapterBean.Chapters chapters1 = chaptersList.get(i);
                chapters1.mulitDownTotalNumber = chaptersList.size();
                chapters1.mulitDownIndex = 0;
                mView.addDownChapter(chapters1);
            }
            subscriber.onNext(chaptersList);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapter -> {
            if (chapter.size() == 0) {
                mView.toastMessage("下载完成");
                mView.allDown();
            }
        }));
    }

}