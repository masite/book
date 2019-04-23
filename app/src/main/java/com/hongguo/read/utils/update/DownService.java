package com.hongguo.read.utils.update;

import android.os.Handler;
import android.text.TextUtils;

import com.hongguo.common.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/***
 * 文件下载统一管理
 */
public class DownService {

    //开启现成数量
    private static final int THREAD_NUMBER = 1;

    //优先级高的下载任务
    private List<Task> mHeighPriority = new ArrayList<>();
    //优先级低的下载任务
    private List<Task> mLowerPriority = new ArrayList<>();
    //正在下载的url
    private List<Task> mDowningUrl    = new ArrayList<>();

    private List<DownTask> mDownTasks = new ArrayList<>();

    private Semaphore mSemaphore = new Semaphore(0);

    private Handler mHandler = new Handler();

    public static DownService createDownService() {
        return new DownService();
    }

    /**
     * 初始化下载线程
     */
    public void init() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            DownTask downTask = new DownTask();
            mDownTasks.add(downTask);
            downTask.start();
        }
    }

    /**
     * 添加下载任务
     *
     * @param url              下载地址
     * @param savePath         下载路径
     * @param downLoadListener 下载监听
     */
    public synchronized void addTask(String url, String tempPath, String savePath, DownLoadListener downLoadListener) {
        Task task = null;
        int taskIndex = taskExits(mLowerPriority, url);
        int downIndex = taskExits(mDowningUrl, url);
        if (taskIndex != -1) {
            task = mLowerPriority.get(taskIndex);
        } else if (downIndex != -1) {
            task = mDowningUrl.get(downIndex);
        } else {
            task = new Task();
        }
        task.urlPath = url;
        task.savePath = savePath;
        task.tempPath = tempPath;
        task.downloadListener = downLoadListener;
        if (taskIndex == -1 && downIndex == -1) {
            mLowerPriority.add(task);
            waitDown();
            mSemaphore.release();
        }
    }

    /**
     * 添加 任务级别高的下载任务
     *
     * @param url
     * @param savePath
     * @param downLoadListener
     */
    public synchronized void addHeighPriorityTask(String url, String tempPath,String savePath, DownLoadListener downLoadListener) {
        Task task = null;
        int taskIndex = taskExits(mHeighPriority, url);
        int downIndex = taskExits(mDowningUrl, url);
        if (taskIndex != -1) {
            task = mHeighPriority.get(taskIndex);
        } else if (downIndex != -1) {
            task = mDowningUrl.get(downIndex);
        } else {
            task = new Task();
        }
        task.urlPath = url;
        task.savePath = savePath;
        task.tempPath = tempPath;
        task.downloadListener = downLoadListener;
        if (taskIndex == -1 && downIndex == -1) {
            mHeighPriority.add(task);
            waitDown();
            mSemaphore.release();
        }
    }

    /**
     * 判断任务是否存在
     *
     * @param tasks
     * @param url
     * @return
     */
    private int taskExits(List<Task> tasks, String url) {
        int position = -1;
        for (Task task : tasks) {
            position++;
            if (task.urlPath.equals(url)) {
                return position;
            }
        }
        return -1;
    }


    /**
     * 清楚下载线程，防止内存泄露
     *
     * @param downLoadListener
     */
    public void removeDownListener(DownLoadListener downLoadListener) {

        //清楚正在下载的任务
        for (DownTask downTask : mDownTasks) {
            downTask.clearListener(downLoadListener);
        }

        for (Task task : mHeighPriority) {
            if (task.downloadListener == downLoadListener) {
                task.downloadListener = null;
                break;
            }
        }

        for (Task task : mLowerPriority) {
            if (task.downloadListener == downLoadListener) {
                task.downloadListener = null;
                break;
            }
        }

        for (Task task : mDowningUrl) {
            if (task.downloadListener == downLoadListener) {
                task.downloadListener = null;
                break;
            }
        }

    }


    /**
     * 获取下载任务 (先从级别高的-->普通级别 同步锁)
     *
     * @return
     */
    public synchronized Task getTask() {
        Task task = null;
        task = getHeighNextTask();
        if (task == null) {
            task = getNextTask();
        }
        return task;
    }

    /**
     * 获取普通下载任务
     *
     * @return
     */
    public Task getNextTask() {
        if (mLowerPriority.size() == 0) return null;
        Task task = mLowerPriority.remove(0);
        mDowningUrl.add(task);
        return task;

    }

    /**
     * 获取下一个高级别任务
     *
     * @return
     */
    public Task getHeighNextTask() {
        if (mHeighPriority.size() == 0) return null;
        Task pop = mHeighPriority.remove(mHeighPriority.size() - 1);
        mDowningUrl.add(pop);
        return pop;
    }


    /**
     * 下载实现
     */
    private class DownTask extends Thread {

        private Task mCurrentTask;

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    //没任务挂起线程
                    mSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取下一个任务，去执行
                Task task = getTask();
                if (task != null) {
                    mCurrentTask = task;
                    downLoadFile(task);
                    mSemaphore.release();
                    mDowningUrl.remove(task);
                }
            }
        }

        /**
         * 清楚绑定监听(防 内存泄露)
         *
         * @param downLoadListener
         */
        public void clearListener(DownLoadListener downLoadListener) {
            if (mCurrentTask != null && mCurrentTask.downloadListener == downLoadListener) {
                mCurrentTask.downloadListener = null;
            }
        }

        /**
         * 下载实现
         *
         * @param task
         */
        private void downLoadFile(Task task) {
            String errorMessage = "";
            File file = new File(task.tempPath);
            File saveFile = new File(task.savePath);
            FileUtils.createFileIfNotExit(file);
            FileUtils.createFileIfNotExit(saveFile);

            //开始下载
            mHandler.post(() -> {
                if (task.downloadListener != null)
                    task.downloadListener.downStart();
            });

            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(task.urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setConnectTimeout(30 * 1000); //30s
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    int totalSize = conn.getContentLength();

                    //文件已经下载过,直接返回成功
                    if (saveFile.exists() && saveFile.length() == totalSize) {
                        publishProgress(mCurrentTask, totalSize, totalSize, 100);
                        pushSuccess(task);
                        return;
                    }

                    //没有下载过，继续下载
                    inputStream = conn.getInputStream();
                    fileOutputStream = new FileOutputStream(file);
                    int readLenght = 0;
                    int totalRead = 0;
                    long mCurrentTime = System.currentTimeMillis();
                    byte[] bt = new byte[4 * 1024];  //4kb
                    publishProgress(mCurrentTask, 0, totalSize, 0);
                    while ((readLenght = inputStream.read(bt)) != -1) {
                        fileOutputStream.write(bt, 0, readLenght);
                        totalRead += readLenght;
                        //一秒更新一次，避免卡顿
                        if (System.currentTimeMillis() - mCurrentTime >= 1000) {
                            int mCurrentPercent = totalSize == 0 ? 0 : (int) ((float) totalRead / totalSize * 100);
                            //更新下载进度
                            publishProgress(mCurrentTask, totalRead, totalSize, mCurrentPercent);
                            mCurrentTime = System.currentTimeMillis();
                        }
                    }

                    FileUtils.closeInput(inputStream);
                    FileUtils.closeOutput(fileOutputStream);
                    //将temp 文件拷贝至正式文件,删除临时文件
                    FileUtils.copyFile(task.tempPath, task.savePath);
                    publishProgress(mCurrentTask, totalSize, totalSize, 100);
                } else {
                    errorMessage = "连接服务器失败,请稍后再试";
                }
            } catch (Exception e) {
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    errorMessage = "连接服务器失败，请检查当前网络";
                } else if (e instanceof SocketTimeoutException) {
                    errorMessage = "连接服务器超时";
                } else {
                    errorMessage = "下载失败";
                }
            } finally {
                FileUtils.closeInput(inputStream);
                FileUtils.closeOutput(fileOutputStream);
            }
            if (TextUtils.isEmpty(errorMessage)) {
                pushSuccess(task);
                return;
            }
            pushError(task, errorMessage);
        }

        private void publishProgress(Task task, int currentSize, int totalSize, int progress) {
            mHandler.post(() -> {
                if (task.downloadListener != null)
                    task.downloadListener.currentProgress(currentSize, totalSize, progress);
            });
        }

        private void pushError(Task task, String finalErrorMessage) {
            mHandler.post(() -> {
                if (task.downloadListener != null)
                    task.downloadListener.downError(finalErrorMessage);
            });
        }

        private void pushSuccess(Task task) {
            mHandler.post(() -> {
                if (task.downloadListener != null)
                    task.downloadListener.success(mCurrentTask.urlPath, mCurrentTask.savePath);
            });
        }
    }

    private void waitDown() {
        for (Task task : mLowerPriority) {
            task.downloadListener.waitDown();
        }
        for (Task task : mHeighPriority) {
            task.downloadListener.waitDown();
        }
    }

    public static class Task {
        public String           urlPath;
        public String           savePath;
        public String           tempPath;
        public DownLoadListener downloadListener;
    }

    public abstract static class DownLoadListener {


        public void downStart() {
        }

        public void waitDown() {

        }

        public abstract void downError(String errorMessage);

        public void currentProgress(int currentSize, int maxSize, int progress) {
        }

        public abstract void success(String fileUrl, String savePath);
    }

}
