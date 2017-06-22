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
    private static final String SOUND_GAME = "soundGame";
    private static final String NOTIFICATIONS = "notifications";

    private synchronized static SharedPreferences getPrefs(){
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }

    public static int getNearMeRadius(){
        String s = getPrefs().getString(NEAR_ME_RADIUS , "100");
        if (s.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(s);
    }

    public static boolean getSoundGame(){
        return getPrefs().getBoolean(SOUND_GAME, true);
    }

    public static boolean getNotifications(){
        return getPrefs().getBoolean(NOTIFICATIONS, true);
    }
}
