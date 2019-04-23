package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hongguo.read.R;
import com.losg.library.utils.InputMethodUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by losg on 2017/12/30.
 */

public class EmojiView extends LinearLayout implements EmojiAdapter.EmojiClikcListener, View.OnLongClickListener, EmojiconEditText.EditClickUp, View.OnClickListener {

    private static final int EMOJI_ROW_NUMBER     = 6;
    private static final int DEFAULT_EMOJI_HEIGHT = 200;

    private AppCompatActivity mCurrentActivity;
    private View              mRootView;
    private EmojiconEditText  mEmojiconEditText;
    private RecyclerView      mEmojiIconList;
    private ImageView         mShowEmoji;
    private boolean mFirstInit = true;
    private int mLastTop;
    private int mSoftHeight;
    private int[] mViewLocal;

    public EmojiView(Context context) {
        this(context, null);
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewLocal = new int[2];
    }

    private void initView() {
        mCurrentActivity = (AppCompatActivity) getContext();
        mRootView = mCurrentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        mShowEmoji = (ImageView) mRootView.findViewById(R.id.show_emoji);
        mEmojiconEditText = (EmojiconEditText) mRootView.findViewById(R.id.emoji_edit);

        int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_EMOJI_HEIGHT, getResources().getDisplayMetrics());

        mEmojiIconList = new RecyclerView(getContext());
        mEmojiIconList.setLayoutParams(new LayoutParams(-1, defaultHeight));
        mEmojiIconList.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), EMOJI_ROW_NUMBER);
        mEmojiIconList.setLayoutManager(gridLayoutManager);
        mEmojiIconList.setVisibility(GONE);
        EmojiAdapter adapter = new EmojiAdapter(getContext());
        adapter.setEmojiClikcListener(this);
        mEmojiIconList.setAdapter(adapter);
        addView(mEmojiIconList);

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        mEmojiIconList.setPadding(0, margin, 0, margin);
        mEmojiIconList.setClipToPadding(false);
        mEmojiIconList.setClipChildren(false);

        mEmojiconEditText.setEditClickUp(this);
        mShowEmoji.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && mFirstInit) {
            mFirstInit = false;
            initView();
        }
    }

    //检测 软键盘的高度
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mLastTop != 0) {
                getLocationInWindow(mViewLocal);
                int dx = mLastTop - mViewLocal[1];
                if(dx != 0) {
                    mSoftHeight = dx;
                }
            } else {
               getLocationInWindow(mViewLocal);
                mLastTop = mViewLocal[1];
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        super.onDetachedFromWindow();
    }

    public CharSequence getEmojiText() {
        return mEmojiconEditText.getText();
    }

    @Override
    public void emojiClick(String str) {
        Editable text = mEmojiconEditText.getText();
        int selectionStart = mEmojiconEditText.getSelectionStart();
        text.insert(selectionStart, str);
    }


    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    public void editUp() {
        mCurrentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (mEmojiIconList.getVisibility() == View.GONE) {
            return;
        }

        mCurrentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        new Handler().postDelayed(() -> {
            mCurrentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mEmojiIconList.setVisibility(View.GONE);
        }, 300);
    }

    public boolean onBackPress(){
        if(mEmojiIconList.getVisibility() != View.GONE){
            mEmojiIconList.setVisibility(GONE);
            return true;
        }
        int[] local = new int[2];
        getLocationInWindow(local);
        if (local[1] < getResources().getDisplayMetrics().heightPixels * 2 / 3) {
            //有键盘弹出，取消键盘，显示recyclerview
            InputMethodUtils.hideInputMethod(mCurrentActivity);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (mSoftHeight != 0) {
            ViewGroup.LayoutParams layoutParams = mEmojiIconList.getLayoutParams();
            layoutParams.height = mSoftHeight;
            mEmojiIconList.setLayoutParams(layoutParams);
        }
        mEmojiIconList.setVisibility(mEmojiIconList.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        int[] local = new int[2];
        getLocationInWindow(local);
        if (local[1] < getResources().getDisplayMetrics().heightPixels * 2 / 3) {
            //有键盘弹出，取消键盘，显示recyclerview
            mCurrentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            InputMethodManager inputMethodManager = (InputMethodManager) mCurrentActivity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mCurrentActivity.getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
