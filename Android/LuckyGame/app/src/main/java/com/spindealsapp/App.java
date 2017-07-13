package com.spindealsapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.spindealsapp.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Kvm on 04.05.2017.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        instance = this;

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        //built.setIndicatorsEnabled(true);
        //built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    public static String getResourceString(int resId) {
        return instance.getString(resId);
    }

    public static App getInstance() {
        return instance;
    }
}
