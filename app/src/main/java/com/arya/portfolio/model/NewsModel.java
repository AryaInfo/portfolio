package com.arya.portfolio.model;

import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.utils.Util;
import com.arya.portfolio.constant.Constant;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.database.DBManagerAP;
import com.arya.portfolio.utility.Utils;
import com.arya.portfolio.utility.XMLParser;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by user on 16/09/16.
 */

public class NewsModel extends BasicModel {

    private RefreshNewsTask refreshNewsTask = null;
    private SetGetNewsTask setGetNewsTask = null;

    public void refreshNews(String newsType, String favNews, String searchNews) {
        if (refreshNewsTask == null) {
            refreshNewsTask = new RefreshNewsTask();
            refreshNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, newsType, favNews, searchNews);
        }
    }

    public void setGetRecentNewsTask(String newsType, String favNews, String searchNews) {
        if (setGetNewsTask == null) {
            setGetNewsTask = new SetGetNewsTask();
            setGetNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, newsType, favNews, searchNews);
        }
    }

    class RefreshNewsTask extends AsyncTask<String, Void, ArrayList<NewsData>> {

        @Override
        protected ArrayList<NewsData> doInBackground(String... params) {
            ArrayList<NewsData> listNews = null;
            try {
                String newsType = params[0];
                String favNews = params[1];
                String searchNews = params[2];
                listNews = callNewsAPI(newsType, favNews, searchNews);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return listNews;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsData> response) {
            refreshNewsTask = null;
            NewsModel.this.notifyObservers(response);
        }
    }


    class SetGetNewsTask extends AsyncTask<String, Void, ArrayList<NewsData>> {
        @Override
        protected ArrayList<NewsData> doInBackground(String... params) {
            ArrayList<NewsData> listNews = null;
            try {
                String newsType = params[0];
                String favNews = params[1];
                String searchNews = params[2];
                listNews = DBManagerAP.getInstance().getFirstTimeNews(newsType, favNews, searchNews);
                if (newsType.equalsIgnoreCase("TECHNOLOGY")) {
                    listNews = getTechnologyNewsWithImage(listNews);
                }
                if (listNews.isEmpty() && TextUtils.isEmpty(favNews)) {
                    listNews = callNewsAPI(newsType, favNews, searchNews);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return listNews;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsData> listNews) {
            Utils.dismissProDialog();
            setGetNewsTask = null;
            notifyObservers(listNews);
        }
    }


    private ArrayList<NewsData> getTechnologyNewsWithImage(ArrayList<NewsData> listNews) {
        ArrayList<NewsData> listTechnology = new ArrayList<>();
        for (NewsData newsData : listNews) {
            if (!TextUtils.isEmpty(newsData.imagelink)) {
                listTechnology.add(newsData);
            }
        }
        return listTechnology;
    }

    private ArrayList<NewsData> callNewsAPI(String newsType, String favNews, String searchNews) {
        ArrayList<NewsData> listNews = null;
        if (Util.isDeviceOnline()) {
            NetworkResponse response = NetworkMgr.httpPost(Constant.GET_COUNTRY_CODE_URL, "", "");
            if (response != null) {
                String countryCode = getConutryCode(response);
                if (!TextUtils.isEmpty(countryCode)) {
                    response = NetworkMgr.httpPost(Constant.NEWS_URL_TECH_PRE + countryCode + Constant.NEWS_URL_TECH_POST, "", "");
                    if (response != null) {
                        listNews = xmlParsing(response, "TECHNOLOGY");
                        createAryaNewsData(listNews);
                        DBManagerAP.getInstance().insertDataInNewsTable(listNews);
                        listNews = DBManagerAP.getInstance().getFirstTimeNews(newsType, favNews, searchNews);
                        if (newsType.equalsIgnoreCase("TECHNOLOGY")) {
                            listNews = getTechnologyNewsWithImage(listNews);
                        }
                    }
                }
            }
        }
        return listNews;
    }

    private String getConutryCode(NetworkResponse response) {
        String countryCode = null;
        try {
            if (response.respStr != null) {
                JSONObject jsonObject = new JSONObject(response.respStr);
                countryCode = jsonObject.getString("countryCode");
                countryCode = countryCode.toLowerCase().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryCode;
    }

    public ArrayList<NewsData> xmlParsing(NetworkResponse response, String newsType) {

        ArrayList<NewsData> listNews = null;
        try {
            listNews = new ArrayList<>();
            if (response.respStr != null) {
                XMLParser xmlParser = new XMLParser();
                String xml = response.respStr;
                Document doc = xmlParser.getDomElement(xml);
                NodeList nl = doc.getElementsByTagName(NewsData.KEY_ITEM);
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    NewsData newsData = new NewsData();
                    newsData.articleid = Long.parseLong(xmlParser.getValue(e, NewsData.KEY_NEWS_ARTICLEID));
                    newsData.title = Html.fromHtml(xmlParser.getValue(e, NewsData.KEY_NEWS_TITLE)).toString().trim();
                    newsData.description = Html.fromHtml(xmlParser.getValue(e, NewsData.KEY_NWES_DESCRIPTION).replaceAll("<(.|\\n)*?>", "")).toString().trim();
                    newsData.pubdate = xmlParser.getValue(e, NewsData.KEY_NEWS_PUBLISH_DATE);
                    newsData.imagelink = getImageLink(xmlParser.getValue(e, NewsData.KEY_NEWS_IMAGES));
                    newsData.link = xmlParser.getValue(e, NewsData.KEY_NEWS_LINK);
                    newsData.newsType = newsType;

                    listNews.add(newsData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listNews;
    }

    private void createAryaNewsData(ArrayList<NewsData> listTechNews) {

        NewsData newsData1 = new NewsData();
        newsData1.title = "Emulation on Android";
        newsData1.newsType = "ARYANEWS";
        newsData1.pubdate = "Fri, 11 Dec 2015 05:30:00 +0000";
        newsData1.articleid = 0000001;
        newsData1.description = "ClassicBoy (Emulator) – it emulates a bunch of systems all in one app. Some of the included emulators are SNES, PSX, GameBoy Advance and Color, NES, and Sega. It seems to work generally well but there are some titles that the emulator is not made for and thus has a lower game compatibility than a specific game emulator would.\\n•DraStic DS Emulator – It supports a huge number of games and has good controls and menu options. It isn’t totally complete just yet, but in terms of features, smooth game play, and ROM compatiblity, this is about as good as you can get.\\n•FPse for Android – It’s always been hotly debated on which PlayStation emulator was the best. Many say that ePSXe is the best but if you want full control, all the options, and the best overall experience, FPse wins out.\\n•John GBC (GBC Emulator) – If you were more of a handheld console person, there are some great emulators out there for you too. To start, John GBC which is a Game Boy and Game Boy Color emulator. It’s highly rated, stable, and has the best ROM compatibility.\\n•MD.emu – It is actually quite difficult to find a good Sega Genesis emulator on Android but MD.emu fits the bill. This one is an open-source Sega Genesis/Mega Drive, Sega CD, and Master System/Mark III emulator that pretty much covers all of the early Sega years.\\n•MegaN64 N64 Emulator – The emulator crowd on Android was begging for a functioning N64 emulator and they finally got one. It’s not perfect and there are some minor graphical glitches here and there, but it’s the most complete N64 emulator you can get right now. It’s also based on the popular Mupen64 so if you liked that one, this one is pretty similar. Also, it’s free (with ads) and free stuff is awesome.\\n•GBA Emulator – In terms of pure Google Play ratings, My Boy is the highest rated on the list. It’s incredibly stable with great compatibility and a lot of options. This includes the fast forward control so you can speed through slow parts of the game.\\n•My Boy! – GBA Emulator – My Boy! is a super fast and full-featured emulator to run GameBoy Advance games on the broadest range of Android devices, from very low-end phones to modern tablets. It emulates nearly all aspects of the real hardware correctly";
        listTechNews.add(newsData1);

        NewsData newsData2 = new NewsData();
        newsData2.title = "Vault..";
        newsData2.newsType = "ARYANEWS";
        newsData2.pubdate = "Mon, 14 Dec 2015 05:30:00 +0000";
        newsData2.articleid = 0000002;
        newsData2.description = "Vault helps you control your privacy, keeping your pictures, videos, SMS, and contacts private, and hiding them from prying eyes.  Top features ☆Photos & videos protection: All files will be stored in a safe place and can only be viewed in Vault after a numeric passcode is entered. ☆Contacts/SMS/Call logs protection: Allows users to create “private contacts”, whose messages and call logs will be hidden from the phone screen – Vault also hides all incoming message alerts and texts from those contacts. ☆App Lock: Apps that you choose will be protected with a password. Premium users can select an unlimited number of apps to lock. Advanced Features ►FAKE VAULT: Create a decoy to protect the real one. If someone insists on seeing the secrets stored in your Vault, just show them the “fake” one. Put photos that are ok for others to see in your fake Vault, and keep secret data protected in your real one. ►STEALTH MODE: Hide Vault icon on the phone’s Home screen. When stealth mode is activated, the icon will disappear and can be opened again by entering your password via the phone dial pad. ►BREAK-IN ALERTS : Snaps a picture of anyone who enters the wrong password in an attempt to access your Vault, without letting the intruder know. This feature requires an Android 2.3 (or higher) device with a front-facing camera. ►CLOUD BACKUP Back up your files to NQ’s cloud space.";
        listTechNews.add(newsData2);

        NewsData newsData3 = new NewsData();
        newsData3.title = "TextPlus Free Text + Calls";
        newsData3.newsType = "ARYANEWS";
        newsData3.pubdate = "Thu, 10 Dec 2015 05:30:00 +0000";
        newsData3.articleid = 0000003;
        newsData3.description = "exting and calling app. Always get free text / SMS to anyone in the US or Canada, cheap phone calls to any number in the world. Or message and call other textPlus users for free. It’s your choice. Keeping in touch made simple, cheap and hassle free. TextPlus Free Text + Calls";
        listTechNews.add(newsData3);

        NewsData newsData4 = new NewsData();
        newsData4.title = "Roll the Ball: slide puzzle";
        newsData4.newsType = "ARYANEWS";
        newsData4.pubdate = "Tue, 08 Dec 2015 05:30:00 +0000";
        newsData4.articleid = 0000004;
        newsData4.description = "Roll the Ball has all the elements. \uD83D\uDE09 • Sliding Puzzles, Just move and move! • Puzzle Games, Thought-provoking fun. • Brain Teasers, Test yourself. • Escape Games, Can you get out? • Hidden Object Games, Find the hidden path. • Physics Puzzler, Physics-based gaming. • Match-3 Puzzle, Easy to learn but hard to master. Move the blocks with your finger to create a path for moving the ball to the red GOAL block. But riveted blocks can’t be moved FEATURES • Tons of epic levels • No time restriction, play at your own pace • More Variation: Rotation Blocks, Star Lane • Daily Reward Bonus • Support both Phones and Tablets. • Leaderboard & Achievements from Google Play Games.";
        listTechNews.add(newsData4);


    }

    public String getImageLink(String imagelink) {
        String img = "";
        try {
            imagelink = imagelink.replaceAll("\"/><img src=\"", "");
            imagelink = imagelink.replaceAll("\"/>", "");
            imagelink = imagelink.replaceAll("<img src=\"", "");
            String[] parts = imagelink.split("http:");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i];
            }

            if (parts.length != 0) {
                if (parts.length == 2) {
                    img = "http:" + parts[1];
                }
                img = img.trim();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}
