package com.hongguo.read.mvp.ui.book.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.widget.recycler.RecyclerViewWithBar;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookReaderChapterAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.contractor.book.detail.BookReaderChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.mvp.presenter.book.BookDiscountPresenter;
import com.hongguo.read.mvp.presenter.book.chapter.ChapterPresenter;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.downdialog.DownLoadAllDialog;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/17.
 * 与 {@link BookReaderActivity} 数据交互
 */

public class BookReaderChapterFragment extends FragmentEx implements BookReaderChapterContractor.IView, ChapterContractor.IView, BookDiscountContractor.IView, LoadingView.LoadingViewClickListener, DownLoadAllDialog.DownLoadAllDialogChooseListener {

    private static final String INTENT_BOOK_ID    = "intent_book_id";
    private static final String INTENT_BOOK_FROM  = "intent_book_from";
    private static final String INTENT_BOOK_NAME  = "intent_book_name";
    private static final String INTENT_BOOK_COVER = "intent_book_Cover";

    @Inject
    ChapterPresenter      mChapterPresenter;
    @Inject
    BookDiscountPresenter mBookDiscountPresenter;

    @BindView(R.id.chapter_list)
    RecyclerViewWithBar mChapterList;
    @BindView(R.id.chapter_root)
    LinearLayout        mChapterRoot;
    @BindView(R.id.title)
    TextView            mTitle;
    @BindView(R.id.down_all)
    TextView            mDownAll;
    @BindView(R.id.chapter_content)
    LinearLayout        mChapterContent;

    private List<ChapterBean.Chapters> mItems;
    private BookReaderChapterAdapter   mChapterAdapter;

    private String            mBookId;
    private int               mBookFrom;
    private String            mBookName;
    private String            mBookCover;
    private DownLoadAllDialog mDownLoadAllDialog;
    private int               mBackgroundResource;
    private int               mBackgroundColor;
    private boolean           mNativeIsLimitFree;
    private boolean           mIsFromNative;
    private int mTextColor = 0xff000000;

    private int mFreeType = -1;
    private LinearLayoutManager mVerticalLinearManager;
    private int mUserCoin = -1;
    private int mGiveCoin = -1;

