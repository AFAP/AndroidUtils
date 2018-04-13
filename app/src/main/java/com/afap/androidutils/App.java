package com.afap.androidutils;

import android.app.Application;
import android.util.Log;

import com.afap.androidutils.database.DB;

public class App extends Application {
    private static final String TAG = "SysApplication";

    private static App mInstance;
    private DB mDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("App", "Application被创建");
        mInstance = this;
        mDB = new DB(this);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }


    public synchronized DB getDB() {
        if (mDB == null) {
            mDB = new DB(this);
        }
        return mDB;
    }

}