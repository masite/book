package com.hongguo.read.widget.downdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
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

public class DownChapterDialog extends Dialog {

    @BindView(R.id.left_coin)
    TextView mLeftCoin;
    @BindView(R.id.down_now)
    TextView mDownNow;
    @BindView(R.id.need_coin)
    TextView mNeedCoin;
    @BindView(R.id.refresh_coin)
    TextView mRefreshCoin;
    @BindView(R.id.coin_tip)
    TextView mCoinTip;
    @BindView(R.id.orgin_coin)
    TextView mOrginCoin;
    @BindView(R.id.auto_buy)
    CheckBox mAutoBuy;

    @BindView(R.id.buy_current)
    RadioButton mBuyCurrent;
    @BindView(R.id.buy_next_one_type)
    RadioButton mBuyNextTypeOne;
    @BindView(R.id.buy_next_two_type)
    RadioButton mBuyNextTypeTwo;
    @BindView(R.id.buy_next_three_type)
    RadioButton mBuyNextTypeThree;

    private Unbinder mBind;

    private int     mTotalCoin  = -1;
    private int     mGiveCoin   = 0;
    private int     mBookType   = 0;
    private boolean mGiveCanUse = false;
    private DownChapterDialogPresenter     mDownChapterDialogPresenter;
    private List<DownChapterInfo>          mDownChapterSpliteInfo;
    private ChapterBean.Chapters           mChapters;
    private BuyChapterDialogChooseListener mBuyChapterDialogChooseListener;

    public DownChapterDialog(@NonNull Context context) {

        super(context, R.style.FullWidthDialog);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        setContentView(R.layout.dialog_down_chapter);
        mBind = ButterKnife.bind(this);
        mDownChapterDialogPresenter = new DownChapterDialogPresenter(this);
    }

    public void show(String bookId, int bookType, ChapterBean.Chapters chapters) {
        super.show();
        mChapters = chapters;
        mDownChapterSpliteInfo = null;
        mDownChapterDialogPresenter.loading(bookId, bookType, chapters.chapterId);
        initView();
    }

    @OnClick(value = {R.id.buy_current, R.id.buy_next_one_type, R.id.buy_next_two_type, R.id.buy_next_three_type})
    void initView() {
        if (mTotalCoin == -1 || mChapters == null) {
            mDownNow.setEnabled(false);
            mDownNow.setText("加载中..");
            return;
        }
        mDownNow.setEnabled(true);
        int size = 0;
        size = mDownChapterSpliteInfo == null ? 0 : mDownChapterSpliteInfo.size();
        setBuyMenu(size, mDownChapterSpliteInfo);
        int price = 0;
        if (mBuyCurrent.isChecked()) {
            price = mChapters.coin;
        } else if (mBuyNextTypeOne.isChecked()) {
            price = mDownChapterSpliteInfo.get(0).price;
        } else if (mBuyNextTypeTwo.isChecked()) {
            price = mDownChapterSpliteInfo.get(1).price;
        } else {
            price = mDownChapterSpliteInfo.get(2).price;
        }
        mNeedCoin.setText(getNeedPayMoney(price));

        if (hasMoenyToBuy(price, mTotalCoin, mGiveCoin, mGiveCanUse)) {
            mDownNow.setText("立即购买");
            mDownNow.setTag(true);
        } else {
            mDownNow.setText("限时特惠，充多送多");
            mDownNow.setTag(false);
        }
    }


    private void setBuyMenu(int size, List<DownChapterInfo> downChapterSpliteInfo) {
        if (size == 0)
            mBuyCurrent.setChecked(true);
        if (size < 1) {
            mBuyNextTypeOne.setEnabled(false);
            mBuyNextTypeOne.setVisibility(View.INVISIBLE);
        } else {
            mBuyNextTypeOne.setText("后" + mDownChapterSpliteInfo.get(0).mChapters.size() + "章");
            mBuyNextTypeOne.setEnabled(true);
            mBuyNextTypeOne.setVisibility(View.VISIBLE);
        }

        if (size < 2) {
            mBuyNextTypeTwo.setEnabled(false);
            mBuyNextTypeTwo.setVisibility(View.INVISIBLE);
        } else {
            mBuyNextTypeTwo.setText("后" + mDownChapterSpliteInfo.get(1).mChapters.size() + "章");
            mBuyNextTypeTwo.setEnabled(true);
            mBuyNextTypeTwo.setVisibility(View.VISIBLE);
        }

        if (size < 3) {
            mBuyNextTypeThree.setEnabled(false);
            mBuyNextTypeThree.setVisibility(View.INVISIBLE);
        } else {
            mBuyNextTypeThree.setText("后" + mDownChapterSpliteInfo.get(2).mChapters.size() + "章");
            mBuyNextTypeThree.setEnabled(true);
            mBuyNextTypeThree.setVisibility(View.VISIBLE);
        }
    }


