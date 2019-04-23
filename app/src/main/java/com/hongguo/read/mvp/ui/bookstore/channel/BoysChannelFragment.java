package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.read.R;
import com.hongguo.read.adapter.GirlsOrBoysStoreAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.BoysChannelContractor;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.model.topic.TopicBean;
import com.hongguo.read.mvp.presenter.bookstore.channel.BoysChannelPresenter;
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

public class BoysChannelFragment extends FragmentEx  implements BoysChannelContractor.IView, LoadingView.LoadingViewClickListener {

	@Inject
	BoysChannelPresenter mBoysChannelPresenter;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendList;
    private GirlsOrBoysStoreAdapter mBookStoreAdapter;

    public static BoysChannelFragment getInstance() {
        return new BoysChannelFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_recommend_channel_girls_boys;
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

        mBoysChannelPresenter.bingView(this);
        mBoysChannelPresenter.loading();
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
    public void setFantasyItem(RecommendItemBean.ObjBean romanticItem) {
        mBookStoreAdapter.setAppendOneItem(romanticItem);
        mBookStoreAdapter.notifyChange();
    }

    @Override
    @ViewMethod
    public void setUrbanItem(RecommendItemBean.ObjBean ancientItem) {
        mBookStoreAdapter.setAppendTwoItem(ancientItem);
        mBookStoreAdapter.notifyChange();
    }


    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent){
        if(bookStoreRefreshEvent.mFragment == this){
            mBoysChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBoysChannelPresenter.loading();
    }
}
