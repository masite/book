package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.hongguo.common.widget.recycler.GridCell;
import com.hongguo.read.R;
import com.hongguo.read.adapter.SkinAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.SkinContractor;
import com.hongguo.read.mvp.model.mine.SkinBean;
import com.hongguo.read.mvp.presenter.mine.SkinPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/8.
 */

public class SkinActivity extends ActivityEx implements SkinContractor.IView, SkinAdapter.SkinItemClick, LoadingView.LoadingViewClickListener {

    @Inject
    SkinPresenter mSkinPresenter;

    @BindView(R.id.skin_list)
    RecyclerView mSkinList;
    private List<SkinBean.SkinlistBean> mItems;
    private SkinAdapter                 mSkinAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SkinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_skin;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("个性皮肤");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mSkinList.setLayoutManager(gridLayoutManager);
        mSkinList.addItemDecoration(new GridCell(2, DisplayUtil.dip2px(mContext, 16), 0));

        mItems = new ArrayList<>();
        mSkinAdapter = new SkinAdapter(mContext, mItems);
        mSkinAdapter.setSkinItemClick(this);
        mSkinList.setAdapter(mSkinAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mSkinList, 0);

        mSkinPresenter.bingView(this);
        mSkinPresenter.loading();
    }

    @ViewMethod
    public void setCurrentSelected(String skinName) {
        mSkinAdapter.setCurrentSelected(skinName);
        mSkinAdapter.notifyChange();
    }

    @ViewMethod
    public void setSkinList(List<SkinBean.SkinlistBean> skinList) {
        mItems.clear();
        mItems.addAll(skinList);
        mSkinAdapter.notifyChange();
    }

    @Override
    public void skinClick(int position, String url, String name, boolean needDown) {
        if (needDown)
            mSkinPresenter.downSkin(position, url, name);
        else {
            mSkinPresenter.changeSkin(name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, 0, 0, "默认");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 0){
            mSkinPresenter.changeDefaultSkin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mSkinPresenter.loading();
    }
}
