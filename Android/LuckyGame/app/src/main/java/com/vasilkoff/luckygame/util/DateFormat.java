package com.vasilkoff.luckygame.util;

import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
                return String.format(App.getResourceString(R.string.day), d);
            } else {
                long h = diff / 3600000;
                long m = (diff - (h * 3600000)) / 60000;
                return String.format(App.getResourceString(R.string.time), h, m);
            }
        } else {
            return null;
        }
    }

}
