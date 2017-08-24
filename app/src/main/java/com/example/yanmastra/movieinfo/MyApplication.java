package com.example.yanmastra.movieinfo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Yan Mastra on 8/23/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
