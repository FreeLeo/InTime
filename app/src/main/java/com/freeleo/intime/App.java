package com.freeleo.intime;

import android.app.Application;

import com.freeleo.intime.utils.PreferencesUtil;

/**
 * Description: application
 * Author: lizhen
 * Create Date: 21/03/2018
 */
public class App extends Application {
    private static App mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        PreferencesUtil.initialize(this);
    }

    public static App getInstance(){
        return  mInstance;
    }
}
