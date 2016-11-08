package com.arya.portfolio.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.AryaConnect.dao.UserData;
import com.AryaConnect.utils.AryaConnectClient;
import com.arya.lib.init.Env;
import com.arya.portfolio.R;
import com.arya.portfolio.constant.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class Utils {
    public static ProgressDialog progressDialog;

    /**
     * Parse pub date formate string.
     *
     * @param pubDate the pub date
     * @return the string
     */
    public static String parsePubDateFormate(String pubDate) {
        String str = "";
        try {
            Date date = new Date(pubDate);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
            str = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Gets image link.
     *
     * @param imagelink the imagelink
     * @param imageView the image view
     */
    public static void getImageLink(String imagelink, ImageView imageView) {
        try {
            imagelink = imagelink.replaceAll("\"/><img src=\"", "");
            imagelink = imagelink.replaceAll("\"/>", "");
            imagelink = imagelink.replaceAll("<img src=\"", "");
            String[] parts = imagelink.split("http:");
            String img = "";
            if (parts.length != 0) {
                if (parts.length == 2) {
                    img = "http:" + parts[1];
                }
                img = img.trim();
                if (!TextUtils.isEmpty(img)) {
                    Picasso.with(Env.currentActivity)
                            .load(img)
                            .placeholder(R.mipmap.ic_news_black)
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.mipmap.ic_news_black);
                }
            } else {
                imageView.setImageResource(R.mipmap.ic_news_black);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide key board method.
     *
     * @param con  the con
     * @param view the view
     */
    public static void hideKeyBoardMethod(final Context con, final View view) {
        try {
            InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showKeyboard(final Context con, final View view) {
        try {
            InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Write d bfile on sdcard.
     *
     * @param context the context
     */
    public static void writeDBfileOnSdcard(Context context) {
        try {
            File f = new File(context.getDatabasePath(Constant.DB_NAME).getAbsolutePath());
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                fis = new FileInputStream(f);
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aryaportfolio.sqlite");
                while (true) {
                    int i = fis.read();
                    if (i != -1) {
                        fos.write(i);
                    } else {
                        break;
                    }
                }
                fos.flush();
                Log.v("DB file Exported", "Success");
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("DB file Exported", "Failed");
            } finally {
                try {
                    fos.close();
                    fis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Dialog showProDialog(String msg) {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(Env.currentActivity, R.style.NewDialog);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(Env.currentActivity.getResources().getDrawable(R.drawable.custom_progress_bar));
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    public static Dialog dismissProDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.hide();
            }
            progressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    public static void startActAnimation(Activity ctx) {
        ctx.overridePendingTransition(R.anim.right_in, R.anim.right_out);

    }

    public static void openChatScreen() {
        try {
            AryaConnectClient aryaConnectClient = AryaConnectClient.getInstance(Env.appContext);
            //aryaConnectClient.goToChatUI(Env.currentActivity, Preferences.getUserId(), Preferences.getUserImageUrl(), Preferences.getUserName(), filterUserList());
            aryaConnectClient.goToChatUI(Env.currentActivity, 2, "http://www.planetware.com/photos-large/F/france-paris-eiffel-tower.jpg", "User", UserList());
            aryaConnectClient.setThemeColor(R.color.color_green);
            aryaConnectClient.setTabTextColor(R.color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<UserData> UserList() {
        ArrayList<UserData> arrayList = new ArrayList<>();
        UserData aryaSupport = new UserData();
        aryaSupport.userId = 1;
        aryaSupport.imageUrl = "http://www.aryausa.com/images/arya-logo.png";
        aryaSupport.userType = "Support Team";
        aryaSupport.userName = "Support Team";
        aryaSupport.userDeviceId = 2 + "";
        arrayList.add(aryaSupport);
        return arrayList;
    }
}
