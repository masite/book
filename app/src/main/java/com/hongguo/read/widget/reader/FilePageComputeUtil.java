package com.hongguo.read.widget.reader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;

import com.hongguo.read.R;
import com.hongguo.read.utils.AppUtils;
import com.losg.library.utils.DisplayUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/19.
 */

public class FilePageComputeUtil {

    //默认值
    private static final int DEFAULT_TEXT_SIZE        = 18;
    private static final int DEFAULT_LINE_HEIGHT      = 20;
    private static final int DEFAULT_PARAGRAPH_HEIGHT = 50;
    private static final int DEFAULT_MARGIN           = 16;
    private static final int DEFAULT_PAGE_HEADER_SIZE = 14;

    private int mPayTextSize;
    private int mWidth;
    private int mHeight;
    private int mDefaultMargin;
    private int mPageHeaderTextSize;
    private int mLineHeight;
    private int mParagraphHeight;
    private int mHeaderHeight;
    private int mTextSize;
    private int mTwoWidth;

    private int              mTextHeight;
    private Paint            mComputePaint;
    private Context          mContext;
    private int              mNoTitlePageContentHeight;
    private int              mContentWidth;
    private int              mTitleHeight;
    private int              mFirstPageContentHeight;
    private int              mTitleTextSize;
    private List<String>     mTitleLines;
    private String           mFilePath;
    private ChapterPagerInfo mChapterPagerInfo;
    private Paint            mLineBackground;

    private int mLineWidth = 2;

    private int mTextColor = 0xff666666;

    public FilePageComputeUtil(Context context) {
        mContext = context;
        mChapterPagerInfo = new ChapterPagerInfo();

        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mLineHeight = DisplayUtil.dip2px(mContext, DEFAULT_LINE_HEIGHT);
        mDefaultMargin = DisplayUtil.dip2px(mContext, DEFAULT_MARGIN);
        mTextSize = DisplayUtil.sp2px(mContext, DEFAULT_TEXT_SIZE);
        mParagraphHeight = DisplayUtil.sp2px(mContext, DEFAULT_PARAGRAPH_HEIGHT);
        mPageHeaderTextSize = DisplayUtil.sp2px(mContext, DEFAULT_PAGE_HEADER_SIZE);

        //5.0往上采用全屏显示
        if (Build.VERSION.SDK_INT > 19) {
            mHeight = context.getResources().getDisplayMetrics().heightPixels;
        } else {
            //4.4及以下采用去除标题栏
            mHeight = context.getResources().getDisplayMetrics().heightPixels - AppUtils.getStatusBarSize(context);
        }

        mPayTextSize = (int) (mWidth / 2f / 13);
        mTitleLines = new ArrayList<>();
        mComputePaint = new Paint();
        mComputePaint.setAntiAlias(true);

        mLineBackground = new Paint();
        mLineBackground.setColor(0x20000000);
        mLineBackground.setAntiAlias(true);
    }

    /**
     * 设置 文字大小
     *
     * @param textSize
     */
    public void initSize(int textSize) {
        mTextSize = textSize;
        mComputePaint.setTextSize(textSize);
        mTwoWidth = (int) mComputePaint.measureText("测试", 0, 2);
        mTextHeight = (int) (mComputePaint.getFontMetrics().descent - mComputePaint.getFontMetrics().ascent);
        mTitleTextSize = (int) (mTextSize * 1.2f);
        initItemHeight("");
    }

