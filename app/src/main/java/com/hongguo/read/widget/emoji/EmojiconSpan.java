
package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import java.io.InputStream;
import java.lang.ref.WeakReference;


public class EmojiconSpan extends DynamicDrawableSpan {
    private Context mContext;

    private String mName;

    private int mSize;

    private int mTextSize;

    private int mHeight;

    private int mWidth;

    private int mTop;

    private Drawable                mDrawable;
    private int                     mStartPosition;
    private WeakReference<Drawable> mDrawableRef;

    public EmojiconSpan(Context context, String name, int size, int alignment, int textSize, int startPosition) {
        super(alignment);
        mContext = context;
        mName = name;
        mWidth = mHeight = mSize = size;
        mTextSize = textSize;
        mStartPosition = startPosition;
    }

    public String getName() {
        return mName;
    }

    public int getStartPosition() {
        return mStartPosition;
    }

    public void setStartPosition(int startPosition) {
        mStartPosition = startPosition;
    }

    public Drawable getDrawable() {
        if (mDrawable == null) {
            try {
                AssetManager assets = mContext.getAssets();
                String imageName = EmojiFactory.getInstance().getEmojiImages().get(mName);
                InputStream open = assets.open(EmojiFactory.EMOJI_PATH + imageName + EmojiFactory.EMOJI_SUFFIX);
                mDrawable = Drawable.createFromStream(open, imageName + EmojiFactory.EMOJI_SUFFIX);
                mHeight = mSize;
                mWidth = mHeight * mDrawable.getIntrinsicWidth() / mDrawable.getIntrinsicHeight();
                mTop = (mTextSize - mHeight) / 2;
                mDrawable.setBounds(0, 0, mWidth, mHeight);
            } catch (Exception e) {
                // swallow
            }
        }
        return mDrawable;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();
        int transY  = top + ((bottom - top) / 2) - ((b.getBounds().bottom - b.getBounds().top) / 2) - mTop;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        if (mDrawableRef == null || mDrawableRef.get() == null) {
            mDrawableRef = new WeakReference<Drawable>(getDrawable());
        }
        return mDrawableRef.get();
    }
}