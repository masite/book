package com.hongguo.read.utils;

import android.content.Context;
import android.content.Intent;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.BuildConfig;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.ui.loading.LoadingActivity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CrashHandler implements UncaughtExceptionHandler {

    private Context mContext;

    public CrashHandler(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Intent intent = new Intent(mContext, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        writeError(throwable);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void writeError(Throwable ex) {
        if (!BuildConfig.DEBUG) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("崩溃时间----" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "----\n");
        StackTraceElement[] stackTrace = ex.getStackTrace();
        sb.append((ex.getCause() != null ? ex.getCause().toString() : "") + "\n");
        if (stackTrace != null) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                sb.append(stackTraceElement.toString().replace("\n", "") + "\n");
            }
        }
        String result = sb.toString();
        LogUtils.log(result);
        writeErrorInfo(result);
    }

    private void writeErrorInfo(String result) {
        FileUtils.writeFile(FileManager.getErrorReport(), result, true);

    }
}
