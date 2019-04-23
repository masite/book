package com.hongguo.read.widget.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by losg on 2018/1/18.
 */
public class PageController {

    //前一章节
    public static final int PAGE_FORWARD = -1;
    //当前章节
    public static final int PAGE_CURRENT = 0;
    //下一章节
    public static final int PAGE_NEXT    = 1;

    //电量信息
    private int mBattery = 50;

    //是否显示开通VIP的按钮
    private boolean mShowOpenVipButton = true;

    //开通VIP提示信息
    private String mOpenVipTip = "开通会员享折扣";

    //文字转图片
    private FileReaderFactory mFileReaderFactory;

    private int                mChapterIndex;
    private Context            mContext;
    private BookChangeListener mPageChangeListener;
    private BaseBookAdapter    mBaseBookAdapter;
    private String             mBookName;
    private String             mChapterName;
    private int mLastPageNumber = -1;

    //阅读页常规设置
    private int              mTextSize;
    private int              mTextColor;
    private BookViewListener mBookViewListener;
    private DecimalFormat    mDecimalFormat;

    private Bitmap mBackgroundBitmap;
    private int    mBackgroundResource;
    private int    mBackgroundColor;

    //需要画的bitmap(动态页面)
    private Bitmap mDrawBitmap;
    //缓存bitmap用来画静态的页面
    private Bitmap mCacheBitmap;

    public PageController(Context context) {
        mContext = context;
        mChapterIndex = 0;
        mFileReaderFactory = new FileReaderFactory(mContext);
        //创建缓存bitmap,在一个bitmap上画图
        mDrawBitmap = Bitmap.createBitmap(mFileReaderFactory.getViewWidth(), mFileReaderFactory.getViewHeight(), Bitmap.Config.RGB_565);
        mCacheBitmap = Bitmap.createBitmap(mFileReaderFactory.getViewWidth(), mFileReaderFactory.getViewHeight(), Bitmap.Config.RGB_565);
        mDecimalFormat = new DecimalFormat("0.0");
    }

    /**
     * 传入需要操作的bitmap(需要画的页面)
     * 在  mCacheBitmap  mDrawBitmap中选取,避免多次创建
     *
     * @param bitmap
     */
    public void setDrawBitmap(Bitmap bitmap) {
        mFileReaderFactory.setDrawBitmap(bitmap);
    }

    /**
     * 当前章节是否是首章节
     *
     * @return
     */
    public boolean currentChapterIsFirst() {
        return mChapterIndex == 0;
    }

    /**
     * 当前章节是否是末章节
     *
     * @return
     */
    public boolean currentChapterIsEnd() {
        return mChapterIndex >= mBaseBookAdapter.getChapterCount() - 1;
    }

    /**
     * 当前页是否是首页
     *
     * @return
     */
    public boolean currentPageIsStart() {
        return mFileReaderFactory.getChapterPageInfo().mPageLines.size() == 0 || mFileReaderFactory.getCurrentPageInfo().pageNumber == 0;
    }

    /**
     * 当前页是否是末页
     *
     * @return
     */
    public boolean currentPageIsEnd() {
        return mFileReaderFactory.currentIsEndPage();
    }

    /**
     * 当前页是否为支付页
     *
     * @return
     */
    public boolean currentPageIsPay() {
        return mFileReaderFactory.getCurrentPageLoadingInfo() == BaseChapter.PageLoad.NEND_PAY;
    }

    /**
     * 当前页是否为支付页
     *
     * @return
     */
    public boolean currentPageIsError() {
        return mFileReaderFactory.getCurrentPageLoadingInfo() == BaseChapter.PageLoad.NET_ERROR;
    }

    /**
     * 当前页是否为加载页
     *
     * @return
     */
    public boolean currentPageIsLoading() {
        return mFileReaderFactory.getCurrentPageLoadingInfo() == BaseChapter.PageLoad.LOADING;
    }


    public void drawHeader(Canvas canvas, boolean firstPage) {
        mFileReaderFactory.drawHeader(canvas, firstPage);
        mFileReaderFactory.drawFooter(canvas);
    }


    public DrawPageInfo drawPage(Canvas canvas, int page, int contentOffset, ChapterPagerInfo.PagerInfo drawPagerInfo, boolean noHeader) {
        return drawPage(canvas, page, contentOffset, drawPagerInfo, noHeader, true);
    }


    public DrawPageInfo drawPage(Canvas canvas, int page, int contentOffset, ChapterPagerInfo.PagerInfo drawPagerInfo, boolean noHeader, boolean background) {
        return drawPage(canvas, page, contentOffset, drawPagerInfo, -1, noHeader, background);
    }

