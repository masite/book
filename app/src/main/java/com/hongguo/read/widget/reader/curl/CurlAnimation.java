package com.hongguo.read.widget.reader.curl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.hongguo.read.widget.reader.PageController;
import com.hongguo.read.widget.reader.base.BaseViewControl;


/**
 * Created by losg on 2018/4/2.
 */

public class CurlAnimation extends BaseViewControl {

    private static final int ANIMATION_TIME = 300;
    private static final int SHADOW_WIDTH   = 30;

    private int mCornerX = 1; // 拖拽点对应的页脚
    private int mCornerY = 1;
    private Path mPath0;
    private Path mPath1;

    private PointF mBezierStart1   = new PointF(); // 贝塞尔曲线起始点
    private PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
    private PointF mBeziervertex1  = new PointF(); // 贝塞尔曲线顶点
    private PointF mBezierEnd1     = new PointF(); // 贝塞尔曲线结束点

    private PointF mBezierStart2   = new PointF(); // 另一条贝塞尔曲线
    private PointF mBezierControl2 = new PointF();
    private PointF mBeziervertex2  = new PointF();
    private PointF mBezierEnd2     = new PointF();

    private Scroller mScroller;

    private float                  mMiddleX;
    private float                  mMiddleY;
    private float                  mDegrees;
    private float                  mTouchToCornerDis;
    private ColorMatrixColorFilter mColorMatrixFilter;
    private Matrix                 mMatrix;
    private float[] mMatrixArray = {0, 0, 0, 0, 0, 0, 0, 0, 1.0f};

    private boolean mIsRTandLB; // 是否属于右上左下
    private int[]   mBackShadowColors;// 背面颜色组
    private int[]   mFrontShadowColors;// 前面颜色组

    private GradientDrawable mBackShadowDrawableLR; // 有阴影的GradientDrawable
    private GradientDrawable mBackShadowDrawableRL;
    private GradientDrawable mFolderShadowDrawableLR;
    private GradientDrawable mFolderShadowDrawableRL;
    private GradientDrawable mFrontShadowDrawableHBT;
    private GradientDrawable mFrontShadowDrawableHTB;
    private GradientDrawable mFrontShadowDrawableVLR;
    private GradientDrawable mFrontShadowDrawableVRL;

    private Paint mPaint;

    private float mMaxLength;

    private float mMinMove;
    private float mMoveX;

    //屏幕的尺寸
    private int mScreenWidth;
    private int mScreenHeight;

    public CurlAnimation(View view, PageController pageProvider) {
        super(view, pageProvider);
        mScreenWidth = pageProvider.getViewRect().width();
        mScreenHeight = pageProvider.getViewRect().height();
        mMaxLength = (float) Math.hypot(mScreenWidth, mScreenHeight);

        mPath0 = new Path();
        mPath1 = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        createDrawable();

        ColorMatrix cm = new ColorMatrix();//设置颜色数组
        float array[] = {1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0};
        cm.set(array);
        mColorMatrixFilter = new ColorMatrixColorFilter(cm);
        mMatrix = new Matrix();

        mMinMove = ViewConfiguration.get(view.getContext()).getScaledTouchSlop() * 1.2f;


        mCurrentPosition.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
        mCurrentPosition.y = 0.01f;

        mScroller = new Scroller(view.getContext(), new LinearInterpolator());
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
        middlePointChange();
        mBookView.invalidate();
        return true;
    }

    private void middlePointChange() {
        //触摸y中间位置吧y变成屏幕高度
        if ((mDragStartPos.y > mScreenHeight / 3 && mDragStartPos.y < mScreenHeight * 2 / 3) || mMoveDirectLeftRight == MOVE_TO_RIGHT) {
            mCurrentPosition.y = mScreenHeight;
        }

        if (mDragStartPos.y > mScreenHeight / 3 && mDragStartPos.y < mScreenHeight / 2 && mMoveDirectLeftRight == MOVE_TO_LEFT) {
            mCurrentPosition.y = 1;
        }
    }

