package com.hongguo.read.adapter;

import android.os.Environment;
import android.text.TextUtils;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.widget.reader.BaseBookAdapter;
import com.hongguo.read.widget.reader.BaseChapter;

import java.util.List;

/**
 * Created by losg on 2018/1/16.
 */

public class BookReaderAdapter extends BaseBookAdapter<ChapterBean.Chapters> {
    //下载偏移 (以章节未节点，下载上下的信息)
    private static final int DOWN_OFFSET = 2;

    private BookReaderDownListener mBookReaderDownListener;
    private String                 mBookId;
    private int                    mBookType;
    private boolean                mAutoBuy;

    public BookReaderAdapter(String mBookId, String bookName,int mBookType, List<ChapterBean.Chapters> chapters) {
        super(chapters);
        if(mChapters.size() == 0) {
            ChapterBean.Chapters chapter = new ChapterBean.Chapters();
            chapter.chapterName = bookName + "加载中";
            chapter.bookid = mBookId;
            chapter.chapterId = "-1";
            chapter.coin = 0;
            mChapters.add(chapter);
        }
        this.mBookId = mBookId;
        this.mBookType = mBookType;
    }

    @Override
    public void initChater(ChapterBean.Chapters chapters, int position) {

        chapters.pageLoad = BaseChapter.PageLoad.SUCCESS;
        chapters.filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/123.txt";
        chapters.hasDownTotal = true;


//        //当前页下载有错误
//        if(!TextUtils.isEmpty(chapters.error)){
//            chapters.pageLoad = BaseChapter.PageLoad.NET_ERROR;
//            downOffset(position);
//            return;
//        }
//
//        if(chapters.chapterId.equals("-1")){
//            chapters.pageLoad = BaseChapter.PageLoad.LOADING;
//            return;
//        }
//
//        //购买过
//        if (chapterPay(chapters) || mAutoBuy) {
//            String filePath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), mBookType, mBookId, chapters.chapterId);
//            if (FileUtils.fileExist(filePath)) {
//                chapters.pageLoad = BaseChapter.PageLoad.SUCCESS;
//                chapters.filePath = filePath;
//                chapters.hasDownTotal = true;
//            } else {
//                chapters.pageLoad = BaseChapter.PageLoad.LOADING;
//                if (mBookReaderDownListener != null) {
//                    mBookReaderDownListener.needDown(chapters, true);
//                }
//            }
//        } else {
//            //没有购买
//            String filePath = FileManager.getBookDownDescribePath(mBookType, mBookId, chapters.chapterId);
//            if (FileUtils.fileExist(filePath)) {
//                chapters.pageLoad = BaseChapter.PageLoad.NEND_PAY;
//                chapters.filePath = filePath;
//            } else {
//                chapters.pageLoad = BaseChapter.PageLoad.LOADING;
//                if (mBookReaderDownListener != null) {
//                    mBookReaderDownListener.needDown(chapters, false);
//                }
//            }
//        }
//        downOffset(position);
    }

    //预加载上下几页
    private void justDown(ChapterBean.Chapters chapters) {
        //购买过
        if (chapterPay(chapters)) {
            String filePath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), mBookType, mBookId, chapters.chapterId);
            if (!FileUtils.fileExist(filePath)) {
                if (mBookReaderDownListener != null) {
                    mBookReaderDownListener.needDown(chapters, true);
                }
            }
        } else {
            //没有购买
            String filePath = FileManager.getBookDownDescribePath(mBookType, mBookId, chapters.chapterId);
            if (!FileUtils.fileExist(filePath)) {
                if (mBookReaderDownListener != null) {
                    mBookReaderDownListener.needDown(chapters, false);
                }
            }
        }
    }

    /**
     * 预加载上下三章的内容
     * @param position
     */
    private void downOffset(int position) {
        int start = position - DOWN_OFFSET < 0 ? 0 : position - DOWN_OFFSET;
        int end = position + DOWN_OFFSET > mChapters.size() - 1 ? mChapters.size() - 1 : position + DOWN_OFFSET;
        for (int i = start; i < position; i++) {
            ChapterBean.Chapters chapters = mChapters.get(i);
            justDown(chapters);
        }

        for (int i = position + 1; i <= end; i++) {
            ChapterBean.Chapters chapters = mChapters.get(i);
            justDown(chapters);
        }
    }


    private boolean chapterPay(ChapterBean.Chapters chapters) {
        return chapters.coin == 0 || chapters.isBuy == 1 || chapters.mTimeFree;
    }

    public void setBookReaderDownListener(BookReaderDownListener bookReaderDownListener) {
        mBookReaderDownListener = bookReaderDownListener;
    }

    public void setAutoBuy(boolean autoBuy) {
        mAutoBuy = autoBuy;
    }

    public interface BookReaderDownListener {
        void needDown(ChapterBean.Chapters chapters, boolean total);
    }
}