    /**
     * 初始化 高度 setTextSize 之后
     *
     * @param chapterName
     */
    public void initItemHeight(String chapterName) {

        //页眉 + 上下边距
        mHeaderHeight = mPageHeaderTextSize + 2 * mDefaultMargin;

        //整体高度 - 顶部页眉高度 - 底部页脚高度
        mNoTitlePageContentHeight = mHeight - mHeaderHeight * 2;

        //可用宽度
        mContentWidth = mWidth - 2 * mDefaultMargin;

        //初始化第一页的高度
        mTitleHeight = 0;
        mTitleHeight += mDefaultMargin * 2 + mTitleTextSize;

        //默认占一行
        mFirstPageContentHeight = mNoTitlePageContentHeight - mDefaultMargin * 2 - mTitleTextSize;

        mComputePaint.setTextSize(mTitleTextSize);

        if (TextUtils.isEmpty(chapterName)) {
            chapterName = "测试";
        }
        int chapterLength = mComputePaint.breakText(chapterName, true, mWidth - 2 * mDefaultMargin, new float[2]);
        mTitleLines.clear();
        mTitleLines.add(chapterName.substring(0, chapterLength));

        for (int i = chapterLength; i < chapterName.length(); ) {
            mFirstPageContentHeight -= mTitleTextSize + mDefaultMargin;
            mTitleHeight += mTitleTextSize + mDefaultMargin;
            chapterLength = mComputePaint.breakText(chapterName.substring(i, chapterName.length()), true, mWidth - 2 * mDefaultMargin, new float[2]);
            mTitleLines.add(chapterName.substring(i, i + chapterLength));
            i += chapterLength;
        }
        mTitleHeight += mLineWidth + 4 * mDefaultMargin;
        mFirstPageContentHeight -= mLineWidth + 4 * mDefaultMargin;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    /***************************画笔 获取 start***********************************/
    public Paint getTextPaint() {
        mComputePaint.setAlpha(255);
        mComputePaint.setTextSize(mTextSize);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getDescribeLastLinePaint() {
        mComputePaint.setAlpha(50);
        mComputePaint.setTextSize(mTextSize);
        int color = Color.argb(50, Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor));
        mComputePaint.setColor(color);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        return mComputePaint;
    }

    public Paint getLinePaint() {
        mComputePaint.setStrokeWidth(mLineWidth);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.STROKE);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getLoadingPaint() {
        mComputePaint.setAlpha(255);
        mComputePaint.setTextSize(mTextSize * 1.2f);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.CENTER);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getPayTipPaint() {
        mComputePaint.setTextAlign(Paint.Align.CENTER);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextSize(mPayTextSize);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setAlpha(100);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getBuyBorderPaint() {
        mComputePaint.setStrokeWidth(1);
        mComputePaint.setStyle(Paint.Style.STROKE);
        mComputePaint.setAlpha(50);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint GetBuyPaint() {
        mComputePaint.setColor(150);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setTextSize(mPayTextSize * 4 / 3);
        mComputePaint.setTextAlign(Paint.Align.CENTER);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getSuggestPaint() {
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setColor(Color.WHITE);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextSize(mPayTextSize * 2 / 3);
        mComputePaint.setTextAlign(Paint.Align.CENTER);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getSuggestBackgroundPaint() {
        mComputePaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getLineBackground() {
        return mLineBackground;
    }


    public Paint getHeaderPaint() {
        mComputePaint.setAlpha(150);
        mComputePaint.setTextSize(mPageHeaderTextSize);
        mComputePaint.setAntiAlias(true);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getTitlePaint() {
        mComputePaint.setTextSize(mTitleTextSize);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getFooterPaint() {
        mComputePaint.setAlpha(100);
        mComputePaint.setTextSize(mPageHeaderTextSize);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getTestBackPaint() {
        mComputePaint.setAlpha(10);
        mComputePaint.setColor(0xff00ffff);
        mComputePaint.setStyle(Paint.Style.FILL);
        mComputePaint.setTextAlign(Paint.Align.LEFT);
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    public Paint getBatteryPaint() {
        mComputePaint.setAlpha(100);
        mComputePaint.setTextSize(mPageHeaderTextSize);
        mComputePaint.setColor(mTextColor);
        mComputePaint.setStyle(Paint.Style.STROKE);
        mComputePaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));
        mComputePaint.setFlags(mComputePaint.getFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        return mComputePaint;
    }

    /***************************画笔 获取 end***********************************/
    public int getHeaderStart() {
        return (int) (mPageHeaderTextSize + mDefaultMargin - getHeaderPaint().getFontMetrics().descent);
    }

    public int getTitleStart() {
        return (int) (mDefaultMargin * 2 + mTitleTextSize - getTitlePaint().getFontMetrics().descent);
    }


    /***************************高度 偏移量获取 start ****************************/


    /***************************高度 偏移量获取 end ****************************/


    public ChapterPagerInfo.PagerInfo getPageInfo(int page) {
        if (page > mChapterPagerInfo.mPageLines.size() - 1) {
            page = mChapterPagerInfo.mPageLines.size() - 1;
        }
        return mChapterPagerInfo.mPageLines.get(page);
    }

    public ChapterPagerInfo getChapterPageInfo() {
        return mChapterPagerInfo;
    }


    public int getDefaultMargin() {
        return mDefaultMargin;
    }

    public int getLineHeight() {
        return mLineHeight;
    }

    public int getParagraphHeight() {
        return mParagraphHeight;
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public int getTextHeight() {
        return mTextHeight;
    }

    public int getNoTileContentHeight() {
        return mNoTitlePageContentHeight;
    }

    public int getTitleHeight() {
        return mTitleHeight;
    }

    public int getFirstPageContentHeight() {
        return mFirstPageContentHeight;
    }

    public int getContentWidth() {
        return mContentWidth;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getTwoWidth() {
        return mTwoWidth;
    }

    public int getTitleTextSize() {
        return mTitleTextSize;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getPageHeaderTextSize() {
        return mPageHeaderTextSize;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }


    public void compute() {
        Reader reader = null;
        try {
            reader = new FileReader(mFilePath);
            BufferedReader br = new BufferedReader(reader);
            loadPages(br);
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPages(BufferedReader br) throws Exception {
        mChapterPagerInfo = new ChapterPagerInfo();

        boolean pageStart = true;
        int currentHeight = 0;
        int currentPage = 0;
        String paragraph;
        //最后是否刚好是一页(不是一页，在循环后需要加上最后一页的数据)
        boolean justPage = false;

        ChapterPagerInfo.PagerInfo pagerInfo = new ChapterPagerInfo.PagerInfo();

        while ((paragraph = br.readLine()) != null) {
            paragraph = paragraph.replaceAll("\\s", "");
            paragraph = halfToFull(paragraph);
            boolean firstLine = true;

            while (paragraph.length() > 0) {

                int height = currentHeight + getTextHeight();
                if (firstLine & !pageStart) {
                    height += getParagraphHeight() - mLineHeight;
                }
                int maxHeight = currentPage == 0 ? mFirstPageContentHeight : getNoTileContentHeight();

                //不能容纳下一行
                if (height > maxHeight) {
                    pagerInfo.pageNumber = currentPage;
                    mChapterPagerInfo.mPageLines.add(pagerInfo);
                    pagerInfo = new ChapterPagerInfo.PagerInfo();
                    pageStart = true;
                    currentPage++;
                    currentHeight = 0;
                    justPage = true;
                    continue;
                }

                //断行
                int wordCount;
                //段落首行 缩进两字符
                if (firstLine) {
                    wordCount = getTextPaint().breakText(paragraph, true, mContentWidth - mTwoWidth, null);
                } else {
                    wordCount = getTextPaint().breakText(paragraph, true, mContentWidth, null);
                }

                //添加每行的信息
                ChapterPagerInfo.PagerInfo.LineInfo lineInfo = new ChapterPagerInfo.PagerInfo.LineInfo();
                lineInfo.isNewLine = firstLine;
                lineInfo.line = paragraph.substring(0, wordCount);
                pagerInfo.lines.add(lineInfo);
                lineInfo.currentY = currentHeight + getTextHeight();
                currentHeight += getTextHeight() + mLineHeight;

                //首行,添加段高
                if (firstLine & !pageStart) {
                    currentHeight += getParagraphHeight() - mLineHeight;
                }

                pageStart = false;
                firstLine = false;
                justPage = false;
                paragraph = paragraph.substring(wordCount, paragraph.length());
            }
        }

        if (!justPage) {
            pagerInfo.pageNumber = currentPage;
            mChapterPagerInfo.mPageLines.add(pagerInfo);
        }
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     *
     * @param input
     * @return
     */
    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public void setParagraphHeight(int lineHeight, int paragraphHeight) {
        mLineHeight = lineHeight;
        mParagraphHeight = paragraphHeight;
    }

}
