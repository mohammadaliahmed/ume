package com.umetechnologypvt.ume.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.Models.UserModel;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }




    public static void setUserModel(UserModel model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("UserModel", json);
    }

    public static UserModel getUserModel() {
        Gson gson = new Gson();
        UserModel model = gson.fromJson(preferenceGetter("UserModel"), UserModel.class);
        return model;
    }


    public static void setSettingDone(String fcmKey) {
        preferenceSetter("setting", fcmKey);
    }

    public static String getSettingDone() {
        return preferenceGetter("setting");
    }

    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }

    public static void setPhoneNumber(String value) {
        preferenceSetter("phone", value);
    }

    public static String getPhoneNumber() {
        return preferenceGetter("phone");
    }

    public static void setCountryCode(String value) {
        preferenceSetter("getCountryCode", value);
    }

    public static String getCountryCode() {
        return preferenceGetter("getCountryCode");
    }


    public static void setIsLoggedIn(boolean value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("loggedIn", value);
        editor.apply();
    }

    public static boolean getIsLoggedIn() {
        SharedPreferences pref;
        boolean value;
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getBoolean("loggedIn", false);
        return value;
    }

    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }

    public static void setNotificationCount(String count) {
        preferenceSetter("setChatCount", count);
    }

    public static String getNotificationCount() {
        return preferenceGetter("setChatCount");
    }
    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
