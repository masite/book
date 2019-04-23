package com.hongguo.read.utils.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;

import com.hongguo.read.BaApp;
import com.hongguo.read.R;
import com.hongguo.read.constants.FileManager;

/**
 * Created by losg on 2017/7/27.
 */

public class ApkDownService extends Service {

    private NotificationManager mNotificationManager;
    private DownService         mDownService;

    public static void startSelfService(Context context) {
        Intent intent = new Intent(context, ApkDownService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownService = ((BaApp) getApplication()).getDownService();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownBinder();
    }

    private void createNotification(int value, int totalSize, int percent, boolean voice) {
        if (mNotificationManager == null) return;

        String channelId = "channel_1";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, "红果阅读", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= 26)
            builder = new NotificationCompat.Builder(this, channelId);
        else
            builder = new NotificationCompat.Builder(this);

        builder.setProgress(100, percent, false);
        builder.setContentInfo(percent + "%");
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (voice)
            builder.setDefaults(Notification.DEFAULT_ALL);
        else
            builder.setDefaults(~Notification.DEFAULT_ALL);
        if (totalSize == 0) {
            builder.setContentText("0M / --M");
        } else {
            builder.setContentText(Formatter.formatFileSize(getApplicationContext(), value) + " / " + Formatter.formatFileSize(getApplicationContext(), totalSize));
        }
        builder.setContentTitle(getResources().getString(R.string.app_name) + "正在下载");
        Intent intent = new Intent();
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setColor(Color.WHITE);
        builder.setSmallIcon(R.mipmap.logo_hongguo);
        Notification notification = builder.build();
        mNotificationManager.notify(1, notification);
    }

    public void startDown(String url, String fileName, boolean quiet, DownLoadProgressListener downLoadingProgressListener) {
        mDownService.addTask(url, FileManager.getTempPath(fileName), FileManager.getApkDownPath(fileName), new DownService.DownLoadListener() {

            @Override
            public void downStart() {
                super.downStart();
                if (!quiet)
                    createNotification(0, 0, 0, true);
            }

            @Override
            public void currentProgress(int currentSize, int totalSize, int progress) {
                if (downLoadingProgressListener != null) {
                    downLoadingProgressListener.progress(progress);
                    if (!quiet) {
                        createNotification(currentSize, totalSize, progress, false);
                    }
                }
            }

            @Override
            public void downError(String errorMessage) {
                if (downLoadingProgressListener != null)
                    downLoadingProgressListener.error(errorMessage);
                if (!quiet) {
                    mNotificationManager.cancel(0);
                }
            }

            @Override
            public void success(String fileUrl, String savePath) {
                if (downLoadingProgressListener != null) {
                    downLoadingProgressListener.success(savePath);
                }
                if (!quiet) {
                    mNotificationManager.cancel(0);
                }
            }
        });
    }

    public class DownBinder extends Binder {
        //开始下载任务(quiet true 后台下载, false notification 提示信息下载，下载完成后直接安装)
        public void startDown(String url, String fileName, boolean quiet, DownLoadProgressListener downLoadingProgressListener) {
            ApkDownService.this.startDown(url, fileName, quiet, downLoadingProgressListener);
        }
    }

    public static abstract class DownLoadProgressListener {

        public void progress(int currentProgress) {

        }

        public abstract void success(String filePath);

        public void error(String errorMessage) {

        }
    }

}
