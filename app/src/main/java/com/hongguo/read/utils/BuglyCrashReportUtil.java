package com.hongguo.read.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.BuildConfig;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.ui.loading.LoadingActivity;
import com.losg.library.utils.NetworkUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by losg on 2018/5/2.
 * bugly 日志管理
 */

public class BuglyCrashReportUtil {

    public static final String BUGLY_KEY = "845f5088f2";

    /**
     * 初始化Bugly
     * 在崩溃的时候需要重启APP，因为将当前进程直接杀死，导致Bugly无法上传到服务器上，捕捉到错误日志后，保存，在App重启后上传到服务器
     *
     * @param context
     */
    public static void init(Context context) {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(context);
        userStrategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int i, String s, String s1, String s2) {
                Map<String, String> map = super.onCrashHandleStart(i, s, s1, s2);
                //debug 版本记录在本地文件中，线上版本提交到服务器中
                if (BuildConfig.DEBUG) {
                    writeError(s + "--" + s + "--" + s1 + "--\n" + s2);
                } else {
                    new AppCrashException().parseToString(context, s, s1, s2);
                }
                reboot(context);
                return map;
            }
        });
        userStrategy.setUploadProcess(true);
        CrashReport.initCrashReport(context, BUGLY_KEY, BuildConfig.DEBUG, userStrategy);
        Exception exception = new AppCrashException().toException(context);

        //线上版本提交到bugly后台
        if (exception != null && !BuildConfig.DEBUG) {
            if (NetworkUtils.isNetworkConnected(context)) {
                CrashReport.postCatchedException(exception);
                new AppCrashException().deleteCrash(context);
            }
        }
    }

    /**
     * 重启APP
     *
     * @param context
     */
    private static void reboot(Context context) {
        Intent intent = new Intent(context, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent);

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * DEBUG 版本写错误日志文件，方便调试使用
     *
     * @param error
     */
    private static void writeError(String error) {
        if (!BuildConfig.DEBUG) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("崩溃时间----" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "----\n");
        sb.append(error);
        LogUtils.log(error);
        writeErrorInfo(error);
    }

    private static void writeErrorInfo(String result) {
        FileUtils.writeFile(FileManager.getErrorReport(), result, true);
    }
}
