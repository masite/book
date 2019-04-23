package com.hongguo.read.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongguo.common.base.BaseActivity;
import com.hongguo.common.base.BaseFragment;
import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.common.eventbus.DataUpdateEvent;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.common.widget.refresh.RefreshRecyclerView;
import com.hongguo.common.widget.skin.IDynamicNewView;
import com.hongguo.common.widget.skin.attr.base.DynamicAttr;
import com.hongguo.common.widget.skin.loader.SkinInflaterFactory;
import com.hongguo.read.BaApp;
import com.hongguo.read.dagger.component.DaggerFragmentComponent;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.dagger.module.ActivityModule;
import com.hongguo.read.dagger.module.FragmentModule;
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

public abstract class FragmentEx extends BaseFragment {

    private FragmentComponent mFragmentComponent;

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mFragmentComponent = DaggerFragmentComponent.builder().appComponent(findApp().getAppComponent()).fragmentModule(new FragmentModule(this)).build();
        inject(mFragmentComponent);
    }

    protected void inject(FragmentComponent fragmentComponent) {
    }

    public BaApp findApp() {
        return (BaApp) getActivity().getApplication();
    }

}
