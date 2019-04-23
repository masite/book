package com.hongguo.read.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by losg on 2018/4/13.
 */

public class AndroidInstaller {

    private Context mContext;
    private String  mApkPath;

    public void intallApk(Context context, String apk) {
        mContext = context;
        mApkPath = apk;
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                com.losg.library.utils.AppUtils.installApp(context, apk);
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10010);
            }
        } else {
            com.losg.library.utils.AppUtils.installApp(context, apk);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10010:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    com.losg.library.utils.AppUtils.installApp(mContext, mApkPath);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    ((Activity) mContext).startActivityForResult(intent, 10012);
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10012:
                intallApk(mContext, mApkPath);
                break;
        }
    }
}
