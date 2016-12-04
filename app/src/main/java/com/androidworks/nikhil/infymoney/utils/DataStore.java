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

    private static DataStore instance;
    private static SharedPreferences sharedPreferences;
    static Gson gson = new Gson();
    static ArrayList<String> countries;
    static ArrayList<SMS> timeZones;

    private DataStore() {
        // private constructor to enforce singleton
    }

    public static DataStore getInstance(Context context) {
        if (instance == null) {
            instance = new DataStore();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            countries = new ArrayList<>();
            timeZones = new ArrayList<>();
        }
        return instance;
    }

    public void storeMessages(SMS item) {

        timeZones = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_SMS, timeZones.toString()), new TypeToken<ArrayList<SMS>>() {
        }.getType());
        timeZones.add(item);
        sharedPreferences.edit().putString(Constants.SHARED_PREF_SMS, gson.toJson(timeZones)).apply();
    }

    public ArrayList<SMS> getMessages() {

        Log.d("nikhil", "messages items list " + gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_SMS, timeZones.toString())
                , new TypeToken<ArrayList<SMS>>() {
                }.getType()).toString());

        return gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_SMS, timeZones.toString())
                , new TypeToken<ArrayList<SMS>>() {
                }.getType());
    }

    public void deleteAllMessages() {
        sharedPreferences.edit().clear().apply();
        timeZones.clear();
        sharedPreferences.edit().putString(Constants.SHARED_PREF_SMS, gson.toJson(timeZones)).apply();
    }

    public void storeBalance(String balance) {
        sharedPreferences.edit().putString(Constants.SHARED_PREF_BALANCE, balance).apply();
    }

    public String getBalance() {
        return sharedPreferences.getString(Constants.SHARED_PREF_BALANCE, "Nil");
    }

    public void storeAccount(String account) {
        sharedPreferences.edit().putString(Constants.SHARED_PREF_ACCOUNT, account).apply();
    }

    public String getAccount() {
        return sharedPreferences.getString(Constants.SHARED_PREF_ACCOUNT, "");
    }

    public void setIsLoggedIn() {
        sharedPreferences.edit().putBoolean(Constants.SHARED_PREF_LOGGED, true).apply();
    }

    public boolean isLoggedIn() {
        return (sharedPreferences.getBoolean(Constants.SHARED_PREF_LOGGED, false));
    }
}
