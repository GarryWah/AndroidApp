package com.example.admin.androidapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 3/8/2017.
 */

public class UtilPreferences {

    private static UtilPreferences instance;
    private static SharedPreferences sharedPreferences;

    public UtilPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static UtilPreferences init(Context context) {
        if (instance == null)
            instance = new UtilPreferences(context);
        return instance;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static String getId() {
        return sharedPreferences.getString("id", null);
    }

    public static String getEmail() {
        return sharedPreferences.getString("email", null);
    }

    public static String getPassword() {
        return sharedPreferences.getString("password", null);
    }

    public static void putEmail(String email) {
        sharedPreferences.edit().putString("email", email).apply();
    }

    public static void putPassword(String password) {
        sharedPreferences.edit().putString("password", password).apply();
    }

    public static void putRemember(boolean remember) {
        sharedPreferences.edit().putBoolean("remember", true).apply();
    }

}
