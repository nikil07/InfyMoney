package com.androidworks.nikhil.infymoney.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.androidworks.nikhil.infymoney.model.SMS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Nikhil on 23-Nov-16.
 */
public class DataStore {

    static Gson gson = new Gson();
    static ArrayList<SMS> timeZones;

    private DataStore() {
        // private constructor to enforce singleton
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(Application.getAppContext());
    }

    private static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public static void storeMessages(SMS item) {

        timeZones = new ArrayList<>();
        timeZones = gson.fromJson(getSharedPreferences().getString(Constants.SHARED_PREF_SMS, timeZones.toString()), new TypeToken<ArrayList<SMS>>() {
        }.getType());
        timeZones.add(item);
        getEditor().putString(Constants.SHARED_PREF_SMS, gson.toJson(timeZones)).apply();
    }

    public static ArrayList<SMS> getMessages() {

        timeZones = new ArrayList<>();
        return gson.fromJson(getSharedPreferences().getString(Constants.SHARED_PREF_SMS, timeZones.toString())
                , new TypeToken<ArrayList<SMS>>() {
                }.getType());
    }

    public static void deleteAllMessages() {
        timeZones.clear();
        getEditor().putString(Constants.SHARED_PREF_SMS, gson.toJson(timeZones)).apply();
    }

    public static void storeBalance(String balance) {
        getEditor().putString(Constants.SHARED_PREF_BALANCE, balance).apply();
    }

    public static String getBalance() {
        return getSharedPreferences().getString(Constants.SHARED_PREF_BALANCE, "Nil");
    }

    public static void storeAccount(String account) {
        getEditor().putString(Constants.SHARED_PREF_ACCOUNT, account).apply();
    }

    public static String getAccount() {
        return getSharedPreferences().getString(Constants.SHARED_PREF_ACCOUNT, "");
    }

    public static void setIsLoggedIn() {
        getEditor().putBoolean(Constants.SHARED_PREF_LOGGED, true).apply();
    }

    public static boolean isLoggedIn() {
        return (getSharedPreferences().getBoolean(Constants.SHARED_PREF_LOGGED, false));
    }

}
