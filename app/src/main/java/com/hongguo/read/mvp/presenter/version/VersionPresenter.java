package com.hongguo.read.mvp.presenter.version;

import android.content.Context;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.read.mvp.contractor.version.VersionContractor;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.utils.update.ApkDownService;
import com.hongguo.read.utils.update.ServiceBindUtils;
import com.losg.library.utils.AppUtils;
import com.losg.library.utils.NetworkUtils;

import javax.inject.Inject;

/**
 * Created by losg on 2018/1/8.
 */

public class VersionPresenter extends BaseImpPresenter<VersionContractor.IView> implements VersionContractor.IPresenter {

    private ServiceBindUtils<ApkDownService.DownBinder> mDownBinderServiceBindUtils;

    @Inject
    @ContextLife
    Context mContext;

    @Inject
    public VersionPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        mDownBinderServiceBindUtils = new ServiceBindUtils<>(mContext);
    }

    @PresenterMethod
    public void versionCheck(boolean quietly, boolean showCheckVersionDialog) {
        RxJavaResponseDeal rxJavaResponseDeal = RxJavaResponseDeal.create(this).withLoading(false);
        if(showCheckVersionDialog)
            rxJavaResponseDeal.widthDialog("正在查询版本");
        mApiService.queryVersion(UserRepertory.getUserID()).compose(rxJavaResponseDeal.commonDeal(versionBean -> {
            if (versionBean.data.ver.compareTo(AppUtils.getVersionName(mContext)) > 0) {
                if (!quietly || !NetworkUtils.isWifiConnected(mContext) || AppRepertory.getAutoUpdate()) {
                    tipUpdate(versionBean.data.link, versionBean.data.desc, versionBean.data.utag == Constants.UPDATE_TYPE.FORCE_UPDATE, versionBean.data.ver);
                } else if (NetworkUtils.isWifiConnected(mContext)) {
                    downAndUpdate(versionBean.data.link, versionBean.data.ver);
                }
            }
        }));
    }

    private void tipUpdate(String url, String messsage, boolean force, String versionName) {
        mView.showAppUpdateDialog("发现新版本", messsage, force, () -> {
            downAndUpdate(url, versionName);
        });
    }

    private void downAndUpdate(String url, String versionName) {
        mDownBinderServiceBindUtils.bindService(ApkDownService.class, downBinder -> {
            downBinder.startDown(url, "hgread_" + versionName + ".apk", false, new ApkDownService.DownLoadProgressListener() {
                @Override
                public void success(String filePath) {
                    mView.installApk(filePath);
                }
            });
        });
    }


    @PresenterMethod
    public void justCheckVersion() {
        mApiService.queryVersion(UserRepertory.getUserID()).compose(RxJavaResponseDeal.create(this).withLoading(false).commonDeal(versionBean -> {
            mView.needUpdate(versionBean.data.ver.compareTo(AppUtils.getVersionName(mContext)) > 0);
        }));

    }
}
