package com.hongguo.read.mvp.ui.book;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.utils.AndroidBatteryUtils;
import com.hongguo.common.utils.DrawerLayoutListenerImp;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.UmengShareHelper;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookReaderAdapter;
import com.hongguo.read.adapter.BookReaderChapterAdapter;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.AddShelfSuccessEvent;
import com.hongguo.read.eventbus.BookChapterSelectedEvent;
import com.hongguo.read.mvp.contractor.book.BookReaderContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.mvp.presenter.book.BookReaderPresenter;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.ui.book.detail.BookReaderChapterFragment;
import com.hongguo.read.mvp.ui.discuss.AllDiscussActivity;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.read.mvp.ui.reward.UserRewardActivity;
import com.hongguo.read.mvp.ui.vip.VipActivity;
import com.hongguo.read.utils.AppUtils;
import com.hongguo.read.utils.down.HgReadDownManager;
import com.hongguo.read.utils.down.exception.CoinNotEnoughException;
import com.hongguo.read.widget.AnimationUtils;
import com.hongguo.read.widget.BookErrorReport;
import com.hongguo.read.widget.DownXunFeiDialog;
import com.hongguo.read.widget.ReadModePanelDialog;
import com.hongguo.read.widget.downdialog.DownChapterDialog;
import com.hongguo.read.widget.reader.BookView;
import com.hongguo.read.widget.reader.BookViewListener;
import com.hongguo.read.widget.reader.tts.TTSAnimation;
import com.losg.library.widget.TransStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2017/12/25.
 */

@Route(path = AppRouter.BOOK_READER)
public class BookReaderActivity extends MineInfoActivity implements BookReaderContractor.IView, BookViewListener, BookReaderAdapter.BookReaderDownListener, HgReadDownManager.ChapterDownListener, AndroidBatteryUtils.BatteryChanageListener, DownChapterDialog.BuyChapterDialogChooseListener, TTSAnimation.StartReadBookListener, ReadModePanelDialog.ReadModePanelListener, BookErrorReport.BookErrorSubmit, DownXunFeiDialog.XunFeiDownSuccess, BookReaderControlPanel.BookReaderControlChangeListener {

    private static final String INTENT_BOOK_ID            = "intent_book_id";
    private static final String INTENT_BOOK_FROM          = "intent_book_from";
    private static final String INTENT_BOOK_NAME          = "intent_book_name";
    private static final String INTENT_BOOK_COVER         = "intent_book_Cover";
    private static final String INTENT_BOOK_AUTHOR        = "intent_book_Author";
    private static final String INTENT_BOOK_CHAPTER_INDEX = "intent_book_chapter_index";
    private static final String INTENT_BOOK_PAGE          = "intent_book_page";

    @Inject
    BookReaderPresenter mBookReaderPresenter;
    @Inject
    MinePresenter       mMinePresenter;

    @BindView(R.id.book_name)
    TextView       mReaderTitle;
    @BindView(R.id.book_control_panel)
    LinearLayout   mBookControlPanel;
    @BindView(R.id.reader_toolbar)
    RelativeLayout mReaderToolbar;

    @BindView(R.id.book_reader)
    BookView       mBookReader;
    @BindView(R.id.trans_tool)
    TransStatusBar mTransTool;
    @BindView(R.id.setting_layer)
    LinearLayout   mSettingLayer;
    @BindView(R.id.book_drawer)
    DrawerLayout   mDrawerLayout;
    @BindView(R.id.book_chapter)
    FrameLayout    mBookChapterContent;
    @BindView(R.id.book_light)
    TextView       mBookLight;

    @Autowired(name = "bookId")
    String mBookId;
    @Autowired(name = "bookAuthor")
    String mBookAuthor;
    @Autowired(name = "bookFrom")
    int    mBookFrom;
    @Autowired(name = "bookName")
    String mBookName;
    @Autowired(name = "bookCover")
    String mBookCover;

