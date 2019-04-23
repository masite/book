package com.hongguo.read.widget.bookstyle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.utils.DisplayUtil;

import java.util.Random;

/**
 * Created by losg
 */

public class BookTypeView extends LinearLayout {

    private LinearLayout      mBookItem;
    private BookTypeItemClick mBookTypeItemClick;

    public BookTypeView(Context context) {
        this(context, null);
    }

    public BookTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        initView();
    }

    private void initView() {
        mBookItem = (LinearLayout) View.inflate(getContext(), R.layout.view_book_type_item, null);
        addView(mBookItem, new LayoutParams(-1, -2));
        for (int i = 0; i < mBookItem.getChildCount(); i++) {
            int finalI = i;
            mBookItem.getChildAt(i).setOnClickListener(v -> {
                if (mBookTypeItemClick != null) {
                    mBookTypeItemClick.click(finalI);
                }
            });
        }
    }

    public void initRadomColor() {
        for (int i = 0; i < 3; i++) {
            Drawable radomColorDrawable = createRadomColorDrawable();
            ((TextView) mBookItem.getChildAt(i)).setBackground(radomColorDrawable);
            ((TextView) mBookItem.getChildAt(i)).setTextColor(Color.WHITE);
        }
    }

    private Drawable createRadomColorDrawable() {
        Random random = new Random();
        int red = 100 + random.nextInt(100);
        int green = 100 + random.nextInt(100);
        int blue = 100 + random.nextInt(100);
        int color = Color.argb(255, red, green, blue);
        int radio = DisplayUtil.dip2px(getContext(), 50);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{radio, radio, radio, radio, radio, radio, radio, radio}, null, null));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public void setItemBackground(int resource) {
        for (int i = 0; i < mBookItem.getChildCount(); i++) {
            mBookItem.getChildAt(i).setBackgroundResource(resource);
        }
    }

    public void setVisiable(int position, boolean visiable) {
        if (visiable) {
            mBookItem.getChildAt(position).setVisibility(VISIBLE);
            mBookItem.getChildAt(position).setEnabled(true);
        } else {
            mBookItem.getChildAt(position).setVisibility(INVISIBLE);
            mBookItem.getChildAt(position).setEnabled(false);
        }
    }

    public void setBookTypeItemClick(BookTypeItemClick bookTypeItemClick) {
        mBookTypeItemClick = bookTypeItemClick;
    }

    public void setTypeName(int position, String name) {
        ((TextView) mBookItem.getChildAt(position)).setText(name);
    }


    public interface BookTypeItemClick {
        void click(int position);
    }
}
