package com.hongguo.common.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by losg on 17-12-19.
 */

public class FileUtils {

    /**
     * 创建文件所在文件环境
     */
    public static void createFileIfNotExit(String filePath) {
        File file = new File(filePath);
        createFileIfNotExit(file);
    }


    public static void createFileIfNotExit(File file) {
        if (file.exists()) return;
        File parentFile = file.getParentFile();
        parentFile.mkdirs();
    }


    /**
     * 创建文件文件夹
     */
    public static void createDirIfNotExit(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) return;
        file.mkdirs();
    }

    public static boolean fileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        try {
            file.delete();
        }catch (Exception e){}
    }

    /**
     * 删除文件夹
     *
     * @param dir
     */
    public static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory() && dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        if (dir == null) {
            return;
        }
        dir.delete();
    }

    /**
     * 拷贝文件
     *
     * @param orginFilePath
     * @param armFilePath
     */
    public static boolean copyFile(String orginFilePath, String armFilePath) {
        return copyFile(orginFilePath, armFilePath, true);
    }

    /**
     * 拷贝文件 并删除临时文件
     *
     * @param orginFilePath
     * @param armFilePath
     */
    public static boolean copyFile(String orginFilePath, String armFilePath, boolean deleteTemp) {
        File orginFile = new File(orginFilePath);
        File armFile = new File(armFilePath);

        createFileIfNotExit(armFile);

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(orginFile);
            fileOutputStream = new FileOutputStream(armFile);
            byte[] temp = new byte[1024 * 30];
            int readLength;
            while ((readLength = fileInputStream.read(temp)) != -1) {
                fileOutputStream.write(temp, 0, readLength);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInput(fileInputStream);
            closeOutput(fileOutputStream);
            if (deleteTemp)
                deleteFile(orginFile);
        }
        return false;
    }

    /**
     * 拷贝流
     */
    public static boolean copyStream(InputStream fileInputStream, OutputStream fileOutputStream) {
        try {
            byte[] temp = new byte[1024 * 30];
            int readLength;
            while ((readLength = fileInputStream.read(temp)) != -1) {
                fileOutputStream.write(temp, 0, readLength);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInput(fileInputStream);
            closeOutput(fileOutputStream);
        }
        return false;
    }

    /**
     * 解压文件
     *
     * @param zipFilePath
     * @param armDir
     * @return
     */
    public static boolean ZipFile(String zipFilePath, String armDir) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                //是文件夹
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    String path = armDir + "/" + name;
                    createDirIfNotExit(path);
                } else {
                    File file = new File(armDir + File.separator + zipEntry.getName());
                    if (!file.getParentFile().exists()) {
                        createDirIfNotExit(file.getParentFile().getPath());
                    }
                    createFileIfNotExit(file);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    copyStream(inputStream, new FileOutputStream(file));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeZipFile(zipFile);
        }
        return false;
    }


    public static void writeFile(String path, String content) {
        writeFile(path, content, false);
    }

    public static void writeFile(String path, String content, boolean append) {
        createFileIfNotExit(path);

        if (append) {
            String contentOrigin = readFullFile(new File(path));
            if (contentOrigin.length() < 5000) {
                content += contentOrigin;
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            content = content.replaceAll("<br/>", "\r\n");
            fileOutputStream.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeOutput(fileOutputStream);
        }
    }


    public static void closeZipFile(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                zipFile = null;
            }
        }
    }


    public static void closeOutput(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream = null;
            }
        }
    }

    public static void closeInput(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }
    }

    public static void downLoadFile(String fileUrl, String filePath, FileDownLoadCallBack FileDownLoadCallBack) {
        String errorMessage = "";
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileDownLoadCallBack.downStart();
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setConnectTimeout(30 * 1000); //30s
            conn.connect();
            if (conn.getResponseCode() == 200) {
                int totalSize = conn.getContentLength();
                //文件已经下载过,直接返回成功
                if (file.exists() && file.length() == totalSize) {
                    FileDownLoadCallBack.success(filePath);
                    return;
                }
                //没有下载过，继续下载
                inputStream = conn.getInputStream();
                fileOutputStream = new FileOutputStream(file);
                int readLenght = 0;
                byte[] bt = new byte[4 * 1024];  //4kb
                while ((readLenght = inputStream.read(bt)) != -1) {
                    fileOutputStream.write(bt, 0, readLenght);
                }
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
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fileOutputStream = null;
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    inputStream = null;
                }
            }
        }
        if (TextUtils.isEmpty(errorMessage)) {
            FileDownLoadCallBack.success(filePath);
            return;
        }
        FileDownLoadCallBack.downError(errorMessage);
    }


    public static String readFullFile(File file) {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            fileInputStream = new FileInputStream(file);
            int readByte;
            while ((readByte = fileInputStream.read()) != -1) {
                arrayOutputStream.write(readByte);
            }
            return arrayOutputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInput(fileInputStream);
            closeOutput(arrayOutputStream);
        }
        return "";
    }

    public abstract static class FileDownLoadCallBack {

        public void downStart() {
        }

        public abstract void downError(String errorMessage);

        public abstract void success(String savePath);
    }


}
