package com.hongguo.read.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hongguo.read.R;

/**
 * Created by losg on 2017/12/25.
 */

public class BookShelfDeleteView extends PopupWindow implements View.OnClickListener {

    private TextView                    mDeleteBookShelf;
    private BookShelfDeleteClikListener mBookShelfDeleteClikListener;

    public BookShelfDeleteView(Context context) {
        super(-1, -2);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View view = View.inflate(context, R.layout.view_bookshelf_delete, null);
        mDeleteBookShelf = (TextView) view.findViewById(R.id.delete_book_shelf);
        mDeleteBookShelf.setOnClickListener(this);
        setContentView(view);
        setAnimationStyle(R.style.BookShelfDeleteAnimation);
    }

    @Override
    public void onClick(View view) {
        dismissDelete();
        if (mBookShelfDeleteClikListener != null) {
            mBookShelfDeleteClikListener.deleteClick();
        }
    }

    public void setBookShelfDeleteClikListener(BookShelfDeleteClikListener bookShelfDeleteClikListener) {
        mBookShelfDeleteClikListener = bookShelfDeleteClikListener;
    }

    public void setDeleteNumber(int number) {
        mDeleteBookShelf.setText("删除 ( " + number + " )");
    }

    public void showDelete(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void dismissDelete() {
        dismiss();
        mDeleteBookShelf.setText("删除");
    }

    public interface BookShelfDeleteClikListener {
        void deleteClick();
    }
}
