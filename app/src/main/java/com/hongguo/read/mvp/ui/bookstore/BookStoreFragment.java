package com.hongguo.read.mvp.ui.bookstore;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.TabLayoutUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.utils.webview.AppWebView;
import com.hongguo.common.widget.refresh.DesignRefreshLayout;
import com.hongguo.common.widget.skin.ISkinUpdate;
import com.hongguo.common.widget.skin.loader.SkinManager;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.CommonPagerAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BaiDuTimeOut;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.BookStoreContractor;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.read.mvp.presenter.bookstore.BookStorePresenter;
import com.hongguo.read.mvp.ui.bookstore.channel.BoysChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.FreeChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.GirlsChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.RecommendChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.SortChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.TypeChannelFragment;
import com.hongguo.read.mvp.ui.search.SearchActivity;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.widget.SimpleBannerView;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class BookStoreFragment extends FragmentEx implements BookStoreContractor.IView, SwipeRefreshLayout.OnRefreshListener, ISkinUpdate, LoadingView.LoadingViewClickListener {

    @Inject
    BookStorePresenter mBookStorePresenter;

    @BindView(R.id.book_search)
    ImageView           mBookSearch;
    @BindView(R.id.recommend_banner)
    SimpleBannerView    mRecommendBanner;
    @BindView(R.id.vip_tab)
    TabLayout           mVipTab;
    @BindView(R.id.vip_pager)
    ViewPager           mVipPager;
    @BindView(R.id.content_view)
    CoordinatorLayout   mContentView;
    @BindView(R.id.design_refresh)
    DesignRefreshLayout mDesignRefresh;

    private boolean mIsShow = false;
    private CommonPagerAdapter mAdapter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_boostore;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        if (!AppRepertory.getBaiduTimeout()) {
            mAdapter = new CommonPagerAdapter(getChildFragmentManager(), RecommendChannelFragment.class,
                    GirlsChannelFragment.class, BoysChannelFragment.class,
                    FreeChannelFragment.class, TypeChannelFragment.class);

        } else {
            mAdapter = new CommonPagerAdapter(getChildFragmentManager(), RecommendChannelFragment.class,
                    GirlsChannelFragment.class, BoysChannelFragment.class,
                    SortChannelFragment.class, TypeChannelFragment.class);
        }

        mVipPager.setAdapter(mAdapter);

        mVipPager.setOffscreenPageLimit(5);
        mVipTab.setupWithViewPager(mVipPager);

        TabLayoutUtils.setTabLine(mVipTab, 8, 8);

        mDesignRefresh.setOnRefreshListener(this);

        LoadingHelper loadingView = new LoadingHelper(mContext);
        loadingView.setStatus(LoadingView.LoadingStatus.LOADING);
        loadingView.setLoadingViewClickListener(this);
        bindLoadingView(loadingView, mDesignRefresh, 0);

        mBookStorePresenter.bingView(this);
        mBookStorePresenter.loading();

        SkinManager.getInstance().attach(this);

        //手动更改个别皮肤信息
        onThemeUpdate();
    }

    @Override
    @ViewMethod
    public void setBannerUrls(List<CmsBannerBean.DataBean> banners) {
        mRecommendBanner.setImageLoader(ImageLoadUtils::loadingBannel);
        List<String> imageUrls = new ArrayList<>();
        for (CmsBannerBean.DataBean banner : banners) {
            imageUrls.add(banner.cover);
        }
        mRecommendBanner.setLoadUrls(imageUrls);
        mRecommendBanner.setOnBannerItemClickListener(position -> {
            StatisticsUtils.bookstore("轮播" + banners.get(position).linkUrl);
            AppWebView.toActivity(banners.get(position).linkUrl);
        });
    }

    /**
     * 各个模块刷新完成时候回调
     *
     * @param bookStoreRefreshSuccessEvent
     */
    @Subscribe
    public void onEvent(BookStoreRefreshSuccessEvent bookStoreRefreshSuccessEvent) {
        mDesignRefresh.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(BaiDuTimeOut baiDuTimeOut) {
        if (mAdapter == null) return;
        mAdapter = new CommonPagerAdapter(getChildFragmentManager(), RecommendChannelFragment.class,
                GirlsChannelFragment.class, BoysChannelFragment.class,
                SortChannelFragment.class, TypeChannelFragment.class);
        mVipPager.setAdapter(mAdapter);
        onThemeUpdate();
    }

    @Override
    public void onRefresh() {
        Fragment item = mAdapter.getItem(mVipPager.getCurrentItem());
        EventBus.getDefault().post(new BookStoreRefreshEvent(item));
        mBookStorePresenter.loading();
    }

    @OnClick(R.id.book_search)
    void searchBook() {
        SearchActivity.toActivity(mContext);
    }

    @Override
    public void onThemeUpdate() {

        if (mVipTab == null) return;

        mVipTab.getTabAt(0).setCustomView(null);
        mVipTab.getTabAt(1).setCustomView(null);
        mVipTab.getTabAt(2).setCustomView(null);
        mVipTab.getTabAt(3).setCustomView(null);
        mVipTab.getTabAt(4).setCustomView(null);

        TabViewHelper tabViewHelper = new TabViewHelper(mContext);
        tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_chanel_recommend), "精选", SkinResourcesUtils.getColor(R.color.colorPrimary));
        mVipTab.getTabAt(0).setCustomView(tabViewHelper.getView());
        tabViewHelper = new TabViewHelper(mContext);
        tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_chanel_grils), "女频", SkinResourcesUtils.getColor(R.color.colorPrimary));
        mVipTab.getTabAt(1).setCustomView(tabViewHelper.getView());
        tabViewHelper = new TabViewHelper(mContext);
        tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_chanel_men), "男频", SkinResourcesUtils.getColor(R.color.colorPrimary));
        mVipTab.getTabAt(2).setCustomView(tabViewHelper.getView());
        tabViewHelper = new TabViewHelper(mContext);
        if (!AppRepertory.getBaiduTimeout()) {
            tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_chanel_free), "免费", SkinResourcesUtils.getColor(R.color.colorPrimary));
            mVipTab.getTabAt(3).setCustomView(tabViewHelper.getView());
        } else {
            tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_sort), "排行", SkinResourcesUtils.getColor(R.color.colorPrimary));
            mVipTab.getTabAt(3).setCustomView(tabViewHelper.getView());
        }
        tabViewHelper = new TabViewHelper(mContext);
        tabViewHelper.setInfo(SkinResourcesUtils.getDrawable(R.mipmap.ic_chanel_type), "分类", SkinResourcesUtils.getColor(R.color.colorPrimary));
        mVipTab.getTabAt(4).setCustomView(tabViewHelper.getView());

        ViewGroup.LayoutParams layoutParams = mVipTab.getLayoutParams();
        View view = tabViewHelper.getView();
        view.measure(0, 0);
        layoutParams.height = view.getMeasuredHeight() + 2 * DisplayUtil.dip2px(mContext, 8);
        mVipTab.setLayoutParams(layoutParams);

        mVipTab.setSelectedTabIndicatorColor(SkinResourcesUtils.getColor(R.color.colorPrimary));
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBookStorePresenter.loading();
    }
}
