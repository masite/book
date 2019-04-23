package com.hongguo.common.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hongguo.common.eventbus.DataUpdateEvent;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.common.widget.refresh.RefreshRecyclerView;
import com.hongguo.common.widget.skin.IDynamicNewView;
import com.hongguo.common.widget.skin.attr.base.DynamicAttr;
import com.hongguo.common.widget.skin.loader.SkinInflaterFactory;
import com.losg.library.base.BaFragment;
import com.losg.library.widget.dialog.MessageInfoDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by losg on 2017/7/17.
 */

public abstract class BaseFragment extends BaFragment implements BaseViewEx, IDynamicNewView, RefreshRecyclerView.RefreshListener {

    private Unbinder            mUnbinder;
    private List<BasePresenter> mBasePresenters;
    private IDynamicNewView     mIDynamicNewView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBasePresenters = new ArrayList<>();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int initLayout() {
        return 0;
    }

    @Subscribe
    public void onEvent(DataUpdateEvent dataUpdateEvent) {
        if (dataUpdateEvent.isSelf(this.getClass())) {
            for (BasePresenter basePresenter : mBasePresenters) {
                basePresenter.loading();
            }
        }
    }

    protected void bindRefresh(DesignRefreshRecyclerView designRefreshLayout) {
        bindRefreshView(designRefreshLayout);
        designRefreshLayout.setRefreshListener(this);
    }

    public void showChange(boolean show) {

    }


    @Override
    protected abstract void initView(View view);

    @Override
    public void setPresener(BasePresenter basePresenter) {
        if (mBasePresenters.contains(basePresenter)) {
            return;
        }
        mBasePresenters.add(basePresenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeAllView(getView());
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.unBindView();
        }
    }

    private void removeAllView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                removeAllView(viewGroup.getChildAt(i));
            }
            removeViewInSkinInflaterFactory(view);
        } else {
            removeViewInSkinInflaterFactory(view);
        }
    }

    private SkinInflaterFactory getSkinInflaterFactory() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getSkinInflaterFactory();
        }
        return null;
    }

    private void removeViewInSkinInflaterFactory(View v) {
        if (getSkinInflaterFactory() != null) {
            //此方法用于Activity中Fragment销毁的时候，移除Fragment中的View
            getSkinInflaterFactory().removeSkinView(v);
        }
    }

    @Override
    public void showMessDialog(String title, String content, String cancelLeft, String okRight, MessageInfoDialog.DialogButtonClick dialogButtonClick) {
        MessageInfoDialog messageInfoDialog = new MessageInfoDialog(mContext);
        messageInfoDialog.setButtonTitle(okRight, cancelLeft);
        messageInfoDialog.setTitle(title);
        messageInfoDialog.setMessage(content);
        messageInfoDialog.setDialogButtonClick(dialogButtonClick);
        messageInfoDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mIDynamicNewView = (IDynamicNewView) context;
        } catch (ClassCastException e) {
            mIDynamicNewView = null;
        }
    }

    @Override
    public final void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mIDynamicNewView == null) {
            throw new RuntimeException("IDynamicNewView should be implements !");
        } else {
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

    @Override
    public final void dynamicAddView(View view, String attrName, int attrValueResId) {
        mIDynamicNewView.dynamicAddView(view, attrName, attrValueResId);
    }

    @Override
    public final void dynamicAddFontView(TextView textView) {
        mIDynamicNewView.dynamicAddFontView(textView);
    }


    @Override
    public void finishView() {
        getActivity().finish();
    }

    @Override
    public void checkPermission(String... permission) {

    }

    @Override
    public void checkAllPermission(String... permission) {

    }

    public boolean backPress() {
        return false;
    }


    @Override
    public void onLoading() {
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.loadingMore();
        }
    }

    @Override
    public void reload() {
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.reLoad();
        }
    }

    @Override
    public void onRefresh() {
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.refresh();
        }
    }

    @Override
    public Application findApp() {
        return getActivity().getApplication();
    }
}
