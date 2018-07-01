package com.zamanak.msglib.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;

import java.lang.reflect.Field;

/**
 * Created by PirFazel on 11/29/2016.
 */

public abstract class FontUtils {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    private static void replaceFont(String staticTypefaceFieldName,
                                    final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static SpannableString textWithFont(Context context, int text) {
        SpannableString s = new SpannableString(context.getString(text));
        s.setSpan(new TypefaceSpan(context, "IRANSansWeb.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public static SpannableString textWithFont(Context context, String text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new TypefaceSpan(context, "IRANSansWeb.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }


}