package com.arya.portfolio.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arya on 18/10/16.
 */

public class Preferences {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String PREFERANCES_NAME = "aryaPortfolio";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_IMAGE_URL = "userImageUrl";
    private static final String KEY_USER_ID = "userId";
    private static String KEY_USER_DEVICE_ID = "userDeviceId";

    public static void init(Context mContext) {
        Preferences.sharedPreferences = mContext.getSharedPreferences(PREFERANCES_NAME, Context.MODE_PRIVATE);
        Preferences.editor = sharedPreferences.edit();
    }

    public static void setUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId).commit();
    }

    public static long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, 0);
    }

    public static void setUserDeviceId(String userDeviceId) {
        editor.putString(KEY_USER_DEVICE_ID, userDeviceId).commit();
    }

    public static String getUserDeviceId() {
        return sharedPreferences.getString(KEY_USER_DEVICE_ID, null);
    }

    public static void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name).commit();
    }

    public static String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public static void setUserImageUrl(String userImageUrl) {
        editor.putString(KEY_USER_IMAGE_URL, userImageUrl).commit();
    }

    public static String getUserImageUrl() {
        return sharedPreferences.getString(KEY_USER_IMAGE_URL, null);
    }

    public static void clearPreferences() {
        editor.clear().commit();
    }


}
