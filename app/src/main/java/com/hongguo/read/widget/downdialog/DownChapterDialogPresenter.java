package com.hongguo.read.widget.downdialog;

import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.Chapter;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/2/12.
 */

public class DownChapterDialogPresenter {

    private DownChapterDialog mDownChapterDialog;

    public DownChapterDialogPresenter(DownChapterDialog downChapterDialog) {
        mDownChapterDialog = downChapterDialog;
    }

    public void loading(String bookid, int bookType, String chapterId){
        Observable.create((ObservableOnSubscribe<List<ChapterBean.Chapters>>) subscriber -> {
            List<Chapter> chapters = ChapterRepertory.queryPayChapterFromChapter(bookid, bookType, chapterId);
            subscriber.onNext(ChapterRepertory.parse(chapters, bookid, bookType));
        }).compose(RxJavaUtils.androidTranformer()).subscribe(chapters->{
            computeChapter(chapters);
        });
    }


    private void computeChapter(List<ChapterBean.Chapters> chapters) {
        Observable.create((ObservableOnSubscribe<List<DownChapterInfo>>) subscriber -> {
            List<DownChapterInfo> downChapterInfos = new ArrayList<>();
            if(chapters.size() <= 1 ){
                return;
            }
            DownChapterInfo downChapterInfo = new DownChapterInfo();
            downChapterInfo.mChapters = new ArrayList<>();
            int money = 0;
            for (int i = 0; i < chapters.size(); i++) {
                ChapterBean.Chapters chapter = chapters.get(i);
                downChapterInfo.mChapters.add(chapter);
                money+=chapter.coin;
                if(i == 19 || i == 49) {
                    downChapterInfo.price = money;
                    downChapterInfos.add(downChapterInfo);
                    downChapterInfo = new DownChapterInfo();
                    downChapterInfo.mChapters = new ArrayList<>();
                    downChapterInfo.mChapters.addAll(downChapterInfos.get(downChapterInfos.size() - 1).mChapters);
                }
            }
            if(downChapterInfo.mChapters.size() != 0){
                downChapterInfo.price = money;
                downChapterInfos.add(downChapterInfo);
            }
            subscriber.onNext(downChapterInfos);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(infos->{
            mDownChapterDialog.setDownChapterSpliteInfo(infos);
        });

    }

}
