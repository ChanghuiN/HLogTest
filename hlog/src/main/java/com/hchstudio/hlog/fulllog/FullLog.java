package com.hchstudio.hlog.fulllog;

import android.content.Context;

/**
 * Created by hech on 2017/3/24.
 * 主要实现FullLogManager单例
 */

public class FullLog {

    private static class Singleton {
        private static final FullLogManager INSTANCE = new FullLogManager();
    }

//    private FullLog getInstance() {
//        return Singleton.INSTANCE;
//    }
//
//    public static void init(Context context) {
//        .init(context);
//    }
//
//    public static void setRootPath(String rootPath) {
//        mFullLogManager.setRootPath(rootPath);
//    }
//
//    public static void setFileName(String fileName) {
//        mFullLogManager.setFileName(fileName);
//    }
//
//    public static void startLog() {
//        mFullLogManager.startLog();
//    }

}
