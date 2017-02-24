package com.vasilkoff.luckygame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            System.out.println("TEST+ = " + intent.getStringExtra("id"));
        }
        else {
            Log.d(getClass().getSimpleName(), "exiting");
            System.out.println("TEST- = " + intent.getStringExtra("id"));
        }
    }
}
