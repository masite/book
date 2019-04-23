package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.utils.RecyclerScrollTransHelper;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookDetailAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.AddShelfSuccessEvent;
import com.hongguo.read.eventbus.DiscussTitleClickEvent;
import com.hongguo.read.eventbus.WriteDiscussEvent;
import com.hongguo.read.eventbus.WriteReplyEvent;
import com.hongguo.read.mvp.contractor.book.BookDiscountContractor;
import com.hongguo.read.mvp.contractor.book.detail.BookDetailContractor;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.presenter.book.BookDiscountPresenter;
import com.hongguo.read.mvp.presenter.book.detail.BookDetailPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.mvp.ui.book.chapter.DownChapterActivity;
import com.hongguo.read.mvp.ui.discuss.AllDiscussActivity;
import com.hongguo.read.mvp.ui.discuss.WriteDiscussForBookActivity;
import com.hongguo.read.utils.AppUtils;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.BaiDuTimeOutDialog;
import com.hongguo.read.widget.CenterAlignImageSpan;
import com.hongguo.read.widget.TransActionRelativeLayout;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2017/12/25.
 */

@Route(path = AppRouter.BOOK_DETAIL)
public class BookDetailActivity extends ActivityEx implements BookDetailContractor.IView, BookDiscountContractor.IView, RecyclerScrollTransHelper.ScrollTransProgressListener, LoadingView.LoadingViewClickListener {

    public static final String INTENT_BOOK_ID   = "intent_book_id";
    public static final String INTENT_BOOK_TYPE = "intent_book_type";

    @Inject
    BookDetailPresenter   mBookDetailPresenter;
    @Inject
    BookDiscountPresenter mBookDiscountPresenter;

    @BindView(R.id.book_detail)
    RecyclerView              mBookDetail;
    @BindView(R.id.tool_bg)
    ImageView                 mToolBg;
    @BindView(R.id.detail_tool)
    RelativeLayout            mDetailTool;
    @BindView(R.id.book_detail_panel)
    LinearLayout              mBookDetailPanel;
    @BindView(R.id.book_name)
    TextView                  mBookName;
    @BindView(R.id.time_out_label)
    TextView                  mTimeOutLabel;
    @BindView(R.id.start_read)
    TextView                  mStartRead;
    @BindView(R.id.add_bookshelf)
    TextView                  mAddBookShelf;
    @BindView(R.id.label_time_out)
    LinearLayout              mLabelTimeOut;
    @BindView(R.id.trans_toolbar_layer)
    TransActionRelativeLayout mTransActionRelativeLayout;

    private BookDetailAdapter         mBookDetailAdapter;
    private BookDetailHeaderHelper    mBookDetailHeaderHelper;
    private RecyclerScrollTransHelper mScrollTransHelper;
    private BookDetailBean            mBookDetailBean;
    private BookDiscussBean           mBookDiscussBean;

    @Autowired(name = "bookid")
    String mBookId;
    @Autowired(name = "bookfrom")
    int    mBookType;

