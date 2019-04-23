package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.R;
import com.hongguo.read.adapter.GirlsOrBoysStoreAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.GirlsChannelContractor;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.GirlsChannelPresenter;
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

public class GirlsChannelFragment extends FragmentEx implements GirlsChannelContractor.IView, LoadingView.LoadingViewClickListener {

    @Inject
    GirlsChannelPresenter mGirlsChannelPresenter;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendList;
    private GirlsOrBoysStoreAdapter mBookStoreAdapter;

    public static GirlsChannelFragment getInstance() {
        return new GirlsChannelFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_recommend_channel_girls;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {

        mRecommendList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mBookStoreAdapter = new GirlsOrBoysStoreAdapter(mContext);
        mRecommendList.setAdapter(mBookStoreAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mRecommendList, 0);

        mGirlsChannelPresenter.bingView(this);
        mGirlsChannelPresenter.loading();

    }

    @Override
    @ViewMethod
    public void setRecommendBook(RecommendItemBean.ObjBean recommendItem) {
        mBookStoreAdapter.setRecommendBook(recommendItem);
        mBookStoreAdapter.notifyChange();
    }

    @Override
    @ViewMethod
    public void setNewBook(RecommendItemBean.ObjBean newbook) {
        mBookStoreAdapter.setNewBook(newbook);
        mBookStoreAdapter.notifyChange();
    }

    @Override
    @ViewMethod
    public void setFinishBook(RecommendItemBean.ObjBean finishBook) {
        mBookStoreAdapter.setFinishBook(finishBook);
        mBookStoreAdapter.notifyChange();
    }

    @Override
    @ViewMethod
    public void setRomanticItem(RecommendItemBean.ObjBean romanticItem) {
        mBookStoreAdapter.setAppendOneItem(romanticItem);
        mBookStoreAdapter.notifyChange();
    }

    @Override
    @ViewMethod
    public void setAncientItem(RecommendItemBean.ObjBean ancientItem) {
        mBookStoreAdapter.setAppendTwoItem(ancientItem);
        mBookStoreAdapter.notifyChange();
    }


    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent){
        if(bookStoreRefreshEvent.mFragment == this){
            mGirlsChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mGirlsChannelPresenter.loading();
    }
}
