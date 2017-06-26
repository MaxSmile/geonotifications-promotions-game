package com.vasilkoff.luckygame.database;

import android.content.res.TypedArray;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.eventbus.Events;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Kvm on 23.06.2017.
 */

public class FirebaseData {

    public static void placeListener() {
        Constants.DB_PLACE.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getCoupons();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void companyListener() {
        Constants.DB_COMPANY.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getCoupons();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void checkCouponsByCode(final String code) {
        if (code != null) {
            Constants.DB_COUPON.child(code.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        getCouponsByCode(code);
                        EventBus.getDefault().post(new Events.CheckCoupons(true));
                    } else {
                        EventBus.getDefault().post(new Events.CheckCoupons(false));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            EventBus.getDefault().post(new Events.CheckCoupons(false));
        }
    }

    private static void getCouponsByCode(String code) {
        List<String> couponsCode = new ArrayList<String>();
        couponsCode.add(code);
        getCouponsList(couponsCode);
    }

    public static void getCoupons() {
        getCouponsList(DBHelper.getInstance(App.getInstance()).getCoupons());
    }

    private static void getCouponsList(final List<String> couponsCode) {
        final List<CouponExtension> newCoupons = new ArrayList<CouponExtension>();
        for (int i = 0; i < couponsCode.size(); i++) {
            Constants.DB_COUPON.child(couponsCode.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final CouponExtension coupon = dataSnapshot.getValue(CouponExtension.class);
                    if (coupon != null) {
                        Constants.DB_PLACE.child(coupon.getPlaceKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Place place = dataSnapshot.getValue(Place.class);
                                coupon.setPlaceName(place.getName());
                                coupon.setType(place.getType());
                                coupon.setGeoLat(place.getGeoLat());
                                coupon.setGeoLon(place.getGeoLon());
                                coupon.setCity(place.getCity());
                                Constants.DB_COMPANY.child(coupon.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Company company = dataSnapshot.getValue(Company.class);
                                        coupon.setCompanyName(company.getName());
                                        coupon.setLogo(company.getLogo());
                                        Constants.DB_GIFT.child(coupon.getGiftKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Gift gift = dataSnapshot.getValue(Gift.class);
                                                coupon.setRules(gift.getRules());
                                                coupon.setDescription(gift.getDescription());
                                                newCoupons.add(coupon);

                                                if (newCoupons.size() > 0 && newCoupons.size() == couponsCode.size()) {
                                                    updateCoupons(newCoupons);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private static void updateCoupons(List<CouponExtension> coupons) {
        DBHelper.getInstance(App.getInstance()).saveCoupons(coupons);
        EventBus.getDefault().post(new Events.UpdateCoupons());
    }
}
