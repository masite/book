package com.hongguo.read.widget.reader.tts;

import android.graphics.Canvas;
import android.view.View;

import com.hongguo.read.widget.reader.ChapterPagerInfo;
import com.hongguo.read.widget.reader.PageController;
import com.hongguo.read.widget.reader.base.BaseViewControl;
import com.iflytek.cloud.SpeechError;

import java.util.ArrayList;
import java.util.List;

public class TTSAnimation extends BaseViewControl {

    private ChapterPagerInfo.PagerInfo mPagerInfo;
    private int                        mCurrentLine;

    private TTSManager            mTTSManager;
    private StartReadBookListener mReadBookListener;
    private String                mSpeakerName;
    private int                   mSpeed;

    private boolean mRefreshRead  = false;
    private boolean mStartTTSRead = false;

    public TTSAnimation(View view, PageController pageProvider, TTSManager ttsManager) {
        super(view, pageProvider);
        mTTSManager = ttsManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int currentPage = mPageController.getCurrentPage();
        if (mPagerInfo == null) {
            mPagerInfo = mPageController.drawPage(canvas, currentPage, 0, null, mCurrentLine, false, true).pagerInfo;
        } else {
            mPageController.drawPage(canvas, currentPage, 0, null, mCurrentLine, false, true);
        }
        if (mRefreshRead) {
            startRead(mSpeakerName, mSpeed);
        }
    }


    public boolean startTTSRead(int speech, String speakerName, StartReadBookListener readBookListener) {

        mSpeakerName = speakerName;
        mSpeed = speech;

        if (mCurrentLine < 0) {
            mCurrentLine = 0;
        }

        mReadBookListener = readBookListener;

        //检查是否安装过讯飞
        if (!mTTSManager.ttsInstall() && !mStartTTSRead) {
            readBookListener.needInstall(mTTSManager.getDownUrl());
            return false;
        }
        mStartTTSRead = true;

        if (!mTTSManager.isBindSuccess()) return false;

        //安装过，可能没有绑定成功或服务没有开起来
        if (mTTSManager.findSpeekers() == null || mTTSManager.findSpeekers().size() == 0) {
            openChooseSpeaker();
            if(mReadBookListener != null){
                mReadBookListener.openChooseSpeaker();
            }
            return false;
        }
        mRefreshRead = true;
        readBookListener.startSuccess();
        mBookView.invalidate();
        return true;
    }

    private void startRead(String speakerName, int speech) {
        List<String> lineInfo = new ArrayList<>();

        if (mPageController.currentPageIsError()) {
            lineInfo.add("数据加载异常");
        } else if (mPageController.currentPageIsPay()) {
            lineInfo.add("该章节需要收费");
        } else if (mPageController.currentPageIsLoading()) {
            lineInfo.add("正在加载中，请稍等");
        } else {
            //加载成功
            lineInfo = findLineInfo(mPagerInfo);
        }
        readBook(lineInfo, speakerName, speech);
    }

    private List<String> findLineInfo(ChapterPagerInfo.PagerInfo pagerInfo) {
        List<String> lineInfo = new ArrayList<>();
        String line = "";
        for (int i = 0; i < pagerInfo.lines.size(); i++) {
            ChapterPagerInfo.PagerInfo.LineInfo lineInfo1 = pagerInfo.lines.get(i);
            if (i != 0 && lineInfo1.isNewLine) {
                lineInfo.add(line);
                line = "";
            }
            line += lineInfo1.line;
            if (i == pagerInfo.lines.size() - 1) {
                lineInfo.add(line);
            }
        }
        return lineInfo;
    }

    private void readBook(List<String> lineInfo, String speakerName, int speech) {
        mTTSManager.setSpeekerName(speakerName);
        mTTSManager.setSpeed(speech);
        mTTSManager.startRead(lineInfo.get(mCurrentLine), new TTSService.TTSListener() {
            @Override
            public void onCompleted(SpeechError speechError) {
                mCurrentLine++;
                //本页已经阅读完成，转为下一页
                if (mCurrentLine >= lineInfo.size()) {
                    if (mPageController.currentPageIsLoading() || mPageController.currentPageIsPay()
                            || mPageController.currentPageIsError()) {
                        mRefreshRead = false;
                        return;
                    }
                    loadNext();
                } else {
                    mRefreshRead = false;
                    //阅读下一段
                    readBook(lineInfo, speakerName, speech);
                    mBookView.invalidate();
                }
            }
        });
    }

    public void closeTTSRead() {
        mTTSManager.close();
    }


    public void openChooseSpeaker() {
        mTTSManager.chooseSpeaker();
    }

    public List<TTSService.Speaker> findSpeaker() {
        return mTTSManager.findSpeekers();
    }

    /**
     * 加载上一章节
     */
    public boolean loadPre() {
        if (mPageController.currentPageIsStart()) {
            if (mPageController.currentChapterIsFirst()) {
                return false;
            }
            mPageController.chapterChange(PageController.PAGE_FORWARD);
            mCurrentLine = 0;
            mPageController.justLoadPre();
            mPageController.setCurrentPage(-1);
            mPagerInfo = null;
            mRefreshRead = true;
            mBookView.invalidate();
            return false;
        }
        mCurrentLine = 0;
        mPagerInfo = mPageController.getPageInfo(mPageController.getCurrentPage() - 1);
        mPageController.setCurrentPage(mPagerInfo.pageNumber);
        mRefreshRead = true;
        mBookView.invalidate();
        return true;
    }

    /**
     * 加载下一章节
     */
    public boolean loadNext() {
        //切换下一章节
        if (mPageController.currentPageIsEnd()) {
            if (mPageController.currentChapterIsEnd()) {
                return false;
            }
            mPageController.chapterChange(PageController.PAGE_NEXT);
            mCurrentLine = 0;
            mPageController.justLoadNext();
            mPageController.setCurrentPage(0);
            mPagerInfo = null;
            mRefreshRead = true;
            mBookView.invalidate();
            return true;
        }
        mCurrentLine = 0;
        mPagerInfo = mPageController.getPageInfo(mPageController.getCurrentPage() + 1);
        mPageController.setCurrentPage(mPagerInfo.pageNumber);
        mRefreshRead = true;
        mBookView.invalidate();
        return true;
    }

    @Override
    public void notifyDataChange() {
        mPagerInfo = null;
        mRefreshRead = true;
        mCurrentLine = 0;
        if (mTTSManager != null) {
            mTTSManager.pauseSpeek();
        }
        super.notifyDataChange();
    }

    public interface StartReadBookListener {
        void startSuccess();

        void needInstall(String installUrl);

        void openChooseSpeaker();
    }

    public void setSpeakerName(String speakerName) {
        mSpeakerName = speakerName;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }
}