    /**
     * 画某一页
     *
     * @param canvas
     * @param page          当前页
     * @param contentOffset 页面偏移量 主要应用在上下滚动
     * @return
     */
    public DrawPageInfo drawPage(Canvas canvas, int page, int contentOffset, ChapterPagerInfo.PagerInfo drawPagerInfo, int lineBack, boolean noHeader, boolean background) {
        DrawPageInfo drawPageInfo = new DrawPageInfo();
        ChapterPagerInfo.PagerInfo pagerInfo = null;
        int totalPageSize = 0;

        mFileReaderFactory.setVipVisiable(mShowOpenVipButton);
        mFileReaderFactory.setTextSize(mTextSize);
        mFileReaderFactory.setTextColor(mTextColor);
        mFileReaderFactory.setVipDescribe(mOpenVipTip);
        mFileReaderFactory.setBackgroundBitmap(mBackgroundBitmap);
        mFileReaderFactory.setBatteryPercent(mBattery);
        mFileReaderFactory.setBackgroundColor(mBackgroundColor);
        mFileReaderFactory.setBookName(mBookName);
        int chapterCount = mBaseBookAdapter.getChapterCount();
        if (chapterCount == 0) {
            mFileReaderFactory.setPagePercent(0 + "");
        } else {
            float percent = 0;
            if (chapterCount == mChapterIndex) {
                percent = 100;
            } else {
                percent = (float) (mChapterIndex + 1) / chapterCount * 100;
            }
            mFileReaderFactory.setPagePercent(mDecimalFormat.format(percent) + "");
        }

        if (canvas == null) {
            canvas = new Canvas(mFileReaderFactory.getDrawBitmap());
        }

        BaseChapter baseChapter = mBaseBookAdapter.bindChapter(mChapterIndex);
        if (TextUtils.isEmpty(mFileReaderFactory.getFilePath())) {
            mFileReaderFactory.setFilePath(baseChapter.filePath);
            mFileReaderFactory.compute();
        }
        mFileReaderFactory.setChapterName(baseChapter.chapterName);

        if (baseChapter.pageLoad == BaseChapter.PageLoad.LOADING) {
            pagerInfo = new ChapterPagerInfo.PagerInfo();
            pagerInfo.pageNumber = 0;
            pagerInfo.totalSize = 1;
            totalPageSize = 1;
            mFileReaderFactory.drawLoading(canvas, contentOffset, noHeader);
            mFileReaderFactory.getChapterPageInfo().pageLoadingStatus = BaseChapter.PageLoad.LOADING;
        } else if (baseChapter.pageLoad == BaseChapter.PageLoad.NEND_PAY) {
            pagerInfo = new ChapterPagerInfo.PagerInfo();
            pagerInfo.pageNumber = 0;
            pagerInfo.totalSize = 1;
            totalPageSize = 1;
            mFileReaderFactory.drawPaying(canvas, contentOffset, noHeader);
            mFileReaderFactory.getChapterPageInfo().pageLoadingStatus = BaseChapter.PageLoad.NEND_PAY;
        } else if (baseChapter.pageLoad == BaseChapter.PageLoad.NET_ERROR) {
            totalPageSize = 1;
            pagerInfo = new ChapterPagerInfo.PagerInfo();
            pagerInfo.pageNumber = 0;
            pagerInfo.totalSize = 1;
            mFileReaderFactory.drawError(canvas, contentOffset, noHeader);
            mFileReaderFactory.getChapterPageInfo().pageLoadingStatus = BaseChapter.PageLoad.NET_ERROR;
        } else {
            mFileReaderFactory.getChapterPageInfo().pageLoadingStatus = BaseChapter.PageLoad.SUCCESS;
            pagerInfo = mFileReaderFactory.drawPage(canvas, page, drawPagerInfo, contentOffset, lineBack, noHeader, background);
            totalPageSize = pagerInfo.totalSize;
            if (mBookViewListener != null && mLastPageNumber != mFileReaderFactory.getCurrentPage()) {
                mBookViewListener.pageChanged(mFileReaderFactory.getCurrentPage());
                mLastPageNumber = mFileReaderFactory.getCurrentPage();
            }
        }
        drawPageInfo.pageLoad = baseChapter.pageLoad;
        drawPageInfo.drawBitmap = mFileReaderFactory.getDrawBitmap();
        drawPageInfo.pagerInfo = pagerInfo;
        drawPageInfo.mTotalSize = totalPageSize;
        return drawPageInfo;
    }

    /**
     * 只要状态是成功才会调用
     *
     * @param page
     * @return
     */
    public ChapterPagerInfo.PagerInfo getPageInfo(int page) {
        List<ChapterPagerInfo.PagerInfo> pageLines = mFileReaderFactory.getChapterPageInfo().mPageLines;
        if (page == -1) {
            return mFileReaderFactory.getChapterPageInfo().mPageLines.get(pageLines.size() - 1);
        }
        if(page > mFileReaderFactory.getChapterPageInfo().mPageLines.size() - 1){
            page = mFileReaderFactory.getChapterPageInfo().mPageLines.size() - 1;
        }
        return mFileReaderFactory.getChapterPageInfo().mPageLines.get(page);
    }