    public static BookReaderChapterFragment getInstance(String bookid, int bookFrom, String bookName, String bookCover) {
        BookReaderChapterFragment bookReaderChapterFragment = new BookReaderChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_BOOK_FROM, bookFrom);
        bundle.putString(INTENT_BOOK_ID, bookid);
        bundle.putString(INTENT_BOOK_NAME, bookName);
        bundle.putString(INTENT_BOOK_COVER, bookCover);
        bookReaderChapterFragment.setArguments(bundle);
        return bookReaderChapterFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_book_reader_chapter;
    }


    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        mBookId = getArguments().getString(INTENT_BOOK_ID);
        mBookName = getArguments().getString(INTENT_BOOK_NAME);
        mBookCover = getArguments().getString(INTENT_BOOK_COVER);
        mBookFrom = getArguments().getInt(INTENT_BOOK_FROM, Constants.BOOK_FROM.FROM_SLEF);

        mDownLoadAllDialog = new DownLoadAllDialog(mContext);
        mDownLoadAllDialog.setDownLoadAllDialogChooseListener(this);
        mDownLoadAllDialog.setTotalCoin(mUserCoin, mGiveCoin, mBookFrom == Constants.BOOK_FROM.FROM_SLEF);


        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mChapterContent, 1);

        mTitle.setText(mBookName);

        mItems = new ArrayList<>();
        mChapterAdapter = new BookReaderChapterAdapter(mContext, mItems, mBookId, mBookFrom);
        mChapterAdapter.setTextColor(mTextColor);
        mVerticalLinearManager = RecyclerLayoutUtils.createVertical(mContext);
        mChapterList.setLayoutManager(mVerticalLinearManager);
        mChapterList.setAdapter(mChapterAdapter);

        if (mBackgroundResource != 0 || mBackgroundColor != 0) {
            setChapterBackground(mBackgroundResource, mBackgroundColor);
        }

        //查询章节信息
        mChapterPresenter.bingView(this);
        mChapterPresenter.init(mBookId, mBookFrom, mBookName, mBookCover);

        //查询书籍是否免费的
        mBookDiscountPresenter.bingView(this);
        mBookDiscountPresenter.init(mBookId, mBookFrom);
        mBookDiscountPresenter.loading();
    }


    /**
     * 本地信息查询完成回调
     * 先查询本地 -- > 存在 ->检测更新
     * 不存在 -> 拉取第一页数据 --> 拉取全部数据
     *
     * @param items
     */
    @Override
    public void nativeChapters(List<ChapterBean.Chapters> items) {
        if (items.size() != 0) {
            mItems.clear();
            mItems.addAll(items);
            mChapterAdapter.notifyChange();
            ((BookReaderActivity) getActivity()).setChapters(items, true);
            mIsFromNative = true;
            //限免,直接刷新
            if (items.get(0).limitType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
                mNativeIsLimitFree = true;
                //查询后台查询全部章节
                mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", false);
                LogUtils.log(this, "本地限免书籍___刷新章节列表");
            } else {
                LogUtils.log(this, "检查章节更新信息");
                mChapterPresenter.checkUpate(mBookId, mBookName, items.get(0).updateTime, mBookCover);
            }
        } else {
            LogUtils.log(this, "本地无数据，直接从网络获取数据");
            mIsFromNative = false;
            ((BookReaderActivity) getActivity()).setChapters(new ArrayList<>(), true);
            mChapterPresenter.queryChapter(mBookId);
        }
    }

    /**
     * 查询出的一页数据
     * 查询成功后查询整本书的章节信息
     *
     * @param items
     */
    @Override
    public void setPageChapters(List<ChapterBean.Chapters> items) {
        ((BookReaderActivity) getActivity()).setChapters(items, false);
        //查询后台查询全部章节
        mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", false);
    }

    /**
     * 全部的章节信息
     *
     * @param items
     */
    @Override
    public void setChapters(List<ChapterBean.Chapters> items) {
        mChapterAdapter.setDataBeen(items);
        mChapterAdapter.notifyChange();
        ((BookReaderActivity) getActivity()).setChapters(items, false);
        if (mFreeType != -1 && mFreeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
            LogUtils.log(this, "章节保存后发现是限免，修改本地数据库信息");
            mChapterPresenter.refreshChapterLimitType(mBookId, mBookFrom, mFreeType);
        }
    }


    public void setBackgroundResource(int resource, int color) {
        mBackgroundResource = resource;
        mBackgroundColor = color;
        if (mChapterList != null) {
            setChapterBackground(resource, color);
        }
    }

    public void notifyAdapter() {
        if (mChapterAdapter != null)
            mChapterAdapter.notifyDataSetChanged();
    }

    private void setChapterBackground(int backgroundResource, int color) {
        if (backgroundResource != 0) {
            mChapterContent.setBackgroundResource(backgroundResource);
        } else {
            mChapterContent.setBackgroundColor(color);
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mChapterPresenter.init(mBookId, mBookFrom, mBookName, mBookCover);
    }

    @Override
    public void setDiscountInfo(int freeType) {
        mFreeType = freeType;
        mChapterAdapter.setFree(mBookDiscountPresenter.getUserCanReadByFreeType(freeType));
        mDownLoadAllDialog.setTimeFree(mBookDiscountPresenter.getUserCanReadByFreeType(freeType));
        mChapterAdapter.notifyDataSetChanged();
        boolean isFree = mBookDiscountPresenter.getUserCanReadByFreeType(freeType);
        String userBuyDiscountDescribe = mBookDiscountPresenter.getUserBuyDiscountDescribe(freeType);
        boolean describe = TextUtils.isEmpty(userBuyDiscountDescribe);
        ((BookReaderActivity) getActivity()).setVipShowInfo(userBuyDiscountDescribe, !(isFree || describe));

        mChapterPresenter.refreshChapterLimitType(mBookId, mBookFrom, freeType);
        //只要有一方是免费的，刷新章节列表 mNativeIsLimitFree 已经刷新过
        if (mIsFromNative && (!mNativeIsLimitFree && freeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE)) {
            LogUtils.log(this, "章节从本地取，本地不是免费，但是服务器是免费，刷新章节列表");
            mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", false);
        }
    }

    @OnClick(R.id.down_all)
    void downAll() {
        mDownLoadAllDialog.show(mBookId, mBookFrom);
    }

    /**
     * {@link BookReaderActivity}
     * 设置用户红果币
     */
    public void setUserCoin(int userCoin, int giveCoin) {
        mUserCoin = userCoin;
        mGiveCoin = giveCoin;
        if (mDownLoadAllDialog != null)
            mDownLoadAllDialog.setTotalCoin(userCoin, giveCoin, mBookFrom == Constants.BOOK_FROM.FROM_SLEF);
    }

    /**
     * 批量下载弹窗
     * {@link DownLoadAllDialog}
     *
     * @param chapters
     */
    @Override
    public void downLoadAll(List<ChapterBean.Chapters> chapters) {
        if (chapters.size() == 0) {
            toastMessage("没有需要章节");
            return;
        }
        mDownAll.setText("等待下载");
        ((BookReaderActivity) getActivity()).downChapters(chapters);
    }

    @Override
    public void refreshCoin() {
        ((BookReaderActivity) getActivity()).refreshCoin();
    }

    @Override
    public void toRecharge() {
        ((BookReaderActivity) getActivity()).closeMenu();
        RechargeActivity.toActivity(mContext);
    }

    public void updateChapter() {
        mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", false);
    }

    public void setSelectedIndex(int index) {
        mChapterAdapter.setSelectedIndex(index);
    }

    /**
     * 设置下载进度
     *
     * @param percent
     */
    public void setPrecent(int percent) {
        if (percent == -1) return;
        if (mDownAll != null) {
            if (percent != 100) {
                mDownAll.setEnabled(false);
                mDownAll.setText("下载中 (" + percent + "%)");
            } else {
                mDownAll.setEnabled(true);
                mDownAll.setText("批量下载");
                mChapterAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.location)
    public void location() {
        mChapterList.smoothScrollToPosition(mChapterAdapter.getSelectedIndex());
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        if (mChapterAdapter != null) {
            mChapterAdapter.setTextColor(textColor);
            mChapterAdapter.notifyDataSetChanged();
        }
    }
}
