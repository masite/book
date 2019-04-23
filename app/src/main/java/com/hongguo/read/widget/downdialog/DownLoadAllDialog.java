package com.hongguo.read.widget.downdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.losg.library.utils.stylestring.StyleString;
import com.losg.library.utils.stylestring.StyleStringBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/12.
 */

public class DownLoadAllDialog extends Dialog {

    @BindView(R.id.title)
    TextView    mTitle;
    @BindView(R.id.all_free_chapter)
    RadioButton mAllFreeChapter;
    @BindView(R.id.all_chapter)
    RadioButton mAllChapter;
    @BindView(R.id.pay_chapter)
    RadioButton mPayChapter;
    @BindView(R.id.left_coin)
    TextView    mLeftCoin;
    @BindView(R.id.down_now)
    TextView    mDownNow;
    @BindView(R.id.need_coin)
    TextView    mNeedCoin;
    @BindView(R.id.refresh_coin)
    TextView    mRefreshCoin;
    @BindView(R.id.coin_tip)
    TextView    mCoinTip;
    @BindView(R.id.orgin_coin)
    TextView    mOrginCoin;

    private List<ChapterBean.Chapters>        mPayChapters;
    private List<ChapterBean.Chapters>        mFreeChapters;
    private ChapterRepertory.ChapterTotalInfo mFreeChapterInfo;
    private ChapterRepertory.ChapterTotalInfo mPayChapterInfo;
    private boolean                           mTimeFree;

    private int mTotalCoin = -1;
    private int mGiveCoin;
    private boolean mGiveCanUse = false;

    private       DownLoadAllDialogChooseListener mDownLoadAllDialogChooseListener;
    private final DownLoadAllDialogPresenter      mDownLoadAllDialogPresenter;
    private final Unbinder                        mBind;

    public DownLoadAllDialog(@NonNull Context context) {
        super(context, R.style.FullWidthDialog);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        setContentView(R.layout.dialog_down_all_chapter);
        mDownLoadAllDialogPresenter = new DownLoadAllDialogPresenter(this);
        mBind = ButterKnife.bind(this);
    }

    @OnClick(R.id.all_free_chapter)
    public void onAllFreeChapterClicked() {
        if (mFreeChapters == null || mFreeChapterInfo == null || mTotalCoin == -1) {
            mDownNow.setEnabled(false);
            mDownNow.setText("加载中...");
        } else {
            mNeedCoin.setText("0 红果币");
            mOrginCoin.setVisibility(View.GONE);
            mDownNow.setEnabled(true);
            mDownNow.setText("批量下载");
            mDownNow.setTag(true);
        }
    }

    @OnClick(R.id.all_chapter)
    public void onAllChapterClicked() {
        if (mFreeChapters == null || mFreeChapterInfo == null || mTotalCoin == -1 || mPayChapters == null || mPayChapterInfo == null) {
            mDownNow.setEnabled(false);
            mDownNow.setText("加载中...");
        } else {
            mNeedCoin.setText(getNeedPayMoney(mPayChapterInfo.totalPrice));
            mDownNow.setEnabled(true);
            //余额不够
            if (!hasMoenyToBuy(mPayChapterInfo.totalPrice, mTotalCoin, mGiveCoin, mGiveCanUse)) {
                mDownNow.setText("限时特惠，充多送多");
                mDownNow.setTag(false);
            } else {
                mDownNow.setText("批量下载");
                mDownNow.setTag(true);
            }
        }
    }

    @OnClick(R.id.pay_chapter)
    public void onPayChapterClicked() {
        if (mTotalCoin == -1 || mPayChapters == null || mPayChapterInfo == null) {
            mDownNow.setEnabled(false);
            mDownNow.setText("加载中...");
        } else {
            mNeedCoin.setText(getNeedPayMoney(mPayChapterInfo.totalPrice));
            mDownNow.setEnabled(true);
            //余额不够
            if (!hasMoenyToBuy(mPayChapterInfo.totalPrice, mTotalCoin, mGiveCoin, mGiveCanUse)) {
                mDownNow.setText("限时特惠，充多送多");
                mDownNow.setTag(false);
            } else {
                mDownNow.setText("批量下载");
                mDownNow.setTag(true);
            }
        }
    }

