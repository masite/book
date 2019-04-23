package com.hongguo.read.mvp.ui.discuss;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.discuss.WriteDiscussForBookContractor;
import com.hongguo.read.mvp.presenter.discuss.WriteDiscussForBookPresenter;
import com.hongguo.read.widget.emoji.EmojiView;
import com.hongguo.read.widget.emoji.EmojiconEditText;
import com.losg.library.utils.InputMethodUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2017/12/31.
 */

public class WriteDiscussForBookActivity extends ActivityEx implements WriteDiscussForBookContractor.IView {

    @Inject
    WriteDiscussForBookPresenter mWriteDiscussForBookPresenter;

    private static final String INTENT_BOOK_ID   = "book_id";
    private static final String INTENT_BOOK_TYPE = "book_type";
    private static final String INTENT_BOOK_NAME = "book_name";
    @BindView(R.id.emoji_edit)
    EmojiconEditText mEmojiEdit;
    @BindView(R.id.show_emoji)
    ImageView        mShowEmoji;
    @BindView(R.id.emoji_view)
    EmojiView        mEmojiView;

    private String mBookId;
    private int    mBookType;
    private String mBookName;

    public static void toActivity(Context context, String bookid, int bookType, String bookName) {
        Intent intent = new Intent(context, WriteDiscussForBookActivity.class);
        intent.putExtra(INTENT_BOOK_ID, bookid);
        intent.putExtra(INTENT_BOOK_TYPE, bookType);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_write_discuss_for_book;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("写评论");
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mBookType = getIntent().getIntExtra(INTENT_BOOK_TYPE, 0);
        mBookName = getIntent().getStringExtra(INTENT_BOOK_NAME);

        mWriteDiscussForBookPresenter.bingView(this);
        mWriteDiscussForBookPresenter.loading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem submit = menu.add(0, 0, 0, "提交");
        submit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            submit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        InputMethodUtils.hideInputMethod(this);
        mWriteDiscussForBookPresenter.sumbit(mBookId, mBookName, mBookType, mEmojiEdit.getText().toString(), mEmojiEdit.getText().toString());
    }

}
