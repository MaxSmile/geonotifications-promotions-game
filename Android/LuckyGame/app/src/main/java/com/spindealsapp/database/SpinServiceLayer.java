package com.spindealsapp.database;

import android.content.res.TypedArray;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.R;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.DateUtils;
import com.spindealsapp.util.Rrule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Volodymyr Kusenko on 24.08.2017.
 */

public class SpinServiceLayer {

    public static void saveSpins(ArrayList<Spin> spins) {
        Map<String, Spin> oldSpins = DBHelper.getInstance(App.getInstance()).getSpins();
        for (Spin spin : spins) {
            Spin oldSpin = oldSpins.get(spin.getId());
            if (oldSpin != null) {
                spin.setExtraCreateTime(oldSpin.getExtraCreateTime());
                spin.setExtra(oldSpin.isExtra());
            }
        }
        if (DBHelper.getInstance(App.getInstance()).saveSpins(spins)) {
            EventBus.getDefault().post(new Events.UpdatePlaces());
        }
    }

    public static Map<String, Spin> getSpins() {
        ArrayList<Spin> spins = new ArrayList<Spin>(DBHelper.getInstance(App.getInstance()).getSpins().values());
        Map<String, Spin> mSpins = new HashMap<String, Spin>();
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);
        for (Spin spin : spins) {
            if (spin.getExtraCreateTime() > 0 && DateUtils.isToday(new Date(spin.getExtraCreateTime()))) {
                spin.setExtraAvailable(false);
            } else {
                spin.setExtraAvailable(true);
            }
            if (Rrule.isAvailable(spin.getRrule())) {
                spin.setTimeLeft(DateFormat.getDiff(Rrule.getTimeEnd(spin.getRrule())));
                if (spin.getSpent() < spin.getLimit() || spin.isExtra()) {
                    spin.setAvailable(true);
                    updateSpinData(spin, spinIcon, spinType);
                    mSpins.put(spin.getPlaceKey(), spin);
                } else if (mSpins.get(spin.getPlaceKey()) == null ||
                        (!mSpins.get(spin.getPlaceKey()).isAvailable() && spin.isExtraAvailable())){
                    spin.setAvailable(false);
                    updateSpinData(spin, spinIcon, spinType);
                    mSpins.put(spin.getPlaceKey(), spin);
                }
            } else if (mSpins.get(spin.getPlaceKey()) == null) {
                spin.setAvailable(false);
                spin.setExtraAvailable(false);
                updateSpinData(spin, spinIcon, spinType);
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
        updateSpinData(spin, spinIcon, spinType);
        spinIcon.recycle();
        return spin;
    }

    public static void updateSpin(Spin spin) {
        DBHelper.getInstance(App.getInstance()).updateSpin(spin);
    }

    private static void updateSpinData(Spin spin, TypedArray spinIcon, String[] spinType) {
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
    }
}