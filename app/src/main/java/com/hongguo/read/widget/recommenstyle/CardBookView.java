package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;

/**
 * Created by losg
 */

public class CardBookView extends FrameLayout {

    private static final float RATIO = 0.85f;

    private FrameLayout mCardBook;
    private ImageView   mCardImage;
    private ImageView   mBookImage;
    private TextView    mBookName;

    public CardBookView(Context context) {
        this(context, null);
    }

    public CardBookView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardBookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mCardBook = (FrameLayout) View.inflate(getContext(), R.layout.view_card_book, null);
        mCardImage = (ImageView) mCardBook.getChildAt(0);
        mBookImage = (ImageView) mCardBook.findViewById(R.id.book_image);
        mBookName = (TextView) mCardBook.findViewById(R.id.book_name);
        addView(mCardBook, new LayoutParams(-1, -2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        mCardImage.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (measuredHeight * RATIO), MeasureSpec.EXACTLY));
    }

    public void setBookUrlAndName(String imageUrl, String bookName) {
        ImageLoadUtils.loadUrl(mBookImage, imageUrl);
        mBookName.setText(bookName);
    }

}
