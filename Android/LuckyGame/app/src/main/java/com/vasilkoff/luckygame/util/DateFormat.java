package com.vasilkoff.luckygame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
}