    private boolean hasMoenyToBuy(int total, int currentCoin, int giveCoin, boolean giveCanUse) {
        if (mTimeFree) return true;
        int totalCoin = currentCoin;
        if (giveCanUse) {
            totalCoin += giveCoin;
        }
        double userDiscount = total * UserRepertory.getUserDiscount();
        int needPay;
        if(userDiscount > 0 && userDiscount < 1){
            needPay = 1;
        }else{
            needPay = (int) userDiscount;
        }
        return totalCoin >= needPay;
    }

    private String getNeedPayMoney(int total) {
        if (UserRepertory.getUserDiscount() == 1 && !mTimeFree) {
            mOrginCoin.setVisibility(View.GONE);
        } else {
            mOrginCoin.setVisibility(View.VISIBLE);
            mOrginCoin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mOrginCoin.setText(total + " 红果币");
            if (mTimeFree) {
                mOrginCoin.setText(total + " 红果币");
                total = 0;
            }
        }
        return (int) (total * UserRepertory.getUserDiscount()) + " 红果币";
    }

    @OnClick(R.id.down_now)
    public void onDownNowClicked() {
        if (mDownLoadAllDialogChooseListener == null) return;
        boolean tag = (boolean) mDownNow.getTag();
        if (tag) {
            //所有免费
            if (mAllFreeChapter.isChecked()) {
                mDownLoadAllDialogChooseListener.downLoadAll(mFreeChapters);
            } else if (mPayChapter.isChecked()) {
                //所有付费
                mDownLoadAllDialogChooseListener.downLoadAll(mPayChapters);
            } else {
                List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
                chaptersList.addAll(mFreeChapters);
                chaptersList.addAll(mPayChapters);
                mDownLoadAllDialogChooseListener.downLoadAll(chaptersList);
            }
        } else {
            mDownLoadAllDialogChooseListener.toRecharge();
        }
        dismiss();
    }

    @OnClick(R.id.refresh_coin)
    public void onRefreshCoinClicked() {
        if (mDownLoadAllDialogChooseListener != null) {
            mDownLoadAllDialogChooseListener.refreshCoin();
        }
    }

    public void setTotalCoin(int totalCoin, int give, boolean giveCanUse) {
        mTotalCoin = totalCoin;
        mGiveCoin = give;
        mGiveCanUse = giveCanUse;

        StyleStringBuilder styleStringBuilder = new StyleStringBuilder();
        styleStringBuilder.append(mTotalCoin + " + ");
        if (!giveCanUse) {
            mCoinTip.setVisibility(View.VISIBLE);
            StyleString styleString = new StyleString("( " + give + " )");
            styleString.setStrikethrough();
            styleStringBuilder.append(styleString);
        } else {
            mCoinTip.setVisibility(View.GONE);
            styleStringBuilder.append("( " + give + " )");
        }
        styleStringBuilder.append(" 红果币");
        mLeftCoin.setText(styleStringBuilder.build());
        initView();
    }

    public void show(String bookId, int bookType) {
        super.show();
        reset();
        mDownLoadAllDialogPresenter.loading(bookId, bookType);
    }

    private void initView() {
        if (mAllChapter.isChecked()) {
            onAllChapterClicked();
        } else if (mPayChapter.isChecked()) {
            onPayChapterClicked();
        } else {
            onAllFreeChapterClicked();
        }
    }

    private void reset() {
        mPayChapters = null;
        mFreeChapters = null;
        mFreeChapterInfo = null;
        mPayChapterInfo = null;
    }


    public void setPayChapters(List<ChapterBean.Chapters> payChapters) {
        mPayChapters = payChapters;
        initView();
    }

    public void setFreeChapters(List<ChapterBean.Chapters> freeChapters) {
        mFreeChapters = freeChapters;
        initView();
    }

    public void setFreeChapterInfo(ChapterRepertory.ChapterTotalInfo freeChapterInfo) {
        mFreeChapterInfo = freeChapterInfo;
        initView();
    }

    public void setPayChapterInfo(ChapterRepertory.ChapterTotalInfo payChapterInfo) {
        mPayChapterInfo = payChapterInfo;
        initView();
    }

    public void setTimeFree(boolean timeFree) {
        mTimeFree = timeFree;
        initView();
    }

    public void setDownLoadAllDialogChooseListener(DownLoadAllDialogChooseListener downLoadAllDialogChooseListener) {
        mDownLoadAllDialogChooseListener = downLoadAllDialogChooseListener;
    }

    public interface DownLoadAllDialogChooseListener {
        void downLoadAll(List<ChapterBean.Chapters> chapters);

        void refreshCoin();

        void toRecharge();
    }

    public void onDestroy() {
        mBind.unbind();
    }
}