    private BookReaderAdapter          mBaseBookAdapter;
    private HgReadDownManager          mChapterDownService;
    private List<ChapterBean.Chapters> mChapters;
    private BookReaderChapterFragment  mBookChapterMenuFragment;
    private AndroidBatteryUtils        mAndroidBatteryUtils;
    private DownChapterDialog          mDownChapterDialog;
    private DownXunFeiDialog           mDownXunFeiDialog;
    private UmengShareHelper           mUmengShareHelper;
    private BookReaderControlPanel     mBookReaderControlPanel;

    //阅读模式的控制面板
    private ReadModePanelDialog mReadModePanelDialog;
    private BookErrorReport     mBookErrorReport;
    private boolean             mBookShelfExist;
    private int                 mCurrentChapterIndex;
    private int                 mCurrentPage;

    private boolean mReadMode = false;

    private boolean mFirstInitChapterIndex = true;

    public static void toActivity(Context context, String bookid, int bookFrom, String bookName, String bookCover, String author) {
        toActivity(context, bookid, bookFrom, bookName, bookCover, -1, -1, author);
    }

    public static void toActivity(Context context, String bookid, int bookFrom, String bookName, String bookCover, int bookChapterIndex, int page, String author) {
        Intent bookIntent = getBookIntent(context, bookid, bookFrom, bookName, bookCover, bookChapterIndex, page, author);
        context.startActivity(bookIntent);
    }

    public static Intent getBookIntent(Context context, String bookid, int bookFrom, String bookName, String bookCover, int bookChapterIndex, int page, String author) {
        Intent intent = new Intent(context, BookReaderActivity.class);
        intent.putExtra(INTENT_BOOK_FROM, bookFrom);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        intent.putExtra(INTENT_BOOK_COVER, bookCover);
        intent.putExtra(INTENT_BOOK_AUTHOR, author);
        intent.putExtra(INTENT_BOOK_CHAPTER_INDEX, bookChapterIndex);
        intent.putExtra(INTENT_BOOK_PAGE, page);
        return intent;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_book_reader;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ARouter.getInstance().inject(this);

        hiddenToolBar();
        //获取intent中的传值
        getIntentData();

        //下载任务
        mChapterDownService = findApp().getChapterDownService();
//        mChapterDownService.bindChapterListener(mBookId, this);

        //检测电量信息
        mAndroidBatteryUtils = new AndroidBatteryUtils(mContext);
        mAndroidBatteryUtils.setBatteryChanageListener(this);

        mChapters = new ArrayList<>();
        //设置显示的控制器
        mBaseBookAdapter = new BookReaderAdapter(mBookId, mBookName, mBookFrom, mChapters);
        mBookErrorReport = new BookErrorReport(mContext);
        mBookErrorReport.setBookErrorSubmit(this);
        mBaseBookAdapter.setBookReaderDownListener(this);
        mReaderTitle.setText(mBookName);
        mBookReader.setBookName(mBookName);
        mBookReader.setBaseBookAdatper(mBaseBookAdapter);
        mBookReader.setBookViewClickListener(this);

        //下载任务弹窗
        mDownChapterDialog = new DownChapterDialog(mContext);
        mDownChapterDialog.setBuyChapterDialogChooseListener(this);
        mDownChapterDialog.setBookType(mBookFrom);

        //插件下载
        mDownXunFeiDialog = new DownXunFeiDialog(this);
        mDownXunFeiDialog.setXunFeiDownSuccess(this);
        mReadModePanelDialog = new ReadModePanelDialog(this);
        mReadModePanelDialog.setReadModePanelListener(this);

        //左滑菜单
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        initDrawer();
        //菜单信息
        mBookChapterContent.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels * 5 / 6;
        mBookChapterMenuFragment = BookReaderChapterFragment.getInstance(mBookId, mBookFrom, mBookName, mBookCover);

        mBookReaderControlPanel = new BookReaderControlPanel(mContext, mBookControlPanel, this);

        mUmengShareHelper = new UmengShareHelper(mContext);
    }


