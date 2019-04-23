package com.hongguo.read.mvp.ui.discuss;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hongguo.read.R;
import com.hongguo.read.adapter.ReplyAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.discuss.ReplyContractor;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.presenter.discuss.ReplyPresenter;
import com.hongguo.read.widget.emoji.EmojiView;
import com.hongguo.read.widget.emoji.EmojiconEditText;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.utils.InputMethodUtils;
import com.losg.library.widget.loading.BaLoadingView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/14.
 */

public class ReplyActivity extends ActivityEx implements ReplyContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    ReplyPresenter mReplyPresenter;

    private static final String INTENT_BOOK_ID      = "intent_book_id";
    private static final String INTENT_DISCUSS_INFO = "intent_discuss_info";

    @BindView(R.id.design_refresh)
    DesignRefreshRecyclerView mDesignRefresh;
    @BindView(R.id.emoji_edit)
    EmojiconEditText          mEmojiEdit;
    @BindView(R.id.emoji_view)
    EmojiView                 mEmojiView;

    private ReplyHeaderHelper mReplyHeaderHelper;
    private ReplyAdapter      mReplyAdapter;

    private String                   mBookid;
    private String                   mDiscussId;
    private BookDiscussBean.DataBean mDataBean;
    private View mLoadingFooter;

    public static void toActivity(Context context, String bookId, BookDiscussBean.DataBean dataBean) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra(INTENT_BOOK_ID, bookId);
        intent.putExtra(INTENT_DISCUSS_INFO, dataBean);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_reply;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("评论详情");

        mDataBean = getIntent().getParcelableExtra(INTENT_DISCUSS_INFO);
        mBookid = getIntent().getStringExtra(INTENT_BOOK_ID);
        mDiscussId = mDataBean.id;

        mReplyAdapter = new ReplyAdapter(mContext, mBookid);
        mDesignRefresh.setAdapter(mReplyAdapter);
        bindRefresh(mDesignRefresh);
        initHeader();
        View footer = initFooter();
        mReplyAdapter.addFooter(footer);
        mReplyAdapter.addHeader(mReplyHeaderHelper.getView());
        mReplyPresenter.bingView(this);
        mReplyPresenter.loading();
        mReplyPresenter.queryReply(mBookid, mDiscussId);
    }

    private View initFooter() {
        mLoadingFooter = View.inflate(mContext, R.layout.adpter_loading_footer, null);
        View loadingContent = mLoadingFooter.findViewById(R.id.loading_footer);
        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("暂时还没有回复", "");
        loadingHelper.setResultNullVisible(false);
        bindLoadingView(loadingHelper, loadingContent, 0);
        return mLoadingFooter;
    }

    private void initHeader() {
        mReplyHeaderHelper = new ReplyHeaderHelper(mContext);
        mReplyHeaderHelper.setDiscussInfo(mDataBean);
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
        mReplyAdapter.setLoadingStatus(status);
    }

    @ViewMethod
    public void setBookDiscussInfo(BookDiscussBean bookDiscussInfo) {
        mReplyAdapter.setBookDiscussBean(bookDiscussInfo);
        mReplyAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mReplyPresenter.queryReply(mBookid, mDiscussId);
    }

    @OnClick(R.id.reply)
    void reply() {
        InputMethodUtils.hideInputMethod(this);
        mReplyPresenter.reply(mBookid, mDiscussId, mEmojiEdit.getText().toString());
    }

    @ViewMethod
    public void repaySuccess(){
        mEmojiEdit.setText("");
    }

    @Override
    public void onBackPressed() {
        if(!mEmojiView.onBackPress()){
            super.onBackPressed();
        }
    }
}
