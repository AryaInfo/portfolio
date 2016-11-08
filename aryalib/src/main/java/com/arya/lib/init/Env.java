package com.arya.lib.init;

import android.content.Context;

import com.arya.lib.database.DBHelper;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public class Env {
    public static Context appContext;
    public static Context currentActivity;
    public static DBHelper dbHelper;
    public static String logFilePath;
    public static boolean isDebugMode;
    public static AppState application_state;

    public static void init(Context appContext, DBHelper dbHelper, String logFilePath, boolean isDebugMode) {
        Env.appContext = appContext;
        Env.dbHelper = dbHelper;
        Env.logFilePath = logFilePath;
        Env.isDebugMode = isDebugMode;
    }

    public enum AppState {
        STATE_VISIBLE, STATE_BACKGROUND
    }
}
