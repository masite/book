package com.hongguo.read.widget.reader.down;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.ScrollerCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hongguo.read.widget.reader.ChapterPagerInfo;
import com.hongguo.read.widget.reader.PageController;
import com.hongguo.read.widget.reader.base.BaseViewControl;


/**
 * Created by losg on 2018/4/2.
 * 单章节话滑动，章节之间采用上下式(整页滑动)
 */

public class DownAnimation extends BaseViewControl {

    private static final int SHADOW_WIDTH = 30;
    private static final int ANIMTION_TIME = 500;

    private ScrollerCompat mScrollerCompat;
    private float mMinMove;
    private DrawItem mCurrentItem;
    private DrawItem mCacheItem;
    private int mChapterTotalMoveSize;
    private int mMinFling;
    private int mStartScroll;
    private boolean mFirstScroll;
    private boolean mChapterChange;
    private int mChapterMove;
    private GradientDrawable mBackShadowDrawableLR;

    public DownAnimation(View view, PageController pageProvider) {
        super(view, pageProvider);
        mMinMove = ViewConfiguration.get(view.getContext()).getScaledTouchSlop() * 1.2f;
        mCurrentItem = new DrawItem();
        mCacheItem = new DrawItem();
        mMinFling = ViewConfiguration.get(view.getContext()).getScaledMinimumFlingVelocity();
        mScrollerCompat = ScrollerCompat.create(view.getContext());

        int[] mBackShadowColors = new int[]{0x66000000, 0x00000000};
        mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent me, float distanceX, float distanceY) {
        super.onScroll(e1, me, distanceX, distanceY);
        //第一次 distanceY 跳跃(不用)
        if (mFirstScroll) {
            mFirstScroll = false;
            return true;
        }
        startScroll((int) distanceY);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent me) {
        mScrollerCompat.abortAnimation();
        mFirstScroll = true;
        return super.onDown(me);
    }