    public static void toActivity(Context context, String bookid, int bookType) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_TYPE, bookType);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);

        getSupportActionBar().hide();
        mToolLayer.setVisibility(View.GONE);
        mBookName.setText("书籍详情");

        if (TextUtils.isEmpty(mBookId)) {
            mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
            mBookType = getIntent().getIntExtra(INTENT_BOOK_TYPE, 0);
        }

        mLabelTimeOut.setVisibility(mBookType == Constants.BOOK_FROM.FROM_BAIDU ? View.VISIBLE : View.GONE);

        mBookDetailHeaderHelper = new BookDetailHeaderHelper(mContext);

        mBookDetail.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mBookDetailAdapter = new BookDetailAdapter(mContext, mBookId, mBookType);
        mBookDetail.setAdapter(mBookDetailAdapter);

        mBookDetailAdapter.addHeader(mBookDetailHeaderHelper.getHeaderView());
        mBookDetailPanel.setVisibility(View.GONE);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mBookDetail, 1);
        mToolBg.setAlpha(1f);

        mBookDetailPresenter.bingView(this);
        mBookDetailPresenter.init(mBookId, mBookType);
        mBookDetailPresenter.loading();

        mBookDiscountPresenter.bingView(this);
        mBookDiscountPresenter.init(mBookId, mBookType);
        mBookDiscountPresenter.loading();

        mScrollTransHelper = new RecyclerScrollTransHelper(mBookDetail);
        mScrollTransHelper.setArmView(mTransActionRelativeLayout);
        mScrollTransHelper.setScrollView(mBookDetailHeaderHelper.getBookTitleContentView());

        String timeOutLabel = mTimeOutLabel.getText().toString();
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_baidu_time_out);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenterAlignImageSpan centerAlignImageSpan = new CenterAlignImageSpan(drawable);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(timeOutLabel);
        spannableStringBuilder.setSpan(centerAlignImageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTimeOutLabel.setText(spannableStringBuilder);


        ViewGroup.LayoutParams layoutParams = mDetailTool.getLayoutParams();
        layoutParams.height = AppUtils.getActionBarHeight(mContext);
        if(mBookType == Constants.BOOK_FROM.FROM_BAIDU){
            showTimeOut();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mBookDetailPresenter.queryBookReadInfo(mBookId, mBookType + "");
    }

    @ViewMethod
    public void setBookDetailCommonInfo(BookDetailBean bookDetailCommonInfo) {

        StatisticsUtils.bookDetail(bookDetailCommonInfo.data.bookName);
        mToolBg.setAlpha(0f);
        mScrollTransHelper.setScrollTransProgressListener(this);
        mBookDetailPanel.setVisibility(View.VISIBLE);
        mBookName.setText(bookDetailCommonInfo.data.bookName);
        mBookDetailHeaderHelper.setBookDetailCommonInfo(bookDetailCommonInfo, mBookType);
        mBookDetailBean = bookDetailCommonInfo;
    }

    @ViewMethod
    public void setBookVipDescribe(String vipDescribe) {
        mBookDetailHeaderHelper.setVipDescribe(vipDescribe);
    }

    @ViewMethod
    public void setFavorInfo(boolean hasInSelf) {
        if (hasInSelf) {
            mAddBookShelf.setText("已添加");
            mAddBookShelf.setEnabled(false);
        } else {
            mAddBookShelf.setText("+书架");
            mAddBookShelf.setEnabled(true);
        }
    }

    @OnClick(R.id.add_bookshelf)
    public void addBookShelf() {
        mBookDetailPresenter.addBookShelf(mBookId, mBookType, mBookDetailBean);
        mAddBookShelf.setEnabled(false);
        mAddBookShelf.setText("已添加");
    }

    @ViewMethod
    public void setBookDiscussInfo(BookDiscussBean bookDiscussInfo) {
        mBookDetailAdapter.setBookDiscussBean(bookDiscussInfo);
        mBookDetailAdapter.notifyChange();
        mBookDiscussBean = bookDiscussInfo;
    }

    @ViewMethod
    public void setRewardTop(AwardRankBean awardRankBean) {
        mBookDetailAdapter.setAwardRankBean(awardRankBean);
        mBookDetailAdapter.notifyChange();
    }

    @ViewMethod
    public void setSimilar(SimilarBookBean similar) {
        mBookDetailAdapter.setSimilarBookBean(similar);
        mBookDetailAdapter.notifyChange();
    }

    @ViewMethod
    public void setAuthor(BookAuthorBean author) {
        mBookDetailAdapter.setAuthorBean(author);
        mBookDetailAdapter.notifyChange();
    }

    @OnClick(R.id.down_chapter)
    public void downChapter() {
        DownChapterActivity.toActivity(mContext, mBookId, mBookType, mBookDetailBean.data.bookName, mBookDetailBean.data.coverPicture);
    }

    @Override
    public void progressChange(int percent) {
        mToolBg.setAlpha(percent / 100f);
    }


    @Override
    protected void onDestroy() {
        mScrollTransHelper.destory();
        mBookDetailHeaderHelper.destory();
        super.onDestroy();
    }

    /**
     * {@link BookDetailDiscussHelper}
     *
     * @param discussTitleClickEvent
     */
    @Subscribe
    public void onEvent(DiscussTitleClickEvent discussTitleClickEvent) {
        if (discussTitleClickEvent.bookId.equals(mBookId)) {
            AllDiscussActivity.toActivity(mContext, mBookId, mBookType, mBookDetailBean.data.coverPicture, mBookDetailBean.data.bookName, mBookDetailBean.data.author, mBookDiscussBean.pager.total);
        }
    }

    /**
     * 没有评论点击去评论
     * {@link BookDetailDiscussHelper}
     *
     * @param writeDiscussEvent
     */
    @Subscribe
    public void onEvent(WriteDiscussEvent writeDiscussEvent) {
        if (writeDiscussEvent.bookId.equals(mBookId))
            WriteDiscussForBookActivity.toActivity(mContext, mBookId, mBookType, mBookDetailBean.data.bookName);
    }

    /**
     * 回复评论成功后
     * {@link com.hongguo.read.mvp.presenter.discuss.ReplyPresenter}
     * {@link com.hongguo.read.mvp.presenter.discuss.WriteDiscussForBookPresenter}
     *
     * @param writeReplyEvent
     */
    @Subscribe
    public void onEvent(WriteReplyEvent writeReplyEvent) {
        mBookDetailPresenter.queryBookDiscuss(mBookId, mBookType + "");
    }

    @Override
    @ViewMethod
    public void setDiscountInfo(int freeType) {
        mBookDetailHeaderHelper.setVipDescribe(mBookDiscountPresenter.getBookDiscountDescribe(freeType));
    }


    @OnClick(R.id.to_shelf)
    void toShelf() {
        MainActivity.toActivity(mContext, 0);
    }

    @OnClick(R.id.view_back)
    void back() {
        finish();
    }


    @OnClick(R.id.start_read)
    void startToRead() {
        StatisticsUtils.bookDetail("去阅读" + mBookName);
        if (mBookDetailBean == null) return;
        BookReaderActivity.toActivity(mContext, mBookId, mBookType, mBookDetailBean.data.bookName, mBookDetailBean.data.coverPicture, mBookDetailBean.data.author);
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookDetailPresenter.loading();
        mBookDiscountPresenter.loading();
    }

    @Subscribe
    public void onEvent(AddShelfSuccessEvent addShelfSuccessEvent) {
        if (addShelfSuccessEvent.mBookId.equals(mBookId)) {
            mAddBookShelf.setText("已添加");
            mAddBookShelf.setEnabled(false);
        }
    }

    @ViewMethod
    public void setBookHasRead(boolean hasRead) {
        if (hasRead) {
            mStartRead.setText("继续阅读");
        } else {
            mStartRead.setText("开始阅读");
        }
    }

    private void showTimeOut() {
        new BaiDuTimeOutDialog(mContext).show();
    }
}
