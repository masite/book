package com.hongguo.read.mvp.ui.discuss;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hongguo.read.R;
import com.hongguo.read.adapter.AllDiscussAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.WriteReplyEvent;
import com.hongguo.read.mvp.contractor.discuss.AllDiscussContractor;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.presenter.discuss.AllDiscussPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2017/12/28.
 */

public class AllDiscussActivity extends ActivityEx implements AllDiscussContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    AllDiscussPresenter mAllDiscussPresenter;

    private static final String INTENT_BOOK_ID            = "book_id";
    private static final String INTENT_BOOK_TYPE          = "book_type";
    private static final String INTENT_BOOK_COVER         = "book_cover";
    private static final String INTENT_BOOK_NAME          = "book_name";
    private static final String INTENT_BOOK_AUTHOR        = "book_author";
    private static final String INTENT_BOOK_DISCUSS_COUNT = "book_discuss_count";

    @BindView(R.id.discuss_all)
    DesignRefreshRecyclerView mDiscussAll;
    private AllDiscussAdapter     mAllDiscussAdapter;
    private AllDiscussTitleHelper mAllDiscussTitleHelper;

    private String mBookId;
    private int    mBookType;
    private View mLoadingFooter;

    public static void toActivity(Context context, String bookid, int bookType, String bookCover, String bookName, String bookAuthor, int discussCount) {
        Intent intent = new Intent(context, AllDiscussActivity.class);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_TYPE, bookType);
        intent.putExtra(INTENT_BOOK_COVER, bookCover);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        intent.putExtra(INTENT_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(INTENT_BOOK_DISCUSS_COUNT, discussCount);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_discuss_all;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        setTitle("评论详情");
        setStatusTrans();
        initIntent();

        mAllDiscussAdapter.addHeader(mAllDiscussTitleHelper.getHeaderView());
        View footer = initFooter();
        mAllDiscussAdapter.addFooter(footer);

        mDiscussAll.setAdapter(mAllDiscussAdapter);
        mDiscussAll.setRefreshListener(this);
        bindRefresh(mDiscussAll);

        mAllDiscussPresenter.bingView(this);
        mAllDiscussPresenter.loading();
        mAllDiscussPresenter.queryBookDiscuss(mBookId, mBookType + "");
    }

    private View initFooter() {
        mLoadingFooter = View.inflate(mContext, R.layout.adpter_loading_footer, null);
        View loadingContent = mLoadingFooter.findViewById(R.id.loading_footer);
        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("暂无评论内容", "");
        loadingHelper.setResultNullVisible(false);
        bindLoadingView(loadingHelper, loadingContent, 0);
        return mLoadingFooter;
    }


    @Override
    public void changeLoadingStatus(BaLoadingView.LoadingStatus status, int tag) {
        super.changeLoadingStatus(status, tag);
        ViewGroup.LayoutParams layoutParams = mLoadingFooter.getLayoutParams();
        if(layoutParams == null) return;
        if(status == BaLoadingView.LoadingStatus.LADING_SUCCESS) {
            layoutParams.height = 0;
        }else{
            layoutParams.height = -2;
        }
        mLoadingFooter.setLayoutParams(layoutParams);
        mAllDiscussAdapter.setLoadingStatus(status);
    }

    @ViewMethod
    public void setBookDiscussInfo(BookDiscussBean bookDiscussInfo) {
        mAllDiscussTitleHelper.setDiscussNumber(bookDiscussInfo.pager.total + "");
        mAllDiscussAdapter.setBookDiscussBean(bookDiscussInfo);
        mAllDiscussAdapter.notifyChange();
    }

    private void initIntent() {
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mBookType = getIntent().getIntExtra(INTENT_BOOK_TYPE, 0);
        String cover = getIntent().getStringExtra(INTENT_BOOK_COVER);
        String bookName = getIntent().getStringExtra(INTENT_BOOK_NAME);
        String bookAuthor = getIntent().getStringExtra(INTENT_BOOK_AUTHOR);
        int discussCount = getIntent().getIntExtra(INTENT_BOOK_DISCUSS_COUNT, 0);
        mAllDiscussAdapter = new AllDiscussAdapter(mContext, mBookId);
        mAllDiscussTitleHelper = new AllDiscussTitleHelper(mContext);
        mAllDiscussTitleHelper.setTitleInfo(cover, bookName, bookAuthor, discussCount + "");
    }

    @OnClick(R.id.write_discuss)
    void witeDiscuss(){
        WriteDiscussForBookActivity.toActivity(mContext, mBookId, mBookType, INTENT_BOOK_NAME);
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mAllDiscussPresenter.refresh();
    }

    /**
     * 回复评论成功后
     *{@link com.hongguo.read.mvp.presenter.discuss.ReplyPresenter}
     * {@link com.hongguo.read.mvp.presenter.discuss.WriteDiscussForBookPresenter}
     * @param writeReplyEvent
     */
    @Subscribe
    public void onEvent(WriteReplyEvent writeReplyEvent){
        mAllDiscussPresenter.refresh();
    }
}
