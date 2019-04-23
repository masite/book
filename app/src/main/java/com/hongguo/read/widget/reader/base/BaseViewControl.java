package com.hongguo.read.widget.reader.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hongguo.read.widget.reader.BookViewListener;
import com.hongguo.read.widget.reader.PageController;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by losg on 2017/5/4.
 */

public abstract class BaseViewControl {

    protected static final int MOVE_TO_TOP             = 0;
    protected static final int MOVE_TO_BOTTOM          = 1;
    protected static final int MOVE_TO_TOP_BOTTOM_NONE = 2;
    protected static final int MOVE_TO_LEFT            = 3;
    protected static final int MOVE_TO_RIGHT           = 4;
    protected static final int MOVE_TO_LEFT_RIGHT_NONE = 5;

    protected Bitmap       mStaticBitmap;
    protected Bitmap       mMoveBitmap;
    protected Bitmap       mTemp;
    protected List<Bitmap> mDrawBitmaps;


    //中间部分点击（弹出设置信息 0 - 1f 百分比）
    private static final float BETWEEN_DISTANCE = 0.3f;

    //是否为点击
    private boolean mSingleTap = false;

    protected int mWidth;
    protected int mHeight;

    protected boolean mAnimationStart = false;

    //开始拖动的位置
    protected PointF mDragStartPos    = new PointF();
    protected PointF mCurrentPosition = new PointF();

    protected PageController   mPageController;
    protected BookViewListener mBookViewClickListener;

    protected Paint mDrawPaint;
    protected View  mBookView;

    protected int mMoveDirectLeftRight;
    protected int mMoveDirectTopBottom;

    private GestureDetectorCompat mGestureDetectorCompat;
    protected boolean mHasNotify = false;


    public BaseViewControl(View view, PageController pageProvider) {
        setWidthAndHeight(pageProvider.getViewRect().width(), pageProvider.getViewRect().height());
        mPageController = pageProvider;
        mStaticBitmap = mPageController.getDrawBitmap();
        mMoveBitmap = mPageController.getCacheBitmap();

        mBookView = view;
        mDrawPaint = new Paint();
        mSimpleOnGestureListener = new GestureTouch();
        mGestureDetectorCompat = new GestureDetectorCompat(mBookView.getContext(), mSimpleOnGestureListener);
        mGestureDetectorCompat.setIsLongpressEnabled(false);
        mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;
        mMoveDirectTopBottom = MOVE_TO_TOP_BOTTOM_NONE;
        mDrawBitmaps = new ArrayList<>();

    }

    /**
     * 点击事件
     *
     * @param me
     * @return
     */
    public boolean onSingleTapUp(MotionEvent me) {
        mCurrentPosition.set(me.getX(), me.getY());
        mSingleTap = true;
        if (mBookViewClickListener == null) return true;

        if (mPageController.rewawrdRect().contains(me.getX(), me.getY())) {
            if (mBookViewClickListener != null) {
                mBookViewClickListener.bookRewardClick();
            }
            return true;
        }

        //检查支付信息
        if (mPageController.currentPageIsPay()) {
            if (mPageController.payPageBtnRect().contains(me.getX(), me.getY())) {
                mBookViewClickListener.bookPayClicked(mPageController.getCurrentPage());
                return true;
            } else if (mPageController.payVipBtnRect().contains(me.getX(), me.getY())) {
                mBookViewClickListener.bookVipClicked(mPageController.getCurrentPage());
                return true;
            }
        }

        //检查是否为失败页面
        if (mPageController.currentPageIsError() && mPageController.netErrorRect().contains(me.getX(), me.getY())) {
            mBookViewClickListener.errorReloadClick(mPageController.getCurrentPage());
            return true;
        }

        //是否点击设置
        if (mDragStartPos.x >= BETWEEN_DISTANCE * mWidth && mDragStartPos.x <= (1 - BETWEEN_DISTANCE) * mWidth) {
            mBookViewClickListener.bookSettingClick();
            return true;
        }

        if (mBookViewClickListener != null) {
            if (mDragStartPos.x >= (1 - BETWEEN_DISTANCE) * mWidth) {
                animationMove(false);
                return true;
            } else if (mDragStartPos.x <= BETWEEN_DISTANCE * mWidth) {
                animationMove(true);
                return true;
            }

        }

        return false;
    }

