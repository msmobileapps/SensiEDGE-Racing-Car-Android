package com.msapps.carracing.log;

import android.util.Log;

public class LogHandler {

    public static void d(String debug, String message){
        Log.d("[CR]" + debug, message);
    }

    public static void e(String debug, String message){
        Log.e("[CR]" + debug, message);
    }

} 