package com.vasilkoff.luckygame;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kvm on 19.05.2017.
 */

public class Constants {

    public static final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference dbVersion = reference.child("ver1");
    public static final DatabaseReference dbCoupon = dbVersion.child("coupon");
    public static final DatabaseReference dbCompany = dbVersion.child("company");
    public static final DatabaseReference dbPlace = dbVersion.child("place");
    public static final DatabaseReference dbGift = dbVersion.child("gift");
    public static final DatabaseReference dbSpin = dbVersion.child("spin");
    public static final DatabaseReference dbUser = dbVersion.child("user");

    public static final String PLACE_KEY = "placeKey";

    public static final String[] companyTypeNames = App.getInstance().getResources().getStringArray(R.array.company_type);

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
