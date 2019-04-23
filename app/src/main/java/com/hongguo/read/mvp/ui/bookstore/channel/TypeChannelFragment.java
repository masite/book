package com.hongguo.read.mvp.ui.bookstore.channel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookTypeAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.BookStoreRefreshEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.TypeChannelContractor;
import com.hongguo.read.mvp.presenter.bookstore.channel.TypeChannelPresenter;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class TypeChannelFragment extends FragmentEx implements TypeChannelContractor.IView, LoadingView.LoadingViewClickListener {

	@Inject
	TypeChannelPresenter mTypeChannelPresenter;

    @BindView(R.id.book_type)
    RecyclerView mBookType;
    private BookTypeAdapter mBookTypeAdapter;
    private List<ClassifyRepertory.Classify> mClassifys;

    public static TypeChannelFragment getInstance() {
        return new TypeChannelFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_type;
    }

    
	@Override
	protected void inject(FragmentComponent fragmentComponent) {
		fragmentComponent.inject(this);
	}

	@Override
    protected void initView(View view)  {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);

        mBookType.setLayoutManager(gridLayoutManager);
        mClassifys = new ArrayList<>();
        mBookTypeAdapter = new BookTypeAdapter(mContext, mClassifys);
        mBookType.setAdapter(mBookTypeAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mBookType, 0);

        mTypeChannelPresenter.bingView(this);
        mTypeChannelPresenter.loading();

    }

    @ViewMethod
    public void setBookType(List<ClassifyRepertory.Classify> classifies){
        mClassifys.clear();
        mClassifys.addAll(classifies);
        mBookTypeAdapter.notifyChange();
    }

    @Subscribe
    public void onEvent(BookStoreRefreshEvent bookStoreRefreshEvent){
        if(bookStoreRefreshEvent.mFragment == this){
            mTypeChannelPresenter.loading();
        }
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mTypeChannelPresenter.loading();
    }
}
