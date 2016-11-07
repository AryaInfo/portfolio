package com.arya.portfolio.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.arya.lib.database.DatabaseMgr;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.dao.PortfolioData;

import java.util.ArrayList;
import java.util.List;

public class DBManagerAP {

    private static DatabaseMgr dbMgr;
    private static DBManagerAP instance;

    public static void init() {
        try {
            dbMgr = DatabaseMgr.getInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBManagerAP getInstance() {
        if (instance == null) {
            instance = new DBManagerAP();
        }
        return instance;
    }

    /**
     * Insert data to Portfolio table
     *
     * @param listProduct
     * @return number of inserted row in Data base
     */
    public int insertDataInPortfolioTable(List<PortfolioData> listProduct) {
        int insertedRow = 0;
        try {
            if (listProduct != null) {
                ContentValues[] contentValues = new ContentValues[listProduct.size()];
                int i = 0;
                for (PortfolioData portfolioData : listProduct) {
                    contentValues[i] = convertPortfolioToCV(portfolioData);
                    i++;
                }
                insertedRow = DatabaseMgr.getInstance().insertRows(PortfolioData.TABLE_NAME, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertedRow;
    }

    private ContentValues convertPortfolioToCV(PortfolioData portfolioData) throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioData.KEY_PROJECT_ID, portfolioData.projectId);
        contentValues.put(PortfolioData.KEY_PROJECT_NAME, portfolioData.projectName);
        contentValues.put(PortfolioData.KEY_PROJECT_IMAGE, portfolioData.projectImage);
        contentValues.put(PortfolioData.KEY_PROJECT_OVERVIEW, portfolioData.projectOverview);
        contentValues.put(PortfolioData.KEY_PROJECT_WEB_LINK, portfolioData.projectWeblink);
        contentValues.put(PortfolioData.KEY_PROJECT_CATEGORY, portfolioData.projectCategory);
        contentValues.put(PortfolioData.KEY_PROJECT_PLATFORM, portfolioData.projectPlateform);
        contentValues.put(PortfolioData.KEY_PROJECT_TECHNOLOGY, portfolioData.projectTechnology);
        contentValues.put(PortfolioData.KEY_PROJECT_ACHIEVEMENT, portfolioData.projectAchievement);
        contentValues.put(PortfolioData.KEY_PROJECT_CHALLENGES, portfolioData.projectChallenges);
        contentValues.put(PortfolioData.KEY_PROJECT_IOS_LINK, portfolioData.projectIOSLink);
        contentValues.put(PortfolioData.KEY_PROJECT_ANDROID_LINK, portfolioData.projectAndroidLink);
        contentValues.put(PortfolioData.KEY_PROJECT_INDUSTRY_CATEGORY, portfolioData.projectIndstryCategory);
        return contentValues;
    }

    public ArrayList<PortfolioData> getProductAccToCategory(String productName, String technology, String industryCategory) {
        ArrayList<PortfolioData> listProduct;
        listProduct = new ArrayList<>();
        try {
            Cursor cursor;

            if (!TextUtils.isEmpty(industryCategory)) {
                cursor = queryTable(TableType.CreatePortfolioTable, null, PortfolioData.KEY_PROJECT_INDUSTRY_CATEGORY + "=?", new String[]{String.valueOf(industryCategory)}, null, null, null, null);
            } else if (!TextUtils.isEmpty(technology)) {
                cursor = queryTable(TableType.CreatePortfolioTable, null, PortfolioData.KEY_PROJECT_TECHNOLOGY + "  like ?", new String[]{"%" + technology + "%"}, null, null, null, null);
            } else if (!TextUtils.isEmpty(productName)) {
                cursor = queryTable(TableType.CreatePortfolioTable, null, PortfolioData.KEY_PROJECT_NAME + "  like ?", new String[]{"%" + productName + "%"}, null, null, null, null);
            } else {
                cursor = queryTable(TableType.CreatePortfolioTable, null, null, null, null, null, null, null);
            }
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    listProduct.add(getTablePortfolioData(cursor));
                    cursor.moveToNext();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProduct;
    }

    private PortfolioData getTablePortfolioData(Cursor cursor) {
        PortfolioData productData = new PortfolioData();
        try {
            productData.projectId = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_ID));
            productData.projectName = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_NAME));
            productData.projectImage = cursor.getInt(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_IMAGE));
            productData.projectOverview = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_OVERVIEW));
            productData.projectWeblink = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_WEB_LINK));
            productData.projectTechnology = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_TECHNOLOGY));
            productData.projectAchievement = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_ACHIEVEMENT));
            productData.projectChallenges = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_CHALLENGES));
            productData.projectPlateform = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_PLATFORM));
            productData.projectCategory = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_CATEGORY));
            productData.projectAndroidLink = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_ANDROID_LINK));
            productData.projectIOSLink = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_IOS_LINK));
            productData.projectIndstryCategory = cursor.getString(cursor.getColumnIndex(PortfolioData.KEY_PROJECT_INDUSTRY_CATEGORY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productData;
    }

    public Cursor queryTable(TableType tableType, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cur = null;
        try {
            if (limit != null) {
                cur = dbMgr.sqLiteDb.query(tableType.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            } else {
                cur = dbMgr.sqLiteDb.query(tableType.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            cur = null;
        }
        return cur;
    }

    public int insertDataInNewsTable(List<NewsData> listNews) {
        int insertedRow = 0;
        try {
            if (listNews != null) {
                ContentValues[] contentValues = new ContentValues[listNews.size()];
                int i = 0;
                for (NewsData newsData : listNews) {
                    contentValues[i] = convertNewsArrayToCV(newsData);
                    i++;
                }
                insertedRow = DatabaseMgr.getInstance().insertRows(NewsData.TABLE_NAME, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertedRow;
    }

    private NewsData getTableNewsData(Cursor cursor) {
        NewsData newsData = new NewsData();
        try {
            newsData.item = cursor.getString(cursor.getColumnIndex(NewsData.KEY_ITEM));
            newsData.title = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NEWS_TITLE));
            newsData.articleid = cursor.getLong(cursor.getColumnIndex(NewsData.KEY_NEWS_ARTICLEID));
            newsData.description = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NWES_DESCRIPTION));
            newsData.imagelink = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NEWS_IMAGES));
            newsData.pubdate = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NEWS_PUBLISH_DATE));
            newsData.link = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NEWS_LINK));
            newsData.favrouite = cursor.getInt(cursor.getColumnIndex(NewsData.KEY_FAVROUITE));
            newsData.newsType = cursor.getString(cursor.getColumnIndex(NewsData.KEY_NEWS_TYPE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsData;
    }

    private ContentValues convertNewsArrayToCV(NewsData newsData) throws Exception {
        ContentValues contentValues = null;
        try {
            contentValues = new ContentValues();
            contentValues.put(NewsData.KEY_ITEM, newsData.item);
            contentValues.put(NewsData.KEY_NEWS_TITLE, newsData.title);
            contentValues.put(NewsData.KEY_NEWS_ARTICLEID, newsData.articleid);
            contentValues.put(NewsData.KEY_NWES_DESCRIPTION, newsData.description);
            contentValues.put(NewsData.KEY_NEWS_IMAGES, newsData.imagelink);
            contentValues.put(NewsData.KEY_NEWS_PUBLISH_DATE, newsData.pubdate);
            contentValues.put(NewsData.KEY_NEWS_LINK, newsData.link);
            // contentValues.put(NewsData.KEY_FAVROUITE, newsData.favrouite);
            contentValues.put(NewsData.KEY_FAVROUITE, getFavValue(newsData.title));
            contentValues.put(NewsData.KEY_NEWS_TYPE, newsData.newsType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    public void updateTableNews(long articleid, int favrouite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsData.KEY_FAVROUITE, favrouite);
        try {
            DatabaseMgr.getInstance().updateRow(NewsData.TABLE_NAME, contentValues, NewsData.KEY_NEWS_ARTICLEID + "=?", new String[]{String.valueOf(articleid)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFavValue(String title) {
        int fav = 0;
        try {
            Cursor cursor = queryTable(TableType.CreateNewsTable, null, NewsData.KEY_NEWS_TITLE + "=?", new String[]{String.valueOf(title)}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                fav = cursor.getInt(cursor.getColumnIndex(NewsData.KEY_FAVROUITE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fav;
    }


    public ArrayList<NewsData> getFirstTimeNews(String newsType, String favNews, String searchNews) {
        ArrayList<NewsData> listNews = new ArrayList<>();
        try {
            Cursor cursor = null;
            if (!TextUtils.isEmpty(searchNews)) {
                cursor = queryTable(TableType.CreateNewsTable, null, NewsData.KEY_NEWS_TYPE + "=?" + " and " + NewsData.KEY_NEWS_TITLE + "  like ?", new String[]{newsType, "%" + searchNews + "%"}, null, null, null, null);
            } else {
                if (!TextUtils.isEmpty(newsType)) {
                    cursor = queryTable(TableType.CreateNewsTable, null, NewsData.KEY_NEWS_TYPE + "=?", new String[]{String.valueOf(newsType)}, null, null, null, null);
                } else {
                    cursor = queryTable(TableType.CreateNewsTable, null, NewsData.KEY_FAVROUITE + "=?", new String[]{String.valueOf(1)}, null, null, null, null);
                }
            }
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    listNews.add(getTableNewsData(cursor));
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listNews;
    }

}
