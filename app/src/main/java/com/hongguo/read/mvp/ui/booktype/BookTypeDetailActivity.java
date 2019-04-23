package com.hongguo.read.mvp.ui.booktype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookTypeDetailAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.booktype.BookTypeDetailContractor;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.mvp.presenter.booktype.BookTypeDetailPresenter;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/14.
 */

public class BookTypeDetailActivity extends ActivityEx implements BookTypeDetailContractor.IView, LoadingView.LoadingViewClickListener, BookTypeDetailHeaderHelper.BookTypeDetailHeaderListener {

    private static final String INTENT_BOOK_TYPE_ID = "intent_book_type_id";

    @Inject
    BookTypeDetailPresenter   mBookTypeDetailPresenter;
    @BindView(R.id.book_type_list)
    DesignRefreshRecyclerView mBookTypeList;
    @BindView(R.id.choose_info)
    TextView                  mChooseInfo;
    @BindView(R.id.choose_layer)
    LinearLayout              mChooseLayer;
    @BindView(R.id.float_type)
    LinearLayout              mFloatType;

    private List<BookStoreBookBean.DataBean> mItems;

    private BookTypeDetailAdapter mBookTypeDetailAdapter;

    private String mTypeId;
    private int    mScrollY;

    private View                       mHeaderView;
    private BookTypeDetailHeaderHelper mBookTypeDetailHeaderHelper;
    private BookTypeDetailHeaderHelper mBookFloatTypeHelper;

    /**
     * @param context
     * @param typeid  点击对应服务器端的类型id
     */

    public static void toActivity(Context context, String typeid) {
        Intent intent = new Intent(context, BookTypeDetailActivity.class);
        intent.putExtra(INTENT_BOOK_TYPE_ID, typeid);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_book_type_detail;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("分类详情");
        mTypeId = getIntent().getStringExtra(INTENT_BOOK_TYPE_ID);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mBookTypeList, 0);

        View footer = initFooter();
        mItems = new ArrayList<>();
        mBookTypeDetailAdapter = new BookTypeDetailAdapter(mContext, mItems);
        mBookTypeList.setCanRefresh(false);
        mBookTypeList.setAdapter(mBookTypeDetailAdapter);
        mBookTypeDetailAdapter.addFooter(footer);

        bindRefresh(mBookTypeList);

        mBookTypeDetailPresenter.bingView(this);
        mBookTypeDetailPresenter.loading();
        mBookTypeDetailPresenter.queryBookType(mTypeId, "", "");

        addScrollListener();
    }

    private void addScrollListener() {

        mBookTypeList.setOnScrollChangeListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
                if (mFloatType.getChildCount() != 2) return;
                if (mFloatType.getVisibility() == View.VISIBLE) {
                    mFloatType.setVisibility(View.GONE);
                }
                if (mScrollY >= mHeaderView.getMeasuredHeight()) {
                    if (mChooseLayer.getVisibility() == View.GONE) {
                        mChooseLayer.setVisibility(View.VISIBLE);
                        mChooseInfo.setText(mBookFloatTypeHelper.getSelectedInfo());
                    }
                } else {
                    mChooseLayer.setVisibility(View.GONE);
                }

            }
        });
    }

    private View initFooter() {
        View loadingFooter = View.inflate(mContext, R.layout.adpter_loading_footer, null);
        View loadingContent = loadingFooter.findViewById(R.id.loading_footer);
        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        loadingHelper.setResultNullDescribe("尚未该分类书籍，尝试其它分类", "");
        loadingHelper.setResultNullVisible(false);
        bindLoadingView(loadingHelper, loadingContent, 1);
        return loadingFooter;
    }

    @ViewMethod
    public void setType(List<ClassifyRepertory.Classify> classifies) {

        if (mBookTypeDetailHeaderHelper == null) {

            mBookTypeDetailHeaderHelper = new BookTypeDetailHeaderHelper(mContext);
            mBookFloatTypeHelper = new BookTypeDetailHeaderHelper(mContext);
            mBookTypeDetailHeaderHelper.setBookTypeDetailHeaderListener(this);
            mBookFloatTypeHelper.setBookTypeDetailHeaderListener(this);
            mBookTypeDetailHeaderHelper.setInitPosition(mTypeId);
            mBookFloatTypeHelper.setInitPosition(mTypeId);

            View header = mBookTypeDetailHeaderHelper.createHeader(classifies);
            View floatHeader = mBookFloatTypeHelper.createHeader(classifies);
            header.setBackgroundColor(Color.WHITE);
            floatHeader.setBackgroundColor(Color.WHITE);

            if (mFloatType.getChildCount() >= 2) {
                mFloatType.removeViewAt(0);
            }
            mFloatType.addView(floatHeader, 0);
            mBookTypeDetailAdapter.addHeader(header);
            mHeaderView = header;
            mBookTypeDetailAdapter.notifyChange();
        }
    }

    @Override
    public void changeLoadingStatus(BaLoadingView.LoadingStatus status, int tag) {
        super.changeLoadingStatus(status, tag);
        if (tag == 1) {
            mBookTypeDetailAdapter.setLoadingStatus(status);
        }
    }

    @ViewMethod
    public void setBooks(List<BookStoreBookBean.DataBean> dataBeans) {
        mBookTypeDetailAdapter.setDataBeans(dataBeans);
        mBookTypeDetailAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookTypeDetailPresenter.reLoad();
    }

    @Override
    public void bookTypeClick(BookTypeDetailHeaderHelper bookTypeDetailHeaderHelper, String typeid, String status, String sort) {
        mScrollY = 0;
        mTypeId = typeid;
        mBookTypeDetailPresenter.queryTypeBooks(typeid, status, sort);
        mChooseInfo.setText(bookTypeDetailHeaderHelper.getSelectedInfo());
        setBooks(new ArrayList<>());
        mFloatType.setVisibility(View.GONE);
        mChooseLayer.setVisibility(View.GONE);

        if (bookTypeDetailHeaderHelper == mBookTypeDetailHeaderHelper) {
            mBookFloatTypeHelper.setSelected(typeid, status, sort);
        } else {
            mBookTypeDetailHeaderHelper.setSelected(typeid, status, sort);
        }
    }

    @OnClick(R.id.choose_layer)
    public void chooseLayer() {
        mFloatType.setVisibility(View.VISIBLE);
    }
}
