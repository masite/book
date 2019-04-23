package com.hongguo.read.mvp.ui.discuss;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.CommonImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by losg on 2017/12/29.
 */

public class AllDiscussTitleHelper {

    @BindView(R.id.book_cover)
    CommonImageView  mBookCover;
    @BindView(R.id.book_name)
    TextView         mBookName;
    @BindView(R.id.book_author)
    TextView         mBookAuthor;
    @BindView(R.id.book_title)
    ConstraintLayout mBookTitle;
    @BindView(R.id.discuss_info)
    TextView         mDiscussInfo;
    private Context  mContext;
    private Unbinder mBind;
    private final View mView;

    public AllDiscussTitleHelper(Context context) {
        mContext = context;
        mView = View.inflate(mContext, R.layout.view_all_discuss_title, null);
        mBind = ButterKnife.bind(this, mView);
    }

    public View getHeaderView(){
        return mView;
    }

    public void setTitleInfo(String bookCover, String bookName, String bookAuther, String discussNumber) {
        ImageLoadUtils.loadUrl(mBookCover, bookCover);
        mBookName.setText(bookName);
        mBookAuthor.setText(bookAuther);
        mDiscussInfo.setText(discussNumber+"人评论");

        if(TextUtils.isEmpty(bookAuther)){
            mBookAuthor.setVisibility(View.GONE);
        }
    }

    public void setDiscussNumber(String discussNumber){
        mDiscussInfo.setText(discussNumber+"人评论");
    }

    public void destory() {
        mBind.unbind();
    }
}
