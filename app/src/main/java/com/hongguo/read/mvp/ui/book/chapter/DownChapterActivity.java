package com.hongguo.read.mvp.ui.book.chapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BuyChapterAdapter;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.contractor.book.chapter.DownChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.mvp.presenter.book.BookDiscountPresenter;
import com.hongguo.read.mvp.presenter.book.chapter.ChapterPresenter;
import com.hongguo.read.mvp.presenter.book.chapter.DownChapterPresenter;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.utils.down.HgReadDownManager;
import com.hongguo.read.widget.DownChapterMenuPop;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/15.
 */

public class DownChapterActivity extends MineInfoActivity implements DownChapterContractor.IView, ChapterContractor.IView, BookDiscountContractor.IView, LoadingView.LoadingViewClickListener, BuyChapterAdapter.ChapterItemClickListener, DownChapterMenuPop.PopItemClickListener, HgReadDownManager.ChapterDownListener {

    private static final String INTENT_BOOKID    = "intent_bookid";
    private static final String INTENT_BOOKFROM  = "intent_bookfrom";
    private static final String INTENT_BOOKNAME  = "intent_bookname";
    private static final String INTENT_BOOKCOVER = "intent_bookCover";

    @Inject
    DownChapterPresenter  mDownChapterPresenter;
    @Inject
    ChapterPresenter      mChapterPresenter;
    @Inject
    BookDiscountPresenter mBookDiscountPresenter;
    @Inject
    MinePresenter         mMinePresenter;

    @BindView(R.id.chapter_list)
    RecyclerView mChapterList;
    @BindView(R.id.chapter_info)
    TextView     mChapterInfo;
    @BindView(R.id.need_pay)
    TextView     mNeedPay;
    @BindView(R.id.left_account)
    TextView     mLeftAccount;
    @BindView(R.id.orgin_price)
    TextView     mOrginPrice;
    @BindView(R.id.down_content)
    LinearLayout mDownContent;

    @BindView(R.id.download_now)
    TextView mDownloadNow;
    @BindView(R.id.chapter_menu)
    TextView mChapterMenu;

    private String mBookId;
    private int    mBookFrom;
    private String mBookName;
    private String mBookCover;
    private int    mTotalCoin;
    private int    mNeedPayMoney;

    private List<ChapterBean.Chapters> mItems;
    private BuyChapterAdapter          mBuyChapterAdapter;
    private DownChapterMenuPop         mChapterMenuPop;
    private HgReadDownManager          mChapterDownService;
    private boolean                    mIsTimeFree;

    public static void toActivity(Context context, String bookid, int bookFrom, String bookName, String bookCover) {
        Intent intent = new Intent(context, DownChapterActivity.class);
        intent.putExtra(INTENT_BOOKFROM, bookFrom);
        intent.putExtra(INTENT_BOOKID, bookid);
        intent.putExtra(INTENT_BOOKNAME, bookName);
        intent.putExtra(INTENT_BOOKCOVER, bookCover);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_down_chapter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        getSupportActionBar().hide();
        mToolLayer.setVisibility(View.GONE);

        mBookName = getIntent().getStringExtra(INTENT_BOOKNAME);
        mBookCover = getIntent().getStringExtra(INTENT_BOOKCOVER);
        mBookId = getIntent().getStringExtra(INTENT_BOOKID);
        mBookFrom = getIntent().getIntExtra(INTENT_BOOKFROM, Constants.BOOK_FROM.FROM_SLEF);

        setTitle(mBookName);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mDownContent, 1);

        mItems = new ArrayList<>();
        mBuyChapterAdapter = new BuyChapterAdapter(mContext, mBookId, mBookFrom, mItems);
        mChapterList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mBuyChapterAdapter.setChapterItemClickListener(this);
        mChapterList.setAdapter(mBuyChapterAdapter);

        mChapterPresenter.bingView(this);
        mChapterPresenter.init(mBookId, mBookFrom, mBookName, mBookCover);

