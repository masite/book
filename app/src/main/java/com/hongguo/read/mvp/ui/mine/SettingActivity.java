package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.SettingContractor;
import com.hongguo.read.mvp.contractor.version.VersionContractor;
import com.hongguo.read.mvp.presenter.mine.SettingPresenter;
import com.hongguo.read.mvp.presenter.version.VersionPresenter;
import com.hongguo.read.utils.update.UpdateDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/7.
 */

public class SettingActivity extends ActivityEx implements SettingContractor.IView, VersionContractor.IView {

    @Inject
    SettingPresenter mSettingPresenter;
    @Inject
    VersionPresenter mVersionPresenter;

    @BindView(R.id.switch_night)
    SwitchCompat   mSwitchNight;
    @BindView(R.id.switch_vol_page)
    SwitchCompat   mSwitchVolPage;
    @BindView(R.id.auto_down)
    SwitchCompat   mAutoDown;
    @BindView(R.id.cache)
    TextView       mCache;
    @BindView(R.id.clear_cache)
    RelativeLayout mClearCache;
    @BindView(R.id.current_verion)
    TextView       mCurrentVerion;
    @BindView(R.id.new_version_mark)
    TextView       mNewVersionMark;
    @BindView(R.id.version_check)
    RelativeLayout mVersionCheck;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("偏好设置");
        mSettingPresenter.bingView(this);
        mSettingPresenter.loading();

        mVersionPresenter.bingView(this);
        mVersionPresenter.loading();
        mVersionPresenter.justCheckVersion();
    }

    @ViewMethod
    public void initSetting(boolean isNeight, boolean volumePage, boolean autoUpdate) {
        mSwitchNight.setChecked(isNeight);
        mSwitchVolPage.setChecked(volumePage);
        mAutoDown.setChecked(autoUpdate);
    }

    @ViewMethod
    public void setCache(String cache) {
        mCache.setText(cache);
    }

    @ViewMethod
    public void setVersion(String version) {
        mCurrentVerion.setText(version);
    }


    @OnClick(R.id.switch_night)
    void changeNeight() {
        mSettingPresenter.changeNightMode(mSwitchNight.isChecked());
    }

    @OnClick(R.id.switch_vol_page)
    void changeVolumePage() {
        mSettingPresenter.changeVolumePage(mSwitchVolPage.isChecked());
    }

    @OnClick(R.id.auto_down)
    void changeAutoDown() {
        mSettingPresenter.changeUpdate(mAutoDown.isChecked());
    }

    @OnClick(R.id.clear_cache)
    void clearCache() {
        mSettingPresenter.clearCache();
    }

    @OnClick(R.id.version_check)
    void checkVersion() {
        mVersionPresenter.versionCheck(false,true);
    }

    @OnClick(R.id.about_us)
    void aboutUs() {
        AboutUsActivity.toActivity(mContext);
    }


    @Override
    public void needUpdate(boolean hasNew) {
        mNewVersionMark.setVisibility(hasNew ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAppUpdateDialog(String title, String message, boolean force, UpdateDialog.UpdateDialogClickListener updateDialogClickListener) {
        UpdateDialog updateDialog = new UpdateDialog(mContext);
        updateDialog.setTitle(title);
        if (force)
            updateDialog.setForce();
        updateDialog.showDescribe(message);
        updateDialog.setUpdateDialogClickListener(updateDialogClickListener);
        updateDialog.show();
    }

}
