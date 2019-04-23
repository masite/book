package com.hongguo.read.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.R;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by losg
 */

public class BookShelfAdapter extends IosRecyclerAdapter {

    private List<BookReader>       mBookShelves;
    private BookShelfClickListener mBookShelfClickListener;
    private DecimalFormat          mDecimalFormat;

    private boolean mIsInEditMode = false;

    public BookShelfAdapter(Context context, List<BookReader> bookShelves) {
        super(context);
        mBookShelves = bookShelves;
        mDecimalFormat = new DecimalFormat("0.0");
    }

    @Override
    protected int getContentResource(int areaPosition) {
        if (areaPosition == 0)
            return R.layout.adapter_book_shelf;
        else
            return R.layout.adapter_bookshelf_add;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        if (areaPosition == 1) {
            initAddBook(hoder);
            return;
        }
        BookReader bookShelf = mBookShelves.get(index);
        ImageView bookImage = hoder.getViewById(R.id.book_image);
        ImageLoadUtils.loadUrl(bookImage, bookShelf.coverPicture);
        hoder.setText(R.id.book_name, bookShelf.bookName);
        hoder.setText(R.id.read_progress, "已读 " + mDecimalFormat.format(bookShelf.readProgress) + "%");
        hoder.getViewById(R.id.book_update).setVisibility(bookShelf.bookHasUpdate() ? View.VISIBLE : View.GONE);

        //选中 or 未选中状态
        ImageView selectImage = hoder.getViewById(R.id.selection_img);
        selectImage.setVisibility(mIsInEditMode ? View.VISIBLE : View.GONE);
        selectImage.setSelected(bookShelf.isSelected);

        hoder.getViewById(R.id.cover_layer).setTag(bookShelf.coverPicture);

        hoder.itemView.setOnClickListener(v -> {
            if (mIsInEditMode) {
                selectImage.setSelected(!selectImage.isSelected());
                bookShelf.isSelected = selectImage.isSelected();
                return;
            }
            mBookShelfClickListener.bookClick(bookImage, bookShelf);
        });

        hoder.itemView.setOnLongClickListener(v -> {
            mBookShelfClickListener.enterEditMode();
            if (mIsInEditMode) return true;
            mIsInEditMode = true;
            notifyChange();
            return true;
        });


    }

    private void initAddBook(BaseHoder hoder) {
        hoder.itemView.setOnClickListener(v -> {
            mBookShelfClickListener.addBookClick();
        });
    }

    @Override
    protected int getAreaSize() {
        return 2;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (areaPosition == 0) return mBookShelves.size();
        return 1;
    }

    public void setBookShelfClickListener(BookShelfClickListener bookShelfClickListener) {
        mBookShelfClickListener = bookShelfClickListener;
    }

    public boolean isInEditMode() {
        return mIsInEditMode;
    }

    public void setCommendMode() {
        mIsInEditMode = false;
        notifyChange();
    }

    public interface BookShelfClickListener {

        void bookClick(ImageView clickImage, BookReader bookShelf);

        void addBookClick();

        void enterEditMode();

    }
}
