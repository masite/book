package com.hongguo.read.utils;

import android.content.Context;
import android.text.TextUtils;

import com.hongguo.common.utils.FileUtils;
import com.losg.library.utils.JsonUtils;
import com.losg.library.utils.MathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/5/2.
 */

public class AppCrashException {

    public String            errorTitle;
    public String            errorInfo;
    public List<ErrorDetail> stackTraceElements;

    public Exception toException(Context context) {
        String absolutePath = context.getCacheDir().getAbsolutePath();
        String error = absolutePath + "/" + "error/error.dat";
        File file = new File(error);
        if(!file.exists()){
            return null;
        }
        String errorInfo = FileUtils.readFullFile(file);
        if (TextUtils.isEmpty(errorInfo)) return null;
        AppCrashException appCrashException = JsonUtils.fromJson(errorInfo, AppCrashException.class);
        Exception exception = new Exception(appCrashException.errorTitle + "\n" + appCrashException.errorInfo);
        if(appCrashException.stackTraceElements != null && appCrashException.stackTraceElements.size() > 0){
            StackTraceElement[] stackTraceElements = new StackTraceElement[appCrashException.stackTraceElements.size()];
            for (int i = 0; i < appCrashException.stackTraceElements.size(); i++) {
                ErrorDetail errorDetail = appCrashException.stackTraceElements.get(i);
                stackTraceElements[i] = new StackTraceElement(errorDetail.declaringClass, errorDetail.methodName, errorDetail.fileName, errorDetail.lineNumber);
            }
            exception.setStackTrace(stackTraceElements);
        }
        return exception;
    }

    public void deleteCrash(Context context){
        String absolutePath = context.getCacheDir().getAbsolutePath();
        String error = absolutePath + "/" + "error/error.dat";
        File file = new File(error);
        if(!file.exists()){
            return;
        }
        file.delete();
    }

    public void parseToString(Context context, String errorTitle, String errorInfo, String errorDetailStr) {
        this.errorInfo = errorInfo;
        this.errorTitle = errorTitle;
        if (TextUtils.isEmpty(errorDetailStr)) {
            this.stackTraceElements = new ArrayList<>();
        } else {
            String[] split = errorDetailStr.split("\n");
            stackTraceElements = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                String error = split[i];
                ErrorDetail errorDetail = new ErrorDetail();
                String className = error;
                String methodName = "";
                String fileName = "";
                int lineNumber = -1;
                if(error.contains("(")){
                    className = error.substring(0, error.indexOf("("));
                    fileName = error.substring(error.indexOf("(") + 1, error.indexOf(")"));
                    if(className.contains(".")){
                        methodName = className.substring(className.lastIndexOf(".") + 1);
                        className = className.substring(0, className.lastIndexOf("."));
                    }

                    if(fileName.contains(":")){
                        lineNumber = MathUtils.stringToInteger(fileName.substring(fileName.lastIndexOf(":") + 1));
                        fileName = fileName.substring(0, fileName.lastIndexOf(":"));
                    }
                }
                errorDetail.methodName = methodName;
                errorDetail.declaringClass = className;
                errorDetail.fileName = fileName;
                errorDetail.lineNumber = lineNumber;
                stackTraceElements.add(errorDetail);
            }
        }
        String json = JsonUtils.toJson(this);
        String absolutePath = context.getCacheDir().getAbsolutePath();
        String error = absolutePath + "/" + "error/error.dat";
        File file = new File(error);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        FileUtils.writeFile(error, json);
    }

    public static class ErrorDetail {
        public String declaringClass;
        public String methodName;
        public String fileName;
        public int    lineNumber;
    }
}
