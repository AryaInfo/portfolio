package com.arya.portfolio.utility;

import android.content.Context;
import android.graphics.Typeface;
import java.lang.reflect.Field;

/**
 * Created by user on 23/09/16.
 */

public class FontsOverride {

    public static void setDefaultFont(Context context,String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,final Typeface newTypeface) {
        try {
            Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}