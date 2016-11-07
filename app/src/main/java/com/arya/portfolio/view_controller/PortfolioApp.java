package com.arya.portfolio.view_controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import com.arya.lib.init.Env;
import com.arya.portfolio.database.DBHelperAP;
import com.arya.portfolio.database.DBManagerAP;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PortfolioApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Env.init(this, new DBHelperAP(), null, true);
        DBManagerAP.init();
        DBManagerAP.getInstance();

        setDefaultFont(this, "DEFAULT", "benton_sans_regular.ttf");

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }

    public void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected void replaceFont(String staticTypefaceFieldName, Typeface newTypeface) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Map<String, Typeface> newMap = new HashMap<>();
                newMap.put(staticTypefaceFieldName, newTypeface);
                try {
                    final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
                    staticField.setAccessible(true);
                    staticField.set(null, newMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
                    defaultFontTypefaceField.setAccessible(true);
                    defaultFontTypefaceField.set(null, newTypeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
