package com.hongguo.read.widget.reader.none;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hongguo.read.widget.reader.PageController;
import com.hongguo.read.widget.reader.base.BaseViewControl;


/**
 * Created by losg on 2018/4/2.
 */

public class NoneAnimation extends BaseViewControl {

    private float            mMinMove;

    public NoneAnimation(View view, PageController pageProvider) {
        super(view, pageProvider);

        mMinMove = ViewConfiguration.get(view.getContext()).getScaledTouchSlop() * 1.2f;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent me, float distanceX, float distanceY) {
        super.onScroll(e1, me, distanceX, distanceY);
        if (mMoveDirectLeftRight == MOVE_TO_LEFT_RIGHT_NONE) {
            if (Math.abs(mDragStartPos.x - mCurrentPosition.x) < mMinMove) return true;
            mMoveDirectLeftRight = (mDragStartPos.x - mCurrentPosition.x) > 0 ? MOVE_TO_LEFT : MOVE_TO_RIGHT;
            if (!prepareMove(mMoveDirectLeftRight)) {
                mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;
            }
            return true;
        }
        return true;
    }


    private boolean prepareMove(int direct) {

        if (mBookViewClickListener != null) {
            mBookViewClickListener.bookScrolled();
        }
        int currentPage;

        switch (direct) {
            //往左滑滑动(获取下一页 当前页为动,下一页为静态)
            case MOVE_TO_LEFT:
                //没有下一页了
                if (mPageController.currentPageIsEnd() && mPageController.currentChapterIsEnd()) {
                    if(mBookViewClickListener != null){
                        mBookViewClickListener.bookIsEndChapter();
                    }
                    return false;
                }

                mTemp = mMoveBitmap;
                mMoveBitmap = mStaticBitmap;
                mStaticBitmap = mTemp;

                mDrawBitmaps.clear();
                mDrawBitmaps.add(mStaticBitmap);

                currentPage = mPageController.getCurrentPage();
                //当前不是最后一页
                if (!mPageController.currentPageIsEnd()) {
                    currentPage++;
                } else {
                    //章节改变
                    mPageController.chapterChange(PageController.PAGE_NEXT);
                    currentPage = 0;
                }
                mPageController.setDrawBitmap(mStaticBitmap);
                mStaticBitmap = mPageController.drawPage(null, currentPage, 0, null,false).drawBitmap;

                break;
            //往右滑动(获取上一页，当前页为静，上一页为动态)
            case MOVE_TO_RIGHT:
                if (mPageController.currentPageIsStart() && mPageController.currentChapterIsFirst()) {
                    if(mBookViewClickListener != null){
                        mBookViewClickListener.bookIsFirstChapter();
                    }
                    return false;
                }

                mDrawBitmaps.clear();
                mDrawBitmaps.add(mMoveBitmap);

                currentPage = mPageController.getCurrentPage();
                if (currentPage != 0) {
                    currentPage--;
                } else {
                    mPageController.chapterChange(PageController.PAGE_FORWARD);
                    currentPage = -1;
                }

                mPageController.setDrawBitmap(mMoveBitmap);
                mMoveBitmap = mPageController.drawPage(null, currentPage, 0, null,false).drawBitmap;
                break;

        }
        return true;

    }

    @Override
    public boolean onTouch(MotionEvent me) {
        //actionUp super 主要处理是否是单击事件，如果单击时间，本成不做处理
        boolean event = super.onTouch(me);
        if (event && me.getAction() == MotionEvent.ACTION_UP) {
            if (mMoveDirectLeftRight != MOVE_TO_LEFT_RIGHT_NONE) {
                mBookView.invalidate();
                if (mMoveDirectLeftRight == MOVE_TO_LEFT) {
                    mAnimationStart = false;
                    mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;
                    if(mDrawBitmaps.contains(mMoveBitmap))
                    mDrawBitmaps.remove(mMoveBitmap);

                } else if (mMoveDirectLeftRight == MOVE_TO_RIGHT) {
                    mAnimationStart = false;
                    mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;

                    mTemp = mStaticBitmap;
                    mStaticBitmap = mMoveBitmap;
                    mMoveBitmap = mTemp;

                    mDrawBitmaps.remove(mMoveBitmap);
                }
            }
        }
        return event;
    }


    @Override
    public void onDraw(Canvas canvas) {
        drawStatic(canvas);
    }


    /**
     * 画静态
     *
     * @param canvas
     */
    private void drawStatic(Canvas canvas) {
        canvas.drawBitmap(mDrawBitmaps.get(0), 0, 0, mDrawPaint);
    }

}
