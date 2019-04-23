package com.hongguo.read.widget.reader;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hongguo.read.widget.reader.base.BaseViewControl;
import com.hongguo.read.widget.reader.curl.CurlAnimation;
import com.hongguo.read.widget.reader.down.DownAnimation;
import com.hongguo.read.widget.reader.none.NoneAnimation;
import com.hongguo.read.widget.reader.transx.TransXAnimation;
import com.hongguo.read.widget.reader.tts.TTSAnimation;
import com.hongguo.read.widget.reader.tts.TTSManager;
import com.hongguo.read.widget.reader.tts.TTSService;

import java.util.ArrayList;
import java.util.List;


/**
 * 流程
 * BaseBookAdapter(书籍信息以及状态) --->PageController(画图) ----> View 展示
 */
public class BookView extends View {

    //仿真
    public static final int ANIM_CURL  = 0;
    //覆盖
    public static final int ANIM_COVER = 1;
    //上下
    public static final int ANIM_DOWN  = 2;
    //无
    public static final int ANIM_NONE  = 3;

    //处理页面获取操作(主要将文字转换成图片操作)
    private PageController mPageProvider;

    //阅读事件监听
    private BookViewListener mBookViewClickListener;

    //当前阅读动画类型
    private int mBookAnimationType = 0;

    //各种动画管理
    private BaseViewControl mPageAnim;

    //当前页是否第一次显示了
    private boolean mHasShow = false;

    private String mSpeakerName = "";

    private int mSpeed = 50;

    private TTSManager mTTSManager;


    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        mTTSManager =new TTSManager(context);
    }

    private void initView() {
        mPageProvider = new PageController(getContext());

        //默认横向滑动
        mPageAnim = new CurlAnimation(this, mPageProvider);
    }

    public void setBaseBookAdatper(BaseBookAdapter baseBookAdatper) {
        mPageProvider.setBaseBookAdapter(baseBookAdatper);
        baseBookAdatper.setBookView(this);
    }

    /**
     * 章节信息由 总控制器去处理(可以控制选取的章节信息)
     *
     * @param chapterIndex
     */
    public void setChapterIndex(int chapterIndex) {
        mPageProvider.setChapterIndex(chapterIndex);
    }

    /**
     * 当前的显示页数有滑动控制去处理(在那边请求处理获取哪一页的数据)
     *
     * @param pageIndex
     */
    public void setCurrentPage(int pageIndex) {
        mPageProvider.setCurrentPage(pageIndex);
    }

    public void setTextSize(int size) {
        mPageProvider.setTextSize(size);
    }

    public void setParagraphHeight(int lineHeight, int paragraphHeight) {
        mPageProvider.setParagraphHeight(lineHeight, paragraphHeight);
    }

    public void setTextColor(int color) {
        mPageProvider.setTextColor(color);
    }

    public void setBackgroundColor(int color) {
        mPageProvider.setBackgroundColor(color);
    }

    public void setChapterName(String chapterName) {
        mPageProvider.setChapterName(chapterName);
    }

    public void setBookName(String bookName) {
        mPageProvider.setBookName(bookName);
    }

    public void setBattery(int percent) {
        mPageProvider.setBattery(percent);
    }

    public void setVipBuyInfo(String buttonDescribe, boolean show) {
        mPageProvider.setOpenVipTip(buttonDescribe);
        mPageProvider.setShowOpenVipButton(show);
    }

    public void setBackgroundResource(int resource) {
        mPageProvider.setBackgroundResource(resource);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPageAnim.show();
        mHasShow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPageAnim instanceof TTSAnimation) {
            ((TTSAnimation) mPageAnim).closeTTSRead();
        }
        mTTSManager.destory();
        mPageProvider.destroy();
        mHasShow = false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mPageAnim.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mPageAnim.onTouch(event);
    }

    public void setBookAnimationType(int animationType) {
        mBookAnimationType = animationType;
        switch (animationType) {
            case ANIM_CURL:
                mPageAnim = new CurlAnimation(this, mPageProvider);
                break;
            case ANIM_COVER:
                mPageAnim = new TransXAnimation(this, mPageProvider);
                break;
            case ANIM_DOWN:
                mPageAnim = new DownAnimation(this, mPageProvider);
                break;
            case ANIM_NONE:
                mPageAnim = new NoneAnimation(this, mPageProvider);
                break;
        }
        if (mBookViewClickListener != null) {
            mPageAnim.setBookViewClickListener(mBookViewClickListener);
        }
    }


    public void setBookViewClickListener(BookViewListener bookViewClickListener) {
        mBookViewClickListener = bookViewClickListener;
        mPageProvider.setBookViewListener(mBookViewClickListener);
        mPageAnim.setBookViewClickListener(bookViewClickListener);
    }

    /**
     * 只是改动部分内容，不影响页面的显示 (主要是字体颜色，背景图片，以及动画类型修改后)
     */
    public void notifyDataChange() {
        mPageAnim.notifyDataChange();
    }

    /**
     * 当页面大小改动时(主要是字的大小，行间距),导致计算的有出入，需要重新计算每页显示的位置以及大小(通知PageController 重新计算)
     */
    public void notifySizeChange() {
        // mPageProvider 必须先更新，page需要最新的控制信息
        mPageProvider.notifySizeChange();
        mPageAnim.notifyDataChange();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPageAnim.onDraw(canvas);
    }

    public int getChapterIndex() {
        return mPageProvider.getChapterIndex();
    }

    public void startRead(TTSAnimation.StartReadBookListener startReadBookListener) {
        mPageAnim = new TTSAnimation(this, mPageProvider,mTTSManager);
        if (mBookViewClickListener != null) {
            mPageAnim.setBookViewClickListener(mBookViewClickListener);
        }
        ((TTSAnimation) mPageAnim).startTTSRead(mSpeed, mSpeakerName, startReadBookListener);
    }

    public void stopRead() {
        if (isReadMode()) {
            ((TTSAnimation) mPageAnim).closeTTSRead();
        }
        setBookAnimationType(mBookAnimationType);
        notifyDataChange();
    }

    /**
     * 是否为阅读模式
     *
     * @return
     */
    public boolean isReadMode() {
        return mPageAnim instanceof TTSAnimation;
    }

    public List<TTSService.Speaker> getSpeakers() {
        if (isReadMode()) {
            return ((TTSAnimation) mPageAnim).findSpeaker();
        }
        return new ArrayList<>();
    }

    public void setSpeakerName(String speakerName) {
        mSpeakerName = speakerName;
        if (isReadMode()) {
            ((TTSAnimation) mPageAnim).setSpeakerName(speakerName);
        }
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
        if (isReadMode()) {
            ((TTSAnimation) mPageAnim).setSpeed(mSpeed);
        }
    }

    public void openChooseSpeaker() {
        ((TTSAnimation) mPageAnim).openChooseSpeaker();
    }

    public void animationMove(boolean pre) {
        mPageAnim.animationMove(pre);
    }

}

