package com.hongguo.read.mvp.contractor.version;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.utils.update.UpdateDialog;

public class VersionContractor {

	public interface IView extends BaseViewEx {
		void showAppUpdateDialog(String title, String message, boolean force, UpdateDialog.UpdateDialogClickListener updateDialogClickListener) ;
		void needUpdate(boolean needUpdate);
		void installApk(String apkPath);
	}

	public interface IPresenter extends BasePresenter {
	}
}