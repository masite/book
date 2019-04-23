package com.hongguo.read.mvp.ui.book.chapter;

import android.content.Context;
import android.content.Intent;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.widget.recycler.RecyclerViewWithBar;
import com.hongguo.read.R;
import com.hongguo.read.adapter.ChapterAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.mvp.contractor.book.chapter.ChapterContractor;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.mvp.presenter.book.BookDiscountPresenter;
import com.hongguo.read.mvp.presenter.book.chapter.ChapterPresenter;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/15.
 */

public class ChapterActivity extends ActivityEx implements ChapterContractor.IView, BookDiscountContractor.IView, LoadingView.LoadingViewClickListener, ChapterAdapter.ChapterClickListener {

    private static final String INTENT_BOOK_ID    = "intent_book_id";
    private static final String INTENT_BOOK_FROM  = "intent_book_from";
    private static final String INTENT_BOOK_NAME  = "intent_book_name";
    private static final String INTENT_BOOK_COVER = "intent_book_Cover";

    @Inject
    ChapterPresenter mChapterPresenter;

    @Inject
    BookDiscountPresenter mBookDiscountPresenter;

    @BindView(R.id.chapter_list)
    RecyclerViewWithBar mChapterList;

    private String mBookId;
    private int    mBookFrom;
    private String mBookName;
    private String mBookCover;

    private boolean mNativeIsLimitFree;
    private boolean mIsFromNative;

    private int mFreeType = -1;

    private List<ChapterBean.Chapters> mItems;
    private ChapterAdapter             mChapterAdapter;

    public static void toActivity(Context context, String bookid, int bookFrom, String bookName, String bookCover) {
        Intent intent = new Intent(context, ChapterActivity.class);
        intent.putExtra(INTENT_BOOK_FROM, bookFrom);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        intent.putExtra(INTENT_BOOK_COVER, bookCover);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        setTitle("章节目录");

        mBookName = getIntent().getStringExtra(INTENT_BOOK_NAME);
        mBookCover = getIntent().getStringExtra(INTENT_BOOK_COVER);
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mBookFrom = getIntent().getIntExtra(INTENT_BOOK_FROM, Constants.BOOK_FROM.FROM_SLEF);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mChapterList, 1);

        mItems = new ArrayList<>();
        mChapterAdapter = new ChapterAdapter(mContext, mBookId, mBookFrom, mItems);
        mChapterAdapter.setChapterClickListner(this);
        mChapterList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mChapterList.setAdapter(mChapterAdapter);
        mChapterPresenter.bingView(this);
        mChapterPresenter.init(mBookId, mBookFrom, mBookName, mBookCover);

        mBookDiscountPresenter.bingView(this);
        mBookDiscountPresenter.init(mBookId, mBookFrom);
        mBookDiscountPresenter.loading();
    }

    @ViewMethod
    public void nativeChapters(List<ChapterBean.Chapters> items) {
        if (items.size() != 0) {
            mItems.clear();
            mItems.addAll(items);
            mChapterAdapter.notifyChange();
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
            //查询后台查询全部章节
            mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", true);
        }
    }

    @ViewMethod
    public void setChapters(List<ChapterBean.Chapters> items) {
        mItems.clear();
        mItems.addAll(items);
        mChapterAdapter.notifyChange();
        if (mFreeType != -1 && mFreeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE) {
            LogUtils.log(this, "章节保存后发现是限免，修改本地数据库信息");
            mChapterPresenter.refreshChapterLimitType(mBookId, mBookFrom, mFreeType);
        }
    }

    @Override
    public void setPageChapters(List<ChapterBean.Chapters> items) {

    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mChapterPresenter.init(mBookId, mBookFrom, mBookName, mBookCover);
    }

    @Override
    public void setDiscountInfo(int freeType) {
        mFreeType = freeType;
        mChapterAdapter.setFree(mBookDiscountPresenter.getUserCanReadByFreeType(freeType));
        mChapterAdapter.notifyDataSetChanged();
        mChapterPresenter.refreshChapterLimitType(mBookId, mBookFrom, freeType);
        //只要有一方是免费的，刷新章节列表 mNativeIsLimitFree 已经刷新过
        if (mIsFromNative && (!mNativeIsLimitFree && freeType == Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE)) {
            LogUtils.log(this, "章节从本地取，本地不是免费，但是服务器是免费，刷新章节列表");
            mChapterPresenter.queryAllChapter(mBookId, mBookName, mBookCover, "", false);
        }
    }

    @Override
    public void chapterClick(ChapterBean.Chapters chapters, int chapterIndex) {
        BookReaderActivity.toActivity(mContext, mBookId, mBookFrom, mBookName, mBookCover, chapterIndex, 0, "");
    }
}
