package com.hongguo.read.mvp.ui.booktype;

import android.content.Context;
import android.content.Intent;

import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookTypeDetailAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.booktype.BookTypeDetailContractor;
import com.hongguo.read.mvp.contractor.booktype.BookTypeWithoutSelectContractor;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.mvp.presenter.booktype.BookTypeDetailPresenter;
import com.hongguo.read.mvp.presenter.booktype.BookTypeWithoutSelectPresenter;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/14.
 */

public class BookTypeWithoutSelectActivity extends ActivityEx implements BookTypeWithoutSelectContractor.IView, BookTypeDetailContractor.IView, LoadingView.LoadingViewClickListener {

    private static final String INTENT_BOOK_TYPE_ID = "intent_book_type_id";
    private static final String INTENT_BOOK_NAME    = "intent_book_name";
    private static final String INTENT_BOOK_SORT    = "intent_book_sort";
    private static final String INTENT_BOOK_STATUS  = "intent_book_status";

    @Inject
    BookTypeWithoutSelectPresenter mBookTypeWithoutSelectPresenter;

    @Inject
    BookTypeDetailPresenter   mBookTypeDetailPresenter;
    @BindView(R.id.book_type_list)
    DesignRefreshRecyclerView mBookTypeList;

    private List<BookStoreBookBean.DataBean> mItems;

    private String                mTypeId;
    private String                mStatus;
    private String                mSort;
    private BookTypeDetailAdapter mBookTypeDetailAdapter;

    /**
     * @param context
     * @param typeid  点击对应服务器端的类型id
     */
    public static void toActivity(Context context, String title, String typeid,String status,String sort) {
        Intent intent = new Intent(context, BookTypeWithoutSelectActivity.class);
        intent.putExtra(INTENT_BOOK_NAME, title);
        intent.putExtra(INTENT_BOOK_TYPE_ID, typeid);
        intent.putExtra(INTENT_BOOK_SORT, sort);
        intent.putExtra(INTENT_BOOK_STATUS, status);
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
        mTypeId = getIntent().getStringExtra(INTENT_BOOK_TYPE_ID);
        mSort = getIntent().getStringExtra(INTENT_BOOK_SORT);
        mStatus = getIntent().getStringExtra(INTENT_BOOK_STATUS);

        String title = getIntent().getStringExtra(INTENT_BOOK_NAME);
        setTitle(title);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mBookTypeList, 1);

        mItems = new ArrayList<>();
        mBookTypeDetailAdapter = new BookTypeDetailAdapter(mContext, mItems);
        mBookTypeDetailAdapter.setLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS);
        mBookTypeList.setCanRefresh(false);
        mBookTypeList.setAdapter(mBookTypeDetailAdapter);
        bindRefresh(mBookTypeList);

        mBookTypeWithoutSelectPresenter.bingView(this);
        mBookTypeWithoutSelectPresenter.loading();
        mBookTypeDetailPresenter.bingView(this);
        mBookTypeDetailPresenter.loading();
        mBookTypeDetailPresenter.queryTypeBooks(mTypeId, mStatus, mSort);
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookTypeDetailPresenter.queryTypeBooks(mTypeId, mStatus, mSort);
    }


    @Override
    public void setType(List<ClassifyRepertory.Classify> classifies) {

    }

    @Override
    public void setBooks(List<BookStoreBookBean.DataBean> dataBeans) {
        mBookTypeDetailAdapter.setDataBeans(dataBeans);
        mBookTypeDetailAdapter.notifyChange();
    }
}
