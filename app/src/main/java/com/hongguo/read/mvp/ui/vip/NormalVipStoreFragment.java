package com.hongguo.read.mvp.ui.vip;


import android.view.View;

import com.hongguo.read.R;
import com.hongguo.read.adapter.NormalVipStoreAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.VIPStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.vip.NormalVipStoreContractor;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.presenter.vip.NormalVipStorePresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/11/27
 *
 * @author losg
 */

public class NormalVipStoreFragment extends FragmentEx implements NormalVipStoreContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    NormalVipStorePresenter mNormalVipStorePresenter;

    @BindView(R.id.vip_list)
    DesignRefreshRecyclerView mVipList;

    private NormalVipStoreAdapter  mNormalVipStoreAdapter;
    private List<VipBean.DataBean> mItems;
    private NormalVipHeaderHelper  mNormalVipHeaderHelper;

    @Override
    protected int initLayout() {
        return R.layout.fragment_vip_store;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mNormalVipHeaderHelper = new NormalVipHeaderHelper(mContext);
        mItems = new ArrayList<>();
        mNormalVipStoreAdapter = new NormalVipStoreAdapter(getContext(), mItems);
        mNormalVipStoreAdapter.addHeader(mNormalVipHeaderHelper.getView());

        mVipList.setAdapter(mNormalVipStoreAdapter);
        mVipList.setCanRefresh(false);
        bindRefresh(mVipList);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mVipList, 0);

        mNormalVipStorePresenter.bingView(this);
        mNormalVipStorePresenter.loading();
    }

    @ViewMethod
    public void setTopBooks(List<VipBean.DataBean> items) {
        mNormalVipHeaderHelper.setTopBooks(items);
    }

    @ViewMethod
    public void setVipBooks(List<VipBean.DataBean> items) {
        mNormalVipStoreAdapter.setItems(items);
        mNormalVipStoreAdapter.notifyChange();
    }


    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mNormalVipStorePresenter.loading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNormalVipHeaderHelper.destory();
    }

    /**
     * 请求刷新
     * {@link VipFragment}
     * @param vipStoreRefreshEvent
     */
    @Subscribe
    public void onEvent(VIPStoreRefreshEvent vipStoreRefreshEvent){
        if(vipStoreRefreshEvent.mFragment == this){
            mNormalVipStorePresenter.refresh();
        }
    }
}
