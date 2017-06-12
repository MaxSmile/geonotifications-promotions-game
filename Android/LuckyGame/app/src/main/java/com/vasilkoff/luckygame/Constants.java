package com.vasilkoff.luckygame;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kvm on 19.05.2017.
 */

public class Constants {

    public static final DatabaseReference REFERENCE = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference DB_VERSION = REFERENCE.child("ver1");
    public static final DatabaseReference DB_COUPON = DB_VERSION.child("coupon");
    public static final DatabaseReference DB_COMPANY = DB_VERSION.child("company");
    public static final DatabaseReference DB_PLACE = DB_VERSION.child("place");
    public static final DatabaseReference DB_GIFT = DB_VERSION.child("gift");
    public static final DatabaseReference DB_SPIN = DB_VERSION.child("spin");
    public static final DatabaseReference DB_USER = DB_VERSION.child("user");

    public static final String PLACE_KEY = "placeKey";
    public static final String SPIN_TYPE_KEY = "spinType";

    public static final String[] COMPANY_TYPE_NAMES = App.getInstance().getResources().getStringArray(R.array.company_type);

    public static final int COUPON_STATUS_ACTIVE = -1;
    public static final int COUPON_STATUS_LOCK = 0;
    public static final int COUPON_STATUS_REDEEMED = 1;
    public static final int COUPON_STATUS_EXPIRED = 2;

    public static final int SPIN_STATUS_ACTIVE = 0;

    public static final int USER_TYPE_FACEBOOK = 0;
    public static final int USER_TYPE_GOOGLE = 1;
    public static final int DAY_TIME_SHIFT = 86400000;

    public static final int SPIN_TYPE = 0;
    public static final int EXTRA_SPIN_TYPE = 1;

    public static final int GAME_WIN = 1;
    public static final int GAME_LOSE  = 0;

}
