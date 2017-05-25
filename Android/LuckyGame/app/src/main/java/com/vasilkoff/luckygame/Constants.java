package com.vasilkoff.luckygame;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kvm on 19.05.2017.
 */

public class Constants {

    public static final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference dbVersion = reference.child("ver1");
    public static final DatabaseReference dbData = dbVersion.child("data");
    public static final DatabaseReference dbCompanies = dbData.child("companies");
    public static final DatabaseReference dbCoupons = dbVersion.child("coupons");
    public static final int COUPON_STATUS_ACTIVE = -1;
    public static final int COUPON_STATUS_LOCK = 0;
    public static final int COUPON_STATUS_REDEEMED = 1;
    public static final int COUPON_STATUS_EXPIRED = 2;
}
