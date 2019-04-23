package com.hongguo.read.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.UserRepertory;

import java.util.List;

/**
 * Created by losg on 2017/8/22.
 */

public class BuyChapterAdapter extends IosRecyclerAdapter {

    private List<ChapterBean.Chapters> mDataBeen;
    private ChapterItemClickListener   mChapterItemClickListener;

    private boolean mDownStatus = false;
    private boolean mFree;
    private int     mBookFrom;
    private String  mBookID;

    public BuyChapterAdapter(Context context, String bookid, int bookFrom, List<ChapterBean.Chapters> dataBeen) {
        super(context);
        mDataBeen = dataBeen;
        mBookID = bookid;
        mBookFrom = bookFrom;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_buy_chapter_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, final int index) {
        ChapterBean.Chapters dataBean = mDataBeen.get(index);

        TextView chapterName = hoder.getViewById(R.id.chapter_name);
        TextView price = hoder.getViewById(R.id.price);
        ImageView bookCheck = hoder.getViewById(R.id.book_check);
        TextView downStatus = hoder.getViewById(R.id.down_status);

        bookCheck.setSelected(dataBean.isChoose);

        hoder.setText(R.id.chapter_name, dataBean.chapterName);

        if (!dataBean.hasDownTotal) {
            dataBean.hasDownTotal = FileUtils.fileExist(FileManager.getBookChapterDownPath(UserRepertory.getUserID(), mBookFrom, mBookID, dataBean.chapterId));
        }

        boolean free = bookIsFree(dataBean);

        if (dataBean.hasDownTotal && free) {
            bookCheck.setVisibility(View.GONE);
            downStatus.setVisibility(View.VISIBLE);
            chapterName.setTextColor(0xff333333);
            price.setVisibility(View.GONE);
        } else {
            price.setVisibility(View.VISIBLE);
            bookCheck.setVisibility(View.VISIBLE);
            downStatus.setVisibility(View.GONE);
            chapterName.setTextColor(0xff999999);
        }

        price.setText(dataBean.coin == 0 ? "免费" : dataBean.coin + " 红果币");
        if (mFree && dataBean.coin != 0) {
            price.setText("限时免费");
        }
        if (dataBean.isBuy == 1) {
            price.setText("已购买");
        }


        bookCheck.setOnClickListener(v -> {
            //当前在下载状态，不让用户点击
            if (mDownStatus) {
                return;
            }

            bookCheck.setSelected(!bookCheck.isSelected());
            dataBean.isChoose = bookCheck.isSelected();
            if (mChapterItemClickListener != null) {
                mChapterItemClickListener.chapterItemClick(index);
            }
        });

    }

    public void setDownStatus(boolean downStatus) {
        mDownStatus = downStatus;
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeen.size();
    }

    public void setChapterItemClickListener(ChapterItemClickListener chapterItemClickListener) {
        mChapterItemClickListener = chapterItemClickListener;
    }

    public void setDataBeen(List<ChapterBean.Chapters> dataBeen) {
        mDataBeen = dataBeen;
    }

    public void setFree(boolean free) {
        mFree = free;
    }

    private boolean bookIsFree(ChapterBean.Chapters chapter) {
        return chapter.isBuy == 1 || chapter.coin == 0 || mFree || chapter.mTimeFree;
    }

    public interface ChapterItemClickListener {
        void chapterItemClick(int position);
    }
}