    /**
     * @param width
     * @param height
     */
    public void setWidthAndHeight(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    /**
     * 滚动时
     *
     * @param e1
     * @param me
     * @param distanceX
     * @param distanceY
     * @return
     */
    public boolean onScroll(MotionEvent e1, MotionEvent me, float distanceX, float distanceY) {
        mCurrentPosition.set(me.getX(), me.getY());
        return true;
    }

    /**
     * 滑动
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void animationMove(boolean pre) {
        int width = mPageController.getViewRect().width();
        int height = mPageController.getViewRect().height();
        float downX = width * (pre ? 0.2f : 0.8f);

        float minWidth = ViewConfiguration.get(mBookView.getContext()).getScaledTouchSlop() * 2f;
        MotionEvent eventDown = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, downX, height / 2 - 10, 0);
        onTouch(eventDown);

        downX = (pre ? minWidth : -minWidth) + downX;
        MotionEvent eventMove = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis() + 500, MotionEvent.ACTION_MOVE, downX, height / 2 - 10, 0);
        onTouch(eventMove);

        downX = (pre ? minWidth : -minWidth) + downX;
        eventMove = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis() + 500, MotionEvent.ACTION_MOVE, downX, height / 2 - 10, 0);
        onTouch(eventMove);


        eventMove = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis() + 1000, MotionEvent.ACTION_UP, downX, height / 2 - 10, 0);
        onTouch(eventMove);
    }


    public void computeScroll() {

    }

    /**
     * 按下操作
     *
     * @param me
     * @return
     */
    public boolean onDown(MotionEvent me) {
        mCurrentPosition.set(me.getX(), me.getY());
        mDragStartPos.set(me.getX(), me.getY());
        mSingleTap = false;
        return true;
    }

    protected void clearAnimaion() {
        mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;
        mMoveDirectTopBottom = MOVE_TO_TOP_BOTTOM_NONE;
        mAnimationStart = false;
    }

    /**
     * 触摸屏幕时
     *
     * @param me
     * @return
     */
    public boolean onTouch(MotionEvent me) {
        //在动画中，不让用户操作
        if (mAnimationStart) return false;
        mCurrentPosition.set(me.getX(), me.getY());
        mGestureDetectorCompat.onTouchEvent(me);
        switch (me.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mSingleTap) {
                    return false;
                }
        }
        return true;
    }

    /**
     * 画图
     *
     * @param canvas
     */
    public abstract void onDraw(Canvas canvas);


    /**
     * 检查是否还有下一页
     *
     * @return
     */
    protected boolean checkHasNext() {
        if ((mPageController.currentPageIsEnd() && mPageController.currentChapterIsEnd())) {
            if (mBookViewClickListener != null) {
                mBookViewClickListener.bookIsEndChapter();
            }
            return false;
        }
        return true;
    }

    /**
     * 检查是否还有上一页
     *
     * @return
     */
    protected boolean checkHasForward() {
        if ((mPageController.currentPageIsStart() && mPageController.currentChapterIsFirst())) {
            if (mBookViewClickListener != null) {
                mBookViewClickListener.bookIsFirstChapter();
            }
            return false;
        }
        return true;
    }

    public void setBookViewClickListener(BookViewListener bookViewClickListener) {
        mBookViewClickListener = bookViewClickListener;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener;

    private class GestureTouch extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent me) {
            return BaseViewControl.this.onSingleTapUp(me);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent me, float distanceX, float distanceY) {
            return BaseViewControl.this.onScroll(e1, me, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return BaseViewControl.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent me) {
            return BaseViewControl.this.onDown(me);
        }
    }

    public void show() {
        if (mDrawBitmaps.size() == 0) {
            mPageController.setDrawBitmap(mStaticBitmap);
            mStaticBitmap = mPageController.drawPage(null, mPageController.getCurrentPage(), 0, null, false).drawBitmap;
            mDrawBitmaps.add(mStaticBitmap);
        }
    }

    /**
     * 只做简单的更新，更新的内容，主要更新 背景，动画样式
     */
    public void notifyDataChange() {
        mHasNotify = true;
        if (mAnimationStart) return;
        mDrawBitmaps.clear();
        //重新获取显示的内容
        show();
        initOthers();
        mBookView.invalidate();
        mHasNotify = false;
    }

    /**
     * 在发生变化时 初始化其他内容
     */
    public void initOthers() {

    }

    public void animtionEndCheckUpate() {
        clearAnimaion();
        if (mHasNotify) {
            notifyDataChange();
        }
    }

}
