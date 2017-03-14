package com.hchstudio.hlog.logfile;

/**
 * Created by hech on 2017/2/28.
 */

interface LogFileManager {

    LogFileManager setRootPath(String rootPath);

    LogFileManager setFileName(String fileName);

    void saveLog(int pid, int tid, String logLevel, String tag, String msg);

    void cleanLogDir();
}
