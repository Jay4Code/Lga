package com.lga.util;

import android.util.Log;

/**
 * Created by Jay.X
 */
public class LogUtil {
    private static final String TAG = "kelly";
    private static boolean DEBUG = true;

    private static void i(String tag, String msg) {
        if(msg == null) {
            Log.i(tag, "----msg is null.----");
        } else if(msg.isEmpty()){
            Log.i(tag, "----msg's len is 0.----");
        } else {
            Log.i(tag, msg);
        }
    }

    public static void i(boolean debug, String msg) {
        if(DEBUG & debug) {
            i(TAG, msg);
        }
    }

    public static void i(boolean debug, String tag, String msg) {
        if(DEBUG & debug) {
            i(tag == null ? TAG : tag, msg);
        }
    }

    private static void w(String tag, String msg) {
        if(msg == null) {
            Log.w(tag, "----msg is null.----");
        } else if(msg.isEmpty()){
            Log.w(tag, "----msg's len is 0.----");
        } else {
            Log.w(tag, msg);
        }
    }

    public static void w(boolean debug, String msg) {
        if(DEBUG & debug) {
            w(TAG, msg);
        }
    }

    public static void w(boolean debug, String tag, String msg) {
        if(DEBUG & debug) {
            w(tag == null ? TAG : tag, msg);
        }
    }

    private static void e(String tag, String msg) {
        if(msg == null) {
            Log.e(tag, "----msg is null.----");
        } else if(msg.isEmpty()){
            Log.e(tag, "----msg's len is 0.----");
        } else {
            Log.e(tag, msg);
        }
    }

    public static void e(boolean debug, String msg) {
        if(DEBUG & debug) {
            e(TAG, msg);
        }
    }

    public static void e(boolean debug, String tag, String msg) {
        if(DEBUG & debug) {
            e(tag == null ? TAG : tag, msg);
        }
    }
}
