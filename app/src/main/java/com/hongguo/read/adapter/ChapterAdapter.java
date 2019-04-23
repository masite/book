package com.hongguo.read.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.read.R;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2017/8/22.
 */

public class ChapterAdapter extends IosRecyclerAdapter {

    private List<ChapterBean.Chapters> mDataBeen;
    private ChapterClickListener        mChapterClickListener;

    //vip svip 限时免费
    private boolean mIsFree = false;

    private int     mBookFrom;
    private String  mBookID;

    public ChapterAdapter(Context context, String bookid, int bookFrom, List<ChapterBean.Chapters> dataBeen) {
        super(context);
        mDataBeen = dataBeen;
        mBookID = bookid;
        mBookFrom = bookFrom;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_chapter1;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        ChapterBean.Chapters dataBean = mDataBeen.get(index);
        hoder.setText(R.id.number, (index + 1) + ". ");
        hoder.setText(R.id.chapter_name, dataBean.chapterName.trim());
        TextView textView = hoder.getViewById(R.id.chapter_name);
        TextView number = hoder.getViewById(R.id.number);
        TextView downDescribe = hoder.getViewById(R.id.down_describe);

        boolean free = bookIsFree(dataBean);

        if (!dataBean.hasDownTotal) {
            dataBean.hasDownTotal = FileUtils.fileExist(FileManager.getBookChapterDownPath(UserRepertory.getUserID(), mBookFrom, mBookID, dataBean.chapterId));
        }

        if (dataBean.hasDownTotal && free) {
            textView.setTextColor(0xff666666);
            number.setTextColor(0xff666666);
        } else {
            textView.setTextColor(0xff999999);
            number.setTextColor(0xff999999);
        }

        if (free) {
            hoder.getViewById(R.id.chapter_lock).setVisibility(View.GONE);
            downDescribe.setVisibility(View.VISIBLE);
            if (mIsFree && dataBean.coin != 0) {
                downDescribe.setText("限免");
            } else if (dataBean.isBuy == 1 && dataBean.coin != 0) {
                downDescribe.setText("已购买");
            } else {
                downDescribe.setText("");
            }
        } else {
            hoder.getViewById(R.id.chapter_lock).setVisibility(View.VISIBLE);
            downDescribe.setVisibility(View.GONE);
        }

        hoder.itemView.setOnClickListener(v -> {
            if(mChapterClickListener != null){
                mChapterClickListener.chapterClick(dataBean, index);
            }
        });
    }

    private boolean bookIsFree(ChapterBean.Chapters chapter) {
        return chapter.isBuy == 1 || chapter.coin == 0 || mIsFree;
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeen.size();
    }

    public void setDataBeen(List<ChapterBean.Chapters> dataBeen) {
        mDataBeen = dataBeen;
    }

    public void setFree(boolean free) {
        mIsFree = free;
    }

    public void setChapterClickListner(ChapterClickListener chapterClickListener) {
        mChapterClickListener = chapterClickListener;
    }

    public interface ChapterClickListener {
        void chapterClick(ChapterBean.Chapters chapters, int chapterIndex);
    }
}