    private boolean hasMoenyToBuy(int total, int currentCoin, int giveCoin, boolean giveCanUse) {
        int totalCoin = currentCoin;
        totalCoin += giveCoin;

        double userDiscount = total * UserRepertory.getUserDiscount();
        int needPay;

        if (userDiscount > 0 && userDiscount < 1) {
            needPay = 1;
        } else {
            needPay = (int) userDiscount;
        }

        if (mBuyCurrent.isChecked()) {
            //当前红果币够用 或者 可以使用赠送币 And 赠送币 够用
            //注: 赠送币和红果币不能一起使用
            if (currentCoin >= needPay || (giveCanUse && giveCoin >= needPay)) {
                return true;
            }
            return false;
        }

        return totalCoin >= needPay;
    }

    private String getNeedPayMoney(int total) {
        if (UserRepertory.getUserDiscount() == 1) {
            mOrginCoin.setVisibility(View.GONE);
        } else {
            mOrginCoin.setVisibility(View.VISIBLE);
            mOrginCoin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mOrginCoin.setText(total + " 红果币");
        }
        int need = 0;
        double needMoney = total * UserRepertory.getUserDiscount();
        if (needMoney < 1 && needMoney > 0) {
            need = 1;
        } else {
            need = (int) (total * UserRepertory.getUserDiscount());
        }
        return need + " 红果币";
    }

    public void setTotalCoin(int totalCoin, int give) {
        mTotalCoin = totalCoin;
        mGiveCoin = give;
        mGiveCanUse = mBookType == Constants.BOOK_FROM.FROM_SLEF;

        StyleStringBuilder styleStringBuilder = new StyleStringBuilder();
        styleStringBuilder.append(mTotalCoin + " + ");
        if (!mGiveCanUse) {
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


    public void onDestroy() {
        mBind.unbind();
    }

    /**
     * 数据库中查询出的后面需要付费的章节
     * {@link DownChapterDialogPresenter}
     */
    public void setDownChapterSpliteInfo(List<DownChapterInfo> downChapterSpliteInfo) {
        mDownChapterSpliteInfo = downChapterSpliteInfo;
        initView();
    }

    @OnClick(R.id.down_now)
    public void downNow() {
        dismiss();
        if (mBuyChapterDialogChooseListener == null) return;
        boolean enough = (boolean) mDownNow.getTag();
        if (enough) {
            List<ChapterBean.Chapters> chapters;
            if (mBuyCurrent.isChecked()) {
                chapters = new ArrayList<>();
                chapters.add(mChapters);
            } else if (mBuyNextTypeOne.isChecked()) {
                chapters = mDownChapterSpliteInfo.get(0).mChapters;
            } else if (mBuyNextTypeTwo.isChecked()) {
                chapters = mDownChapterSpliteInfo.get(1).mChapters;
            } else {
                chapters = mDownChapterSpliteInfo.get(2).mChapters;
            }
            mBuyChapterDialogChooseListener.downLoadAll(chapters);
        } else {
            mBuyChapterDialogChooseListener.toRecharge();
        }

        if (mAutoBuy.isChecked()) {
            mBuyChapterDialogChooseListener.autoBuy();
        }
    }


    @OnClick(R.id.refresh_coin)
    public void refreshCoin() {
        if (mBuyChapterDialogChooseListener == null) return;
        mBuyChapterDialogChooseListener.refreshCoin();
    }

    public void setBookType(int bookType) {
        mBookType = bookType;
    }

    public void setBuyChapterDialogChooseListener(BuyChapterDialogChooseListener buyChapterDialogChooseListener) {
        mBuyChapterDialogChooseListener = buyChapterDialogChooseListener;
    }

    public interface BuyChapterDialogChooseListener {
        void downLoadAll(List<ChapterBean.Chapters> chapters);

        void refreshCoin();

        void toRecharge();

        void autoBuy();
    }


}
