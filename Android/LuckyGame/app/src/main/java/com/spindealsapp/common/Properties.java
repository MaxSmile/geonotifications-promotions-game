package com.spindealsapp.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.spindealsapp.App;
import com.spindealsapp.entity.User;

/**
 * Created by Kvm on 04.05.2017.
 */

public class Properties {

    private static SharedPreferences prefs;
    private static final String NEAR_ME_RADIUS = "nearMeRadius";
    private static final String SOUND_GAME = "soundGame";
    private static final String NOTIFICATIONS = "notifications";
    private static final String SHOW_TUTORIAL = "showTutorial";
    private static final String CURRENT_USER = "currentUser";

    private synchronized static SharedPreferences getPrefs(){
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }

    public static void setUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        getPrefs().edit().putString(CURRENT_USER, json).apply();
    }

    public static User getUser() {
        Gson gson = new Gson();
        String json = getPrefs().getString(CURRENT_USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static int getNearMeRadius(){
        String s = getPrefs().getString(NEAR_ME_RADIUS , "7000");
        if (s.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(s);
    }

    public static boolean getShowTutorial(){
        return getPrefs().getBoolean(SHOW_TUTORIAL, true);
    }

    public static void setShowTutorial(boolean show){
        getPrefs().edit().putBoolean(SHOW_TUTORIAL, show).apply();
    }

    public static boolean getSoundGame(){
        return getPrefs().getBoolean(SOUND_GAME, true);
    }

    public static boolean getNotifications(){
        return getPrefs().getBoolean(NOTIFICATIONS, true);
    }
}
