package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.read.R;
import com.hongguo.read.adapter.FreeAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.FreeChannelContractor;
import com.hongguo.read.mvp.model.bookstore.channel.FreeChannelBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.FreeChannelPresenter;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class FreeChannelFragment extends FragmentEx implements FreeChannelContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    FreeChannelPresenter mFreeChannelPresenter;
    @BindView(R.id.free_items)
    RecyclerView         mFreeItems;

    private FreeAdapter mFreeAdapter;


    public static FreeChannelFragment getInstance() {
        return new FreeChannelFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_free;
    }


    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mFreeItems.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mFreeAdapter = new FreeAdapter(mContext);
        mFreeItems.setAdapter(mFreeAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mFreeItems, 0);

        mFreeChannelPresenter.bingView(this);
        mFreeChannelPresenter.loading();

    }

    @ViewMethod
    public void baiduFree(FreeChannelBean freeChannelBean){
        mFreeAdapter.setBaiduFree(freeChannelBean);
        mFreeAdapter.notifyChange();
    }

    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent){
        if(bookStoreRefreshEvent.mFragment == this){
            mFreeChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mFreeChannelPresenter.loading();
    }
}
