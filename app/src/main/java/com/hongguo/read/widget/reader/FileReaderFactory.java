package com.hongguo.read.widget.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;

import com.hongguo.read.R;
import com.losg.library.utils.DisplayUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by losg on 2017/4/21.
 */

public class FileReaderFactory {

    private static final String PAY_INFO = "支持作者，支持正版";

    //图片，避免多次创建内存抖动
    private Bitmap mDrawBitmap;
    private Bitmap mRewardBitmap;

    private FilePageComputeUtil mFilePageComputeUtil;
    private String              mBookName;
    private String              mChapterName;
    private int                 mBatteryPercent;
    private Bitmap              mBackgroundBitmap;
    private String              mPagePercent;
    private int                 mCurrentPage;
    private SimpleDateFormat    mSimpleDateFormat;
    private String              mFilePath;

    private boolean mShowVipButton = true;

    private String  mVipDescribe;
    private int     mBackgroundColor;
    private Context mContext;

    public FileReaderFactory(Context context) {
        mContext = context;
        mFilePageComputeUtil = new FilePageComputeUtil(context);
        mSimpleDateFormat = new SimpleDateFormat("HH:mm");

        //打赏图片
        mRewardBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_book_reward);
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
        mFilePageComputeUtil.setFilePath(filePath);
    }

    /**
     * 画通用信息
     * 头部信息 + 底部信息 + 背景信息
     *
     * @param canvas    画图
     * @param firstPage 是否是首页
     * @param offset    偏移量(主要是考虑上下滚动的情况)
     * @param noHeader
     */

    public void drawCommonInfo(Canvas canvas, boolean firstPage, int offset, boolean noHeader) {
        drawCommonInfo(canvas, firstPage, offset, noHeader, true);
    }

    public void drawCommonInfo(Canvas canvas, boolean firstPage, int offset, boolean noHeader, boolean background) {
        if (background) {
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
            drawBackground(canvas);
        }
        if (!noHeader)
            drawHeader(canvas, firstPage);

        if (firstPage) {
            drawTitle(canvas, offset, noHeader);
        }

        if (!noHeader)
            drawFooter(canvas);
    }


    /**
     * 在画布上画某页
     *
     * @param canvas
     * @param page          -1 末页 0 首页
     * @param drawPagerInfo
     * @param offset        偏移量
     * @param noHeader
     * @param background
     */
    public synchronized ChapterPagerInfo.PagerInfo drawPage(Canvas canvas, int page, ChapterPagerInfo.PagerInfo drawPagerInfo, int offset, int lineBack, boolean noHeader, boolean background) {

        if (drawPagerInfo != null) {
            drawCommonInfo(canvas, drawPagerInfo.pageNumber == 0, offset, noHeader, background);
            drawContent(canvas, drawPagerInfo, drawPagerInfo.pageNumber == 0, offset, lineBack, noHeader);
            return drawPagerInfo;
        }

        if (page == -1) {
            return drawEndPage(canvas, offset, lineBack, noHeader);
        }

        //主要是针对在页面字的大小改变时，页面页数变动到时超出
        List<ChapterPagerInfo.PagerInfo> mPageLines = mFilePageComputeUtil.getChapterPageInfo().mPageLines;
        if(page >= mPageLines.size()){
            page = mPageLines.size() - 1;
        }

        ChapterPagerInfo.PagerInfo pageInfo = mFilePageComputeUtil.getPageInfo(page);

        mCurrentPage = pageInfo.pageNumber;
        pageInfo.totalSize = mPageLines.size();

        drawCommonInfo(canvas, page == 0, offset, noHeader, background);

        drawContent(canvas, pageInfo, page == 0, offset, lineBack, noHeader);
        return pageInfo;
    }


    /**
     * 画末页
     *
     * @param canvas
     * @param contentOffset
     * @param linkBackground
     */
    public synchronized ChapterPagerInfo.PagerInfo drawEndPage(Canvas canvas, int contentOffset, int linkBackground, boolean noHeader) {
        List<ChapterPagerInfo.PagerInfo> pageLines = mFilePageComputeUtil.getChapterPageInfo().mPageLines;
        ChapterPagerInfo.PagerInfo pagerInfo = pageLines.get(pageLines.size() - 1);
        mCurrentPage = pagerInfo.pageNumber;

        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

        //首页就是末页
        drawCommonInfo(canvas, pagerInfo.pageNumber == 0, contentOffset, noHeader);
        drawContent(canvas, pagerInfo, pagerInfo.pageNumber == 0, contentOffset, linkBackground, noHeader);
        return pagerInfo;
    }


    /**
     * 画背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        //画背景图片
        if (mBackgroundBitmap == null) {
            canvas.drawColor(mBackgroundColor);
        } else {
            Rect fromRect = new Rect(0, 0, mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight());
            RectF toRect = new RectF(0, 0, mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeight());
            canvas.drawBitmap(mBackgroundBitmap, fromRect, toRect, mFilePageComputeUtil.getTextPaint());
        }
    }

    /***
     * 画页眉 首页 页面文字用书名 其余用章节名称
     * @param canvas
     * @param isFirst  true 画书名称  false 画标题内容
     */
    public void drawHeader(Canvas canvas, boolean isFirst) {
        //画页眉
        int currentPosition = mFilePageComputeUtil.getHeaderStart();
        int maxLength = mFilePageComputeUtil.getWidth() / 2 / mFilePageComputeUtil.getPageHeaderTextSize();
        String headerText;
        if (isFirst) {
            headerText = mBookName.length() > maxLength ? mBookName.substring(0, maxLength) + "..." : mBookName;
        } else {
            headerText = mChapterName.length() > maxLength ? mChapterName.substring(0, maxLength) + "..." : mChapterName;
        }
        canvas.drawText(headerText, mFilePageComputeUtil.getDefaultMargin(), currentPosition, mFilePageComputeUtil.getHeaderPaint());


        Paint headerPaint = mFilePageComputeUtil.getHeaderPaint();
        float width = headerPaint.measureText("打赏");
        int headerHeight = mFilePageComputeUtil.getHeaderHeight();
        int y = (int) (headerHeight / 2 - (headerPaint.ascent() + headerPaint.descent()) / 2);
        int left = (int) (mFilePageComputeUtil.getWidth() - mFilePageComputeUtil.getDefaultMargin() - width);
        canvas.drawText("打赏", left, y, headerPaint);
        canvas.drawBitmap(mRewardBitmap, left - DisplayUtil.dip2px(mContext, 4) - mRewardBitmap.getWidth(), headerHeight / 2 - mRewardBitmap.getHeight() / 2, headerPaint);
    }

    public RectF getRewardPosition() {
        Paint headerPaint = mFilePageComputeUtil.getHeaderPaint();
        float width = headerPaint.measureText("打赏");
        int left = (int) (mFilePageComputeUtil.getWidth() - mFilePageComputeUtil.getDefaultMargin() - width) - DisplayUtil.dip2px(mContext, 4) - mRewardBitmap.getWidth();
        RectF rectF = new RectF();
        rectF.set(left, 0, mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight());
        return rectF;
    }

    /**
     * 画内容部分
     *
     * @param canvas
     * @param firstPage      是否为第一页(第一页可画内容的起点不同,因为有标题的存在)
     * @param contentOffset  画布的偏移量(上下滚动使用)
     * @param lineBackground 需要重点标注的行(语音阅读时采用)
     * @param noHeader
     */
    private void drawContent(Canvas canvas, ChapterPagerInfo.PagerInfo pagerInfo, boolean firstPage, int contentOffset, int lineBackground, boolean noHeader) {

        if (noHeader) {
            canvas.save();
            canvas.clipRect(0, mFilePageComputeUtil.getHeaderHeight(), mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
        }

        canvas.save();
        //页眉的高度
        int transHeight = mFilePageComputeUtil.getHeaderHeight();
        //标题的高度(首页加载标题的高度)
        if (firstPage) {
            transHeight += mFilePageComputeUtil.getTitleHeight();
        }
        //进行偏移
        canvas.translate(0, transHeight + contentOffset);


        int ascent = -(int) mFilePageComputeUtil.getTextPaint().getFontMetrics().ascent;

        int currentPosition = ascent;

        int paragraphNumber = 0;

        for (int i = 0; i < pagerInfo.lines.size(); i++) {
            String line = pagerInfo.lines.get(i).line;

            if(i != 0 && pagerInfo.lines.get(i).isNewLine){
                paragraphNumber ++;
            }

            if (i != 0)
                currentPosition += (mFilePageComputeUtil.getTextHeight() + mFilePageComputeUtil.getLineHeight());

            int offset = pagerInfo.lines.get(i).isNewLine ? mFilePageComputeUtil.getTwoWidth() : 0;
            //如果是新起的一行，加载段间距(加上差值)
            if (pagerInfo.lines.get(i).isNewLine && i != 0) {
                currentPosition += mFilePageComputeUtil.getParagraphHeight() - mFilePageComputeUtil.getLineHeight();
            }
            //在听书中的选中段落
            if(paragraphNumber == lineBackground){
                Paint lineBackgroundPaint = mFilePageComputeUtil.getLineBackground();
                float width = mFilePageComputeUtil.getTextPaint().measureText(line);
                int left = offset + mFilePageComputeUtil.getDefaultMargin();
                int top = (int) (currentPosition + mFilePageComputeUtil.getTextPaint().ascent());
                RectF rectF = new RectF(left, top, left + width, top + mFilePageComputeUtil.getTextHeight());
                canvas.drawRect(rectF, lineBackgroundPaint);
            }
            canvas.drawText(line, offset + mFilePageComputeUtil.getDefaultMargin(), currentPosition, mFilePageComputeUtil.getTextPaint());
        }
        canvas.restore();
        if (noHeader) {
            canvas.restore();
        }
    }

    /***
     * 画标题(首页章节名称)
     * @param canvas
     * @param noHeader
     */
    private void drawTitle(Canvas canvas, int offset, boolean noHeader) {

        if (noHeader) {
            canvas.save();
            canvas.clipRect(0, mFilePageComputeUtil.getHeaderHeight(), mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
        }

        canvas.save();
        canvas.translate(0, mFilePageComputeUtil.getHeaderHeight() + offset);

        int margin = mFilePageComputeUtil.getDefaultMargin();
        int currentPosition = mFilePageComputeUtil.getTitleStart();
        int contentWidth = mFilePageComputeUtil.getContentWidth();
        int titleSize = mFilePageComputeUtil.getTitleTextSize();
        int lineHeight = mFilePageComputeUtil.getLineHeight();
        Paint paint = mFilePageComputeUtil.getTitlePaint();

        int chapterLength = mFilePageComputeUtil.getTitlePaint().breakText(mChapterName, true, contentWidth, null);
        canvas.drawText(mChapterName, 0, chapterLength, margin, currentPosition, paint);
        for (int i = chapterLength; i < mChapterName.length(); ) {
            currentPosition += titleSize + margin;
            chapterLength = paint.breakText(mChapterName.substring(i, mChapterName.length()), true, contentWidth, null);
            canvas.drawText(mChapterName, i, i + chapterLength, margin, currentPosition, paint);
            i += chapterLength;
        }

        //画线
        currentPosition += margin + lineHeight;
        canvas.drawLine(margin, currentPosition, mFilePageComputeUtil.getWidth() - margin, currentPosition, mFilePageComputeUtil.getLinePaint());
        canvas.restore();

        if (noHeader) {
            canvas.restore();
        }
    }

    /**
     * 画电池 时间 页脚
     */
    public void drawFooter(Canvas canvas) {
        drawFooterText(canvas, mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
    }

    /**
     * 在某位置上画页脚
     *
     * @param canvas
     * @param position
     */
    public void drawFooterText(Canvas canvas, int position) {
        canvas.save();
        canvas.translate(0, position);

        int margin = mFilePageComputeUtil.getDefaultMargin();
        int headerTextSize = mFilePageComputeUtil.getPageHeaderTextSize();

        int batterWidth = (int) (headerTextSize * 1.3f);
        int batterHeight = (int) (headerTextSize * 0.8f);
        int batterPlusWidth = (int) (batterHeight * 0.4f);


        int batterLeft = margin;
        int batterRight = batterLeft + batterWidth;
        int batterTop = (mFilePageComputeUtil.getHeaderHeight() - batterHeight) / 2;
        int batterBottom = batterTop + batterHeight;

        //电池外框
        RectF rectF = new RectF(batterLeft, batterTop, batterRight, batterBottom);
        canvas.drawRoundRect(rectF, batterHeight / 6, batterHeight / 6, mFilePageComputeUtil.getBatteryPaint());

        int plusLeft = margin + batterWidth;
        int plusRight = plusLeft + batterPlusWidth;
        int plusTop = batterTop + (batterHeight - batterPlusWidth) / 2;
        int plusBottom = plusTop + batterPlusWidth;

        //电池的+号
        rectF = new RectF(plusLeft, plusTop, plusRight, plusBottom);
        canvas.drawRoundRect(rectF, 2, 2, mFilePageComputeUtil.getFooterPaint());

        //当前电量
        int batteryPadding = batterHeight / 8;
        float currentBattery = (batterWidth - batteryPadding * 2) * mBatteryPercent / 100f;
        rectF = new RectF(batterLeft + batteryPadding, batterTop + batteryPadding, batterLeft + currentBattery, batterBottom - batteryPadding);
        canvas.drawRoundRect(rectF, 0, 0, mFilePageComputeUtil.getFooterPaint());


        Paint headerPaint = mFilePageComputeUtil.getHeaderPaint();
        int textLine = (int) ((mFilePageComputeUtil.getHeaderHeight()) / 2 - headerPaint.ascent() / 2 - headerPaint.descent() / 2);

        //当前时间
        String currentTime = mSimpleDateFormat.format(Calendar.getInstance().getTime());
        canvas.drawText(currentTime, plusLeft + batterPlusWidth + margin, textLine, mFilePageComputeUtil.getFooterPaint());

        int totalPage = mFilePageComputeUtil.getChapterPageInfo().mPageLines.size() ;
        String number = (mCurrentPage + 1) + "/"+totalPage;
        float width = mFilePageComputeUtil.getFooterPaint().measureText(number, 0, number.length());
        canvas.drawText(number, mFilePageComputeUtil.getWidth() - margin - width, textLine, mFilePageComputeUtil.getFooterPaint());

        canvas.restore();
    }

    public synchronized void drawLoading(Canvas canvas, int offset, boolean noHeader) {

        drawCommonInfo(canvas, false, offset, noHeader);

        if (noHeader) {
            canvas.save();
            canvas.clipRect(0, mFilePageComputeUtil.getHeaderHeight(), mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
        }


        int width = mFilePageComputeUtil.getWidth();
        int lineHeight = mFilePageComputeUtil.getLineHeight();
        int textSize = mFilePageComputeUtil.getTextSize();
        int contentHeight = mFilePageComputeUtil.getNoTileContentHeight();
        canvas.save();
        canvas.translate(0, offset + mFilePageComputeUtil.getHeaderHeight());
        int measureSize = mFilePageComputeUtil.getLoadingPaint().breakText(mChapterName, true, width / 2, null);
        String drawText = "";
        if (measureSize >= mChapterName.length()) {
            drawText = mChapterName;
        } else {
            drawText = mChapterName.substring(0, measureSize) + "...";
        }

        Paint loadingPaint = mFilePageComputeUtil.getLoadingPaint();
        canvas.drawText(drawText, width / 2, contentHeight / 2 - textSize - lineHeight, loadingPaint);

        loadingPaint.setTextSize(textSize * 0.9f);
        canvas.drawText("下载中", width / 2, contentHeight / 2 + lineHeight, loadingPaint);
        mCurrentPage = 0;

        canvas.restore();

        if (noHeader) {
            canvas.restore();
        }
    }


    public synchronized void drawError(Canvas canvas, int offset, boolean noHeader) {

        drawCommonInfo(canvas, false, offset, noHeader);

        if (noHeader) {
            canvas.save();
            canvas.clipRect(0, mFilePageComputeUtil.getHeaderHeight(), mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
        }
        canvas.save();

        canvas.translate(0, offset + mFilePageComputeUtil.getHeaderHeight());

        int width = mFilePageComputeUtil.getWidth();
        int lineHeight = mFilePageComputeUtil.getLineHeight();
        int textSize = mFilePageComputeUtil.getTextSize();
        int contentHeight = mFilePageComputeUtil.getNoTileContentHeight();
        int twoWord = mFilePageComputeUtil.getTwoWidth();
        int margin = mFilePageComputeUtil.getDefaultMargin();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);

        int infoStart = contentHeight / 2 - textSize - lineHeight;
        String drawText = "网络异常，点击重新加载";
        Paint loadingPaint = mFilePageComputeUtil.getLoadingPaint();
        loadingPaint.setTextSize(textSize);
        canvas.drawText(drawText, width / 2, infoStart, loadingPaint);

        int measureSize = mFilePageComputeUtil.getLoadingPaint().breakText(mChapterName, true, width / 2, null);
        if (measureSize >= mChapterName.length()) {
            drawText = mChapterName;
        } else {
            drawText = mChapterName.substring(0, measureSize) + "...";
        }
        canvas.drawText(drawText, width / 2, infoStart - margin - mFilePageComputeUtil.getTextHeight(), loadingPaint);


        int buttonBottom = infoStart + margin * 3 + buttonHeight;
        RectF rect = new RectF(margin + twoWord, infoStart + margin * 3, width - margin - twoWord, buttonBottom);
        canvas.drawRoundRect(rect, 30, 30, mFilePageComputeUtil.getBuyBorderPaint());

        Paint paint = mFilePageComputeUtil.GetBuyPaint();
        int textStart = (int) (buttonBottom - buttonHeight / 2 - paint.descent() / 2 - paint.ascent() / 2);
        canvas.drawText("重新加载", width / 2, textStart, paint);

        canvas.restore();
        if (noHeader) {
            canvas.restore();
        }
        mCurrentPage = 0;
    }

    public RectF getErrorPosition(int offset) {
        int top = offset + mFilePageComputeUtil.getHeaderHeight();
        int width = mFilePageComputeUtil.getWidth();
        int lineHeight = mFilePageComputeUtil.getLineHeight();
        int textSize = mFilePageComputeUtil.getTextSize();
        int contentHeight = mFilePageComputeUtil.getNoTileContentHeight();
        int twoWord = mFilePageComputeUtil.getTwoWidth();
        int margin = mFilePageComputeUtil.getDefaultMargin();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);

        int infoStart = contentHeight / 2 - textSize - lineHeight;
        int buttonBottom = infoStart + margin * 3 + buttonHeight;
        RectF rect = new RectF(margin + twoWord, top + infoStart + margin * 3, width - margin - twoWord, top + buttonBottom);
        return rect;
    }

    /**
     * 画购买页面
     *
     * @param canvas
     * @param offset
     * @param noHeader
     */
    public synchronized void drawPaying(Canvas canvas, int offset, boolean noHeader) {
        drawCommonInfo(canvas, true, offset, noHeader);

        ChapterPagerInfo.PagerInfo pageInfo = mFilePageComputeUtil.getPageInfo(0);

        if (noHeader) {
            canvas.save();
            canvas.clipRect(0, mFilePageComputeUtil.getHeaderHeight(), mFilePageComputeUtil.getWidth(), mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getNoTileContentHeight());
        }

        canvas.save();
        canvas.translate(0, offset);
        //画数的简介
        drawPayDescribe(canvas, pageInfo);
        //画提醒(需要购买才能阅读)
        drawBuyNotice(canvas);
        //画购买章节按钮
        drawPayBtn(canvas);

        if (mShowVipButton) {
            drawVipBtn(canvas);
        }
        canvas.restore();

        if (noHeader) {
            canvas.restore();
        }
        mCurrentPage = 0;
    }

    /**
     * 画购买描述信息
     *
     * @param canvas
     * @param pagerInfo
     */
    private void drawPayDescribe(Canvas canvas, ChapterPagerInfo.PagerInfo pagerInfo) {

        canvas.save();
        canvas.translate(0, mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight());

        int ascent = -(int) mFilePageComputeUtil.getTextPaint().getFontMetrics().ascent;
        int currentPosition = ascent;

        for (int i = 0; i < pagerInfo.lines.size(); i++) {
            ChapterPagerInfo.PagerInfo.LineInfo lineInfo = pagerInfo.lines.get(i);
            String line = lineInfo.line;

            if (i != 0)
                currentPosition += (mFilePageComputeUtil.getTextHeight() + mFilePageComputeUtil.getLineHeight());
            if (lineInfo.isNewLine && i != 0) {
                currentPosition += mFilePageComputeUtil.getParagraphHeight() - mFilePageComputeUtil.getLineHeight();
            }

            if (currentPosition > mFilePageComputeUtil.getFirstPageContentHeight() / 3) {
                break;
            }

            int offset = lineInfo.isNewLine ? mFilePageComputeUtil.getTwoWidth() : 0;

            Paint paint = mFilePageComputeUtil.getTextPaint();
            if (i == pagerInfo.lines.size() - 1) {
                paint = mFilePageComputeUtil.getDescribeLastLinePaint();
            }

            canvas.drawText(line, offset + mFilePageComputeUtil.getDefaultMargin(), currentPosition, paint);
        }
        canvas.restore();
    }

    private void drawBuyNotice(Canvas canvas) {
        canvas.save();
        canvas.translate(0, mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight() + mFilePageComputeUtil.getFirstPageContentHeight() / 3);

        int width = mFilePageComputeUtil.getWidth();
        int margin = mFilePageComputeUtil.getDefaultMargin();
        canvas.drawText(PAY_INFO, 0, PAY_INFO.length(), width / 2, margin - mFilePageComputeUtil.getPayTipPaint().ascent(), mFilePageComputeUtil.getPayTipPaint());

        canvas.restore();
    }

    //画购买按钮
    private void drawPayBtn(Canvas canvas) {

        int width = mFilePageComputeUtil.getWidth();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);
        int buttonWidth = mFilePageComputeUtil.getContentWidth() - mFilePageComputeUtil.getTwoWidth() * 2;
        int margin = mFilePageComputeUtil.getDefaultMargin();

        canvas.save();
        canvas.translate(0, mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight() + mFilePageComputeUtil.getFirstPageContentHeight() / 3 + 2 * margin + mFilePageComputeUtil.getTextHeight());

        int left = margin + mFilePageComputeUtil.getTwoWidth();
        int top = 0;
        int right = width - left;
        int bottom = buttonHeight;

        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, 30, 30, mFilePageComputeUtil.getBuyBorderPaint());

        Paint paint = mFilePageComputeUtil.GetBuyPaint();
        canvas.drawText("购买章节", 0, 4, width / 2, top + buttonHeight / 2 - paint.getFontMetrics().ascent / 2 - paint.getFontMetrics().descent / 2, mFilePageComputeUtil.GetBuyPaint());

        canvas.restore();
    }

    //画购买vip
    private void drawVipBtn(Canvas canvas) {

        int width = mFilePageComputeUtil.getWidth();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);
        int buttonWidth = mFilePageComputeUtil.getContentWidth() - mFilePageComputeUtil.getTwoWidth() * 2;
        int margin = mFilePageComputeUtil.getDefaultMargin();

        canvas.save();
        canvas.translate(0, mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight()
                + mFilePageComputeUtil.getFirstPageContentHeight() / 3 + 3 * margin + mFilePageComputeUtil.getTextHeight() + buttonHeight);


        int left = margin + mFilePageComputeUtil.getTwoWidth();
        int top = 0;
        int right = width - left;
        int bottom = buttonHeight;

        Paint paint = mFilePageComputeUtil.GetBuyPaint();

        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, 30, 30, mFilePageComputeUtil.getBuyBorderPaint());
        canvas.drawText(mVipDescribe, width / 2, top + buttonHeight / 2 - paint.getFontMetrics().ascent / 2 - paint.getFontMetrics().descent / 2, mFilePageComputeUtil.GetBuyPaint());


        int suggestSize = (int) (buttonHeight * 0.8f);
        canvas.save();
        Path path = new Path();
        path.moveTo(right - suggestSize, top);
        path.lineTo(right, top);
        path.lineTo(right, suggestSize);
        canvas.clipPath(path);

        rectF = new RectF(right - buttonHeight, top, right, bottom);
        canvas.drawRoundRect(rectF, 30, 30, mFilePageComputeUtil.getTextPaint());
        canvas.restore();

        Paint suggestPaint = mFilePageComputeUtil.getSuggestPaint();

        int middleHeigh = (int) Math.sqrt(buttonHeight * buttonHeight / 2);

        path.reset();
        path.moveTo(right - suggestSize, top);
        path.lineTo(right, suggestSize);
        canvas.drawTextOnPath("推荐", path, 0, -middleHeigh / 2 + suggestPaint.getFontMetrics().descent * 3 / 2 - suggestPaint.getFontMetrics().ascent / 2, suggestPaint);

        canvas.restore();
    }


    public RectF findBuyBtn(int offset) {
        int width = mFilePageComputeUtil.getWidth();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);
        int buttonWidth = mFilePageComputeUtil.getContentWidth() - mFilePageComputeUtil.getTwoWidth() * 2;
        int margin = mFilePageComputeUtil.getDefaultMargin();

        offset += mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight() + mFilePageComputeUtil.getFirstPageContentHeight() / 3 + 2 * margin + mFilePageComputeUtil.getTextHeight();

        int left = margin + mFilePageComputeUtil.getTwoWidth();
        int top = offset;
        int right = width - left;
        int bottom = offset + buttonHeight;

        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    public RectF findVipBuyBtn(int offset) {
        int width = mFilePageComputeUtil.getWidth();
        int buttonHeight = DisplayUtil.dip2px(mContext, 40);
        int buttonWidth = mFilePageComputeUtil.getContentWidth() - mFilePageComputeUtil.getTwoWidth() * 2;
        int margin = mFilePageComputeUtil.getDefaultMargin();

        offset += mFilePageComputeUtil.getHeaderHeight() + mFilePageComputeUtil.getTitleHeight()
                + mFilePageComputeUtil.getFirstPageContentHeight() / 3 + 3 * margin + mFilePageComputeUtil.getTextHeight() + buttonHeight;

        int left = margin + mFilePageComputeUtil.getTwoWidth();
        int top = offset;
        int right = width - left;
        int bottom = offset + buttonHeight;

        return new RectF(left, top, right, bottom);
    }

    public synchronized Bitmap getDrawBitmap() {
        return mDrawBitmap;
    }

    /**
     * 当前页是否为本章节的最后一页
     *
     * @return
     */
    public boolean pageIsEnd(int currentPageIndex) {
        ChapterPagerInfo chapterPageInfo = mFilePageComputeUtil.getChapterPageInfo();
        if (chapterPageInfo.pageLoadingStatus != BaseChapter.PageLoad.SUCCESS) {
            return true;
        }
        List<ChapterPagerInfo.PagerInfo> pageLines = chapterPageInfo.mPageLines;
        return pageLines.get(pageLines.size() - 1).pageNumber <= currentPageIndex;
    }

    /**
     * 当前页是否为末页
     *
     * @return
     */
    public boolean currentIsEndPage() {
        return pageIsEnd(mCurrentPage);
    }

    public void resetPageInfo() {
        mCurrentPage = 0;
        mFilePath = "";
    }

    public void setTextColor(int textColor) {

        mFilePageComputeUtil.setTextColor(textColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }


    public synchronized void setTextSize(int textSize) {
        mFilePageComputeUtil.initSize(textSize);
    }

    public void setPagePercent(String mPagePercent) {
        this.mPagePercent = mPagePercent;
    }

    public void setVipVisiable(boolean vipVisiable) {
        mShowVipButton = vipVisiable;
    }

    public void setBackgroundBitmap(Bitmap mBackgroundBit) {
        this.mBackgroundBitmap = mBackgroundBit;
    }

    public void setBookName(String bookName) {
        mBookName = bookName;
    }

    public synchronized void setChapterName(String chapterName) {
        mChapterName = chapterName;
        mFilePageComputeUtil.initItemHeight(chapterName);
    }

    public void setBatteryPercent(int mBatteryPercent) {
        this.mBatteryPercent = mBatteryPercent;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void setDrawBitmap(Bitmap drawBitmap) {
        mDrawBitmap = drawBitmap;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public ChapterPagerInfo.PagerInfo getCurrentPageInfo() {
        if(mCurrentPage >= mFilePageComputeUtil.getChapterPageInfo().mPageLines.size()){
            mCurrentPage = mFilePageComputeUtil.getChapterPageInfo().mPageLines.size() - 1;
        }
        return mFilePageComputeUtil.getPageInfo(mCurrentPage);
    }

    public BaseChapter.PageLoad getCurrentPageLoadingInfo() {
        return mFilePageComputeUtil.getChapterPageInfo().pageLoadingStatus;
    }

    public ChapterPagerInfo getChapterPageInfo() {
        return mFilePageComputeUtil.getChapterPageInfo();
    }


    public int getViewWidth() {
        return mFilePageComputeUtil.getWidth();
    }

    public int getViewHeight() {
        return mFilePageComputeUtil.getHeight();
    }

    public int getContentHeight() {
        return mFilePageComputeUtil.getNoTileContentHeight();
    }


    /**
     * 文字大小变化 行间距变化(调用),加载状态改变时
     */
    public void notifyContentChange() {

        mFilePageComputeUtil.compute();
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setVipDescribe(String vipDescribe) {
        mVipDescribe = vipDescribe;
    }

    public void compute() {
        mFilePageComputeUtil.compute();
    }

    public void setParagraphHeight(int lineHeight, int paragraphHeight){
        mFilePageComputeUtil.setParagraphHeight(lineHeight, paragraphHeight);
    }
}

