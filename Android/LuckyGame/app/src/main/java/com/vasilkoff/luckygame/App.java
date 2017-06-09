package com.vasilkoff.luckygame;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
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
    }

    public static String getResourceString(int resId) {
        return instance.getString(resId);
    }

    public static App getInstance() {
        return instance;
    }
}