    @Override
    public boolean onDown(MotionEvent me) {
        calcCornerXY(me.getX(), me.getY());
        return super.onDown(me);
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
                mStaticBitmap = mPageController.drawPage(null, currentPage, 0, null,false).drawBitmap;
                mMoveX = 0;

                if (mScreenWidth / 2 > mDragStartPos.x) {
                    calcCornerXY(mScreenWidth - mDragStartPos.x, mDragStartPos.y);
                }


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
                mMoveBitmap = mPageController.drawPage(null, currentPage, 0, null,false).drawBitmap;
                mMoveX = mPageController.getViewRect().right + SHADOW_WIDTH;

                if (mDragStartPos.x > mScreenWidth / 2) {
                    calcCornerXY(mDragStartPos.x, mScreenHeight);
                } else {
                    calcCornerXY(mScreenWidth - mDragStartPos.x, mScreenHeight);
                }

                break;

        }
        return true;
    }


    /**
     * 计算拖拽点对应的拖拽脚
     *
     * @param x
     * @param y
     */
    public void calcCornerXY(float x, float y) {
        if (x <= mScreenWidth / 2) {
            mCornerX = 0;
        } else {
            mCornerX = mScreenWidth;
        }
        if (y <= mScreenHeight / 2) {
            mCornerY = 0;
        } else {
            mCornerY = mScreenHeight;
        }

        if ((mCornerX == 0 && mCornerY == mScreenHeight)
                || (mCornerX == mScreenWidth && mCornerY == 0)) {
            mIsRTandLB = true;
        } else {
            mIsRTandLB = false;
        }

    }

    @Override
    public boolean onTouch(MotionEvent me) {
        //actionUp super 主要处理是否是单击事件，如果单击时间，本成不做处理
        boolean event = super.onTouch(me);
        if (event && me.getAction() == MotionEvent.ACTION_UP) {
            if (mMoveDirectLeftRight != MOVE_TO_LEFT_RIGHT_NONE)
                //处理动画信息(翻页动画)
                animationToArm(ANIMATION_TIME);
        }
        return event;
    }


    private void animationToArm(int time) {
        mAnimationStart = true;
        int dx, dy;
        if (mCornerX > 0 && mMoveDirectLeftRight == MOVE_TO_LEFT) {
            dx = -(int) (mScreenWidth + mCurrentPosition.x);
        } else {
            dx = (int) (mScreenWidth - mCurrentPosition.x + mScreenWidth);
        }
        if (mCornerY > 0) {
            dy = (int) (mScreenHeight - mCurrentPosition.y);
        } else {
            dy = (int) (1 - mCurrentPosition.y); // 防止mTouchY最终变为0
        }
        mScroller.startScroll((int) mCurrentPosition.x, (int) mCurrentPosition.y, dx, dy, time);
        mBookView.invalidate();
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            mCurrentPosition.x = x;
            mCurrentPosition.y = y;
            middlePointChange();
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                if (mMoveDirectLeftRight == MOVE_TO_RIGHT) {
                    mTemp = mStaticBitmap;
                    mStaticBitmap = mMoveBitmap;
                    mMoveBitmap = mTemp;
                }
                mDrawBitmaps.remove(mMoveBitmap);
                animtionEndCheckUpate();
            }
            mBookView.postInvalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawStatic(canvas);
        //如果存在move的层
        if (mDrawBitmaps.size() == 2) {
            drawMove(canvas);
        }
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
        switch (mMoveDirectLeftRight) {
            case MOVE_TO_LEFT:
                calcPoints();
                drawCurrentPageArea(canvas, mDrawBitmaps.get(1), mPath0);
                drawNextPageAreaAndShadow(canvas, mDrawBitmaps.get(0));
                drawCurrentPageShadow(canvas);
                drawCurrentBackArea(canvas, mDrawBitmaps.get(1));
                break;
            default:
                calcPoints();
                drawCurrentPageArea(canvas, mDrawBitmaps.get(1), mPath0);
                drawNextPageAreaAndShadow(canvas, mDrawBitmaps.get(0));
                drawCurrentPageShadow(canvas);
                drawCurrentBackArea(canvas, mDrawBitmaps.get(1));
                break;
        }
    }

    public boolean right() {
        if (mCornerX > -4)
            return false;
        return true;
    }

    /**
     * 绘制翻起页背面
     *
     * @param canvas
     * @param bitmap
     */
    private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
        int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
        float f1 = Math.abs(i - mBezierControl1.x);
        int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
        float f2 = Math.abs(i1 - mBezierControl2.y);
        float f3 = Math.min(f1, f2);
        mPath1.reset();
        mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
        mPath1.lineTo(mCurrentPosition.x, mCurrentPosition.y);
        mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mPath1.close();
        GradientDrawable mFolderShadowDrawable;
        int left;
        int right;
        if (mIsRTandLB) {
            left = (int) (mBezierStart1.x - 1);
            right = (int) (mBezierStart1.x + f3 + 1);
            mFolderShadowDrawable = mFolderShadowDrawableLR;
        } else {
            left = (int) (mBezierStart1.x - f3 - 1);
            right = (int) (mBezierStart1.x + 1);
            mFolderShadowDrawable = mFolderShadowDrawableRL;
        }
        canvas.save();
        try {
            canvas.clipPath(mPath0);
            canvas.clipPath(mPath1, Region.Op.INTERSECT);
        } catch (Exception e) {
        }

        mPaint.setColorFilter(mColorMatrixFilter);
        //对Bitmap进行取色
        int color = bitmap.getPixel(1, 1);
        //获取对应的三色
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        //转换成含有透明度的颜色
        int tempColor = Color.argb(200, red, green, blue);


        float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
                mBezierControl2.y - mCornerY);
        float f8 = (mCornerX - mBezierControl1.x) / dis;
        float f9 = (mBezierControl2.y - mCornerY) / dis;
        mMatrixArray[0] = 1 - 2 * f9 * f9;
        mMatrixArray[1] = 2 * f8 * f9;
        mMatrixArray[3] = mMatrixArray[1];
        mMatrixArray[4] = 1 - 2 * f8 * f8;
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);
        mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
        mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
        canvas.drawBitmap(bitmap, mMatrix, mPaint);
        //背景叠加
        canvas.drawColor(tempColor);

        mPaint.setColorFilter(null);

        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
                (int) (mBezierStart1.y + mMaxLength));
        mFolderShadowDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制翻起页的阴影
     *
     * @param canvas
     */
    public void drawCurrentPageShadow(Canvas canvas) {
        double degree;
        if (mIsRTandLB) {
            degree = Math.PI
                    / 4
                    - Math.atan2(mBezierControl1.y - mCurrentPosition.y, mCurrentPosition.x
                    - mBezierControl1.x);
        } else {
            degree = Math.PI
                    / 4
                    - Math.atan2(mCurrentPosition.y - mBezierControl1.y, mCurrentPosition.x
                    - mBezierControl1.x);
        }
        // 翻起页阴影顶点与touch点的距离
        double d1 = (float) 25 * 1.414 * Math.cos(degree);
        double d2 = (float) 25 * 1.414 * Math.sin(degree);
        float x = (float) (mCurrentPosition.x + d1);
        float y;
        if (mIsRTandLB) {
            y = (float) (mCurrentPosition.y + d2);
        } else {
            y = (float) (mCurrentPosition.y - d2);
        }
        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mCurrentPosition.x, mCurrentPosition.y);
        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.close();
        float rotateDegrees;
        canvas.save();
        try {
            canvas.clipPath(mPath0, Region.Op.XOR);
            canvas.clipPath(mPath1, Region.Op.INTERSECT);
        } catch (Exception e) {
            // TODO: handle exception
        }

        int leftx;
        int rightx;
        GradientDrawable mCurrentPageShadow;
        if (mIsRTandLB) {
            leftx = (int) (mBezierControl1.x);
            rightx = (int) mBezierControl1.x + 25;
            mCurrentPageShadow = mFrontShadowDrawableVLR;
        } else {
            leftx = (int) (mBezierControl1.x - 25);
            rightx = (int) mBezierControl1.x + 1;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        }

        rotateDegrees = (float) Math.toDegrees(Math.atan2(mCurrentPosition.x
                - mBezierControl1.x, mBezierControl1.y - mCurrentPosition.y));
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
        mCurrentPageShadow.setBounds(leftx,
                (int) (mBezierControl1.y - mMaxLength), rightx,
                (int) (mBezierControl1.y));
        mCurrentPageShadow.draw(canvas);
        canvas.restore();

        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mCurrentPosition.x, mCurrentPosition.y);
        mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.close();
        canvas.save();
        try {
            canvas.clipPath(mPath0, Region.Op.XOR);
            canvas.clipPath(mPath1, Region.Op.INTERSECT);
        } catch (Exception e) {
        }

        if (mIsRTandLB) {
            leftx = (int) (mBezierControl2.y);
            rightx = (int) (mBezierControl2.y + 25);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else {
            leftx = (int) (mBezierControl2.y - 25);
            rightx = (int) (mBezierControl2.y + 1);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        }
        rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y
                - mCurrentPosition.y, mBezierControl2.x - mCurrentPosition.x));
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
        float temp;
        if (mBezierControl2.y < 0)
            temp = mBezierControl2.y - mScreenHeight;
        else
            temp = mBezierControl2.y;

        int hmg = (int) Math.hypot(mBezierControl2.x, temp);
        if (hmg > mMaxLength)
            mCurrentPageShadow
                    .setBounds((int) (mBezierControl2.x - 25) - hmg, leftx,
                            (int) (mBezierControl2.x + mMaxLength) - hmg,
                            rightx);
        else
            mCurrentPageShadow.setBounds(
                    (int) (mBezierControl2.x - mMaxLength), leftx,
                    (int) (mBezierControl2.x), rightx);

        mCurrentPageShadow.draw(canvas);
        canvas.restore();
    }

    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        mPath1.reset();
        mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.lineTo(mCornerX, mCornerY);
        mPath1.close();

        mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
                - mCornerX, mBezierControl2.y - mCornerY));
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        if (mIsRTandLB) {  //左下及右上
            leftx = (int) (mBezierStart1.x);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
            rightx = (int) mBezierStart1.x;
            mBackShadowDrawable = mBackShadowDrawableRL;
        }
        canvas.save();
        try {
            canvas.clipPath(mPath0);
            canvas.clipPath(mPath1, Region.Op.INTERSECT);
        } catch (Exception e) {
        }


        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
                (int) (mMaxLength + mBezierStart1.y));//左上及右下角的xy坐标值,构成一个矩形
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
        mPath0.reset();
        mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
                mBezierEnd1.y);
        mPath0.lineTo(mCurrentPosition.x, mCurrentPosition.y);
        mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
                mBezierStart2.y);
        mPath0.lineTo(mCornerX, mCornerY);
        mPath0.close();

        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawBitmap(bitmap, 0, 0, null);
        try {
            canvas.restore();
        } catch (Exception e) {

        }

    }


    private void calcPoints() {
        mMiddleX = (mCurrentPosition.x + mCornerX) / 2;
        mMiddleY = (mCurrentPosition.y + mCornerY) / 2;
        mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
                * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
        mBezierControl1.y = mCornerY;
        mBezierControl2.x = mCornerX;

        float f4 = mCornerY - mMiddleY;
        if (f4 == 0) {
            mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                    * (mCornerX - mMiddleX) / 0.1f;

        } else {
            mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                    * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
        }
        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
                / 2;
        mBezierStart1.y = mCornerY;

        // 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
        // 如果继续翻页，会出现BUG故在此限制
        if (mCurrentPosition.x > 0 && mCurrentPosition.x < mScreenWidth) {
            if (mBezierStart1.x < 0 || mBezierStart1.x > mScreenWidth) {
                if (mBezierStart1.x < 0)
                    mBezierStart1.x = mScreenWidth - mBezierStart1.x;

                float f1 = Math.abs(mCornerX - mCurrentPosition.x);
                float f2 = mScreenWidth * f1 / mBezierStart1.x;
                mCurrentPosition.x = Math.abs(mCornerX - f2);

                float f3 = Math.abs(mCornerX - mCurrentPosition.x)
                        * Math.abs(mCornerY - mCurrentPosition.y) / f1;
                mCurrentPosition.y = Math.abs(mCornerY - f3);

                mMiddleX = (mCurrentPosition.x + mCornerX) / 2;
                mMiddleY = (mCurrentPosition.y + mCornerY) / 2;

                mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
                        * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
                mBezierControl1.y = mCornerY;

                mBezierControl2.x = mCornerX;

                float f5 = mCornerY - mMiddleY;
                if (f5 == 0) {
                    mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                            * (mCornerX - mMiddleX) / 0.1f;
                } else {
                    mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                            * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
                }

                mBezierStart1.x = mBezierControl1.x
                        - (mCornerX - mBezierControl1.x) / 2;
            }
        }
        mBezierStart2.x = mCornerX;
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
                / 2;

        mTouchToCornerDis = (float) Math.hypot((mCurrentPosition.x - mCornerX),
                (mCurrentPosition.y - mCornerY));

        mBezierEnd1 = getCross(new PointF(mCurrentPosition.x, mCurrentPosition.y), mBezierControl1, mBezierStart1,
                mBezierStart2);
        mBezierEnd2 = getCross(new PointF(mCurrentPosition.x, mCurrentPosition.y), mBezierControl2, mBezierStart1,
                mBezierStart2);

        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
    }

    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     *
     * @param P1
     * @param P2
     * @param P3
     * @param P4
     * @return
     */
    public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        // 二元函数通式： y=ax+b
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

        float a2 = (P4.y - P3.y) / (P4.x - P3.x);
        float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
        CrossP.x = (b2 - b1) / (a1 - a2);
        CrossP.y = a1 * CrossP.x + b1;
        return CrossP;
    }


    /**
     * 创建阴影的GradientDrawable
     */
    private void createDrawable() {
        int[] color = {0x333333, 0xb0333333};
        mFolderShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, color);
        mFolderShadowDrawableRL
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFolderShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, color);
        mFolderShadowDrawableLR
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowColors = new int[]{0xff111111, 0x111111};
        mBackShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowColors = new int[]{0x80111111, 0x111111};
        mFrontShadowDrawableVLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
        mFrontShadowDrawableVLR
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableVRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
        mFrontShadowDrawableVRL
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHTB = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
        mFrontShadowDrawableHTB
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHBT = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
        mFrontShadowDrawableHBT
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public void initOthers() {
        super.initOthers();
        mMoveX = 0;
    }

}
