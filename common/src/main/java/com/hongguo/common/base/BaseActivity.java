package com.hongguo.common.base;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gw.swipeback.SwipeBackLayout;
import com.hongguo.common.eventbus.DataUpdateEvent;
import com.hongguo.common.utils.PermissionUtils;
import com.hongguo.common.widget.SwipeBackFixLayout;
import com.hongguo.common.widget.refresh.DesignRefreshRecyclerView;
import com.hongguo.common.widget.refresh.RefreshRecyclerView;
import com.hongguo.common.widget.skin.IDynamicNewView;
import com.hongguo.common.widget.skin.ISkinUpdate;
import com.hongguo.common.widget.skin.SkinConfig;
import com.hongguo.common.widget.skin.attr.base.DynamicAttr;
import com.hongguo.common.widget.skin.loader.SkinInflaterFactory;
import com.hongguo.common.widget.skin.loader.SkinManager;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;
import com.hongguo.read.base.R;
import com.losg.library.base.BaActivity;
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

public abstract class BaseActivity extends BaActivity implements BaseViewEx, PermissionUtils.PermissionListener, ISkinUpdate, IDynamicNewView, RefreshRecyclerView.RefreshListener {

    private Unbinder            mUnbinder;
    private List<BasePresenter> mBasePresenters;
    private PermissionUtils     mPermissionUtils;
    private SkinInflaterFactory mSkinInflaterFactory;

    /**
     * 防止 主题设置成透明样式后多次点击造成多次跳转
     */
    private long mLastStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPermissionUtils = new PermissionUtils(this);
        mPermissionUtils.onReBackState(savedInstanceState);
        mPermissionUtils.setPermissionListener(this);
        mSkinInflaterFactory = new SkinInflaterFactory();
        mSkinInflaterFactory.setAppCompatActivity(this);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        mBasePresenters = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setStatusTrans();
        changeStatusColor();
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        dynamicAddView(mToolLayer, "background", R.drawable.ic_toolbar_bg);
        dealSwipBack();

        initData();
    }

    protected void initData() {

    }

    private void dealSwipBack() {
        SwipeBackFixLayout swipeBackLayout = new SwipeBackFixLayout(this);
        swipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
        swipeBackLayout.setSwipeFromEdge(true);
        if (activityAddSwipBackLayout()) {
            swipeBackLayout.attachToActivity(this);
        }
    }

    protected abstract boolean activityAddSwipBackLayout();

    @Override
    protected void bindView() {
        super.bindView();
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    protected void hiddenToolBar() {
        getSupportActionBar().hide();
        mToolLayer.setVisibility(View.GONE);
    }

    protected void bindRefresh(DesignRefreshRecyclerView designRefreshLayout) {
        bindRefreshView(designRefreshLayout);
        designRefreshLayout.setRefreshListener(this);
    }

    @Subscribe
    public void onEvent(DataUpdateEvent dataUpdateEvent) {
        if (dataUpdateEvent.isSelf(this.getClass())) {
            for (BasePresenter basePresenter : mBasePresenters) {
                basePresenter.loading();
            }
        }
    }

    @Override
    protected abstract void initView();

    @Override
    public void setPresener(BasePresenter basePresenter) {
        if (mBasePresenters.contains(basePresenter)) {
            return;
        }
        mBasePresenters.add(basePresenter);
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

    public void showMessDialog(String title, String content, String cancelLeft, String okRight, MessageInfoDialog.DialogButtonClick dialogButtonClick, MessageInfoDialog.DialogCancelButtonClick cancelButtonClick) {
        MessageInfoDialog messageInfoDialog = new MessageInfoDialog(mContext);
        messageInfoDialog.setButtonTitle(okRight, cancelLeft);
        messageInfoDialog.setTitle(title);
        messageInfoDialog.setMessage(content);
        messageInfoDialog.setDialogCancelButtonClick(cancelButtonClick, false);
        messageInfoDialog.setDialogButtonClick(dialogButtonClick);
        messageInfoDialog.show();
    }

    @Override
    public void finishView() {
        finish();
    }


    @Override
    public void onThemeUpdate() {
        mSkinInflaterFactory.applySkin();
        changeStatusColor();
    }

    public void changeStatusColor() {
        if (!SkinConfig.isCanChangeStatusColor()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = SkinResourcesUtils.getColorPrimaryDark();
            if (color != -1) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(SkinResourcesUtils.getColorPrimaryDark());
            }
        }
    }

    public SkinInflaterFactory getSkinInflaterFactory() {
        return mSkinInflaterFactory;
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {
        mSkinInflaterFactory.dynamicAddFontEnableView(this, textView);
    }

    @Override
    public void checkPermission(String... permission) {
        mPermissionUtils.permissionCheckAll(permission);
    }

    @Override
    public void checkAllPermission(String... permission) {
        mPermissionUtils.permissionCheckAll(permission);
        mPermissionUtils.setMust(true);
    }

    /**
     * 申请权限工具
     */
    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
        mPermissionUtils.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.unBindView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPermissionUtils.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void permissionSuccess() {
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.permissionSuccess();
        }
    }

    @Override
    public void permissionFailure() {
        for (BasePresenter basePresenter : mBasePresenters) {
            basePresenter.permissionFailure();
        }
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
    public void startActivity(Intent intent) {
        //时间太小，不跳转
        if (System.currentTimeMillis() - mLastStartTime < 500) {
            return;
        }
        mLastStartTime = System.currentTimeMillis();
        super.startActivity(intent);
    }

    @Override
    public Application findApp() {
        return getApplication();
    }


    @Override
    public void dismissWaitDialog() {
        try {
            super.dismissWaitDialog();
        } catch (Exception e) {

        }
    }

    @Override
    public void dismissWaitDialogWithoutAnim() {
        try {
            super.dismissWaitDialogWithoutAnim();
        } catch (Exception e) {

        }
    }
}

