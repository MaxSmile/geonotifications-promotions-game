package com.spindealsapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import biweekly.ICalVersion;
import biweekly.io.ParseContext;
import biweekly.io.scribe.property.RecurrenceRuleScribe;
import biweekly.parameter.ICalParameters;
import biweekly.property.RecurrenceRule;
import biweekly.util.Frequency;
import biweekly.util.ICalDateFormat;
import biweekly.util.Recurrence;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;

/**
 * Created by Volodymyr Kusenko on 21.08.2017.
 */

public class Rrule {

    public static boolean isAvailable(String rruleString) {
        RecurrenceRule rrule = getRRule(rruleString);
        Recurrence recur = rrule.getValue();
        Map<String, List<String>> map = recur.getXRules();
        DateIterator it = rrule.getDateIterator(ICalDateFormat.parse(map.get("DTSTART").get(0)) , TimeZone.getDefault());
        boolean isAvailable = false;
        while (it.hasNext()) {
            Date date =  it.next();
            if (DateUtils.isToday(date)) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    public static long getTimeStart(String rruleString) {
        RecurrenceRule rrule = getRRule(rruleString);
        Recurrence recur = rrule.getValue();
        Frequency freq = recur.getFrequency();
        Calendar cal = clearCalendar();
        switch (freq) {
            case DAILY:
                return cal.getTimeInMillis();
            case WEEKLY:
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                return cal.getTimeInMillis();
            case MONTHLY:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                return cal.getTimeInMillis();
        }
        return 0;
    }

    private static RecurrenceRule getRRule(String rruleString) {
        RecurrenceRuleScribe scribe = new RecurrenceRuleScribe();
        ParseContext context = new ParseContext();
        context.setVersion(ICalVersion.V2_0);
        return scribe.parseText(rruleString, null, new ICalParameters(), context);
    }

    private static Calendar clearCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }
}
