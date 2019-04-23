package com.hongguo.read.utils.atlas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.runtime.ActivityTaskMgr;
import android.text.TextUtils;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.PackSoBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.update.DownService;
import com.losg.library.utils.AppUtils;
import com.losg.library.utils.CommonUtils;
import com.losg.library.utils.JsonUtils;
import com.taobao.atlas.dex.util.FileUtils;
import com.taobao.atlas.update.AtlasUpdater;
import com.taobao.atlas.update.model.UpdateInfo;

import org.osgi.framework.BundleException;

import java.io.File;

/**
 * Created by losg on 2018/4/9.
 */

public class AtlasUtils {

    /**
     * 初始化 atlas
     *
     * @param context
     */
    public static void initAtlas(Context context, ApiService apiService, DownService downService) {

        Atlas.getInstance().setClassNotFoundInterceptorCallback(intent -> {
            final String className = intent.getComponent().getClassName();
            final String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className);

            if (!TextUtils.isEmpty(bundleName) && !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {

                //远程bundle
                Activity activity = ActivityTaskMgr.getInstance().peekTopActivity();
                File remoteBundleFile = new File(FileManager.getPackSoDownPath(), "lib" + bundleName.replace(".", "_") + ".so");

                String path = "";
                if (remoteBundleFile.exists()) {
                    path = remoteBundleFile.getAbsolutePath();
                } else {
                    getSoUrlToDown("lib" + bundleName.replace(".", "_") + ".so", apiService, downService);
                    StatisticsUtils.collect("补丁", "下载SO模块" + bundleName);
                    CommonUtils.toastMessage(context, "加载模块，请稍候再次尝试");
                    return intent;
                }

                PackageInfo info = activity.getPackageManager().getPackageArchiveInfo(path, 0);
                try {
                    Atlas.getInstance().installBundle(info.packageName, new File(path));
                    StatisticsUtils.collect("补丁", "安装SO成功");
                } catch (BundleException e) {
                    e.printStackTrace();
                    StatisticsUtils.collect("补丁", "安装SO失败" + e);
                }
                activity.startActivities(new Intent[]{intent});
            }
            return intent;
        });
    }

    private static void getSoUrlToDown(String soName, ApiService apiService, DownService downService) {

        apiService.getTopicInfo(CmsTopicInfo.ATLAS_SO_DOWN).compose(CmsTopicDeal.cmsTopDeal(PackSoBean.class)).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<PackSoBean>() {
            @Override
            public void onNext(PackSoBean packSoBean) {
                super.onNext(packSoBean);
                downSo(soName, packSoBean.soBaseUrl + soName, downService);
            }

        });

    }

    private static void downSo(String soName, String downUrl, DownService downService) {
        downService.addTask(downUrl, FileManager.getTempPath(soName), FileManager.getPackSoDownPath() + soName, new DownService.DownLoadListener() {
            @Override
            public void downError(String errorMessage) {

            }

            @Override
            public void success(String fileUrl, String savePath) {

            }
        });
    }

    /**
     * 不能在主线程总调用(必须在子线程中操作)
     *
     * @param context
     * @return
     */
    public static boolean updatePack(Context context, String dir) {
        try {
            String versionName = AppUtils.getVersionName(context);
            //更新文件
            File updateInfo = new File(dir, "update-" + versionName + ".json");
            if (!updateInfo.exists()) {
                return false;
            }
            String jsonStr = new String(FileUtils.readFile(updateInfo));
            UpdateInfo info = JsonUtils.fromJson(jsonStr, UpdateInfo.class);

            //补丁包
            File patchFile = new File(dir, "patch-" + info.updateVersion + "@" + info.baseVersion + ".tpatch");
            AtlasUpdater.update(info, patchFile);
            StatisticsUtils.collect("补丁", "成功");
            return true;
        } catch (Throwable e) {
            StatisticsUtils.collect("补丁", "失败" + Build.MANUFACTURER + "--" + Build.MODEL + e);
            return false;
        }
    }
}
