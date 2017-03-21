package com.rideshare.theteama.rideshare;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created on 03/01/17.
 */
public class Rideshare extends Application {

    public static String SERVER_URL = "";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void saveUser(Context context, HashMap<String, String> userInfo) {
        SharedPreferences preferences = context.getSharedPreferences("com.rideshare.user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("USER_ID", userInfo.get("USER_ID"));
        editor.putString("USER_NAME", userInfo.get("USER_NAME"));
        editor.putString("PHONE", userInfo.get("PHONE"));
        editor.apply();
    }

    public static HashMap<String, String> getUser(Context context) {
        HashMap<String, String> userInfo = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("com.rideshare.user", MODE_PRIVATE);
        if(preferences.contains("USER_ID")) {
            userInfo.put("USER_ID", preferences.getString("USER_ID", null));
            userInfo.put("USER_NAME", preferences.getString("USER_NAME", null));
            userInfo.put("PHONE", preferences.getString("PHONE", null));
            return userInfo;
        }
        return null;
    }
}
