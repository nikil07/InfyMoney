package com.androidworks.nikhil.infymoney.utils;

import android.content.Context;

/**
 * Created by Nikhil on 09-Jan-17.
 */
public class Application extends android.app.Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Application.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Application.context;
    }
}