    private void startScroll(int dy) {
        int contentHeight = mPageController.getContentHeight();
        if (dy == 0) {
            return;
        }

        if (mChapterTotalMoveSize == 0 && mPageController.getCurrentPage() != 0) {
            mChapterTotalMoveSize = contentHeight * (mPageController.getCurrentPage());
        }

        if (mChapterTotalMoveSize <= 0) {
            mChapterTotalMoveSize = 0;
        }

        if (mChapterTotalMoveSize >= (mCurrentItem.mTotalSize - 1) * contentHeight) {
            mChapterTotalMoveSize = (mCurrentItem.mTotalSize - 1) * contentHeight;
        }

        //向下滑动,并且当前是首页,检查是否有上一章节
        if (mChapterTotalMoveSize == 0 && dy < 0 || (mChapterMove != 0 && mMoveDirectTopBottom == MOVE_TO_BOTTOM)) {
            //滚动做准备
            if (mMoveDirectTopBottom == MOVE_TO_TOP_BOTTOM_NONE) {
                mMoveDirectTopBottom = MOVE_TO_BOTTOM;
                prepareMove(mMoveDirectTopBottom);
            }
            if (mMoveDirectTopBottom != MOVE_TO_TOP_BOTTOM_NONE) {
                moveScroll(dy);
            }
            return;
        }

        //往上滑动，并且当前页是最后一页
        if (mChapterTotalMoveSize == (mCurrentItem.mTotalSize - 1) * contentHeight && dy > 0 || (mChapterMove != 0 && mMoveDirectTopBottom == MOVE_TO_TOP)) {
            //滚动做准备
            if (mMoveDirectTopBottom == MOVE_TO_TOP_BOTTOM_NONE) {
                mMoveDirectTopBottom = MOVE_TO_TOP;
                prepareMove(mMoveDirectTopBottom);
            }
            if (mMoveDirectTopBottom != MOVE_TO_TOP_BOTTOM_NONE) {
                moveScroll(dy);
            }
            return;
        }

        mChapterTotalMoveSize += dy;
        //到达末页
        if (mChapterTotalMoveSize >= (mCurrentItem.mTotalSize - 1) * contentHeight) {
            mChapterTotalMoveSize = (mCurrentItem.mTotalSize - 1) * contentHeight;

            if (mCacheItem.mPagerInfo != null && mCacheItem.mPagerInfo.pageNumber == mCacheItem.mTotalSize - 1) {
                DrawItem drawItem = mCurrentItem;
                mCurrentItem = mCacheItem;
                mCacheItem = drawItem;
                mPageController.setCurrentPage(mCurrentItem.mPagerInfo.pageNumber);
            }
            mCurrentItem.offset = 0;
            mCacheItem.offset = 2 * contentHeight;
            mBookView.invalidate();
            return;
        }

        //当前是第一页
        if (mChapterTotalMoveSize <= 0) {
            mChapterTotalMoveSize = 0;
            if (mCacheItem.mPagerInfo != null && mCacheItem.mPagerInfo.pageNumber == 0) {
                DrawItem drawItem = mCurrentItem;
                mCurrentItem = mCacheItem;
                mCacheItem = drawItem;
                mPageController.setCurrentPage(mCurrentItem.mPagerInfo.pageNumber);
            }
            mCurrentItem.offset = 0;
            mCacheItem.offset = 2 * contentHeight;
            mBookView.invalidate();
            return;
        }

        int currentPageStart = mCurrentItem.mPagerInfo.pageNumber * contentHeight;
        int currentPageEnd = (mCurrentItem.mPagerInfo.pageNumber + 1) * contentHeight;


        if (mChapterTotalMoveSize >= currentPageStart && mChapterTotalMoveSize < currentPageEnd) {
            //下一页已经获取到了,直接移动
            if (mCacheItem.mPagerInfo == null || mCacheItem.mPagerInfo.pageNumber < mCurrentItem.mPagerInfo.pageNumber) {
                //没有获取，先获取当前页面信息
                mCacheItem.mPagerInfo = mPageController.getPageInfo(mCurrentItem.mPagerInfo.pageNumber + 1);
                mCacheItem.mTotalSize = mCurrentItem.mTotalSize;
            }
            //做滑动操作
            mCurrentItem.offset = -(mChapterTotalMoveSize - currentPageStart);
            mCacheItem.offset = contentHeight + mCurrentItem.offset;
        } else if (mChapterTotalMoveSize >= currentPageEnd) {
            DrawItem drawItem = mCurrentItem;
            mCurrentItem = mCacheItem;
            mCacheItem = drawItem;
            mPageController.setCurrentPage(mCurrentItem.mPagerInfo.pageNumber);

            currentPageStart = mCurrentItem.mPagerInfo.pageNumber * contentHeight;
            mCurrentItem.offset = -(mChapterTotalMoveSize - currentPageStart);
            mCacheItem.offset = contentHeight + mCurrentItem.offset;

        } else if (mChapterTotalMoveSize < currentPageStart && mChapterTotalMoveSize > currentPageStart - contentHeight) {
            //下一页已经获取到了,直接移动
            if (mCacheItem.mPagerInfo == null || mCacheItem.mPagerInfo.pageNumber > mCurrentItem.mPagerInfo.pageNumber) {
                //没有获取，先获取当前页面信息
                mCacheItem.mPagerInfo = mPageController.getPageInfo(mCurrentItem.mPagerInfo.pageNumber - 1);
                mCacheItem.mTotalSize = mCurrentItem.mTotalSize;
            }
            //做滑动操作
            mCurrentItem.offset = currentPageStart - mChapterTotalMoveSize;
            mCacheItem.offset = mCurrentItem.offset - contentHeight;
        } else if (mChapterTotalMoveSize <= currentPageStart - contentHeight) {
            DrawItem drawItem = mCurrentItem;
            mCurrentItem = mCacheItem;
            mCacheItem = drawItem;
            mPageController.setCurrentPage(mCurrentItem.mPagerInfo.pageNumber);

            currentPageStart = mCurrentItem.mPagerInfo.pageNumber * contentHeight;
            mCurrentItem.offset = currentPageStart - mChapterTotalMoveSize;
            mCacheItem.offset = mCurrentItem.offset - contentHeight;

        }
        mBookView.invalidate();
    }