    public void justLoadPre() {
        chapterChange(-1);
        BaseChapter baseChapter = mBaseBookAdapter.bindChapter(mChapterIndex);
        if (TextUtils.isEmpty(mFileReaderFactory.getFilePath())) {
            mFileReaderFactory.setFilePath(baseChapter.filePath);
            mFileReaderFactory.compute();
        }
    }

    public void justLoadNext() {
        chapterChange(1);
        BaseChapter baseChapter = mBaseBookAdapter.bindChapter(mChapterIndex);
        if (TextUtils.isEmpty(mFileReaderFactory.getFilePath())) {
            mFileReaderFactory.setFilePath(baseChapter.filePath);
            mFileReaderFactory.compute();
        }
    }


    public void chapterChange(int status) {
        mChapterIndex += status;
        if (mChapterIndex <= 0) mChapterIndex = 0;
        if (mChapterIndex >= mBaseBookAdapter.getChapterCount())
            mChapterIndex = mBaseBookAdapter.getChapterCount() - 1;
        mFileReaderFactory.resetPageInfo();
        if (mBookViewListener != null) {
            mBookViewListener.chapterChanged(mChapterIndex);
        }
    }

    public int getContentHeight() {
        return mFileReaderFactory.getContentHeight();
    }

    public RectF payPageBtnRect() {
        return mFileReaderFactory.findBuyBtn(0);
    }

    public RectF payVipBtnRect() {
        return mFileReaderFactory.findVipBuyBtn(0);
    }

    public RectF netErrorRect() {
        return mFileReaderFactory.getErrorPosition(0);
    }

    public RectF rewawrdRect() {
        return mFileReaderFactory.getRewardPosition();
    }

    public void setBookViewListener(BookViewListener bookViewListener) {
        mBookViewListener = bookViewListener;
    }

    public void setCurrentPage(int currentPage) {
        mFileReaderFactory.setCurrentPage(currentPage);
    }

    public int getChapterIndex() {
        return mChapterIndex;
    }

    public interface BookChangeListener {
        void pageChange(int currentPage);
    }

    public Rect getViewRect() {
        return new Rect(0, 0, mFileReaderFactory.getViewWidth(), mFileReaderFactory.getViewHeight());
    }

    public void setBookName(String bookName) {
        mBookName = bookName;
    }

    public void setBattery(int battery) {
        mBattery = battery;
    }

    public void setChapterName(String chapterName) {
        mChapterName = chapterName;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setBackgroundResource(int backgroundResource) {
        if (backgroundResource == mBackgroundResource) return;
        mBackgroundResource = backgroundResource;
        mBackgroundBitmap = BitmapFactory.decodeResource(mContext.getResources(), backgroundResource);
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
        }
        mBackgroundResource = 0;
        mBackgroundBitmap = null;
    }

    public void setShowOpenVipButton(boolean showOpenVipButton) {
        mShowOpenVipButton = showOpenVipButton;
    }

    public void setOpenVipTip(String openVipTip) {
        mOpenVipTip = openVipTip;
    }

    public void setPageChangeListener(BookChangeListener pageChangeListener) {
        mPageChangeListener = pageChangeListener;
    }

    public void setBaseBookAdapter(BaseBookAdapter baseBookAdapter) {
        mBaseBookAdapter = baseBookAdapter;
    }

    public void notifySizeChange() {
        mFileReaderFactory.setFilePath("");
    }

    /**
     * 设置当前显示的章节
     *
     * @param index
     */
    public void setChapterIndex(int index) {
        mChapterIndex = index;
        mFileReaderFactory.resetPageInfo();
        if (mBookViewListener != null) {
            mBookViewListener.chapterChanged(mChapterIndex);
        }
    }

    public void setParagraphHeight(int lineHeight, int paragraphHeight) {
        mFileReaderFactory.setParagraphHeight(lineHeight, paragraphHeight);
    }

    public int getCurrentPage() {
        return mFileReaderFactory.getCurrentPage();
    }

    public Bitmap getDrawBitmap() {
        return mDrawBitmap;
    }

    public Bitmap getCacheBitmap() {
        return mCacheBitmap;
    }


    public void destroy() {
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
        }
        if (mDrawBitmap != null) {
            mDrawBitmap.recycle();
        }
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
        }
    }


    public static class DrawPageInfo {
        public Bitmap                     drawBitmap;
        public ChapterPagerInfo.PagerInfo pagerInfo;
        public int                        mTotalSize;
        public BaseChapter.PageLoad       pageLoad;
    }

}
