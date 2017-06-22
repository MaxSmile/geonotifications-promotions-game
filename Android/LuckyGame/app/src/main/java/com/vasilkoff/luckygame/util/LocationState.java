package com.vasilkoff.luckygame.util;

import android.content.Context;
import android.location.LocationManager;

import com.vasilkoff.luckygame.App;

/**
 * Created by Kvm on 22.06.2017.
 */

public class LocationState {

    public static boolean isEnabled()
    {
        LocationManager lm = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
          return false;
        }

        return true;
    }
}