    private void moveScroll(int dy) {
        //滚动操作
        mChapterMove += dy;

        if (mMoveDirectTopBottom == MOVE_TO_TOP) {
            if (mChapterMove > mPageController.getViewRect().height()) {
                mChapterMove = mPageController.getViewRect().height();
            }
            if (mChapterMove < 0) {
                mChapterMove = 0;
            }
        } else {
            if (mChapterMove > mPageController.getViewRect().height() + SHADOW_WIDTH) {
                mChapterMove = mPageController.getViewRect().height() + SHADOW_WIDTH;
            }
            if (mChapterMove < 0) {
                mChapterMove = 0;
            }
        }

        mBookView.invalidate();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mChapterChange) return true;
        if (Math.abs(velocityY) > mMinFling) {
            mStartScroll = 0;
            mScrollerCompat.fling(0, 0, 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            mBookView.invalidate();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (mChapterChange) {
            drawMove(canvas);
            return;
        }

        if (mCurrentItem.mPagerInfo == null) {
            mCurrentItem.mPagerInfo = mPageController.drawPage(canvas, mPageController.getCurrentPage(), 0, null, true).pagerInfo;
            mCurrentItem.mTotalSize = mCurrentItem.mPagerInfo.totalSize;
        } else {
            mPageController.drawPage(canvas, mCurrentItem.mPagerInfo.pageNumber, mCurrentItem.offset, mCurrentItem.mPagerInfo, true);
        }

        if (mCacheItem.mPagerInfo != null) {
            mPageController.drawPage(canvas, mCacheItem.mPagerInfo.pageNumber, mCacheItem.offset, mCacheItem.mPagerInfo, true, false);
        }

        mPageController.drawHeader(canvas, false);
    }

    private void drawMove(Canvas canvas) {
        //画底部
        canvas.drawBitmap(mDrawBitmaps.get(0), 0, 0, mDrawPaint);
        canvas.drawBitmap(mDrawBitmaps.get(1), 0, -mChapterMove, mDrawPaint);

        mBackShadowDrawableLR.setBounds(0, mPageController.getViewRect().bottom - mChapterMove, mPageController.getViewRect().right, mPageController.getViewRect().bottom - mChapterMove + SHADOW_WIDTH);
        mBackShadowDrawableLR.draw(canvas);
    }

    public static class DrawItem {
        public ChapterPagerInfo.PagerInfo mPagerInfo;         //画图缓存的信息
        public int offset;             //当前页相对计算前的位移
        public int mTotalSize;         //本章节总共的页数
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScrollerCompat.computeScrollOffset()) {
            int currY = mScrollerCompat.getCurrY();
            int dy = mStartScroll - currY;
            if (mChapterTotalMoveSize == (mCurrentItem.mTotalSize - 1) * mPageController.getContentHeight() || mChapterTotalMoveSize == 0) {
                mScrollerCompat.abortAnimation();
            } else {
                startScroll(dy);
            }
            mBookView.postInvalidate();
            mStartScroll = currY;
        }
    }


