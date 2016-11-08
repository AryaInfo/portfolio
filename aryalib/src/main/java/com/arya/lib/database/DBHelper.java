package com.arya.lib.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public interface DBHelper {
    int getDBVersion();
    public String getDBName();
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
