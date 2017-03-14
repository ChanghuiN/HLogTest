package com.hchstudio.hlog;

import android.content.Context;

import com.hchstudio.hlog.logfile.BaseLogFileManager;

/**
 * Created by hech on 2017/3/1.
 */

public class HLog {

    // 日志类型
    public static final String V = "V";
    public static final String D = "D";
    public static final String I = "I";
    public static final String W = "W";
    public static final String E = "E";
    public static final String A = "A";

    // 日志等级
    /**
     * 只打印日志内容
     */
    public static final int SIMPLE = 1;
    /**
     * 打印全日志(一行中显示)
     */
    public static final int WHOLE = 2;
    /**
     * 打印全日志(多行中显示)
     */
    public static final int MUTWHOLE = 3;

    public static Printer printer = new HLogPrinter();

    private HLog() {
    }

    public HLog(String logFileName) {
    }

    /**
     * 设置文件持久化
     *
     * @param logFileManager
     */
    public static void setLogFileManager(BaseLogFileManager logFileManager) {
        printer.setLogFileManager(logFileManager);
    }

    /**
     * 设置是否处于debug状态，如果为false，则控制台中不打印日志
     *
     * @param isDebug
     */
    public static void setIsDebug(boolean isDebug) {
        printer.setIsDebug(isDebug);
    }

    /**
     * 设置控制打印的日志格式
     * SIMPLE / WHOLE / MUTWHOLE
     *
     * @param logLevel
     */
    public static void setLogLevel(int logLevel) {
        printer.setLogLevel(logLevel);
    }

    public static void startLog(Context context) {
        printer.startLog(context);
    }

    public static void v(Object objects) {
        printer.v(objects);
    }

    public static void v(String tag, Object... objects) {
        printer.v(tag, objects);
    }

    public static void d(Object objects) {
        printer.d(objects);
    }

    public static void d(String tag, Object... objects) {
        printer.d(tag, objects);
    }

    public static void i(Object objects) {
        printer.i(objects);
    }

    public static void i(String tag, Object... objects) {
        printer.i(tag, objects);
    }

    public static void w(Object objects) {
        printer.w(objects);
    }

    public static void w(String tag, Object... objects) {
        printer.w(tag, objects);
    }

    public static void e(Object objects) {
        printer.e(objects);
    }

    public static void e(String tag, Object... objects) {
        printer.e(tag, objects);
    }

    public static void a(Object objects) {
        printer.a(objects);
    }

    public static void a(String tag, Object... objects) {
        printer.a(tag, objects);
    }
}
