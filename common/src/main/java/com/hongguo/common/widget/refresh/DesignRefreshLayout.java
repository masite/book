package com.hongguo.common.widget.refresh;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hongguo.read.base.R;
import com.losg.library.base.BaseView;
import com.losg.library.base.IRefreshView;

/**
 * Created by losg
 */

public class DesignRefreshLayout extends SwipeRefreshLayout implements IRefreshView{


    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private View mAppBarLayout;
    private State mCurrentState = State.EXPANDED;

    private float   startY;
    private float   startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private int     mTouchSlop;


    public DesignRefreshLayout(Context context) {
        this(context, null);
    }

    public DesignRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.colorPrimary);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof CoordinatorLayout) {
            mAppBarLayout = ((CoordinatorLayout) child).getChildAt(0);
            if (mAppBarLayout instanceof AppBarLayout) {
                ((AppBarLayout) mAppBarLayout).addOnOffsetChangedListener(mOffsetChangedListener);
            }
        }
    }

    private AppBarLayout.OnOffsetChangedListener mOffsetChangedListener = (appBarLayout, verticalOffset) -> {
        if (verticalOffset == 0) {
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            mCurrentState = State.COLLAPSED;
        } else {
            mCurrentState = State.IDLE;
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAppBarLayout != null && mAppBarLayout instanceof AppBarLayout) {
            ((AppBarLayout) mAppBarLayout).removeOnOffsetChangedListener(mOffsetChangedListener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAppBarLayout != null && mAppBarLayout instanceof AppBarLayout) {
            if (mCurrentState != State.EXPANDED) {
                getChildAt(0).dispatchTouchEvent(ev);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if(mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setRefreshStatus(BaseView.RefreshStatus refreshStatus) {
        setRefreshing(false);
    }


}
