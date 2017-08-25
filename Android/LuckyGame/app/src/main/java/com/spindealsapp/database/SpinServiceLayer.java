package com.spindealsapp.database;

import com.spindealsapp.App;
import com.spindealsapp.entity.Spin;

import java.util.ArrayList;

/**
 * Created by Volodymyr Kusenko on 24.08.2017.
 */

public class SpinServiceLayer {

    public static void saveSpins(ArrayList<Spin> spins) {
        DBHelper.getInstance(App.getInstance()).saveSpins(spins);
    }

    public static ArrayList<Spin> getSpins() {
        ArrayList<Spin> spins = DBHelper.getInstance(App.getInstance()).getSpins();
        ArrayList<String> activeSpinPlace = new ArrayList<String>();
        for (Spin spin : spins) {
            if (spin.getSpent() >= spin.getLimit()) {
                spin.setAvailable(false);
            } else {
                activeSpinPlace.add(spin.getPlaceKey());
            }
        }
        return spins;
    }
}