    @Override
    protected void initData() {
        super.initData();
        //获取红果币信息的控制
        mMinePresenter.bingView(this);
        mMinePresenter.loading();
        //阅读控制
        mBookReaderPresenter.bingView(this);
        mBookReaderPresenter.loading();
        mBookReaderPresenter.queryBookReadInfo(mBookId, mBookFrom);
    }

    /**
     * 数据中查询该书的阅读记录
     *
     * @param chapterIndex
     * @param page
     * @param bookshelfExist
     */
    @ViewMethod
    public void setBookReadInfo(int chapterIndex, int page, boolean bookshelfExist) {
        if (mCurrentChapterIndex == -1) {
            mCurrentChapterIndex = chapterIndex;
            mCurrentPage = page;
        }
        //加载章节信息
        getSupportFragmentManager().beginTransaction().replace(R.id.book_chapter, mBookChapterMenuFragment).commit();
        mBookShelfExist = bookshelfExist;
    }


    @ViewMethod
    public void initBookCommonInfo(int animType, int backgroundColor, int backgroundIndex, int backgroundResource, int textColor, boolean isModeNeight) {
        mBookLight.setSelected(isModeNeight);
        mBookLight.setText(isModeNeight ? "白天" : "夜晚");
        mBookReader.setBookAnimationType(animType);
        mBookReader.setTextColor(textColor);
        mBookChapterMenuFragment.setTextColor(textColor);
        if (backgroundResource == 0)
            mBookReader.setBackgroundColor(backgroundColor);
        mBookReader.setBackgroundResource(backgroundResource);
        mBookChapterMenuFragment.setBackgroundResource(backgroundResource, backgroundColor);
        mBookReaderControlPanel.initBookCommonInfo(animType, backgroundIndex, isModeNeight);
    }

    @ViewMethod
    public void initBookSpecialInfo(int textSize, int lineType, int lineHeight, int paragraphHeight) {
        mBookReader.setParagraphHeight(lineHeight, paragraphHeight);
        mBookReader.setTextSize(textSize);
        mBookReaderControlPanel.initBookSpecialInfo(textSize, lineType);
    }

    @ViewMethod
    public void bookReadInfo(int speed, String speakerName) {
        mBookReader.setSpeed(speed);
        mBookReader.setSpeakerName(speakerName);
        mReadModePanelDialog.setSpeakerName(speakerName);
        mReadModePanelDialog.setSpeakerSpeed(speed);
    }

    @Override
    public void clearErrorReportInfo() {
        mBookErrorReport.clearAll();
    }

    /**
     * 查询出显示的章节信息
     * {@link BookReaderChapterFragment}
     *
     * @param items
     * @param isLocal 是否是本地数据库中的信息
     */
    public void setChapters(List<ChapterBean.Chapters> items, boolean isLocal) {
        //本地不存在(文件删除的可能，直接从第一张重新加载)
        if ((isLocal && items.size() == 0) || items == null) {
            mCurrentPage = 0;
            mCurrentChapterIndex = 0;
            return;
        }

        if (mCurrentChapterIndex > items.size() - 1) {
            mCurrentChapterIndex = 0;
            mCurrentPage = 0;
        }

        if (mFirstInitChapterIndex) {
            mFirstInitChapterIndex = false;
            mBookChapterMenuFragment.setSelectedIndex(mCurrentChapterIndex);
            mBookReader.setChapterIndex(mCurrentChapterIndex);
            mBookReader.setCurrentPage(mCurrentPage);
        }
        dismissWaitDialog();
        mChapters.clear();
        mChapters.addAll(items);
        mBookReaderPresenter.setChapters(items);
        mBookReader.notifySizeChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAndroidBatteryUtils.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAndroidBatteryUtils.onPause();
    }