    private boolean prepareMove(int direct) {

        if (mBookViewClickListener != null) {
            mBookViewClickListener.bookScrolled();
        }
        int currentPage;

        switch (direct) {
            //往左滑滑动(获取下一页 当前页为动,下一页为静态)
            case MOVE_TO_TOP:
                //没有下一页了
                if (mPageController.currentChapterIsEnd()) {
                    mMoveDirectTopBottom = MOVE_TO_TOP_BOTTOM_NONE;
                    mChapterChange = false;
                    if (mBookViewClickListener != null) {
                        mBookViewClickListener.bookIsEndChapter();
                    }
                    return false;
                }

                mTemp = mMoveBitmap;
                mMoveBitmap = mStaticBitmap;
                mStaticBitmap = mTemp;

                mDrawBitmaps.clear();
                mDrawBitmaps.add(mStaticBitmap);
                mDrawBitmaps.add(mMoveBitmap);

                mPageController.setDrawBitmap(mMoveBitmap);
                mMoveBitmap = mPageController.drawPage(null, mCurrentItem.mTotalSize - 1, 0, null, false).drawBitmap;

                //章节改变
                mPageController.chapterChange(PageController.PAGE_NEXT);
                currentPage = 0;

                mChapterChange = true;
                mPageController.setDrawBitmap(mStaticBitmap);
                mStaticBitmap = mPageController.drawPage(null, currentPage, 0, null, false).drawBitmap;
                mChapterMove = 0;
                mBookView.invalidate();
                break;
            //往右滑动(获取上一页，当前页为静，上一页为动态)
            case MOVE_TO_BOTTOM:

                if (mPageController.currentChapterIsFirst()) {
                    mMoveDirectTopBottom = MOVE_TO_TOP_BOTTOM_NONE;
                    mChapterChange = false;
                    if (mBookViewClickListener != null) {
                        mBookViewClickListener.bookIsFirstChapter();
                    }
                    return false;
                }

                mDrawBitmaps.clear();
                mDrawBitmaps.add(mStaticBitmap);
                mDrawBitmaps.add(mMoveBitmap);

                //当前章节的第一页
                mPageController.setDrawBitmap(mStaticBitmap);
                mStaticBitmap = mPageController.drawPage(null, 0, 0, null, false).drawBitmap;

                //上一章节的末页
                mPageController.chapterChange(PageController.PAGE_FORWARD);
                mPageController.setDrawBitmap(mMoveBitmap);
                mMoveBitmap = mPageController.drawPage(null, -1, 0, null, false).drawBitmap;
                mChapterChange = true;
                mChapterMove = mPageController.getViewRect().height() + SHADOW_WIDTH;
                mBookView.invalidate();
                break;

        }
        return true;

    }

    @Override
    public boolean onTouch(MotionEvent me) {
        //actionUp super 主要处理是否是单击事件，如果单击时间，本成不做处理
        boolean event = super.onTouch(me);
        if (event && me.getAction() == MotionEvent.ACTION_UP && mChapterChange) {
            if (mMoveDirectTopBottom != MOVE_TO_LEFT_RIGHT_NONE)
                //处理动画信息(翻页动画)
                animationToArm(ANIMTION_TIME);
        }
        return true;
    }

    private void animationToArm(int time) {
        ValueAnimator valueAnimator = new ValueAnimator();
        if (mMoveDirectTopBottom == MOVE_TO_TOP) {
            valueAnimator.setIntValues(mChapterMove, mPageController.getViewRect().bottom + SHADOW_WIDTH);
        } else {
            valueAnimator.setIntValues(mChapterMove, 0);
        }
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(animation -> {
            mChapterMove = (int) animation.getAnimatedValue();
            if (mMoveDirectTopBottom == MOVE_TO_TOP && mChapterMove >= mPageController.getViewRect().bottom + SHADOW_WIDTH) {
                mAnimationStart = false;
                mDrawBitmaps.remove(mStaticBitmap);
                mDrawBitmaps.remove(mMoveBitmap);
                mChapterMove = 0;

                mChapterTotalMoveSize = 0;
                mCurrentItem.mPagerInfo = null;
                mCacheItem.mPagerInfo = null;
                mChapterChange = false;
                animtionEndCheckUpate();

                mBookView.invalidate();
                return;
            } else if (mMoveDirectTopBottom == MOVE_TO_BOTTOM && mChapterMove <= 0) {

                mAnimationStart = false;

                mDrawBitmaps.remove(mStaticBitmap);
                mDrawBitmaps.remove(mMoveBitmap);

                mChapterMove = 0;
                mChapterTotalMoveSize = 0;
                mCurrentItem.mPagerInfo = null;
                mCacheItem.mPagerInfo = null;
                mChapterChange = false;
                animtionEndCheckUpate();

                mBookView.invalidate();
                return;
            }
            mBookView.invalidate();
        });
        valueAnimator.start();
    }

    @Override
    public void notifyDataChange() {
        mHasNotify = true;
        if (mAnimationStart || mChapterChange) return;
        mDrawBitmaps.clear();
        //重新获取显示的内容
        show();
        initOthers();
        mBookView.invalidate();
        mHasNotify = false;
    }

    @Override
    public void initOthers() {
        super.initOthers();
        mChapterTotalMoveSize = 0;
        mCurrentItem.mPagerInfo = null;
        mCacheItem.mPagerInfo = null;
        mChapterChange = false;
        mChapterTotalMoveSize = 0;
    }
}
