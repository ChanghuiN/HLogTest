package com.hchstudio.hlog.alog;


import android.util.Log;

import com.hchstudio.hlog.HLog;
import com.hchstudio.hlog.HLogUtil;

/**
 * Created by hech on 2017/2/28.
 */

public class Baselog {

    private int logLevel = HLog.WHOLE;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * 500 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 500;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 6;

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public synchronized void printLog(String type, String tag, String message){
        if (logLevel == HLog.MUTWHOLE) {
            logTopBorder(type, tag);
            logHeaderContent(type, tag);

            //get bytes of message with system's default charset (which is UTF-8 for Android)
            byte[] bytes = message.getBytes();
            int length = bytes.length;
            logDivider(type, tag);
            if (length <= CHUNK_SIZE) {
                logContent(type, tag, message);
            } else {
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    int count = Math.min(length - i, CHUNK_SIZE);
                    //create a new String with system's default charset (which is UTF-8 for Android)
                    logContent(type, tag, new String(bytes, i, count));
                }
            }
            logBottomBorder(type, tag);
        } else {
            logSimpleContent(type, tag, message);
        }
    }

    private void logTopBorder(String logType, String tag) {
        log(logType, tag, TOP_BORDER);
    }

    private void logHeaderContent(String logType, String tag) {
//        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String[] strs = HLogUtil.getStackOffsets(MIN_STACK_OFFSET);

        log(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + strs[0] );
        logDivider(logType, tag);

        StringBuilder builder = new StringBuilder();
        builder.append("║ ")
                .append(strs[1])
                .append(".")
                .append(strs[2])
                .append(" ")
                .append(" (")
                .append(strs[3])
                .append(":")
                .append(strs[4])
                .append(")");
        log(logType, tag, builder.toString());
    }

    private void logSimpleContent(String logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        StringBuilder builder = new StringBuilder();
        if (logLevel == HLog.WHOLE) {
            String[] strs = HLogUtil.getStackOffsets(MIN_STACK_OFFSET);
            builder.append(strs[0])
                    .append(" (")
                    .append(strs[3])
                    .append(":")
                    .append(strs[4])
                    .append(")");
        }
        for (String line : lines) {
            builder.append("  ")
                    .append(line);
        }
        log(logType, tag, builder.toString());
    }

    private void logBottomBorder(String logType, String tag) {
        log(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(String logType, String tag) {
        log(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(String logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            log(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
        }
    }

    private void log(String type, String tag, String msg){
        switch (type) {
            case HLog.V:
                Log.v(tag, msg);
                break;
            case HLog.D:
                Log.d(tag, msg);
                break;
            case HLog.I:
                Log.i(tag, msg);
                break;
            case HLog.W:
                Log.w(tag, msg);
                break;
            case HLog.E:
                Log.e(tag, msg);
                break;
            case HLog.A:
                Log.wtf(tag, msg);
                break;
            default:
                break;
        }
    }

}