    @Override
    protected void onDestroy() {
        mBookReaderControlPanel.destory();
        mUmengShareHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengShareHelper.onActivityResult(requestCode, resultCode, data);
    }


    /***********************bookview 点击事件 start*******************************************/
    @Override
    public void bookSettingClick() {
        if (mReadMode) {
            mReadModePanelDialog.show();
            return;
        }

        if (!hiddenFunction()) {
            AppUtils.setTransparentStatusBar(BookReaderActivity.this);
            mTransTool.setVisibility(View.VISIBLE);
            mSettingLayer.setVisibility(View.VISIBLE);
            mReaderToolbar.setVisibility(View.VISIBLE);
            AnimationUtils.animInFromTop(mReaderToolbar, 300, null);
            AnimationUtils.animInFromBottom(mSettingLayer, 300, null);
        }
    }

    /**
     * 书本滑动，隐藏状态信息
     */
    @Override
    public void bookScrolled() {
        hiddenFunction();
    }

    private boolean hiddenFunction() {
        View visiableView = null;
        //哪个功能模块显示，隐藏哪个模块
        if (mSettingLayer.getVisibility() == View.VISIBLE) {
            visiableView = mSettingLayer;
        } else if (mBookControlPanel.getVisibility() == View.VISIBLE) {
            visiableView = mBookControlPanel;
        }

        if (visiableView != null) {
            AnimationUtils.animOutToTop(mReaderToolbar, 200, null);
            View finalVisiableView = visiableView;
            AnimationUtils.animOutToBottom(visiableView, 200, () -> {
                finalVisiableView.setVisibility(View.GONE);
                mReaderToolbar.setVisibility(View.GONE);
                mTransTool.setVisibility(View.GONE);
                AppUtils.setFullScreen(BookReaderActivity.this);
            });
            return true;
        }
        return false;
    }

    @Override
    public void pageChanged(int currentPage) {
        mCurrentPage = currentPage;
    }

    @Override
    public void chapterChanged(int currentChapterIndex) {
        mCurrentChapterIndex = currentChapterIndex;
        mBookChapterMenuFragment.setSelectedIndex(mCurrentChapterIndex);
    }

    @Override
    public void bookPayClicked(int currentPage) {
        mDownChapterDialog.show(mBookId, mBookFrom, mChapters.get(mBookReader.getChapterIndex()));
        mMinePresenter.queryUserCoin();
    }

    @Override
    public void bookVipClicked(int currentPage) {
        VipActivity.toActivity(mContext);
    }

    @Override
    public void bookIsEndChapter() {
        BookEndRecommendActivity.toActivity(mContext, mBookId, mBookFrom, mBookName);
    }

    @Override
    public void bookIsFirstChapter() {
        toastMessage("已经是最前面了~");
    }

    @Override
    public void errorReloadClick(int currentIndex) {
        mBaseBookAdapter.getChapters().get(mCurrentChapterIndex).error = "";
        mBookReader.notifySizeChange();
    }

