package com.hongguo.read.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.BookReaderBackground;
import com.hongguo.read.repertory.share.ReaderBackgroundRepertory;
import com.hongguo.read.widget.BookBackgroundSelectorView;

import java.util.List;

/**
 * Created by losg on 2018/1/16.
 */

public class BookReaderBackgroundAdapter extends IosRecyclerAdapter {

    private List<BookReaderBackground> mBookReaderBackgrounds;
    private BackgroundClickListener    mBackgroundClickListener;
    private ColorStateList             mColorStateList;

    private int mSelectedPosition = 0;

    public BookReaderBackgroundAdapter(Context context) {
        super(context);
        mBookReaderBackgrounds = ReaderBackgroundRepertory.getBookReaderBackgrounds();
        int color = SkinResourcesUtils.getColor(R.color.colorPrimary);
        mColorStateList = ColorStateList.valueOf(color);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_book_backround;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        BookBackgroundSelectorView view = hoder.getViewById(R.id.background_selector);
        BookReaderBackground bookReaderBackground = mBookReaderBackgrounds.get(index);
        view.setColor(bookReaderBackground.backgroundColor);
        View back = hoder.getViewById(R.id.back_selected);
        ViewCompat.setBackgroundTintList(back, mColorStateList);
        if(index == mSelectedPosition){
            back.setVisibility(View.VISIBLE);
        }else{
            back.setVisibility(View.GONE);
        }
        hoder.itemView.setOnClickListener(v -> {
            if (mBackgroundClickListener != null) {
                mBackgroundClickListener.backgroundClick(index, bookReaderBackground.backgroundColor, bookReaderBackground.backgroundResource, bookReaderBackground.textColor);
            }
            mSelectedPosition = index;
            notifyDataSetChanged();
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mBookReaderBackgrounds.size();
    }

    @Override
    protected boolean widthIsWrap() {
        return true;
    }

    public void setBackgroundClickListener(BackgroundClickListener backgroundClickListener) {
        mBackgroundClickListener = backgroundClickListener;
    }

    public void setSelectedIndex(int selectedIndex) {
        mSelectedPosition = selectedIndex;
        notifyDataSetChanged();
    }

    public interface BackgroundClickListener {
        void backgroundClick(int index ,int backColor, String backResource, int textColor);
    }
}
