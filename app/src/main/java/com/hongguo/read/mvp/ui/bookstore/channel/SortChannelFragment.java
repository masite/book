package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.R;
import com.hongguo.read.adapter.SortChannelAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.SortChannelContractor;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.SortChannelPresenter;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/5/29.
 */

public class SortChannelFragment extends FragmentEx implements SortChannelContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    SortChannelPresenter mSortChannelPresenter;

    @BindView(R.id.sort_list)
    RecyclerView mSortList;

    private SortChannelAdapter mSortChannelAdapter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_sort_channel;
    }


    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mSortChannelAdapter = new SortChannelAdapter(mContext);
        mSortList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mSortList.setAdapter(mSortChannelAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mSortList, 0);

        mSortChannelPresenter.bingView(this);
        mSortChannelPresenter.loading();
    }


    @ViewMethod
    public void setClickRank(RankBean rank) {
        mSortChannelAdapter.setClickRank(rank);
        mSortChannelAdapter.notifyChange();
    }

    @ViewMethod
    public void setRecommendRank(RankBean rank) {
        mSortChannelAdapter.setRecommendRank(rank);
        mSortChannelAdapter.notifyChange();
    }

    @ViewMethod
    public void setUpdateRank(RankBean rank) {
        mSortChannelAdapter.setUpdateRank(rank);
        mSortChannelAdapter.notifyChange();
    }

    @ViewMethod
    public void setFavorRank(RankBean rank) {
        mSortChannelAdapter.setFavorRank(rank);
        mSortChannelAdapter.notifyChange();
    }

    @ViewMethod
    public void setRewardRank(RankBean rank) {
        mSortChannelAdapter.setRewardRank(rank);
        mSortChannelAdapter.notifyChange();
    }

    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent) {
        if (bookStoreRefreshEvent.mFragment == this) {
            mSortChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mSortChannelPresenter.loading();
    }
}
