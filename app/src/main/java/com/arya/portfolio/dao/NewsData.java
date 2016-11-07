package com.arya.portfolio.dao;

import java.io.Serializable;

/**
 * Created by user on 14/09/16.
 */
public class NewsData implements Serializable {
    public static String TABLE_NAME = "news";

    public static final String KEY_NEWS_ID = "news_id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_NEWS_TITLE = "title";
    public static final String KEY_NEWS_ARTICLEID = "articleid";
    public static final String KEY_NWES_DESCRIPTION = "description";
    public static final String KEY_NEWS_IMAGES = "imagelink";
    public static final String KEY_NEWS_PUBLISH_DATE = "pubDate";
    public static final String KEY_NEWS_LINK = "link";
    public static final String KEY_FAVROUITE = "favrouite";
    public static final String KEY_NEWS_TYPE = "newsType";


    public String item;
    public String title;
    public long articleid;
    public String description;
    public String imagelink;
    public String pubdate;
    public String newsType;
    public String link;
    public int favrouite;


}
