package com.hongguo.read.mvp.presenter.mine;

import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.BaApp;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.FileManager;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.SkinContractor;
import com.hongguo.read.mvp.model.mine.SkinBean;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;
import com.hongguo.read.utils.update.DownService;
import com.hongguo.common.widget.skin.SkinLoaderListener;
import com.hongguo.common.widget.skin.loader.SkinManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SkinPresenter extends BaseImpPresenter<SkinContractor.IView> implements SkinContractor.IPresenter {

    private List<SkinBean.SkinlistBean>        mSkinlistBeans;
    private List<DownService.DownLoadListener> mDownLoadListeners;

    @Inject
    public SkinPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mDownLoadListeners = new ArrayList<>();
    }

    @Override
    public void loading() {
        mView.setCurrentSelected(AppRepertory.getCurrentSkinName());
        querySkin();
    }

    @PresenterMethod
    public void querySkin() {
        mApiService.getTopicInfo(CmsTopicInfo.SKIN_LIST).compose(CmsTopicDeal.cmsTopDeal(SkinBean.class)).flatMap(skinBean -> {
            mSkinlistBeans = skinBean.skinlist;
            for (SkinBean.SkinlistBean skinlistBean : skinBean.skinlist) {
                //判断文件是否存在
                File file = new File(FileManager.getSkinPath(skinlistBean.name));
                //vip 皮肤判断
                if (skinlistBean.vipstatus == 1 && !(UserRepertory.userIsVip() || UserRepertory.userIsSVip())) {
                    skinlistBean.enable = false;
                    skinlistBean.showMsg = "会员专享";
                } else {
                    skinlistBean.enable = true;
                    if (file.exists()) {
                        skinlistBean.fileExist = true;
                        skinlistBean.showMsg = "使用";
                    } else {
                        if(AppRepertory.getCurrentSkinName().equals(skinlistBean.name)){
                            AppRepertory.setCurrentSkinName("");
                        }
                        skinlistBean.showMsg = "下载";
                    }
                }
            }
            return Observable.just(skinBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(skin -> {
            mView.setSkinList(skin.skinlist);
        }));
    }

    @PresenterMethod
    public void downSkin(int position, String url, String name) {
        DownService.DownLoadListener downLoadListener = new DownService.DownLoadListener() {
            @Override
            public void downError(String errorMessage) {
                mView.toastMessage(errorMessage);
                SkinBean.SkinlistBean skinlistBean = mSkinlistBeans.get(position);
                skinlistBean.enable = true;
                skinlistBean.showMsg = "重新下载";
                mView.setSkinList(mSkinlistBeans);
            }

            @Override
            public void success(String fileUrl, String savePath) {
                SkinBean.SkinlistBean skinlistBean = mSkinlistBeans.get(position);
                skinlistBean.enable = true;
                skinlistBean.fileExist = true;
                skinlistBean.showMsg = "使用";
                mView.setSkinList(mSkinlistBeans);
            }

            @Override
            public void waitDown() {
                super.waitDown();
                SkinBean.SkinlistBean skinlistBean = mSkinlistBeans.get(position);
                skinlistBean.enable = false;
                skinlistBean.showMsg = "等待";
                mView.setSkinList(mSkinlistBeans);
            }

            @Override
            public void currentProgress(int currentSize, int maxSize, int progress) {
                super.currentProgress(currentSize, maxSize, progress);
                SkinBean.SkinlistBean skinlistBean = mSkinlistBeans.get(position);
                skinlistBean.enable = false;
                skinlistBean.showMsg = "已下载" + progress + "%";
                mView.setSkinList(mSkinlistBeans);
            }
        };
        mDownLoadListeners.add(downLoadListener);
        ((BaApp)mView.findApp()).getDownService().addTask(url, FileManager.getTempPath(name), FileManager.getSkinPath(name), downLoadListener);
    }

    @PresenterMethod
    public void changeSkin(String skinName) {
        mView.showWaitDialog(true, "正在切换主题", null);
        SkinManager.getInstance().loadSkin(FileManager.getSkinPath(skinName), new SkinLoaderListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                mView.dismissWaitDialog();
                mView.setCurrentSelected(skinName);
                AppRepertory.setCurrentSkinName(skinName);
            }

            @Override
            public void onFailed(String errMsg) {
                mView.dismissWaitDialog();
                mView.toastMessage(errMsg);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @PresenterMethod
    public void changeDefaultSkin() {
        if(TextUtils.isEmpty(AppRepertory.getDefaultSkinName())){
            SkinManager.getInstance().restoreDefaultTheme();
            AppRepertory.setCurrentSkinName("");
            mView.setCurrentSelected("");
        }else{
            changeSkin(AppRepertory.getDefaultSkinName());
        }
    }

    @Override
    public void unBindView() {
        super.unBindView();
        for (DownService.DownLoadListener downLoadListener : mDownLoadListeners) {
            ((BaApp)mView.findApp()).getDownService().removeDownListener(downLoadListener);
        }
    }
}