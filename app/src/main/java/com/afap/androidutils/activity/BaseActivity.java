package com.afap.androidutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.afap.androidutils.App;
import com.afap.androidutils.database.DB;

public abstract class BaseActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    protected App getApp() {
        return App.getInstance();
    }

    protected DB getDB() {
        return getApp().getDB();
    }


}