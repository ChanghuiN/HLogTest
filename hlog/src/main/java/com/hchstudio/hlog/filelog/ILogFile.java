package com.hchstudio.hlog.filelog;

/**
 * Created by hech on 2017/2/28.
 */

interface ILogFile {

    ILogFile setRootPath(String rootPath);

    ILogFile setFileName(String fileName);

    void saveLog(int pid, int tid, String logLevel, String tag, String msg);

    void cleanLog();
}
