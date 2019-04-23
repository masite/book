package com.hongguo.read.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by losg on 2017/12/20.
 */

public class FileManager {

    /**
     * 主项目目录
     */
    private static final String BASE_PATH = "/hongyue";

    /**
     * 数据库目录(userid + temp)
     */
    private static final String DB_PATH = "/temp";
    /**
     * 数据库名称
     */
    private static final String DB_NAME = ".cache.temp";

    /**
     * 用户书籍下载目录(userid + /temp/file)
     */
    private static final String BOOK_DOWN_PATH     = "/temp/file";
    /**
     * 下载简介位置
     */
    private static final String BOOK_DOWN_DESCRIBE = "/temp/des";

    /**
     * 下载的中间文件
     */
    private static final String TEMP_DOWN = "/temp";

    /**
     * apk下载目录
     */
    private static final String APK_DOWN = "/apk";

    /**
     * 热更新下载目录
     */
    private static final String PACK_DOWN = "/pack";

    /**
     * 错误日志
     */
    private static final String ERROR_REPORT = "/error_report";

    /**
     * 热更新下载目录
     */
    private static final String PACK_DOWN_SO = "/pack/.so";


    /**
     * 皮肤下载路径
     */
    private static final String SKIN_DOWN = "/skin";

    private static String sLocalPath;

    static {
        sLocalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getUserDbPath(String userid) {
        String path = String.format("%s%s/%s%s/%s", sLocalPath, BASE_PATH, userid, DB_PATH, DB_NAME);
        createFileIfNotExit(path);
        return path;
    }

    public static String getBookChapterDownPath(String userid, int booktype, String bookid, String chapterid) {
        String path = String.format("%s%s/%s%s/%s/%s/%s%s", sLocalPath, BASE_PATH, userid, BOOK_DOWN_PATH, booktype, bookid, chapterid, ".temp");
        createFileIfNotExit(path);
        return path;
    }

    public static String getBookDownPath(String userid, int booktype, String bookid) {
        String path = String.format("%s%s/%s%s/%s/%s", sLocalPath, BASE_PATH, userid, BOOK_DOWN_PATH, booktype, bookid);
        return path;
    }

    public static String getBookDownDescribePath(int booktype, String bookid, String chapterid) {
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(BOOK_DOWN_DESCRIBE).append("/").append(booktype).append("/").append(bookid).append("/").append(chapterid).append(".temp").toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getBookDownDescribeDirPath(int booktype, String bookid) {
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(BOOK_DOWN_DESCRIBE).append("/").append(booktype).append("/").append(bookid).toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getTempPath(String fileName) {
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(TEMP_DOWN).append("/").append(fileName).append(".temp").toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getSkinPath(String fileName) {
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(SKIN_DOWN).append("/").append(fileName).append(".skin").toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getApkDownPath(String fileName) {
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(APK_DOWN).append("/").append(fileName).toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getErrorReport() {
        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(ERROR_REPORT).append("/").append("error.dat").toString();
        createFileIfNotExit(path);
        return path;
    }

    public static String getPackDownPath() {

        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(PACK_DOWN).toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getPackSoDownPath() {

        String path = new StringBuilder().append(sLocalPath).append(BASE_PATH).append(PACK_DOWN_SO).toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private static void createFileIfNotExit(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
    }
}

