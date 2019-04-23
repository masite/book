package com.hongguo.read.mvp.ui.book;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.common.widget.recycler.GridCell;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookEndRecommendAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.book.BookEndRecommendContractor;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.presenter.book.BookEndRecommendPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.utils.RecyclerLayoutUtils;
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
 * Created by losg on 2018/4/12.
 */

public class BookEndRecommendActivity extends ActivityEx implements BookEndRecommendContractor.IView, BookDetailContractor.IView, LoadingView.LoadingViewClickListener {

    private static final String INTENT_BOOK_ID   = "intent_book_id";
    private static final String INTENT_BOOK_FROM = "intent_book_from";
    private static final String INTENT_BOOK_NAME = "intent_book_name";

    @BindView(R.id.recommend_book)
    RecyclerView mRecommendBook;

    @Inject
    BookEndRecommendPresenter mBookEndRecommendPresenter;

    private String mBookId;
    private int    mBookType;

    private List<SimilarBookBean.DataBean> mDates;
    private BookEndRecommendAdapter        mBookEndRecommendAdapter;

    public static void toActivity(Context context, String bookid, int bookFrom, String bookName) {
        Intent intent = new Intent(context, BookEndRecommendActivity.class);
        intent.putExtra(INTENT_BOOK_FROM, bookFrom);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_book_end_recommend;
    }

    @Override
    protected void inject(ActivityComponent mActivityComponent) {
        super.inject(mActivityComponent);
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra(INTENT_BOOK_NAME));
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mBookType = getIntent().getIntExtra(INTENT_BOOK_FROM, 0);

        mRecommendBook.setLayoutManager(RecyclerLayoutUtils.createTitleGridManager(mContext, 3));
        mDates = new ArrayList<>();
        mBookEndRecommendAdapter = new BookEndRecommendAdapter(mContext, mDates);
        View header = View.inflate(mContext, R.layout.view_book_end_recommend_header, null);
        mBookEndRecommendAdapter.addHeader(header);
        mRecommendBook.addItemDecoration(new GridCell(3, DisplayUtil.dip2px(mContext, 16), 1));
        mRecommendBook.setAdapter(mBookEndRecommendAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        bindLoadingView(loadingHelper, mRecommendBook, 1);
        loadingHelper.setLoadingViewClickListener(this);
        mBookEndRecommendPresenter.bingView(this);
        mBookEndRecommendPresenter.init(mBookId, mBookType);
        mBookEndRecommendPresenter.loading();
    }

    @Override
    public void setSimilar(SimilarBookBean similar) {
        mDates.clear();
        mDates.addAll(similar.data);
        mBookEndRecommendAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookEndRecommendPresenter.loading();
    }

    @OnClick(R.id.to_book_store)
    public void toBookStore() {
        MainActivity.toActivity(mContext, 1);
    }

    @OnClick(R.id.to_book_shelf)
    public void toBookShelf() {
        MainActivity.toActivity(mContext, 0);
    }


    @Override
    public void setAuthor(BookAuthorBean author) {

    }

    @Override
    public void setBookVipDescribe(String vipDescribe) {

    }

    @Override
    public void setFavorInfo(boolean hasInSelf) {

    }

    @Override
    public void setDiscountInfo(int freeType) {

    }

    @Override
    public void setBookHasRead(boolean hasRead) {

    }


    @Override
    public void setBookDetailCommonInfo(BookDetailBean bookDetailCommonInfo) {

    }

    @Override
    public void setBookDiscussInfo(BookDiscussBean bookDiscussInfo) {

    }

    @Override
    public void setRewardTop(AwardRankBean awardRankBean) {

    }

}
