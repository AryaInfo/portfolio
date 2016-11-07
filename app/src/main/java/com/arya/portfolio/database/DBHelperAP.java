package com.arya.portfolio.database;

import android.database.sqlite.SQLiteDatabase;

import com.arya.lib.database.DBHelper;
import com.arya.portfolio.constant.Constant;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.dao.PortfolioData;

/**
 * Created by Ramnivas Sing on 11/30/2015.
 */
public class DBHelperAP implements DBHelper {
    private static String createTable = "CREATE TABLE IF NOT EXISTS ";

    @Override
    public int getDBVersion() {
        return Constant.DB_VERSION;
    }

    @Override
    public String getDBName() {
        return Constant.DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreatePortfolioTable(db);
        CreateNewsTable(db);
    }

    private void CreateNewsTable(SQLiteDatabase db) {
        db.execSQL(createTable + TableType.CreateNewsTable.getTableName() + "("
                + NewsData.KEY_NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + NewsData.KEY_ITEM + " TEXT,"
                + NewsData.KEY_NEWS_TITLE + " TEXT UNIQUE ON CONFLICT REPLACE,"
                + NewsData.KEY_NEWS_ARTICLEID + " LONG,"
                + NewsData.KEY_NWES_DESCRIPTION + " TEXT,"
                + NewsData.KEY_NEWS_IMAGES + " TEXT,"
                + NewsData.KEY_NEWS_PUBLISH_DATE + " TEXT,"
                + NewsData.KEY_NEWS_TYPE + " TEXT,"
                + NewsData.KEY_FAVROUITE + " INTEGER,"
                + NewsData.KEY_NEWS_LINK + " TEXT);");
    }
    private void CreatePortfolioTable(SQLiteDatabase db) {
        db.execSQL(createTable + TableType.CreatePortfolioTable.getTableName() + "("
                + PortfolioData.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + PortfolioData.KEY_PROJECT_ID + " TEXT UNIQUE ON CONFLICT REPLACE,"
                + PortfolioData.KEY_PROJECT_NAME + " TEXT,"
                + PortfolioData.KEY_PROJECT_IMAGE + " INTEGER,"
                + PortfolioData.KEY_PROJECT_OVERVIEW + " TEXT,"
                + PortfolioData.KEY_PROJECT_WEB_LINK + " TEXT,"
                + PortfolioData.KEY_PROJECT_CATEGORY + " TEXT,"
                + PortfolioData.KEY_PROJECT_PLATFORM + " TEXT,"
                + PortfolioData.KEY_PROJECT_TECHNOLOGY + " TEXT,"
                + PortfolioData.KEY_PROJECT_ACHIEVEMENT + " TEXT,"
                + PortfolioData.KEY_PROJECT_CHALLENGES + " TEXT,"
                + PortfolioData.KEY_PROJECT_IOS_LINK + " TEXT,"
                + PortfolioData.KEY_PROJECT_INDUSTRY_CATEGORY + " TEXT,"
                + PortfolioData.KEY_PROJECT_ANDROID_LINK + " TEXT);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
