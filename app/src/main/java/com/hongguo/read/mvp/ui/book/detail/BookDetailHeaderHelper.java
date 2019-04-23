package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.utils.TimeUtils;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.ui.book.chapter.ChapterActivity;
import com.hongguo.read.mvp.ui.reward.UserRewardActivity;
import com.hongguo.read.mvp.ui.vip.VipActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.BookTitleContentView;
import com.hongguo.read.widget.CollapsibleTextView;
import com.hongguo.read.widget.CommonImageView;
import com.hongguo.read.widget.FollowWeixinPublicDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailHeaderHelper {

    @BindView(R.id.bar_space)
    LinearLayout         mBarSpace;
    @BindView(R.id.book_title_content)
    BookTitleContentView mBookTitleContentView;
    @BindView(R.id.book_cover)
    CommonImageView      mBookCover;
    @BindView(R.id.book_author)
    TextView             mBookAuthor;
    @BindView(R.id.book_type)
    TextView             mBookType;
    @BindView(R.id.text_number)
    TextView             mTextNumber;
    @BindView(R.id.book_status)
    TextView             mBookStatus;
    @BindView(R.id.vip_describe)
    TextView             mVipDescribe;
    @BindView(R.id.user_award)
    TextView             mUserAward;
    @BindView(R.id.user_read)
    TextView             mUserRead;
    @BindView(R.id.vip_books)
    TextView             mVipBooks;
    @BindView(R.id.concern_us)
    TextView             mConcernUs;
    @BindView(R.id.book_descibe)
    CollapsibleTextView  mBookDescibe;
    @BindView(R.id.last_chapter)
    TextView             mLastChapter;

    @BindView(R.id.last_update_chapter_name_time)
    TextView             mLastUpdateChapterNameTime;

    private Context                  mContext;
    private View                     mBookDetailHeader;
    private Unbinder                 mBind;
    private BookDetailBean           mBookDetailCommonInfo;
    private int                      mBookFrom;
    public  FollowWeixinPublicDialog mFollowWeixinPublicDialog;


    public BookDetailHeaderHelper(Context context) {
        mContext = context;
        mBookDetailHeader = View.inflate(mContext, R.layout.view_bookdetail_title, null);
        mFollowWeixinPublicDialog = new FollowWeixinPublicDialog(mContext);
        mBind = ButterKnife.bind(this, mBookDetailHeader);
    }

    public View getHeaderView() {
        return mBookDetailHeader;
    }

    public BookTitleContentView getBookTitleContentView() {
        return mBookTitleContentView;
    }

    public void setBookDetailCommonInfo(BookDetailBean bookDetailCommonInfo, int bookFrom) {
        mBookFrom = bookFrom;
        mBookDetailCommonInfo = bookDetailCommonInfo;
        ImageLoadUtils.loadUrl(mBookCover, bookDetailCommonInfo.data.coverPicture);
        if (mBookTitleContentView.getBookBgImage() != null)
            ImageLoadUtils.loadUrlWithGradient(mBookTitleContentView.getBookBgImage(), bookDetailCommonInfo.data.coverPicture);
        mBookAuthor.setText(bookDetailCommonInfo.data.author);
        mBookType.setText(bookDetailCommonInfo.data.categoryName);
        mTextNumber.setText(bookDetailCommonInfo.data.wordsNumber);
        mBookStatus.setText(bookDetailCommonInfo.data.statusStr);
        mUserRead.setText(bookDetailCommonInfo.data.readersStr + "人阅读");
        mBookDescibe.setText(bookDetailCommonInfo.data.desc.trim());
        mLastChapter.setText(bookDetailCommonInfo.data.lastUpdateChapterName);
        mLastUpdateChapterNameTime.setText(TimeUtils.getTime(bookDetailCommonInfo.data.lastUpdateTime, false));
    }


    public void setVipDescribe(String vipDescribe) {
        mVipDescribe.setVisibility(View.VISIBLE);
        mVipDescribe.setText(vipDescribe);
    }

    public void destory() {
        mBind.unbind();
    }

    @OnClick(R.id.last_update_chapter_layer)
    void showChapter() {
        ChapterActivity.toActivity(mContext, mBookDetailCommonInfo.data.bookId, mBookFrom, mBookDetailCommonInfo.data.bookName, mBookDetailCommonInfo.data.coverPicture);
    }

    @OnClick(R.id.user_award)
    public void award(){
        UserRewardActivity.toActivity(mContext, mBookDetailCommonInfo.data.bookId,mBookDetailCommonInfo.data.bookName ,mBookFrom, mBookDetailCommonInfo.data.coverPicture);
    }

    @OnClick(R.id.concern_us)
    void concernUs(){
        mFollowWeixinPublicDialog.show();
    }

    @OnClick(R.id.vip_books)
    public void vipBooks(){
        VipActivity.toActivity(mContext);
    }
}
