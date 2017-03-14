package com.hchstudio.hlog;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by hech on 2017/3/3.
 * <p>
 * getStackOffsets : 获取当前线程属性
 * isHasRWP : 检查是否有读写权限
 */

public class HLogUtil {

    /**
     * @return String[]
     * 第一个参数： 当前线程名称
     * 第二个参数： 类名
     * 第三个参数： 方法名
     * 第四个参数： 文件名
     * 第五个参数： 所在行数
     */
    public static String[] getStackOffsets(int min_stack_offst) {
        String[] strs = new String[5];
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, min_stack_offst);
        strs[0] = Thread.currentThread().getName();
        strs[1] = getSimpleClassName(trace[stackOffset].getClassName());
        strs[2] = trace[stackOffset].getMethodName();
        strs[3] = trace[stackOffset].getFileName();
        strs[4] = String.valueOf(trace[stackOffset].getLineNumber());
        return strs;
    }

    /**
     * 检查是否有读写权限
     * @param context
     * @return
     */
    public static boolean isHasRWP(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean writeP = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));
        boolean readP = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", context.getPackageName()));
//        Log.i("OK", writeP + "---" + readP);
        return writeP && readP;
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private static int getStackOffset(StackTraceElement[] trace, int min_stack_offst) {
//        for (StackTraceElement stackTraceElement: trace)
//            Log.i("OK", stackTraceElement.toString());
        for (int i = min_stack_offst; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            try {
                Class cla = Class.forName(name);
//                Log.i("OK", name + "---" + i);
                if (!Printer.class.isAssignableFrom(cla) && !HLog.class.isAssignableFrom(cla)) {
//                    Log.i("OK", "---" + i);
                    return i;
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return 0;
    }
}
