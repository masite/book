package com.hongguo.read.mvp.presenter.mine;

import android.content.Context;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.utils.DataCleanManager;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.read.mvp.contractor.mine.SettingContractor;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.BookRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.losg.library.utils.AppUtils;

import javax.inject.Inject;

public class SettingPresenter extends BaseImpPresenter<SettingContractor.IView> implements SettingContractor.IPresenter{

	@Inject
	@ContextLife
	Context mContext;

	@Inject
	public SettingPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
		mView.initSetting(AppRepertory.getNightMode(), BookRepertory.getVolPage(), AppRepertory.getAutoUpdate());
		mView.setVersion("当前版本 V"+AppUtils.getVersionName(mContext));
		queryCache();
	}

	private void queryCache() {
		RxJavaUtils.backgroundDeal(new RxJavaUtils.BackgroundDealListener() {
			String totalSize;
			@Override
			public void run() {
				totalSize = DataCleanManager.getTotalCacheSize(mContext);
			}

			@Override
			public void runSuccess() {
				super.runSuccess();
				mView.setCache(totalSize);
			}
		});
	}

	@PresenterMethod
	public void changeNightMode(boolean mode){
		AppRepertory.setNightMode(mode);
	}

	@PresenterMethod
	public void changeVolumePage(boolean volumePage){
		BookRepertory.setVolumePage(volumePage);
	}

	@PresenterMethod
	public void changeUpdate(boolean update){
		AppRepertory.setAutoUpdate(update);
	}

	@PresenterMethod
	public void clearCache(){
		RxJavaUtils.backgroundDeal(new RxJavaUtils.BackgroundDealListener() {
			@Override
			public void run() {
				DataCleanManager.clearAllCache(mContext);
			}

			@Override
			public void runSuccess() {
				mView.setCache("0.00B");
			}
		});
	}



}