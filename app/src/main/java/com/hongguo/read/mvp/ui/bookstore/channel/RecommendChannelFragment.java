package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.widget.skin.ISkinUpdate;
import com.hongguo.common.widget.skin.loader.SkinManager;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.R;
import com.hongguo.read.adapter.RecommendBookAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.RecommendChannelContractor;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.RecommendChannelPresenter;
import com.hongguo.read.mvp.ui.booktype.BookTypeDetailActivity;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.hongguo.read.widget.pullfind.PullFindLayout;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class RecommendChannelFragment extends FragmentEx implements RecommendChannelContractor.IView, LoadingView.LoadingViewClickListener, PullFindLayout.FindMoreArriveListener, ISkinUpdate {

    @Inject
    RecommendChannelPresenter mRecommendChannelPresenter;

    @BindView(R.id.recommend_list)
    RecyclerView   mRecommendList;
    @BindView(R.id.pull_find_view)
    PullFindLayout mPullFindLayout;

    private RecommendBookAdapter mBookStoreAdapter;
    private TextView             mFindMoreFooter;

    public static RecommendChannelFragment getInstance() {
        return new RecommendChannelFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_recommend_channel;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mRecommendList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mBookStoreAdapter = new RecommendBookAdapter(mContext);
        View footer = View.inflate(mContext, R.layout.view_pull_find_more, null);
        mFindMoreFooter = footer.findViewById(R.id.more);
        mBookStoreAdapter.addFooter(footer);
        mRecommendList.setAdapter(mBookStoreAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mRecommendList, 0);

        mPullFindLayout.setFindMoreArriveListener(this);

        SkinManager.getInstance().attach(this);

        initColor();

        mRecommendChannelPresenter.bingView(this);
        mRecommendChannelPresenter.loading();
    }


    @ViewMethod
    public void setHotsRecommend(RecommendItemBean.ObjBean objBean) {
        mBookStoreAdapter.setHotRecommend(objBean);
        mBookStoreAdapter.notifyChange();
    }

    @ViewMethod
    public void setHotsBook(RecommendItemBean.ObjBean objBean) {
        mBookStoreAdapter.setHotBook(objBean);
        mBookStoreAdapter.notifyChange();
    }

    @ViewMethod
    public void setNewBooks(RecommendItemBean.ObjBean objBean) {
        mBookStoreAdapter.setNewBook(objBean);
        mBookStoreAdapter.notifyChange();
    }

    @ViewMethod
    public void setRecomend(RecommendItemBean.ObjBean objBean) {
        mBookStoreAdapter.setRecommend(objBean);
        mBookStoreAdapter.notifyChange();
    }


    @ViewMethod
    public void setBookFavor(RecommendItemBean.ObjBean objBean) {
        mBookStoreAdapter.setFavorBook(objBean);
        mBookStoreAdapter.notifyChange();
    }


    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent) {
        if (bookStoreRefreshEvent.mFragment == this) {
            mRecommendChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mRecommendChannelPresenter.loading();
    }

    @Override
    public void moreArrive() {
        mFindMoreFooter.setText("松开获取更多内容");
    }

    @Override
    public void showMoreFind() {
        mFindMoreFooter.setText("上拉获取更多内容");
        //默认进入 女频、言情、全部、全部
        BookTypeDetailActivity.toActivity(mContext,"347");
        getActivity().overridePendingTransition(R.anim.anim_find_more, R.anim.anim_stay);
    }

    @Override
    public void onThemeUpdate() {
        initColor();
    }

    public void initColor(){
        int color = SkinResourcesUtils.getColor(R.color.colorPrimary);
        mPullFindLayout.setColor(color);
    }
}


