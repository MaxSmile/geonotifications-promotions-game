package com.vasilkoff.luckygame.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vasilkoff.luckygame.App;

/**
 * Created by Kvm on 04.05.2017.
 */

public class Properties {

    private static SharedPreferences prefs;
    private static final String NEAR_ME_RADIUS = "nearMeRadius";

    private synchronized static SharedPreferences getPrefs(){
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }

    public static int getNearMeRadius(){
        return Integer.parseInt(getPrefs().getString(NEAR_ME_RADIUS , "100"));
    }
}
