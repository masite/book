package com.hongguo.read.mvp.ui.book;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hongguo.common.utils.SeekBarProgressChange;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookReaderBackgroundAdapter;
import com.hongguo.read.mvp.model.book.BookReaderBackground;
import com.hongguo.read.repertory.share.BookRepertory;
import com.hongguo.read.utils.AppUtils;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.losg.library.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by losg on 2018/5/3.
 * 阅读控件 控制panel 操作
 */

public class BookReaderControlPanel implements BookReaderBackgroundAdapter.BackgroundClickListener {

    private Context  mContext;
    private Unbinder mBinder;

    @BindView(R.id.book_bg)
    RecyclerView     mBookBg;
    @BindView(R.id.sub_text_size)
    ImageView        mSubTextSize;
    @BindView(R.id.text_size)
    TextView         mTextSize;
    @BindView(R.id.anim_type)
    RadioGroup       mAnimType;
    @BindView(R.id.book_space_one)
    ImageView        mBookSpaceOne;
    @BindView(R.id.book_space_two)
    ImageView        mBookSpaceTwo;
    @BindView(R.id.book_space_three)
    ImageView        mBookSpaceThree;
    @BindView(R.id.default_book_space)
    TextView         mDefaultBookSpace;
    @BindView(R.id.word_size_default)
    TextView         mWordSizeDefault;
    @BindView(R.id.light_control)
    AppCompatSeekBar mLightControl;
    @BindView(R.id.default_light)
    TextView         mDefaultLight;
    @BindView(R.id.add_text_size)
    ImageView        mAddTextSize;

    private BookReaderBackgroundAdapter     mBookReaderBackgroundAdapter;
    private BookReaderControlChangeListener mBookReaderControlChangeListener;
    private boolean mFirstInitLight = true;
    private CompositeDisposable mSubscriptions;

    public BookReaderControlPanel(Context context, View controllerView, BookReaderControlChangeListener bookReaderControlChangeListener) {
        mContext = context;
        mBinder = ButterKnife.bind(this, controllerView);
        mSubscriptions = new CompositeDisposable();
        mBookReaderControlChangeListener = bookReaderControlChangeListener;
        initView();
    }

    private void initView() {
        //阅读页颜色背景初始化
        mBookBg.setLayoutManager(RecyclerLayoutUtils.createHorizontal(mContext));
        mBookReaderBackgroundAdapter = new BookReaderBackgroundAdapter(mContext);
        mBookReaderBackgroundAdapter.setBackgroundClickListener(this);
        mBookBg.setAdapter(mBookReaderBackgroundAdapter);
        initLightBar();
    }

    private void initLightBar() {
        int light = BookRepertory.getLight();
        initLight(light);
        mLightControl.setOnSeekBarChangeListener(new SeekBarProgressChange() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                super.onProgressChanged(seekBar, progress, fromUser);
                BookRepertory.setLight(progress);
                AppUtils.changeAppBrightness(mContext, progress);
            }

        });
    }

    public void initLight(int light) {
        if (mFirstInitLight && light != -1) {
            mFirstInitLight = false;
            Disposable disposable = RxJavaUtils.delayRun(1000, () -> {
                AppUtils.changeAppBrightness(mContext, light);
                mLightControl.setProgress(light == -1 ? AppUtils.getSystemLight(mContext) : light);
            });
            mSubscriptions.add(disposable);
        } else {
            AppUtils.changeAppBrightness(mContext, light);
        }
    }

    public void initBookCommonInfo(int animType, int backgroundIndex, boolean isModeNeight) {
        ((RadioButton) mAnimType.getChildAt(animType)).setChecked(true);
        mBookReaderBackgroundAdapter.setSelectedIndex(backgroundIndex);
    }

    public void initBookSpecialInfo(int textSize, int lineType) {
        mTextSize.setText(textSize + "");
        LinearLayout linearLayout = (LinearLayout) mBookSpaceOne.getParent();
        clearAllSpace();
        linearLayout.getChildAt(lineType + 1).setSelected(true);
    }

    @OnClick(R.id.sub_text_size)
    public void onSubTextSizeClicked() {
        int textSize = BookRepertory.getTextSize();
        textSize--;
        if (textSize < BookRepertory.getMinTextSize()) {
            textSize = BookRepertory.getMinTextSize();
        }
        BookRepertory.setTextSize(textSize);
        mBookReaderControlChangeListener.sizeChange();
    }

    @OnClick(R.id.add_text_size)
    public void onAddTextSizeClicked() {
        int textSize = BookRepertory.getTextSize();
        textSize++;
        if (textSize > BookRepertory.getMaxTextSize()) {
            textSize = BookRepertory.getMaxTextSize();
        }
        BookRepertory.setTextSize(textSize);
        mBookReaderControlChangeListener.sizeChange();
    }

    @OnClick(value = {R.id.anim_style_emulate, R.id.anim_style_cover, R.id.anim_style_up_down, R.id.anim_style_none})
    public void onAnimStyleEmulateClicked(View view) {
        ((RadioButton) view).setChecked(true);
        BookRepertory.setAnimType(getViewIndex(view));
        mBookReaderControlChangeListener.commonInfoChange();
    }

    @OnClick(value = {R.id.book_space_one, R.id.book_space_two, R.id.book_space_three})
    public void onSpaceClicked(View view) {
        clearAllSpace();
        view.setSelected(true);
        BookRepertory.setLineType(getViewIndex(view) - 1);
        mBookReaderControlChangeListener.sizeChange();
    }

    @OnClick(R.id.default_book_space)
    public void onSpaceDetail() {
        LinearLayout linearLayout = (LinearLayout) mBookSpaceOne.getParent();
        clearAllSpace();
        linearLayout.getChildAt(1).setSelected(true);
        BookRepertory.setLineType(0);
        mBookReaderControlChangeListener.sizeChange();
    }

    @OnClick(R.id.word_size_default)
    public void defaultSize() {
        int defaultTextSize = BookRepertory.getDefaultTextSize();
        BookRepertory.setTextSize(defaultTextSize);
        mBookReaderControlChangeListener.sizeChange();
    }

    @OnClick(R.id.default_light)
    public void setDefaultLight() {
        BookRepertory.setLight(-1);
        AppUtils.changeAppBrightness(mContext, -1);
    }

    private int getViewIndex(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        return viewGroup.indexOfChild(view);
    }

    @Override
    public void backgroundClick(int index, int backColor, String backResource, int textColor) {
        BookReaderBackground bookReaderBackground = new BookReaderBackground(backColor, backResource, textColor);
        bookReaderBackground.index = index;
        BookRepertory.setReadBackground(JsonUtils.toJson(bookReaderBackground));
        mBookReaderControlChangeListener.commonInfoChange();
    }

    public interface BookReaderControlChangeListener {
        void commonInfoChange();

        void sizeChange();
    }

    private void clearAllSpace() {
        mBookSpaceOne.setSelected(false);
        mBookSpaceTwo.setSelected(false);
        mBookSpaceThree.setSelected(false);
    }

    public void destory() {
        mSubscriptions.clear();
        mBinder.unbind();
    }

}
