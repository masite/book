package com.hongguo.read.mvp.presenter;

import android.content.Context;
import android.os.Build;

import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;
import com.hongguo.common.widget.skin.SkinLoaderListenerImp;
import com.hongguo.common.widget.skin.loader.SkinManager;
import com.hongguo.read.BaApp;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.contractor.MainContractor;
import com.hongguo.read.mvp.model.PackBean;
import com.hongguo.read.mvp.model.SkinDefaultBean;
import com.hongguo.read.mvp.model.event.EventDialogBean;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.atlas.AtlasUtils;
import com.hongguo.read.utils.update.DownService;
import com.losg.library.utils.AppUtils;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class MainPresenter extends BaseImpPresenter<MainContractor.IView> implements MainContractor.IPresenter {

    private DownService.DownLoadListener mDownLoadListener;
    @Inject
    @ContextLife
    Context mContext;

    @Inject
    public MainPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryEvent();
        queryDefaultSkin();
        //Android P 标准化API调用方式,无法实现热更新以及插件化设计
        if (Build.VERSION.SDK_INT <= 27) {
            queryPack();
        }else{
            mView.checkVersion();
        }
    }

    private void queryEvent() {
        mApiService.getTopicInfo(CmsTopicInfo.EVENT_DIALOG).compose(CmsTopicDeal.cmsTopDeal(EventDialogBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(eventInfo -> {

            String eventFloat = "";
            String eventBigImage = "";
            String eventUrl = "";
            boolean hasEvent = false;
            boolean showEventDialog = false;

            if (eventInfo.events != null) {
                for (EventDialogBean.EventsBean event : eventInfo.events) {
                    String versionName = AppUtils.getVersionName(mContext);
                    //当前版本大于等于活动版本
                    if (versionName.compareTo(event.appVersion) >= 0) {
                        eventFloat = event.eventFloat;
                        eventBigImage = event.eventBigImage;
                        eventUrl = event.eventUrl;
                        hasEvent = true;

                        if (AppRepertory.getEventCode() < event.eventVersion) {
//                            showEventDialog = true;
                            AppRepertory.setEventCode(event.eventVersion);
                        }
                        break;
                    }
                }
            }
            mView.setEventInfo(eventFloat, eventBigImage, eventUrl, hasEvent, showEventDialog);
        }));
    }

    private void queryDefaultSkin() {
        mApiService.getTopicInfo(CmsTopicInfo.SKIN_DEFAULT).compose(CmsTopicDeal.cmsTopDeal(SkinDefaultBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(skins -> {
            AppRepertory.setDefaultSkinName("defaultV" + skins.skinVersion);
            if (skins.skinTimeout) {
                //当前是节日皮肤, 重置默认皮肤
                if (!textEmpty(AppRepertory.getCurrentSkinName()) && AppRepertory.getCurrentSkinName().equals(AppRepertory.getDefaultSkinName())) {
                    SkinManager.getInstance().restoreDefaultTheme();
                }
                AppRepertory.setDefaultSkinName("");
            } else {
                String defaultPath = FileManager.getSkinPath(AppRepertory.getDefaultSkinName());
                boolean defaultSkinExist = FileUtils.fileExist(defaultPath);
                //当前主题为空或者  当前不是默认主题但是文件不存在，下载并切换默认皮肤
                if (textEmpty(AppRepertory.getCurrentSkinName()) || (!defaultSkinExist && !AppRepertory.getCurrentSkinName().equals(AppRepertory.getDefaultSkinName()))) {
                    //下载并切换切换主题
                    downSkin(skins.skinDownUrl, "defaultV" + skins.skinVersion);
                }
            }
        }));
    }

    private void downSkin(String url, String name) {
        mDownLoadListener = new DownService.DownLoadListener() {
            @Override
            public void downError(String errorMessage) {

            }

            @Override
            public void success(String fileUrl, String savePath) {
                changeSkin(name);
            }
        };
        ((BaApp) mView.findApp()).getDownService().addTask(url, FileManager.getTempPath(name), FileManager.getSkinPath(name), mDownLoadListener);
    }

    private void changeSkin(String skinName) {
        SkinManager.getInstance().loadSkin(FileManager.getSkinPath(skinName), new SkinLoaderListenerImp() {
            @Override
            public void onSuccess() {
                AppRepertory.setCurrentSkinName(skinName);
            }
        });
    }

    @Override
    public void unBindView() {
        super.unBindView();
        if (mDownLoadListener != null)
            ((BaApp) mView.findApp()).getDownService().removeDownListener(mDownLoadListener);
    }

    private void queryPack() {

        mApiService.getTopicInfo(CmsTopicInfo.ATLAS_UPDATE).compose(CmsTopicDeal.cmsTopDeal(PackBean.class)).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<PackBean>() {
            @Override
            public void onNext(PackBean packBean) {
                super.onNext(packBean);
                String versionName = AppUtils.getVersionName(mContext);
                //需要更新当前版本
                if (versionName.compareTo(packBean.baseVersion) >= 0 && versionName.compareTo(packBean.currentVersion) < 0) {
                    downPack(packBean.downUrlBase, versionName);
                }else{
                    mView.checkVersion();
                }
            }
        });

    }

    private void downPack(String downUrlBase, String versionName) {
        ((BaApp) mView.findApp()).getDownService().addTask(downUrlBase + versionName + ".zip", FileManager.getTempPath("versionName_pack"), FileManager.getApkDownPath(versionName), new DownService.DownLoadListener() {
            @Override
            public void downError(String errorMessage) {
                FileUtils.deleteFile(new File(FileManager.getTempPath("versionName_pack")));
            }

            @Override
            public void success(String fileUrl, String savePath) {
                unZipFile(savePath, FileManager.getPackDownPath());
            }
        });
    }

    private void unZipFile(String savePath, String armDir) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            boolean result = FileUtils.ZipFile(savePath, armDir);
            emitter.onNext(result);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(aBoolean -> {
            installPack(armDir + "");
        });
    }

    /**
     * 更新app
     */
    private void installPack(String armDir) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            boolean result = AtlasUtils.updatePack(mContext, armDir + "/" + AppUtils.getVersionName(mContext));
            emitter.onNext(result);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(aBoolean -> {
            if(!aBoolean){
                mView.checkVersion();
            }
        });
    }

}