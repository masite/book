package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hongguo.read.R;
import com.losg.library.utils.DisplayUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/3.
 */

public class CollapsibleTextView extends View implements View.OnClickListener {

    private int mTextSize   = 0;
    private int mTextColor  = 0;
    private int mMaxLines   = 3;
    private int mLineHeight = 0;
    private Paint        mPaint;
    private Paint        mArrowBackgroundPaint;
    private String       mText;
    private List<String> mLines;
    private int mTextHeight = 0;

    private WeakReference<Bitmap> mCacheBitmap;
    private boolean mReMeasureLine     = true;
    private boolean mCollapsibleStatus = true;

    public CollapsibleTextView(Context context) {
        this(context, null);
    }

    public CollapsibleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsibleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleTextView);
        mTextSize = (int) ty.getDimension(R.styleable.CollapsibleTextView_textSize, DisplayUtil.dip2px(context, 14));
        mTextColor = ty.getColor(R.styleable.CollapsibleTextView_textColor, 0xff666666);
        mMaxLines = ty.getInt(R.styleable.CollapsibleTextView_maxLines, 3);
        mText = ty.getString(R.styleable.CollapsibleTextView_text);
        mLineHeight = (int) ty.getDimension(R.styleable.CollapsibleTextView_lineHeight, DisplayUtil.dip2px(context, 4));
        ty.recycle();
        initText();
    }

    private void initText() {
        mLines = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mTextHeight = textHeight();

        mArrowBackgroundPaint = new Paint();
        mArrowBackgroundPaint.setAntiAlias(true);
        mArrowBackgroundPaint.setStyle(Paint.Style.FILL);
        Bitmap arrowBitmap = getArrowBitmap();
        LinearGradient linearGradient = new LinearGradient(0, 0, arrowBitmap.getWidth() * 2, 0, 0x88ffffff, 0xffffffff, Shader.TileMode.CLAMP);
        mArrowBackgroundPaint.setShader(linearGradient);
        setOnClickListener(this);
    }

    public void setCollapsibleStatus(boolean collapsibleStatus) {
        mCollapsibleStatus = collapsibleStatus;
        requestLayout();
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        requestLayout();
    }

    public void setText(String text) {
        mText = text;
        mReMeasureLine = true;
        requestLayout();
    }


    public int measureText(String text) {
        if (!mReMeasureLine) return mLines.size();
        mReMeasureLine = false;
        mLines.clear();
        int space;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        if (layoutParams != null) {
            space = layoutParams.leftMargin + layoutParams.rightMargin;
        }
        space = getPaddingLeft() + getPaddingRight();
        int width = getResources().getDisplayMetrics().widthPixels - space;

        while (true) {
            int end = mPaint.breakText(text, 0, text.length(), true, width, null);
            String lineString = text.substring(0, end);
            if (lineString.contains("\n")) {
                lineString = lineString.substring(0, lineString.indexOf("\n") + 1);
                end = lineString.indexOf("\n") + 1;
            }
            mLines.add(lineString);
            if (end >= text.length()) break;
            text = text.substring(end);
        }
        return mLines.size();
    }

    private int textHeight() {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (TextUtils.isEmpty(mText)) {
            setMeasuredDimension(0, 0);
            return;
        }
        int height = 0;
        int lines = measureText(mText);
        if (lines <= mMaxLines || !mCollapsibleStatus) {
            height = mTextHeight * lines + getPaddingTop() + getPaddingBottom() + (lines - 1) * mLineHeight;
        } else {
            height = mTextHeight * mMaxLines + getPaddingTop() + getPaddingBottom() + (mMaxLines - 1) * mLineHeight;
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int currentTop = (int) (getPaddingTop() + Math.abs(mPaint.getFontMetrics().ascent));
        int left = getPaddingLeft();
        for (int i = 0; i < mLines.size(); i++) {
            if (i >= mMaxLines && mCollapsibleStatus) {
                break;
            }
            String line = mLines.get(i);
            canvas.drawText(line, left, currentTop, mPaint);
            currentTop += mTextHeight + mLineHeight;
        }

        if (mCollapsibleStatus && mMaxLines < mLines.size()) {
            Bitmap arrowBitmap = getArrowBitmap();
            int arrowBgLeft = getMeasuredWidth() - getPaddingRight() - 2 * arrowBitmap.getWidth();
            int bottomTop = (int) (currentTop - mTextHeight - mLineHeight + mPaint.getFontMetrics().ascent);
            canvas.drawRect(arrowBgLeft, bottomTop, arrowBgLeft + 2 * arrowBitmap.getWidth(), bottomTop + mTextHeight, mArrowBackgroundPaint);
            canvas.drawBitmap(arrowBitmap, arrowBgLeft + arrowBitmap.getWidth(), bottomTop, mPaint);
        }
    }

    private Bitmap getArrowBitmap() {
        if (mCacheBitmap == null || mCacheBitmap.get() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_col_text_more);
            mCacheBitmap = new WeakReference<Bitmap>(bitmap);
        }
        return mCacheBitmap.get();
    }

    @Override
    public void onClick(View view) {
        setCollapsibleStatus(!mCollapsibleStatus);
        requestLayout();
    }
}
