package com.hongguo.read.widget.recommenstyle.scrollbook;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.hongguo.read.widget.recommenstyle.scrollbook.scrollviewpager.ScrollImageView;
import com.hongguo.read.widget.recommenstyle.scrollbook.scrollviewpager.ScrollViewPager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by losg
 */

public class ScrollBookView extends LinearLayout {

    private ViewPager                               pager;
    private ScrollBookChangeListener mScrollBookChangeListener;

    private ImageLoader mImageLoader;

    private int WHAT_AUTO_PLAY = 1000;

    private boolean isAutoPlay = true;


    private int autoPlayDuration = 4000;
    private int scrollDuration   = 800;

    private LoopPagerAdapter mPagerAdapter;


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    private OnScrollBookViewClickListener onScrollBookViewClickListener;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                if (pager != null && isAutoPlay) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                }
            }
            return false;
        }
    });

    public ScrollBookView(Context context) {
        this(context, null);
    }

    public ScrollBookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
    }

    private void init(AttributeSet attrs, int defStyle) {
        initPager();
    }


    private void initPager() {
        addPager();
    }

    private void addPager() {
        pager = new ScrollViewPager(getContext());
        setSliderTransformDuration(scrollDuration);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mScrollBookChangeListener != null) {
                    mScrollBookChangeListener.itemChange(position % mPagerAdapter.getRealCount());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(pager, new LayoutParams(-1, -1));
    }


    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public void setLoadUrls(List<String> urls) {

        if (pager != null) {
            removeView(pager);
        }
        addPager();

        mPagerAdapter = new LoopPagerAdapter(getContext(), urls);
        pager.setAdapter(mPagerAdapter);
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % urls.size();
        pager.setCurrentItem(targetItemPosition);
        if (isAutoPlay) {
            startAutoPlay();
        }
    }


    public void setSliderTransformDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), null, duration);
            mScroller.set(pager, scroller);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 开始自动轮播
     */
    private void startAutoPlay() {
        if (isAutoPlay) {
            if (handler.hasMessages(WHAT_AUTO_PLAY)) {
                handler.removeMessages(WHAT_AUTO_PLAY);
            }
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (visibility == VISIBLE) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }


    /**
     * 停止自动轮播
     */
    private void stopAutoPlay() {
        if (pager != null) {
            pager.setCurrentItem(pager.getCurrentItem(), false);
        }
        if (isAutoPlay) {
            handler.removeMessages(WHAT_AUTO_PLAY);
            if (pager != null) {
                pager.setCurrentItem(pager.getCurrentItem(), false);
            }
        }
    }

    /**
     * @param autoPlay 是否自动轮播
     */
    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setOnScrollBookViewClickListener(OnScrollBookViewClickListener onScrollBookViewClickListener) {
        this.onScrollBookViewClickListener = onScrollBookViewClickListener;
    }

    public interface OnScrollBookViewClickListener {
        void onItemClick(int position);
    }

    private class LoopPagerAdapter extends PagerAdapter {

        private Context      mContext;
        private List<String> mUrls;

        LoopPagerAdapter(Context context, List<String> urls) {
            mContext = context;
            mUrls = urls;
        }

        @Override
        public int getCount() {
            if (mUrls == null) {
                return 0;
            }
            return Integer.MAX_VALUE;
        }

        public int getRealCount() {
            if (mUrls == null) {
                return 0;
            }
            return mUrls == null ? 0 : mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;

            view = new ScrollImageView(mContext);
            position = position % mUrls.size();
            if (mImageLoader != null) {
                mImageLoader.displayImage((ImageView) view, mUrls.get(position));
            }

            final int finalPosition = position;
            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (onScrollBookViewClickListener != null) {
                view.setOnClickListener(v -> onScrollBookViewClickListener.onItemClick(finalPosition));
            }

            container.addView(view,new ViewGroup.LayoutParams(30, -2));
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public interface ImageLoader {
        void displayImage(ImageView imageView, String path);
    }

    public void setScrollBookChangeListener(ScrollBookChangeListener ScrollBookChangeListener) {
        mScrollBookChangeListener = ScrollBookChangeListener;
    }

    public interface ScrollBookChangeListener {
        void itemChange(int position);
    }
}
