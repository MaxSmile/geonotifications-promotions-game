package com.spindealsapp.util;

import com.spindealsapp.App;
import com.spindealsapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kvm on 16.05.2017.
 */

public class DateFormat {

    private static Calendar calendar = Calendar.getInstance();

    public static String getDate(String format, Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    public static String getDiff(long time) {
        long diff = time - System.currentTimeMillis();
        if (diff > 0) {
            long d = diff / 86400000;
            if (d > 0) {
                if (d == 1) {
                    return String.format(App.getResourceString(R.string.day), d);
                }
                return String.format(App.getResourceString(R.string.days), d);
            } else {
                long h = diff / 3600000;
                long m = (diff - (h * 3600000)) / 60000;
                if (h > 0) {
                    return String.format(App.getResourceString(R.string.time), h, m);
                }
                return String.format(App.getResourceString(R.string.time_short), m);
            }
        } else {
            return null;
        }
    }

}
