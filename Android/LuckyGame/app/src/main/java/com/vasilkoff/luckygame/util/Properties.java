package com.vasilkoff.luckygame.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vasilkoff.luckygame.App;

/**
 * Created by Kvm on 04.05.2017.
 */

public class Properties {

    private static SharedPreferences prefs;

    private synchronized static SharedPreferences getPrefs(){
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }
}
