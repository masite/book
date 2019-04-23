package com.hongguo.common.widget.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.common.widget.refresh.base.BaFooterView;


/**
 * Created by losg on 2017/5/27.
 */

public class RefreshRecyclerView extends DesignRefreshLayout implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 默认加载的位置
     */
    private static final int DEFAULT_START_LOADING = 5;
    private static final int DEFAULT_PAGE_MIN      = 20;
    private              int mSpanSize             = 1;

    public enum LoadingStatus {
        NORMAL,                //普通
        LOADING,               //加载中
        ALL_LOADED,             //全部加载完成
        LOAD_FAILE              //加载失败
    }

    private LoadingStatus mLoadingStatus;        //当前加载状态
    private BaFooterView  mFooterView;             //上拉加载View
    private boolean mCanLoading = true;           //是否可以加载

    protected RecyclerView         mRecycler;
    private   RecyclerView.Adapter mAdapter;
    private   RefreshListener      mRefreshListener;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mRecycler = new RecyclerView(getContext());
        mRecycler.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);

        addView(mRecycler, new LayoutParams(-1, -1));
        initRecyclerForLoading(mRecycler);
        mLoadingStatus = LoadingStatus.NORMAL;
        setOnRefreshListener(this);
    }


    public void setFooterView(BaFooterView footerView) {
        mFooterView = footerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecycler.setAdapter(mAdapter);
        if (mFooterView == null) {
            return;
        }
        addFooter(mAdapter);
    }


    private void addFooter(RecyclerView.Adapter adapter) {
        if (adapter instanceof IosRecyclerAdapter) {
            ((IosRecyclerAdapter) adapter).addFooter(mFooterView.getFooterView());
            mFooterView.loadNormal();
        }
    }

    private void initRecyclerForLoading(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(mOnScrollChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRecycler != null) {
            mRecycler.removeOnScrollListener(mOnScrollChangeListener);
        }
    }

    private RecyclerView.OnScrollListener mOnScrollChangeListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }

            //如果当前状态为加载失败状态，重新进行加载
            if (mCanLoading && mLoadingStatus == LoadingStatus.LOAD_FAILE) {
                reload();
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                //滑动到末尾,改变加载状态
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int end = DEFAULT_PAGE_MIN % mSpanSize == 0 ? DEFAULT_PAGE_MIN / mSpanSize:  DEFAULT_PAGE_MIN / mSpanSize + 1;
                if (lastCompletelyVisibleItemPosition == -1 || (layoutManager.getItemCount() - end < 0))
                    return;

                int loadingStart = DEFAULT_START_LOADING % mSpanSize == 0 ? DEFAULT_START_LOADING / mSpanSize:  DEFAULT_START_LOADING / mSpanSize + 1;

                //查询当前访问位置
                if (lastCompletelyVisibleItemPosition >= layoutManager.getItemCount() - loadingStart) {
                    //当前状态为预加载状态，并且可以加载,更改加载状态
                    if (mCanLoading && mLoadingStatus == LoadingStatus.NORMAL) {
                        mFooterView.loadding();
                        mLoadingStatus = LoadingStatus.LOADING;
                        if (mRefreshListener != null) {
                            mRefreshListener.onLoading();
                        }
                    }

                }
            }
        }
    };


    @Deprecated
    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    public void setRefreshSuccess() {
        mLoadingStatus = LoadingStatus.NORMAL;
        setRefreshing(false);
        setLoadingAll(false);
    }

    public void setLoadingError(String errorMessage) {
        mLoadingStatus = LoadingStatus.LOAD_FAILE;
        mFooterView.loaddingError(errorMessage);
        mFooterView.getFooterView().setOnClickListener(this);
    }

    public void setLoadingAll(boolean allLoad) {
        if (allLoad) {
            mLoadingStatus = LoadingStatus.ALL_LOADED;
            if (mRecycler.getAdapter().getItemCount() < DEFAULT_PAGE_MIN) {
                mFooterView.allLoaded(false);
            } else {
                mFooterView.allLoaded(true);
            }
        } else {
            mLoadingStatus = LoadingStatus.NORMAL;
            mFooterView.loadNormal();
        }
    }

    private void reload() {
        if (mLoadingStatus == LoadingStatus.LOAD_FAILE && mRefreshListener != null) {
            mFooterView.loadding();
            mLoadingStatus = LoadingStatus.LOADING;
            mRefreshListener.reload();
        }
    }

    @Override
    public void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    //加载失败点击处理
    @Override
    public void onClick(View view) {
        reload();
    }

    public void setCanLoading(boolean canLoading) {
        mCanLoading = canLoading;
        mFooterView.loadNormal();
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public interface RefreshListener extends OnRefreshListener {
        void onLoading();

        void reload();
    }

    public void setSpanSize(int size) {
        mSpanSize = size;
    }

}
