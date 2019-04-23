package com.hongguo.read.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/5.
 */

public class PackageUtils {

    /**
     * 支付宝包名
     */
    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";
    /**
     * 微信包名
     */
    private static final String WEI_XIN_PACKAGE_NAME = "com.tencent.mm";

    /**
     * Package manager has died
     * @param context
     * @return
     */
    public static List<String> getInstallPackage(Context context){
        synchronized (PackageUtils.class) {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
            List<String> list = new ArrayList<>();
            for (PackageInfo installedPackage : installedPackages) {
                list.add(installedPackage.packageName);
            }
            return list;
        }
    }

    public static boolean alipayInstalled(Context context){
        List<String> installPackage = getInstallPackage(context);
        return installPackage.contains(ALIPAY_PACKAGE_NAME);
    }

    public static boolean weixinInstalled(Context context){
        List<String> installPackage = getInstallPackage(context);
        return installPackage.contains(WEI_XIN_PACKAGE_NAME);
    }

}
