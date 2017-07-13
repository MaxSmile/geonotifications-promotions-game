package com.spindealsapp.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kvm on 06.07.2017.
 */

public class ValidatorUtil {

    private static Pattern pattern;
    private static Matcher matcher;

    private static final String COUPON_PATTERN = "^[a-zA-Z0-9]*$";

    public static boolean validateCouponCode(String line){
        pattern = Pattern.compile(COUPON_PATTERN);
        matcher = pattern.matcher(line);
        return matcher.matches();
    }
}
