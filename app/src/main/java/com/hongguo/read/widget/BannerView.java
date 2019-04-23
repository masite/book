package com.hongguo.read.widget;

/**
 * Created by Administrator on 2017/6/13.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
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
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.hongguo.read.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by dongjunkun on 2015/8/9.
 */
public class BannerView extends RelativeLayout {


    private ViewPager            pager;
    //指示器容器
    private LinearLayout         indicatorContainer;
    private BannerChangeListener mBannerChangeListener;

    private ImageLoader mImageLoader;
    private Drawable    unSelectedDrawable;
    private Drawable    selectedDrawable;

    private int WHAT_AUTO_PLAY = 1000;

    private boolean isAutoPlay = true;

    private int selectedIndicatorColor   = 0xffff0000;
    private int unSelectedIndicatorColor = 0x88888888;

    private Shape indicatorShape            = Shape.oval;
    private int   selectedIndicatorHeight   = 6;
    private int   selectedIndicatorWidth    = 6;
    private int   unSelectedIndicatorHeight = 6;
    private int   unSelectedIndicatorWidth  = 6;

    private Position indicatorPosition = Position.centerBottom;
    private int      autoPlayDuration  = 4000;
    private int      scrollDuration    = 200;

    private int indicatorSpace  = 3;
    private int indicatorMargin = 10;

    private BannderAdapter   mBannderAdapter;
    private LoopPagerAdapter mPagerAdapter;

    private ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_CROP;


    private enum Shape {
        rect, oval
    }

    private enum Position {
        centerBottom,
        rightBottom,
        leftBottom,
        centerTop,
        rightTop,
        leftTop
    }

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

    private OnBannerItemClickListener onBannerItemClickListener;

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

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerLayoutStyle, defStyle, 0);
        selectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_selectedIndicatorColor, selectedIndicatorColor);
        unSelectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_unSelectedIndicatorColor, unSelectedIndicatorColor);

        int shape = array.getInt(R.styleable.BannerLayoutStyle_indicatorShape, Shape.oval.ordinal());
        for (Shape shape1 : Shape.values()) {
            if (shape1.ordinal() == shape) {
                indicatorShape = shape1;
                break;
            }
        }
        selectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorHeight, selectedIndicatorHeight);
        selectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorWidth, selectedIndicatorWidth);
        unSelectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorHeight, unSelectedIndicatorHeight);
        unSelectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorWidth, unSelectedIndicatorWidth);

        int position = array.getInt(R.styleable.BannerLayoutStyle_indicatorPosition, Position.centerBottom.ordinal());
        for (Position position1 : Position.values()) {
            if (position == position1.ordinal()) {
                indicatorPosition = position1;
            }
        }
        indicatorSpace = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorSpace, indicatorSpace);
        indicatorMargin = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorMargin, indicatorMargin);
        autoPlayDuration = array.getInt(R.styleable.BannerLayoutStyle_autoPlayDuration, autoPlayDuration);
        scrollDuration = array.getInt(R.styleable.BannerLayoutStyle_scrollDuration, scrollDuration);
        isAutoPlay = array.getBoolean(R.styleable.BannerLayoutStyle_isAutoPlay, isAutoPlay);
        array.recycle();

        //绘制未选中状态图形
        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();

        //绘制选中状态图形
        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();
        switch (indicatorShape) {
            case rect:
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case oval:
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                break;
        }
        unSelectedGradientDrawable.setColor(unSelectedIndicatorColor);
        unSelectedGradientDrawable.setSize(unSelectedIndicatorWidth, unSelectedIndicatorHeight);
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        unSelectedDrawable = unSelectedLayerDrawable;

        selectedGradientDrawable.setColor(selectedIndicatorColor);
        selectedGradientDrawable.setSize(selectedIndicatorWidth, selectedIndicatorHeight);
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        selectedDrawable = selectedLayerDrawable;
        initPager();
    }

    private void initPager() {
        addPager();
        indicatorContainer = new LinearLayout(getContext());
        indicatorContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (indicatorPosition) {
            case centerBottom:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case centerTop:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case leftBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case leftTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case rightBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case rightTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
        }
        //设置margin
        params.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
        addView(indicatorContainer, params);
    }

    private void addPager() {
        pager = new ViewPager(getContext());
        setSliderTransformDuration(scrollDuration);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mBannerChangeListener != null) {
                    mBannerChangeListener.bannerChange(position % mPagerAdapter.getRealCount());
                }
                switchIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(pager, 0);
    }


    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public void setLoadUrls(List<String> urls) {

        if (pager != null) {
            removeView(pager);
        }
        addPager();

        indicatorContainer.removeAllViews();
        //初始化指示器，并添加到指示器容器布局
        for (int i = 0; i < urls.size(); i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(indicatorSpace, indicatorSpace, indicatorSpace, indicatorSpace);
            indicator.setImageDrawable(unSelectedDrawable);
            indicatorContainer.addView(indicator);
        }
        mPagerAdapter = new LoopPagerAdapter(getContext(), urls);
        pager.setAdapter(mPagerAdapter);
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % urls.size();
        pager.setCurrentItem(targetItemPosition);
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    public void setIndicatorVisiable(boolean visiable) {
        indicatorContainer.setVisibility(visiable ? VISIBLE : GONE);
    }

    public void setBannderAdapter(BannderAdapter bannderAdapter) {
        mBannderAdapter = bannderAdapter;
        indicatorContainer.removeAllViews();
        //初始化指示器，并添加到指示器容器布局
        for (int i = 0; i < bannderAdapter.getCount(); i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(indicatorSpace, indicatorSpace, indicatorSpace, indicatorSpace);
            indicator.setImageDrawable(unSelectedDrawable);
            indicatorContainer.addView(indicator);
        }
        mPagerAdapter = new LoopPagerAdapter(getContext(), null);
        pager.setAdapter(mPagerAdapter);
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % bannderAdapter.getCount();
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

    /**
     * 切换指示器状态
     *
     * @param currentPosition 当前位置
     */
    private void switchIndicator(int currentPosition) {
        if (mPagerAdapter.getCount() == 0) {
            return;
        }
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            ((ImageView) indicatorContainer.getChildAt(i)).setImageDrawable(i == currentPosition % mPagerAdapter.getRealCount() ? selectedDrawable : unSelectedDrawable);
        }
    }


    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    public interface OnBannerItemClickListener {
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
            if (mUrls == null && mBannderAdapter == null) {
                return 0;
            }
            return Integer.MAX_VALUE;
        }

        public int getRealCount() {
            if (mUrls == null && mBannderAdapter == null) {
                return 0;
            }
            return mUrls == null ? mBannderAdapter.getCount() : mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (mBannderAdapter != null) {
                position = position % mBannderAdapter.getCount();
                view = mBannderAdapter.getView(position);
            } else {
                view = new ImageView(mContext);
                position = position % mUrls.size();
                if (mImageLoader != null) {
                    mImageLoader.displayImage((ImageView) view, mUrls.get(position));
                }

                final int finalPosition = position;
                ((ImageView) view).setScaleType(mScaleType);

                if (onBannerItemClickListener != null) {
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBannerItemClickListener.onItemClick(finalPosition);
                        }
                    });
                }

            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }

    public static abstract class BannderAdapter {

        protected Context mContext;

        public BannderAdapter(Context context) {
            mContext = context;
        }

        public abstract int getCount();

        public abstract View getView(int position);

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

    public void setBannerChangeListener(BannerChangeListener bannerChangeListener) {
        mBannerChangeListener = bannerChangeListener;
    }

    public interface BannerChangeListener {
        void bannerChange(int position);
    }
}
