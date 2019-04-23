package com.hongguo.read.mvp.ui.vip;

import android.view.View;

import com.hongguo.read.R;
import com.hongguo.read.adapter.SuperVipStoreAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.VIPStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.vip.SuperVipStoreContractor;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.presenter.vip.SuperVipStorePresenter;
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
 * Created time 2017/11/27.
 *
 * @author losg
 */

public class SuperVipStoreFragment extends FragmentEx implements SuperVipStoreContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    SuperVipStorePresenter mSuperVipStorePresenter;

    @BindView(R.id.vip_list)
    DesignRefreshRecyclerView mSuperVipList;

    private SuperVipStoreAdapter   mSuperVipStoreAdapter;
    private List<VipBean.DataBean> mItems;
    private SuperVipHeaderHelper   mSuperVipHeaderHelper;

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

        mItems = new ArrayList<>();
        mSuperVipStoreAdapter = new SuperVipStoreAdapter(mContext, mItems);

        mSuperVipHeaderHelper = new SuperVipHeaderHelper(mContext);
        mSuperVipStoreAdapter.addHeader(mSuperVipHeaderHelper.getView());
        mSuperVipList.setAdapter(mSuperVipStoreAdapter);

        mSuperVipList.setCanRefresh(false);
        bindRefresh(mSuperVipList);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mSuperVipList, 0);

        mSuperVipStorePresenter.bingView(this);
        mSuperVipStorePresenter.loading();
    }

    @ViewMethod
    public void setBannerUrls(List<CmsBannerBean.DataBean> banners) {
        mSuperVipHeaderHelper.setBannerUrls(banners);
    }

    @ViewMethod
    public void setVipBooks(List<VipBean.DataBean> dataBeans) {
        mSuperVipStoreAdapter.setItems(dataBeans);
        mSuperVipStoreAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mSuperVipStorePresenter.loading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSuperVipHeaderHelper.destory();
    }

    /**
     * 请求刷新
     * {@link VipFragment}
     *
     * @param vipStoreRefreshEvent
     */
    @Subscribe
    public void onEvent(VIPStoreRefreshEvent vipStoreRefreshEvent) {
        if (vipStoreRefreshEvent.mFragment == this) {
            mSuperVipStorePresenter.refresh();
        }
    }
}


