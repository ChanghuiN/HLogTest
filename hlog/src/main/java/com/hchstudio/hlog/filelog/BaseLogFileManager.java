package com.hchstudio.hlog.filelog;

import android.content.Context;
import android.text.TextUtils;

import com.hchstudio.hlog.HLog;
import com.hchstudio.hlog.HLogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by hech on 2017/3/8.
 */

public class BaseLogFileManager implements ILogFile {

    private static final String ROOTPATHNAME = "log";
    private static final String FILENAME = "HLog";

    public static final String LOG_FILE_PREFIX = "Log";
    public static final String LOG_FILE_EXTENSION = ".txt";

    private Context mContext;
    private String rootPath;
    private String fileName;
    private BufferedWriter out = null;
    private boolean isHasRW = false;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 6;

    public static final String DateFormat = "yyyy.MM.dd.HH-mm-ss";

    /**
     * 删除日志，保留最多的文件个数
     */
    public static final int LOG_FILE_DELETE_DELAY = 15;

    /**
     * 日志删除线程
     */
    private Thread cleanThread = new Thread(new Runnable() {
        @Override
        public void run() {
            HLog.i("cleanLog---");
            File rootFile = new File(rootPath, fileName);
            if (!rootFile.exists() || !rootFile.isDirectory()) {
                return;
            }
            List<File> files = Arrays.asList(rootFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(LOG_FILE_PREFIX) && name.endsWith(LOG_FILE_EXTENSION);
                }
            }));
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().replace("-", "").compareTo(o2.getName().replace("-", ""));
                }
            });
            if (null != files && files.size() > LOG_FILE_DELETE_DELAY) {
                for (int i = 0; i < files.size() - LOG_FILE_DELETE_DELAY; i++) {
                    boolean success = files.get(i).delete();
                    HLog.d("delete log file:" + files.get(i).getName() + " | success:" + success);
                }
            }
        }
    });

    public BaseLogFileManager(Context context) {
        mContext = context;
        rootPath = context.getExternalFilesDir(ROOTPATHNAME).toString();
        fileName = FILENAME;
        isHasRW = HLogUtil.isHasRWP(context);

        this.cleanLog();
    }

    @Override
    public ILogFile setFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName))
            this.fileName = fileName;
        return this;
    }

    @Override
    public ILogFile setRootPath(String rootPath) {
        if (!TextUtils.isEmpty(rootPath))
            this.rootPath = rootPath;
        return this;
    }

    @Override
    public void saveLog(int pid, int tid, String logLevel, String tag, String msg) {
        if (!isHasRW) {
            isHasRW = HLogUtil.isHasRWP(mContext);
            if (!isHasRW)
                return;
        }

        File file = new File(rootPath, fileName);
        if (!file.exists())
            file.mkdirs();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        String dateStr = sdfs.format(date);

        String[] stacks = HLogUtil.getStackOffsets(MIN_STACK_OFFSET);
        String headerStr = "(" + stacks[1] + ":" + stacks[4] + ")#" + stacks[2] + "  ";
        StringBuilder logCon = new StringBuilder();
        logCon.append("").append(dateStr).append(" ")
                .append(pid).append("-").append(tid).append(" ")
                .append(logLevel).append("/").append(tag).append(" ")
                .append(headerStr).append(" ")
                .append(msg);

        File logFile = new File(file, LOG_FILE_PREFIX + simpleDateFormat.format(date) + LOG_FILE_EXTENSION);
//        Log.i("OK", logFile.getPath().toString());
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (!logFile.exists()) {
                if (out != null)
                    out.close();
                logFile.createNewFile();
            }
            if (out == null) {
                fos = new FileOutputStream(logFile, true);
                osw = new OutputStreamWriter(fos);
                out = new BufferedWriter(osw);
            }
            out.newLine();
            out.write(logCon.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out)
                    out.close();
                if (null != osw)
                    osw.close();
                if (null != fos)
                    fos.close();
                out = null;
                osw = null;
                fos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void cleanLog() {
        cleanThread.start();
    }

}
