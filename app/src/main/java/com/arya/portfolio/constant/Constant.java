package com.arya.portfolio.constant;

public class Constant {

    public static final String DB_NAME = "aryaportfolio";
    public static final int DB_VERSION = 1;

    public static final String GET_COUNTRY_CODE_URL = "http://ip-api.com/json";

    public static final String MAIN_URL = "http://www.newshog.co/full-text-rss/makefulltextfeed.php?url=";
  //  public static final String NEWS_URL_TOP = MAIN_URL + "http%3A%2F%2Fnews%2Egoogle%2Ecom%2Fnews%3Fned%3Din%26topic%3Dh%26output%3Drss%26num%3D20" + "&max=20";
    public static final String NEWS_PRE = "http%3A%2F%2Fnews%2Egoogle%2Ecom%2Fnews%3Fned%3D";
    public static final String NEWS_POST = "%26topic%3Dtc%26output%3Drss%26num%3D20" + "&max=20";
    public static final String NEWS_URL_TECH_PRE = MAIN_URL + NEWS_PRE;
    public static final String NEWS_URL_TECH_POST =NEWS_POST;

}
