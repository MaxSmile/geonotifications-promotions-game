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
        Date start = new Date();
        if (map.size() > 0) {
            start = ICalDateFormat.parse(map.get("DTSTART").get(0));
        }
        DateIterator it = rrule.getDateIterator(start , TimeZone.getDefault());
        boolean isAvailable = false;
        long limit = new Date().getTime() + 86400000;
        while (it.hasNext()) {
            Date date =  it.next();
            if (date.getTime() > limit) {
                return isAvailable;
            }
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

    public static long getTimeEnd(String rruleString) {
        RecurrenceRule rrule = getRRule(rruleString);
        Recurrence recur = rrule.getValue();
        Frequency freq = recur.getFrequency();
        Calendar calEnd = clearEndCalendar();
        if (recur.getByMonthDay().size() > 0 || freq == Frequency.DAILY) {
            return calEnd.getTimeInMillis();
        } else {
            DateIterator it = rrule.getDateIterator(new Date(), TimeZone.getTimeZone("GMT+00:00"));
            Calendar calStart = clearCalendar();

            long previous = calStart.getTimeInMillis();
            long endPeriod = getTimeEndPeriod(freq);
            long today = calStart.getTimeInMillis();
            long endContinuousPeriod = calEnd.getTimeInMillis();
            while (it.hasNext()) {
                Date date =  it.next();
                long time = date.getTime();
                if (time >= today) {
                    if ((time - previous) > 86400000 || time > endPeriod) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(previous);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.clear(Calendar.MILLISECOND);
                        endContinuousPeriod = cal.getTimeInMillis();
                        return endContinuousPeriod;
                    }
                    previous = time;
                }
            }
            return endContinuousPeriod;
        }
    }

    public static long getTimeEndPeriod(Frequency freq) {
        Calendar cal = clearEndCalendar();
        switch (freq) {
            case DAILY:
                return cal.getTimeInMillis();
            case WEEKLY:
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                return cal.getTimeInMillis();
            case MONTHLY:
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
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
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }

    private static Calendar clearEndCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }
}
