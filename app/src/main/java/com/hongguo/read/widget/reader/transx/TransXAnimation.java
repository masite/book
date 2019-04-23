package com.hongguo.read.widget.reader.transx;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hongguo.read.widget.reader.PageController;
import com.hongguo.read.widget.reader.base.BaseViewControl;


/**
 * Created by losg on 2018/4/2.
 */

public class TransXAnimation extends BaseViewControl {

    private static final int ANIMTION_TIME = 500;
    private static final int SHADOW_WIDTH  = 30;

    private float            mMinMove;
    private GradientDrawable mBackShadowDrawableLR;
    private float            mMoveX;

    public TransXAnimation(View view, PageController pageProvider) {
        super(view, pageProvider);
        //shadow 的颜色
        int[] mBackShadowColors = new int[]{0x66000000, 0x00000000};
        mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mMinMove = ViewConfiguration.get(view.getContext()).getScaledTouchSlop() * 1.5f;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent me, float distanceX, float distanceY) {
        super.onScroll(e1, me, distanceX, distanceY);
        if (mMoveDirectLeftRight == MOVE_TO_LEFT_RIGHT_NONE) {
            if (Math.abs(e1.getX() - me.getX()) < mMinMove) return true;
            mMoveDirectLeftRight = (e1.getX() - me.getX()) > 0 ? MOVE_TO_LEFT : MOVE_TO_RIGHT;
            if (!prepareMove(mMoveDirectLeftRight)) {
                mMoveDirectLeftRight = MOVE_TO_LEFT_RIGHT_NONE;
            }
            return true;
        }

        //滚动操作
        mMoveX += distanceX;

        if (mMoveDirectLeftRight == MOVE_TO_LEFT) {
            if (mMoveX > mPageController.getViewRect().right) {
                mMoveX = mPageController.getViewRect().right;
            }

            if (mMoveX < 0) {
                mMoveX = 0;
            }
        }

        mBookView.invalidate();
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
                mStaticBitmap = mPageController.drawPage(null, currentPage, 0, null, false).drawBitmap;
                mMoveX = 0;


                break;
            //往右滑动(获取上一页，当前页为静，上一页为动态)
            case MOVE_TO_RIGHT:

                if (mPageController.currentPageIsStart() && mPageController.currentChapterIsFirst()) {
                    if (mBookViewClickListener != null) {
                        mBookViewClickListener.bookIsFirstChapter();
                    }
                    return false;
                }

                mDrawBitmaps.clear();
                mDrawBitmaps.add(mStaticBitmap);
                mDrawBitmaps.add(mMoveBitmap);

                currentPage = mPageController.getCurrentPage();
                if (currentPage != 0) {
                    currentPage--;
                } else {
                    mPageController.chapterChange(PageController.PAGE_FORWARD);
                    currentPage = -1;
                }

                mPageController.setDrawBitmap(mMoveBitmap);
                mMoveBitmap = mPageController.drawPage(null, currentPage, 0, null, false).drawBitmap;


                mMoveX = mPageController.getViewRect().right + SHADOW_WIDTH;
                break;

        }
        return true;

    }


    @Override
    public boolean onTouch(MotionEvent me) {
        //actionUp super 主要处理是否是单击事件，如果单击时间，本成不做处理
        boolean event = super.onTouch(me);
        if (event && me.getAction() == MotionEvent.ACTION_UP) {
            if (mMoveDirectLeftRight != MOVE_TO_LEFT_RIGHT_NONE)
                //处理动画信息(翻页动画)
                animationToArm(ANIMTION_TIME);
        }
        return event;
    }


    private void animationToArm(int time) {
        mAnimationStart = true;
        ValueAnimator valueAnimator = new ValueAnimator();
        if (mMoveDirectLeftRight == MOVE_TO_LEFT) {
            valueAnimator.setFloatValues(mMoveX, mPageController.getViewRect().right + SHADOW_WIDTH);
        } else {
            valueAnimator.setFloatValues(mMoveX, 0);
        }
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(animation -> {
            mMoveX = (float) animation.getAnimatedValue();
            mBookView.invalidate();
            if (mMoveDirectLeftRight == MOVE_TO_LEFT && mMoveX >= mPageController.getViewRect().right + SHADOW_WIDTH) {
                mDrawBitmaps.remove(mMoveBitmap);
                animtionEndCheckUpate();

            } else if (mMoveDirectLeftRight == MOVE_TO_RIGHT && mMoveX <= 0) {
                mTemp = mStaticBitmap;
                mStaticBitmap = mMoveBitmap;
                mMoveBitmap = mTemp;
                mDrawBitmaps.remove(mMoveBitmap);
                animtionEndCheckUpate();
            }

        });
        valueAnimator.start();
    }


    @Override
    public void onDraw(Canvas canvas) {
        show();
        drawStatic(canvas);

        //如果存在move的层
        if (mDrawBitmaps.size() == 2) {
            drawMove(canvas);
            drawShadow(canvas);
        }
    }

    private void drawShadow(Canvas canvas) {
        mBackShadowDrawableLR.setBounds((int) (mPageController.getViewRect().right - mMoveX), 0, (int) (mPageController.getViewRect().right - mMoveX + SHADOW_WIDTH), mPageController.getViewRect().height());
        mBackShadowDrawableLR.draw(canvas);
    }

    /**
     * 画静态
     *
     * @param canvas
     */
    private void drawStatic(Canvas canvas) {
        canvas.drawBitmap(mDrawBitmaps.get(0), 0, 0, mDrawPaint);
    }

    private void drawMove(Canvas canvas) {
        canvas.drawBitmap(mDrawBitmaps.get(1), -mMoveX, 0, mDrawPaint);
    }

    @Override
    public void initOthers() {
        super.initOthers();
        mMoveX = 0;
    }
}
