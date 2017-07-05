package com.vasilkoff.luckygame.database;

import android.content.res.TypedArray;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.Constants;

import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.CurrentUser;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.entity.UsedSpin;
import com.vasilkoff.luckygame.eventbus.Events;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kvm on 23.06.2017.
 */

public class FirebaseData {

    private static boolean initSpin;
    private static boolean initPlace;
    private static boolean initCompany;
    private static int countOffer;
    private static int countPlace;

    public static void placeListener() {

        Constants.DB_PLACE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (initPlace) {
                    getCoupons();
                }
                initPlace = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void companyListener() {
        final ArrayList<Company> companies = new ArrayList<Company>();
        Constants.DB_COMPANY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (initCompany) {
                    getPlaces();
                }
                initCompany = true;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    companies.add(data.getValue(Company.class));
                }

                DBHelper.getInstance(App.getInstance()).saveCompanies(companies);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void spinListener() {
        Constants.DB_SPIN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (initSpin) {
                    getPlaces();
                }
                initSpin = true;
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
        DBHelper.getInstance(App.getInstance()).saveCoupons(coupons, false);
        EventBus.getDefault().post(new Events.UpdateCoupons());
    }

    public static void getOffer() {
        Constants.DB_OFFER.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<CouponExtension> newCoupons = new ArrayList<CouponExtension>();
                final long count = dataSnapshot.getChildrenCount();
                countOffer = 0;
                for (final DataSnapshot dataOffer : dataSnapshot.getChildren()) {
                    CouponExtension baseCoupon = dataOffer.getValue(CouponExtension.class);
                    countOffer++;
                    Constants.DB_PLACE.orderByChild("offer").equalTo(baseCoupon.getGiftKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            countPlace = 0;
                            final long countLastPlace = dataSnapshot.getChildrenCount();
                            for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                                countPlace++;
                                final CouponExtension coupon = dataOffer.getValue(CouponExtension.class);
                                Place place = dataPlace.getValue(Place.class);
                                coupon.setCouponType(Constants.COUPON_TYPE_OFFER);
                                coupon.setPlaceName(place.getName());
                                coupon.setType(place.getType());
                                coupon.setGeoLat(place.getGeoLat());
                                coupon.setGeoLon(place.getGeoLon());
                                coupon.setCity(place.getCity());
                                coupon.setPlaceKey(place.getId());
                                Constants.DB_COMPANY.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Company company = dataSnapshot.getValue(Company.class);
                                        coupon.setCompanyName(company.getName());
                                        coupon.setLogo(company.getLogo());
                                        newCoupons.add(coupon);

                                        if (countPlace == countLastPlace && countPlace == countLastPlace) {
                                            updateOffer(newCoupons);
                                        }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateOffer(List<CouponExtension> coupons) {
        DBHelper.getInstance(App.getInstance()).saveCoupons(coupons, true);
        EventBus.getDefault().post(new Events.UpdateCoupons());
    }

    public static void getPlaces() {
        Constants.DB_PLACE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Place> places = new ArrayList<Place>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                    final Place place = dataPlace.getValue(Place.class);
                    Constants.DB_SPIN.orderByChild("placeKey").equalTo(place.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                addPlace(places, place, count);
                            }

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                final Spin spin = data.getValue(Spin.class);
                                place.setSpinFinish(spin.getDateFinish());
                                if (spin.getDateStart() <= System.currentTimeMillis() && spin.getDateFinish() >= System.currentTimeMillis()) {
                                    if (CurrentUser.user != null) {
                                        long timeShift = System.currentTimeMillis() - Constants.DAY_TIME_SHIFT;
                                        Constants.DB_USER.child(CurrentUser.user.getId()).child("place").child(place.getId())
                                                .orderByChild("time").startAt(timeShift).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                boolean spinAvailable = true;
                                                boolean extraSpinAvailable = true;

                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    UsedSpin usedSpin = data.getValue(UsedSpin.class);
                                                    if (usedSpin.getType() == Constants.SPIN_TYPE_NORMAL) {
                                                        spinAvailable = false;
                                                    }

                                                    if (usedSpin.getType() == Constants.SPIN_TYPE_EXTRA) {
                                                        extraSpinAvailable = false;
                                                    }
                                                }

                                                place.setSpinAvailable(spinAvailable);
                                                place.setExtraSpinAvailable(extraSpinAvailable);

                                                addPlace(places, place, count);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        place.setSpinAvailable(true);
                                        addPlace(places, place, count);
                                    }
                                } else {
                                    addPlace(places, place, count);
                                }

                            }
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

    private static void addPlace(ArrayList<Place> places, Place place, long count) {
        places.add(place);
        if (count == places.size()) {
            PlaceServiceLayer.updatePlaces(places);
        }
    }


    public static void getGift() {
        Constants.DB_GIFT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Gift> gifts = new ArrayList<Gift>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot dataGift : dataSnapshot.getChildren()) {
                    final Gift gift = dataGift.getValue(Gift.class);
                    String limitKey = DateFormat.getDate("yyyyMMdd", System.currentTimeMillis());
                    Constants.DB_LIMIT.child(gift.getCompanyKey()).child(limitKey).child(gift.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (gift.getDateStart() < System.currentTimeMillis() && gift.getDateFinish() > System.currentTimeMillis()) {
                                gift.setActive(true);
                                long countLimit = 0;
                                if (dataSnapshot.exists()) {
                                    countLimit = dataSnapshot.getValue(Long.class);
                                }
                                long countAvailable = gift.getLimitGifts() - countLimit;

                                if (countAvailable > 0) {
                                    gift.setActive(true);
                                } else {
                                    gift.setActive(false);
                                    countAvailable = 0;
                                }
                                gift.setCountAvailable(countAvailable);

                            } else {
                                gift.setActive(false);
                            }

                            gifts.add(gift);

                            if (gifts.size() == count) {
                                DBHelper.getInstance(App.getInstance()).saveGifts(gifts);
                            }
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
