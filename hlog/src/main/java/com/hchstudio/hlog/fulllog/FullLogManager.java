package com.hchstudio.hlog.fulllog;

import android.content.Context;

import com.hchstudio.hlog.HLog;
import com.hchstudio.hlog.HLogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hech on 2017/3/10.
 * 打印全日志
 */
public class FullLogManager {

    private static final String ROOTPATHNAME = "log";
    private static final String FILENAME = "FullLogManager";

    public static final String LOG_FILE_PREFIX = "Log";
    public static final String LOG_FILE_EXTENSION = ".txt";

    private Context mContext;
    private String rootPath;
    private String fileName;
    private String strFileName;
    private boolean isHasRW = false;

    public static final String DateFormat = "yyyy.MM.dd.HH";

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

    /**
     * 打印全日制线程
     */
    private Thread logThread = new Thread(){

        Process process = null;
        {
            try {
                process = Runtime.getRuntime().exec("logcat -v time");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (!isHasRW) {
                isHasRW = HLogUtil.isHasRWP(mContext);
                if (!isHasRW)
                    return;
            }
            if (null == process) {
                HLog.i("finalProcess == null");
                try {
                    process = Runtime.getRuntime().exec("logcat -v time");
                } catch (IOException e) {
                    e.printStackTrace();
                    HLog.i("Runtime.getRuntime().exec failure");
                    return;
                }
            }
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String str = null;
            try {
                while ((str = bufferedReader.readLine()) != null) {
                    // 将log写进文件。
                    writeToFile(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    protected FullLogManager() {}

    public void init(Context context) {
        mContext = context;
        rootPath = context.getExternalFilesDir(ROOTPATHNAME).toString();
        fileName = FILENAME;
        isHasRW = HLogUtil.isHasRWP(context);

        this.cleanLog();
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void startLog() {
        if (null != logThread) {
            HLog.i("logThread != null");
            return;
        }
//        HLog.i("startLog");
        logThread.start();
    }

    private void cleanLog() {
        if (isHasRW)
            cleanThread.start();
    }

    /**
     * 写入日志文件
     * @param content 日志内容
     */
    private void writeToFile(String content) {
        BufferedWriter out = null;
        OutputStreamWriter osw = null;
        FileOutputStream fops = null;
        try {
            File dir = new File(rootPath, fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (null == strFileName || (getFileSizes(dir.getAbsolutePath() + "/" + strFileName) >= 1048576 * 10)) {
                strFileName = getLogFileName();
            }
            File file = new File(dir, strFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fops = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fops);
            out = new BufferedWriter(osw);
            out.write("\n");
            out.write(content);
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != osw) {
                    osw.close();
                }
                if (null != fops) {
                    fops.close();
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * 通过时间节点生成日志文件名
     * @return 日志文件名
     */
    private String getLogFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        String datetime = sdf.format(date);
        strFileName = LOG_FILE_PREFIX + datetime + LOG_FILE_EXTENSION;
        return strFileName;
    }

    /**
     * 获取文件大小
     * @param path 文件路径
     * @return 文件大小
     */
    public static long getFileSizes(String path) {
        long s = 0;
        try {
            File f = new File(path);
            if (f.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                s = fis.available();
                fis.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return s;
    }
}
