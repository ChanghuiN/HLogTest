package com.hchstudio.hlog;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hech on 2017/3/10.
 */

public class FullLog {

    private static final String ROOTPATHNAME = "log";
    private static final String FILENAME = "FullLog";

    public static final String LOG_FILE_PREFIX = "Log";
    public static final String LOG_FILE_EXTENSION = ".txt";

    private Context mContext;
    private String rootPath;
    private String fileName;
    private String strFileName;
    private BufferedWriter out = null;
    private boolean isHasRW = false;
    private static Thread th = null;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 6;

    protected FullLog() {}

    public void init(Context context) {
        mContext = context;
        rootPath = context.getExternalFilesDir(ROOTPATHNAME).toString();
        fileName = FILENAME;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void startLog() {
        if (null != th) {
            HLog.i("th != null");
            return;
        }
        HLog.i("startLog");
        // Runtime用于抓log的类
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("logcat -v time");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Process finalProcess = process;
        th = new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == finalProcess) {
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(finalProcess.getInputStream()));
                HLog.i("Thread---run");
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
        });
        th.start();
    }

    public void cleanLogDir() {
    }

    private void writeToFile(String content) {
        BufferedWriter out = null;
        OutputStreamWriter osw = null;
        FileOutputStream fops = null;
        try {
            File dir = new File(rootPath, fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

//            if (null == strFileName
//                    || (getFileSizes(rootPath + "/" + strFileName) >= 1048576 * 10)) {
//                strFileName = getLogFileName();
//				System.out.println("strFileName=" + strFileName);
//                isFileRollback(fileName);
//            }

            File file = new File(dir, "qwelog.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            fops = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fops);
            out = new BufferedWriter(osw);
            out.write("\n");
            out.write(content);
        } catch (Exception e) {
            // e.printStackTrace();
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

    public static long getFileSizes(String path) {// 取得文件大小
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
