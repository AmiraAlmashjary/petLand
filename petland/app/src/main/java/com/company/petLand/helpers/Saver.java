package com.company.petLand.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Saver {
    public static final String CUSTOMER = "customer";
    public static final String FAMILY = "family";


    @SuppressLint("ApplySharedPref")
    public static void saveType(Context context, String type) {
        SharedPreferences sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type", type);
        editor.commit();
    }

    public static String getType(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("type", "");
    }

    public static void clear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }
}
