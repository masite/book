package com.hongguo.read.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.eventbus.BookChapterSelectedEvent;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.UserRepertory;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by losg on 2017/8/22.
 */

public class BookReaderChapterAdapter extends IosRecyclerAdapter {

    private List<ChapterBean.Chapters> mDataBeen;
    private int                        mSelectedIndex;
    private String                     mBookID;
    private int                        mBookFrom;

    //vip svip 限时免费
    private boolean mIsFree    = false;
    private int     mTextColor = 0xff000000;
    private int     mSelectedColor = 0xffeba2a2;


    public BookReaderChapterAdapter(Context context, List<ChapterBean.Chapters> dataBeen, String bookId, int bookFrom) {
        super(context);
        mDataBeen = dataBeen;
        mBookID = bookId;
        mBookFrom = bookFrom;
        mSelectedColor = SkinResourcesUtils.getColor(R.color.colorPrimary);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_book_reader_chapter;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {

        ChapterBean.Chapters dataBean = mDataBeen.get(index);
        hoder.setText(R.id.number, (index + 1) + ". ");
        hoder.setText(R.id.chapter_name, dataBean.chapterName);
        TextView textView = hoder.getViewById(R.id.chapter_name);
        TextView number = hoder.getViewById(R.id.number);
        TextView downDescribe = hoder.getViewById(R.id.down_describe);

        if (!dataBean.hasDownTotal) {
            dataBean.hasDownTotal = FileUtils.fileExist(FileManager.getBookChapterDownPath(UserRepertory.getUserID(), mBookFrom, mBookID, dataBean.chapterId));
        }

        boolean free = bookIsFree(dataBean);

        if (dataBean.hasDownTotal && free) {
            textView.setTextColor(mTextColor);
            number.setTextColor(mTextColor);
        } else {
            textView.setTextColor(mTextColor & 0x99ffffff);
            number.setTextColor(mTextColor & 0x99ffffff);
        }

        if (mSelectedIndex == index) {
            int color = mSelectedColor;
            textView.setTextColor(color);
            number.setTextColor(color);
        }

        View line = hoder.getViewById(R.id.line);
        line.setBackgroundColor(mTextColor & 0x99ffffff);

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
            mSelectedIndex = index;
            RxJavaUtils.delayRun(1000, () -> {
                notifyDataSetChanged();
            });
            EventBus.getDefault().post(new BookChapterSelectedEvent(index));
        });
    }

    private boolean bookIsFree(ChapterBean.Chapters chapter) {
        return chapter.isBuy == 1 || chapter.coin == 0 || mIsFree || chapter.mTimeFree;
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

    public void setSelectedIndex(int selectedIndex) {
        mSelectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setFree(boolean free) {
        mIsFree = free;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }
}
