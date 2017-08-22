package com.spindealsapp.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import biweekly.ICalVersion;
import biweekly.io.ParseContext;
import biweekly.io.scribe.property.RecurrenceRuleScribe;
import biweekly.parameter.ICalParameters;
import biweekly.property.RecurrenceRule;
import biweekly.util.ICalDateFormat;
import biweekly.util.Recurrence;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;

/**
 * Created by Volodymyr Kusenko on 21.08.2017.
 */

public class Rrule {
    public static boolean isAvailable(String rruleString) {
        RecurrenceRuleScribe scribe = new RecurrenceRuleScribe();
        ParseContext context = new ParseContext();
        context.setVersion(ICalVersion.V2_0);
        RecurrenceRule rrule = scribe.parseText(rruleString, null, new ICalParameters(), context);
        Recurrence recur = rrule.getValue();
        Map<String, List<String>> map = recur.getXRules();
        DateIterator it = rrule.getDateIterator(ICalDateFormat.parse(map.get("DTSTART").get(0)) , TimeZone.getDefault());
        boolean isAvailable = false;
        while (it.hasNext()) {
            Date date =  it.next();
            System.out.println("myTest = " + date);
            System.out.println("myTest = " + DateUtils.isToday(date));
            if (DateUtils.isToday(date)) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }
}
