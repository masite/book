package com.hongguo.read.widget.pullfind;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.hongguo.read.R;

/**
 * Created by losg on 2018/3/29.
 */

public class WaterDropView extends View {

    private Circle topCircle;
    private Circle bottomCircle;
    private Paint  mPaint;
    private Path   mPath;
    private int   mColor          = 0xffff8b2e;
    private float mMaxHeightRatio = 3.2f;
    private float mMaxScaleRatio  = 0.25f;
    private float mMaxCircleRadius;//圆半径最大值
    private float mMinCircleRadius;//圆半径最小值

    public WaterDropView(Context context) {
        super(context);
        init(context, null);
    }

    public WaterDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaterDropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterDropView, 0, 0);
        int waterDropColor = a.getColor(R.styleable.WaterDropView_waterdrop_color, Color.GRAY);
        mPaint.setColor(waterDropColor);
        mMaxCircleRadius = a.getDimensionPixelSize(R.styleable.WaterDropView_max_circle_radius, 0);

        topCircle.radius = mMaxCircleRadius;
        topCircle.color = mColor;
        bottomCircle.radius = mMaxCircleRadius;
        bottomCircle.color = mColor;

        topCircle.x = mMaxCircleRadius;
        topCircle.y = mMaxCircleRadius;

        bottomCircle.x = mMaxCircleRadius;
        bottomCircle.y = mMaxCircleRadius;

        mMinCircleRadius = a.getDimensionPixelSize(R.styleable.WaterDropView_min_circle_radius, 0);

        a.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        topCircle = new Circle();
        bottomCircle = new Circle();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        parseAttrs(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽度：上圆和下圆的最大直径
        int width = (int) (mMaxCircleRadius * 2);
        //高度：上圆半径 + 圆心距 + 下圆半径
        int height = (int) Math.ceil(mMaxHeightRatio * mMaxCircleRadius + mMinCircleRadius + mMaxCircleRadius);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        makeBezierPath();
        //画顶部
        mPaint.setColor(mColor);
        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(topCircle.x, topCircle.y, topCircle.radius, mPaint);

        //画底部
        canvas.drawCircle(bottomCircle.x, bottomCircle.y, bottomCircle.radius, mPaint);
    }


    private void makeBezierPath() {
        mPath.reset();

        float top_left_x = mMaxCircleRadius - topCircle.radius;
        float top_left_y = topCircle.y;

        float top_right_x = top_left_x + topCircle.radius * 2;
        float top_right_y = topCircle.y;

        float bottom_left_x = mMaxCircleRadius - bottomCircle.radius;

        float bottom_left_y = bottomCircle.y;

        float bottom_right_x = bottom_left_x + bottomCircle.radius * 2;
        float bottom_right_y = bottomCircle.y;

        mPath.moveTo(top_right_x, top_right_y);

        mPath.lineTo(top_left_x, top_left_y);

        mPath.quadTo((bottomCircle.x - bottomCircle.radius),
                (bottomCircle.y + topCircle.y) / 2,
                bottom_left_x,
                bottom_left_y);
        mPath.lineTo(bottom_right_x, bottom_right_y);

        mPath.quadTo((bottomCircle.x + bottomCircle.radius),
                (bottomCircle.y + topCircle.y) / 2,
                top_right_x,
                top_right_y);
        mPath.close();
    }


    /**
     * 完成的百分比
     *
     * @param percent between[0,1]
     */
    public void updatePercent(float percent) {
        //设置topCircle 半径
        float topRadius = (mMaxCircleRadius - mMaxScaleRatio * percent * mMaxCircleRadius);
        topCircle.radius = topRadius;
        //设置bottomCircle 半径
        float bottomRadius = (mMinCircleRadius - mMaxCircleRadius) * percent + mMaxCircleRadius;
        bottomCircle.radius = bottomRadius;

        //两圆点的间距
        float bottomCircleOffset = mMaxHeightRatio * percent * mMaxCircleRadius;
        bottomCircle.y = topCircle.y + bottomCircleOffset;
        requestLayout();
    }

    /**
     * 实心圆
     */
    public class Circle {
        public float x;//圆x坐标
        public float y;//圆y坐标
        public float radius;//圆半径
        public int   color;//圆的颜色
    }

    public float getMaxCircleRadius() {
        return mMaxCircleRadius;
    }

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }
}