    @Override
    public void bookRewardClick() {
        UserRewardActivity.toActivity(mContext, mBookId, mBookName, mBookFrom, mBookCover);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //阅读模式，上下键用做调节音量
        if (!mBookReader.isReadMode()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                mBookReader.animationMove(false);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                mBookReader.animationMove(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /***********************bookview 点击事件 end*******************************************/

    @OnClick(R.id.book_setting)
    void bookSetting() {
        AnimationUtils.animOutToBottom(mSettingLayer, 200, () -> {
            mSettingLayer.setVisibility(View.GONE);
            mBookControlPanel.setVisibility(View.VISIBLE);
            AnimationUtils.animInFromBottom(mBookControlPanel, 300, null);
        });
    }

    @OnClick(R.id.book_menu)
    void showBookMenu() {
        AppUtils.setFullScreen(this);
        hiddenFunction();
        RxJavaUtils.delayRun(300, () -> {
            mDrawerLayout.openDrawer(Gravity.START);
            //防止刷新卡顿
            RxJavaUtils.delayRun(500, () -> {
                mBookChapterMenuFragment.notifyAdapter();
                mBookChapterMenuFragment.location();
            });
        });
    }


    public void closeMenu() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    /**
     * 章节列表点击后 回调到阅读页，进行修改显示内容
     * {@link BookReaderChapterAdapter}
     *
     * @param bookChapterSelectedEvent
     */
    @Subscribe
    public void onEvent(BookChapterSelectedEvent bookChapterSelectedEvent) {
        mDrawerLayout.closeDrawer(Gravity.START);
        //关闭有时间间隔，在主线程操作会造成卡顿现象,延时操作
        RxJavaUtils.delayRun(500, () -> {
            mBookReader.setCurrentPage(0);
            mCurrentPage = 0;
            mBookReader.setChapterIndex(bookChapterSelectedEvent.mChapterIndex);
            mBookReader.notifyDataChange();
        });
    }

    //需要下载的信息
    @Override
    public void needDown(ChapterBean.Chapters chapters, boolean total) {
        LogUtils.log("需要下载__" + chapters.chapterName + (total ? "购买" : "描述信息"));
        mChapterDownService.addChapterDownLoad(chapters, mBookId, mBookFrom, !total);
        if (total) {
            if (!chapters.mTimeFree && chapters.coin != 0 && chapters.isBuy == 0) {
                if (mBookFrom == Constants.BOOK_FROM.FROM_BAIDU)
                    StatisticsUtils.baiduPay("购买" + mBookName + "---" + chapters.chapterName);
                else
                    StatisticsUtils.hgPay("购买" + mBookName + "---" + chapters.chapterName);
            }
        }
    }

    @Override
    public void downError(ChapterBean.Chapters chapters, int percent, Exception errorMessage) {
        dealMulityDownLoadingInfo(percent);
        chapters.error = "下载失败";
        //更新当前章节
        if (chapters.chapterId.equals(mBaseBookAdapter.getChapters().get(mBookReader.getChapterIndex()).chapterId)) {
            if (errorMessage instanceof CoinNotEnoughException) {
                toastMessage("红果币不足~ ~");
                chapters.error = "";
                mBaseBookAdapter.setAutoBuy(false);
                mMinePresenter.queryUserCoin();
            }
            mBookReader.notifySizeChange();
        }
    }

    @Override
    public void downSuccess(ChapterBean.Chapters chapters, int percent) {
        chapters.error = "";
        mBookReaderPresenter.checkBookBuy(chapters, mBookId, mBookFrom);
        dealMulityDownLoadingInfo(percent);
        //是当前显示的章节才去刷新，这边有可能是预加载产生的
        if (chapters.chapterId.equals(mBaseBookAdapter.getChapters().get(mBookReader.getChapterIndex()).chapterId)) {
            mBookReader.notifySizeChange();
        }
    }

    private void dealMulityDownLoadingInfo(int percent) {
        mBookChapterMenuFragment.setPrecent(percent);
        if (percent == -1) return;
        showWaitDialog(true, "下载中 " + percent + "%", null, false);
        if (percent == 100) {
            dismissWaitDialog();
        }
    }

    @Override
    public void batteryChange(int percent) {
        mBookReader.setBattery(percent);
    }

    /***
     * {@link BookReaderChapterFragment}
     * 下载章节
     * @param chapters
     */
    public void downChapters(List<ChapterBean.Chapters> chapters) {
        mBookReaderPresenter.checkDown(chapters, mBookFrom, mBookId);
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {
        super.setCoins(currentCoin, giveCoin);
        mBookChapterMenuFragment.setUserCoin(currentCoin, giveCoin);
        mDownChapterDialog.setTotalCoin(currentCoin, giveCoin);
        dismissWaitDialog();
    }

    /**
     * {@link BookReaderChapterFragment}
     * 刷新红果币
     */
    public void refreshCoin() {
        showWaitDialog(true, "刷新红果币", null);
        mMinePresenter.queryUserCoin();
    }

    @Override
    public void downLoadAll(List<ChapterBean.Chapters> chapters) {
        showWaitDialog(true, "正在加载", null);
        mBookReaderPresenter.checkDown(chapters, mBookFrom, mBookId);
    }

    @Override
    public void toRecharge() {
        RechargeActivity.toActivity(mContext);
    }

    @Override
    public void autoBuy() {
        mBaseBookAdapter.setAutoBuy(true);
    }

    @OnClick(R.id.chapter_refresh)
    void refreshChapter() {
        showWaitDialog(true, "刷新章节", null);
        StatisticsUtils.click("刷新章节");
        mBookChapterMenuFragment.updateChapter();
    }

    @OnClick(R.id.discuss)
    void discuss() {
        AllDiscussActivity.toActivity(mContext, mBookId, mBookFrom, mBookCover, mBookName, mBookAuthor, 0);
    }

    @ViewMethod
    public void allDown() {
        dismissWaitDialog();
        mBookChapterMenuFragment.setPrecent(100);
    }

    private void initDrawer() {
        //解决显示不全的问题
        mDrawerLayout.setDrawerListener(new DrawerLayoutListenerImp() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mDrawerLayout.bringChildToFront(mBookChapterContent);
                mDrawerLayout.requestLayout();
            }
        });
    }

    @OnClick(R.id.chatper_back)
    @Override
    public void onBackPressed() {

        //打开了菜单
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        //书架中不存在该本书
        if (!mBookShelfExist) {
            showMessDialog("加入书架", "加入后，方便您下次继续阅读", "取消", "确定", messageInfoDialog -> {
                messageInfoDialog.dismissWithoutAnim();
                mBookReaderPresenter.addBookShelf(mBookId, mBookFrom, mBookName, mBookCover);
                mBookReaderPresenter.saveReadInfo(mBookId, mBookFrom, mCurrentChapterIndex, mCurrentPage, (float) (mCurrentChapterIndex + 1) / mBaseBookAdapter.getChapterCount() * 100, true);
                EventBus.getDefault().post(new AddShelfSuccessEvent(mBookId));
                finish();
            }, messageInfoDialog -> {
                mBookReaderPresenter.saveReadInfo(mBookId, mBookFrom, mCurrentChapterIndex, mCurrentPage, (float) (mCurrentChapterIndex + 1) / mBaseBookAdapter.getChapterCount() * 100, false);
                messageInfoDialog.dismissWithoutAnim();
                finish();
            });
        } else {
            //存在直接更新书架的信息
            mBookReaderPresenter.saveReadInfo(mBookId, mBookFrom, mCurrentChapterIndex, mCurrentPage, (float) (mCurrentChapterIndex + 1) / mBaseBookAdapter.getChapterCount() * 100, true);
            finish();
        }
    }

    public void setVipShowInfo(String showMessage, boolean show) {
        mBookReader.setVipBuyInfo(showMessage, show);
        mBookReader.notifyDataChange();
    }

    @OnClick(R.id.book_share)
    public void bookShare() {
        hiddenFunction();
        mUmengShareHelper.showShare("推荐好书\"" + mBookName + "\"", "http://a.app.qq.com/o/simple.jsp?pkgname=com.hongguo.read", "我在红果阅读发现一本好书推荐给你，赶快来看看吧", mBookCover);
    }

    @OnClick(R.id.read_mode)
    public void readMode() {
        mBookReader.startRead(this);
    }

    @Override
    public void startSuccess() {
        mReadModePanelDialog.setSpeakers(mBookReader.getSpeakers());
        hiddenFunction();
        mReadMode = true;
    }

    @Override
    public void needInstall(String installUrl) {
        hiddenFunction();
        mBookReader.stopRead();
        mDownXunFeiDialog.setDownUrl(installUrl);
        mDownXunFeiDialog.show();
    }

    @Override
    public void openChooseSpeaker() {
        hiddenFunction();
        mBookReader.stopRead();
    }

    @OnClick(R.id.book_light)
    public void bookNeightMode() {
        mBookReaderPresenter.changeNeightMode();
        mBookReader.notifyDataChange();
    }

    /*************语音阅读模式控制面板回调 start*************/
    @Override
    public void perChapter() {
        int currentChapter = mBookReader.getChapterIndex();
        if (currentChapter == 0) {
            bookIsFirstChapter();
            return;
        }
        mBookReader.setCurrentPage(0);
        mBookReader.setChapterIndex(currentChapter - 1);
        mBookReader.notifyDataChange();
    }

    @Override
    public void nextChapter() {
        int currentChapter = mBookReader.getChapterIndex();
        if (currentChapter == mBaseBookAdapter.getChapterCount() - 1) {
            bookIsEndChapter();
            return;
        }
        mBookReader.setCurrentPage(0);
        mBookReader.setChapterIndex(currentChapter + 1);
        mBookReader.notifyDataChange();
    }

    @Override
    public void exitReadMode() {
        mReadMode = false;
        mBookReader.stopRead();
    }

    @Override
    public void readSpeedChange(int speed) {
        mBookReaderPresenter.saveSpeed(speed);
        mBookReader.setSpeed(speed);
        if (mBookReader.isReadMode()) {
            mBookReader.notifyDataChange();
        }
    }

    @Override
    public void readSpeakerChange(String name) {
        mBookReaderPresenter.saveSpeakerName(name);
        mBookReader.setSpeakerName(name);
        if (mBookReader.isReadMode()) {
            mBookReader.notifyDataChange();
        }
    }

    @Override
    public void chooseOtherSpeaker() {
        mBookReader.openChooseSpeaker();
    }

    /*************语音阅读模式控制面板回调 end*************/

    @OnClick(R.id.read_report)
    public void report() {
        mBookErrorReport.show();
    }

    @Override
    public void errorSubmit(String errorInfo, String suggest, String contactNumber) {
        ChapterBean.Chapters chapters = mBaseBookAdapter.getChapters().get(mBookReader.getChapterIndex());
        mBookReaderPresenter.bookReport(mBookName, chapters.chapterName, errorInfo, suggest, contactNumber);
    }

    @Override
    public void xunFeiDownSuccess(String path) {
        installApk(path);
    }

    private void getIntentData() {
//        if (TextUtils.isEmpty(mBookName)) {
//            mBookName = getIntent().getStringExtra(INTENT_BOOK_NAME);
//            mBookCover = getIntent().getStringExtra(INTENT_BOOK_COVER);
//            mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
//            mBookAuthor = getIntent().getStringExtra(INTENT_BOOK_AUTHOR);
//            mBookFrom = getIntent().getIntExtra(INTENT_BOOK_FROM, Constants.BOOK_FROM.FROM_SLEF);
//            mCurrentChapterIndex = getIntent().getIntExtra(INTENT_BOOK_CHAPTER_INDEX, -1);
//            mCurrentPage = getIntent().getIntExtra(INTENT_BOOK_PAGE, -1);
//        }

        mBookName = "NAME";
        mBookCover = "";
        mBookId = "";
        mBookAuthor = "";
        mBookFrom = 0;
        mCurrentChapterIndex = -1;
        mCurrentPage =-1;
    }

    @Override
    public void commonInfoChange() {
        mBookReaderPresenter.initBookCommonInfo();
        mBookReader.notifyDataChange();
    }

    @Override
    public void sizeChange() {
        mBookReaderPresenter.initBookSpecialInfo();
        mBookReader.notifySizeChange();
    }
}
