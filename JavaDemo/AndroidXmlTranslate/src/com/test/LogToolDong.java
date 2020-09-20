package com.test;

import android.util.Log;

import java.io.File;

public class LogToolDong {
    private static final String TAG = "dong";
    private static final String filePath = "/data/log.on";

    public static boolean ForceLogToolEnable = false;
    public static File file = new File(filePath);
    public static StackTraceElement[] trace;

    public static void e(Exception e) {
        if (file.exists() || ForceLogToolEnable) {
            trace = e.getStackTrace();
            if (!(trace == null || trace.length == 0)) {
                try {
                    Log.d(TAG, "----" + trace[0].getFileName() + "--"
                            + trace[0].getClassName() + ":" + trace[0].getLineNumber());
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            trace = null;
        }
    }

    public static void e(Exception e, String str) {
        if (file.exists() || ForceLogToolEnable) {
            trace = e.getStackTrace();
            if (!(trace == null || trace.length == 0)) {
                try {
                    Log.d(TAG, "----" + trace[0].getFileName() + "--"
                            + trace[0].getClassName() + ":" + trace[0].getLineNumber()
                            + "; ====[" + str + "]");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            trace = null;
        }
    }

    public static void stack(Exception e) {
        if (file.exists() || ForceLogToolEnable) {
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
    }
}
// LogToolDong.e(new Exception());
// LogToolDong.stack(new Exception());
// LogToolDong.e(new Exception(), "preChangeChannelNum=" + preChangeChannelNum);
// import com.test.LogToolDong;
