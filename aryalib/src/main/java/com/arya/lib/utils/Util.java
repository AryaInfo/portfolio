package com.arya.lib.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import com.arya.lib.init.Env;

import org.json.JSONObject;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public class Util {
    public static final String VAL_OK = "OK";
    private static ProgressDialog progressDialog = null;
    public static String FLD_STATUS = "status";
    public static final String STATUS_CODE_USER_LOGOUT = "UserLogout";
    public static final String ACTION_USER_LOGOUT = "arya.spliro.userLogout";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_LOGOUT_MESSAGE = "logout_message";
    public static final String KEY_DEFAULT_SERVER_ERROR = "Server not responding.";
    public static Toast toast;


    public static void showProDialog(Context context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismissProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * method to check whether internet is available or not
     *
     * @return
     */
    public static boolean isDeviceOnline() {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Env.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeviceOnLine;
    }

    public static boolean checkValueForKey(JSONObject jsonObject, String key) {
        boolean b = false;
        if (jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)) {
            b = true;
        }
        return b;
    }


}
