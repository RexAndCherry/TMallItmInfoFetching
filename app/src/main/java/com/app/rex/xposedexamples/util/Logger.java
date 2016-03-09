package com.app.rex.xposedexamples.util;

import android.util.Log;

/**
 * Created by zuoyuxiaofu on 2016/3/9.
 */
public class Logger {

    private static final String TAG = "Logger";

    public static void i(String info) {
        Log.i(TAG, info);
    }

    public static void d(String info) {
        Log.d(TAG, info);
    }

    public static void e(String info) {
        Log.e(TAG, info);
    }


}