        mBookDiscountPresenter.init(mBookId, mBookFrom);
        mBookDiscountPresenter.bingView(this);
        mBookDiscountPresenter.loading();

        mMinePresenter.bingView(this);
        mMinePresenter.loading();

        mDownChapterPresenter.bingView(this);
        mDownChapterPresenter.loading();

        mChapterMenuPop = new DownChapterMenuPop(mContext);
        mChapterMenuPop.setMpopItemClickListener(this);

        mChapterDownService = findApp().getChapterDownService();
        mChapterDownService.bindChapterListener(mBookId, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMinePresenter.queryUserCoin();
    }

    /*************************用户信息 start****************************/
    @Override
    public void setUserInfo(UserInfoBean.DataBean userInfo) {
        super.setUserInfo(userInfo);
    }

    @Override
    public void setUserIsVip(String timeout) {
        super.setUserIsVip(timeout);
    }

    @Override
    public void setUserIsSVip(String timeout) {
        super.setUserIsSVip(timeout);
    }

    @Override
    public void setUserNormal() {
        super.setUserNormal();
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {
        super.setCoins(currentCoin, giveCoin);
        //百度书籍不能使用赠送币，只要我们自己的书才可以用
        //我们自己书显示赠送币
        if (mBookFrom == 0) {
            mLeftAccount.setText(currentCoin + " (+" + giveCoin + ") 红果币");
        } else {
            mLeftAccount.setText(currentCoin + " 红果币");
            //百度书籍直接将赠送币置0
            giveCoin = 0;
        }

        mTotalCoin = (currentCoin + giveCoin);

        //检查选中信息
        boolean hasSelected = false;
        for (ChapterBean.Chapters item : mItems) {
            if (item.isChoose) {
                hasSelected = true;
                break;
            }
        }

        if (!hasSelected) return;

        mDownloadNow.setEnabled(true);
        //足够金额购买
        if (mTotalCoin >= mNeedPayMoney) {
            mDownloadNow.setText("立即下载");
            mDownloadNow.setEnabled(true);
        } else {
            //金额不足
            mDownloadNow.setText("余额不足，充值并购买");
            mDownloadNow.setEnabled(true);
        }
    }

    /*************************用户信息 end****************************/

    @Override
    public void setDiscountInfo(int freeType) {
        mIsTimeFree = mBookDiscountPresenter.getUserCanReadByFreeType(freeType);
        mBuyChapterAdapter.setFree(mBookDiscountPresenter.getUserCanReadByFreeType(freeType));
        mBuyChapterAdapter.notifyDataSetChanged();
    }

    @Override
    public void nativeChapters(List<ChapterBean.Chapters> items) {
        mItems.clear();
        mItems.addAll(items);
        mBuyChapterAdapter.notifyChange();
        if (items.size() != 0) {
            mChapterPresenter.checkUpate(mBookId, mBookName, items.get(0).updateTime, mBookCover);
        } else {
            mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", true);
        }
    }

    @Override
    public void setChapters(List<ChapterBean.Chapters> items) {
        mItems.clear();
        mItems.addAll(items);
        mBuyChapterAdapter.setDataBeen(items);
        mBuyChapterAdapter.notifyChange();
    }

    @Override
    public void setPageChapters(List<ChapterBean.Chapters> items) {

    }

    @ViewMethod
    public void notifyAdapter() {
        mBuyChapterAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookDiscountPresenter.loading();
    }

    @ViewMethod
    public void setTotalPrice(int price, int selectNumber) {
        mDownloadNow.setEnabled(true);
        mChapterInfo.setText("已选 " + selectNumber + "章");
        //折扣优惠
        if (UserRepertory.getUserDiscount() != 1 && price != 0) {
            mOrginPrice.setVisibility(View.VISIBLE);
            mOrginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mOrginPrice.setVisibility(View.GONE);
        }

        mOrginPrice.setText(price + " 红果币");
        if (mIsTimeFree) {
            mNeedPayMoney = 0;
        } else {
            double userDiscount = price * UserRepertory.getUserDiscount();
            if (userDiscount > 0 && userDiscount < 1) {
                mNeedPayMoney = 1;
            } else {
                mNeedPayMoney = (int) userDiscount;
            }
        }

        mNeedPay.setText(mNeedPayMoney + " 红果币");
        if (mTotalCoin < mNeedPayMoney) {
            mDownloadNow.setText("余额不足，请先充值");
        } else {
            mDownloadNow.setText("立即下载");
        }
    }

    @Override
    public void chapterItemClick(int position) {
        //检查选中信息
        boolean hasSelected = false;
        for (ChapterBean.Chapters item : mItems) {
            if (item.isChoose) {
                hasSelected = true;
                break;
            }
        }

        if (hasSelected) {
            mDownloadNow.setEnabled(true);
            mDownloadNow.setText("立即下载");
        } else {
            mDownloadNow.setEnabled(false);
            mDownloadNow.setText("请选择下载的章节");
        }

        mDownChapterPresenter.computeMoney(mItems);
    }


    @OnClick(R.id.download_now)
    void startDown() {
        //需要充值
        if (mTotalCoin < mNeedPayMoney) {
            RechargeActivity.toActivity(mContext);
            return;
        }

        //检查需要下载的章节
        mDownloadNow.setEnabled(false);
        mDownloadNow.setText("下载中,请稍候...");

        showWaitDialog(true, "等待下载...", null);

        mDownChapterPresenter.checkDown(mItems, mBookFrom, mBookId);
    }


    @OnClick(R.id.refresh_coin)
    public void refreshCoin() {
        mMinePresenter.queryUserCoin();
    }

    @ViewMethod
    public void addDownChapter(ChapterBean.Chapters chapter) {
        mChapterDownService.addChapterDownLoad(chapter, mBookId, mBookFrom, false);
    }

    @OnClick(R.id.chapter_menu)
    void chapterMenu() {
        mChapterMenuPop.showAsDropDown(mChapterMenu, 0, -mChapterMenuPop.getArrowHeight() - DisplayUtil.dip2px(mContext, 8));
    }

    @Override
    public void allChapter() {
        mDownChapterPresenter.selectAll(mItems);
    }

    @Override
    public void allFree() {
        mDownChapterPresenter.selectFree(mItems);
    }

    @Override
    public void allNeedPay() {
        mDownChapterPresenter.selectNeedPay(mItems);
    }


    @Override
    public void downError(ChapterBean.Chapters ChapterBean, int percent, Exception errorMessage) {
        mDownloadNow.setEnabled(false);
        mDownloadNow.setText("下载中.." + percent + "%");
        showWaitDialog(true, "下载中 " + percent + "%", null, false);
        if (percent == 100) {
            allDown();
        }
    }

    @Override
    public void downSuccess(ChapterBean.Chapters chapterBean, int percent) {
        mDownloadNow.setEnabled(false);
        mDownloadNow.setText("下载中.." + percent + "%");
        mDownChapterPresenter.checkBookBuy(chapterBean, mBookId, mBookFrom);
        showWaitDialog(true, "下载中 " + percent + "%", null, false);
        if (percent == 100) {
            allDown();
        }
    }

    @ViewMethod
    public void allDown() {
        for (ChapterBean.Chapters item : mItems) {
            item.isChoose = false;
        }
        mMinePresenter.queryUserCoin();
        dismissWaitDialog();

        //初始化数据
        mDownloadNow.setEnabled(false);
        mDownloadNow.setText("请选择下载的章节");
        mBuyChapterAdapter.notifyDataSetChanged();
        mNeedPay.setText("0 红果币");
        mChapterInfo.setText("已选 " + 0 + "章");
        mNeedPayMoney = 0;
        mOrginPrice.setVisibility(View.GONE);
    }

    @OnClick(R.id.chatper_back)
    void back() {
        finish();
    }
}
