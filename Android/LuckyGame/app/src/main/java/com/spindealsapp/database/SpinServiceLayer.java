package com.spindealsapp.database;

import android.content.res.TypedArray;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.R;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.DateUtils;
import com.spindealsapp.util.Rrule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Volodymyr Kusenko on 24.08.2017.
 */

public class SpinServiceLayer {

    public static void saveSpins(ArrayList<Spin> spins) {
        DBHelper.getInstance(App.getInstance()).saveSpins(spins);
    }

    public static Map<String, Spin> getSpins() {
        ArrayList<Spin> spins = DBHelper.getInstance(App.getInstance()).getSpins();
        Map<String, Spin> mSpins = new HashMap<String, Spin>();
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);
        for (Spin spin : spins) {
            if (Rrule.isAvailable(spin.getRrule())) {
                if (spin.getSpent() < spin.getLimit()) {
                    updateSpin(spin, spinIcon, spinType);
                    mSpins.put(spin.getPlaceKey(), spin);
                } else if (mSpins.get(spin.getPlaceKey()) == null ||
                        (!mSpins.get(spin.getPlaceKey()).isAvailable() && spin.isExtraAvailable())){
                    spin.setAvailable(false);
                    updateSpin(spin, spinIcon, spinType);
                    mSpins.put(spin.getPlaceKey(), spin);
                }
            } else if (mSpins.get(spin.getPlaceKey()) == null) {
                spin.setAvailable(false);
                spin.setExtraAvailable(false);
                updateSpin(spin, spinIcon, spinType);
                mSpins.put(spin.getPlaceKey(), spin);
            }
        }
        spinIcon.recycle();
        return mSpins;
    }

    public static Spin getSpinComing() {
        Spin spin = new Spin();
        spin.setId("0");
        spin.setAvailable(false);
        spin.setExtraAvailable(false);
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);
        updateSpin(spin, spinIcon, spinType);
        spinIcon.recycle();
        return spin;
    }

    private static void updateSpin(Spin spin, TypedArray spinIcon, String[] spinType) {
        if (spin.isAvailable()) {
            spin.setStatus(Constants.SPIN_STATUS_ACTIVE);
        } else {
            if (spin.isExtraAvailable()) {
                spin.setStatus(Constants.SPIN_STATUS_EXTRA_AVAILABLE);
            } else {
                spin.setStatus(Constants.SPIN_STATUS_COMING);
            }
        }

        spin.setStatusIcon(spinIcon.getDrawable(spin.getStatus()));
        spin.setStatusString(spinType[spin.getStatus()]);
        spin.setTimeLeft(DateFormat.getDiff(DateUtils.getEnd(new Date()).getTime()));
    }
}
