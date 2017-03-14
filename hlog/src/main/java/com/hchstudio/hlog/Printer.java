package com.hchstudio.hlog;

import android.content.Context;

import com.hchstudio.hlog.logfile.BaseLogFileManager;

/**
 * Created by hech on 2017/3/1.
 */

public interface Printer {

    void setLogFileManager(BaseLogFileManager logFileManager);

    void setIsDebug(boolean isDebug);

    void setLogLevel(int logLevel);

    void setRootPath(String rootPath);

    void startLog(Context context);

    void v(Object objects);

    void v(String tag, Object... objects);

    void d(Object objects);

    void d(String tag, Object... objects);

    void i(Object objects);

    void i(String tag, Object... objects);

    void w(Object objects);

    void w(String tag, Object... objects);

    void e(Object objects);

    void e(String tag, Object... objects);

    void a(Object objects);

    void a(String tag, Object... objects);
}
