package com.hongguo.read.mvp.ui.bookshelf;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.widget.skin.ISkinUpdate;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.CommonImageView;
import com.hongguo.read.widget.WaveView;
import com.hongguo.common.widget.skin.loader.SkinManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/11.
 */

public class BookShelfHeaderHelper extends BaseViewHelper implements ISkinUpdate {

    @BindView(R.id.image_header)
    CommonImageView mImageHeader;
    @BindView(R.id.book_name)
    TextView        mBookName;
    @BindView(R.id.read_progress)
    TextView        mReadProgress;
    @BindView(R.id.sign_info)
    TextView        mSignInfo;
    @BindView(R.id.sign)
    TextView        mSign;
    @BindView(R.id.sign_layer)
    LinearLayout    mSignLayer;
    @BindView(R.id.read_continue)
    TextView        mReadContinue;
    @BindView(R.id.wave)
    WaveView        mWave;
    @BindView(R.id.book_header_image)
    ImageView       mBookHeaderImage;

    private BookShelfHeaderClickListener mBookShelfHeaderClickListener;

    private String mBookCover;
    private String mBookId;
    private int    mBookType;

    public BookShelfHeaderHelper(Context context) {
        super(context);
    }

    public void setBookIdInfo(String bookId, int bookType) {
        mBookId = bookId;
        mBookType = bookType;
    }

    @Override
    protected int initLayout() {
        return R.layout.view_book_shelf_header;
    }

    public void initShelfHeader(String bookCover, String bookName, String readProcess) {

        mBookCover = bookCover;
        mSignInfo.setSelected(true);

        SkinManager.getInstance().attach(this);
        mBookName.setText(bookName);
        mReadProgress.setText(readProcess);
        mBookCover = bookCover;

        onThemeUpdate();
    }

    public void setSignInfo(String message, boolean sign) {
        mSignLayer.setVisibility(View.VISIBLE);
        mSignInfo.setText(message);
        if(sign){
            mSign.setText("已签到");
            mSign.setEnabled(false);
        }else{
            mSign.setText("签到");
            mSign.setEnabled(true);
        }
    }

    public void startWave() {
        mWave.startAnimation();
        mSignInfo.setSelected(true);
    }

    public void pauseWare() {
        mWave.pause();
        mSignInfo.setSelected(false);
    }

    @Override
    public void onThemeUpdate() {
        Drawable drawable = SkinResourcesUtils.getDrawable(R.mipmap.ic_bookshelf_title);
        if (SkinManager.getInstance().isExternalSkin()) {
            mBookHeaderImage.setImageDrawable(drawable);
        } else {
            ImageLoadUtils.loadUrlWithGradient(mBookHeaderImage, mBookCover);
        }
        ImageLoadUtils.loadUrl(mImageHeader, mBookCover);
    }

    @OnClick(R.id.read_continue)
    public void readContinue() {
        if (!TextUtils.isEmpty(mBookId))
            BookReaderActivity.toActivity(mContext, mBookId, mBookType, mBookName.getText().toString(), mBookCover, "");
    }


    @OnClick(R.id.sign)
    public void sign(){
        if(mBookShelfHeaderClickListener != null){
            mBookShelfHeaderClickListener.headerSignClick();
        }
    }

    public void setBookShelfHeaderClickListener(BookShelfHeaderClickListener bookShelfHeaderClickListener) {
        mBookShelfHeaderClickListener = bookShelfHeaderClickListener;
    }

    @Override
    public void destory() {
        super.destory();
        LogUtils.log("destory---");
    }

    public interface BookShelfHeaderClickListener {
        void headerSignClick();
    }
}
