package com.hchstudio.hlog;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.hchstudio.hlog.alog.Baselog;
import com.hchstudio.hlog.filelog.BaseLogFileManager;
import com.hchstudio.hlog.fulllog.FullLogManager;

import java.util.Arrays;

/**
 * Created by hech on 2017/2/28.
 */

public class HLogPrinter implements Printer {

    private static final String DEFAULT_TAG = "HLOG";
    private static final String DEFAULT_MSG = "MSG is null";

    // 控制台打印日志
    private static Baselog mBaselog = new Baselog();
    // 打印实时日志
    private static FullLogManager mFullLog = new FullLogManager();
    // 日志实例化
    private BaseLogFileManager mLogFileManager;

    // 是否在控制台输出日志
    private boolean isDebug = true;
    // 更目录路径
    private String rootPath = "";

    protected HLogPrinter() {}

    @Override
    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public void setLogLevel(int logLevel){
        mBaselog.setLogLevel(logLevel);
    }

    @Override
    public void setLogFileManager(BaseLogFileManager logFileManager) {
        mLogFileManager = logFileManager;
    }

    @Override
    public void setRootPath(String rootPath) {
        if (null != mLogFileManager)
            mLogFileManager.setRootPath(rootPath);
    }

    @Override
    public void startLog(Context context) {
        mFullLog.init(context);
        if (!TextUtils.isEmpty(rootPath))
            mFullLog.setRootPath(rootPath);
        mFullLog.startLog();
    }

    @Override
    public void v(Object object) {
        printLog(HLog.V, DEFAULT_TAG, object);
    }

    @Override
    public void v(String tag, Object... objects) {
        printLog(HLog.V, tag, objects);
    }

    @Override
    public void d(Object object) {
        printLog(HLog.D, DEFAULT_TAG, object);
    }

    @Override
    public void d(String tag, Object... objects) {
        printLog(HLog.D, tag, objects);
    }

    @Override
    public void i(Object object) {
        printLog(HLog.I, DEFAULT_TAG, object);
    }

    @Override
    public void i(String tag, Object... objects) {
        printLog(HLog.I, tag, objects);
    }

    @Override
    public void w(Object object) {
        printLog(HLog.W, DEFAULT_TAG, object);
    }

    @Override
    public void w(String tag, Object... objects) {
        printLog(HLog.W, tag, objects);
    }

    @Override
    public void e(Object object) {
        printLog(HLog.E, DEFAULT_TAG, object);
    }

    @Override
    public void e(String tag, Object... objects) {
        printLog(HLog.E, tag, objects);
    }

    @Override
    public void a(Object object) {
        printLog(HLog.A, DEFAULT_TAG, object);
    }

    @Override
    public void a(String tag, Object... objects) {
        printLog(HLog.A, tag, objects);
    }

    private synchronized void printLog(String type, String tag, Object... objects) {
        if (tag == null || tag.equals(""))
            tag = DEFAULT_TAG;
        String msg = getObjectsString(objects);

        // 控制台打印日志
        if (isDebug)
            mBaselog.printLog(type, tag, msg);

        // 持久化日志
        if (mLogFileManager != null)
            mLogFileManager.saveLog(Process.myPid(), Process.myTid(), type, tag, msg);
    }

    private String getObjectsString(Object... objects) {
        if (objects.length > 0) {
            return objects[0].toString();
        } else if (objects.length > 1) {
            return Arrays.toString(objects);
        } else {
            return DEFAULT_MSG;
        }
    }
}
