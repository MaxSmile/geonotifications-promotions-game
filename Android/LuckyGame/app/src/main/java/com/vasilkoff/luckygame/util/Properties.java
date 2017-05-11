package com.vasilkoff.luckygame.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vasilkoff.luckygame.App;

/**
 * Created by Kvm on 04.05.2017.
 */

public class Properties {

    private static SharedPreferences prefs;
    private static final String SPIN_TIME_CREATE = "spin_time_create";
    private static final String SPIN_ACTIVE = "spin_active";

    private synchronized static SharedPreferences getPrefs(){
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }

    public static long getSpinTimeCreate(){
        return getPrefs().getLong(SPIN_TIME_CREATE, 0);
    }

    public static void setSpinTimeCreate(long time){
        getPrefs().edit().putLong(SPIN_TIME_CREATE, time).apply();
    }

    public static boolean getSpinActive(){
        return getPrefs().getBoolean(SPIN_ACTIVE, false);
    }
    public static void setSpinActive(boolean active){
        getPrefs().edit().putBoolean(SPIN_ACTIVE, active).apply();
    }
}
