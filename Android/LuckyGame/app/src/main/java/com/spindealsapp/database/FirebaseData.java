package com.spindealsapp.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.entity.UsedSpin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.DateUtils;
import com.spindealsapp.util.Rrule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kvm on 23.06.2017.
 */

public class FirebaseData {

    private static boolean initPlace;
    private static boolean initCompany;

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

    public static void checkCouponsByCode(final String code) {
        if (code != null) {
            Constants.DB_COUPON.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void getKeywords() {
        Constants.DB_KEYWORDS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> keywords = new ArrayList<String>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    keywords.add(data.getValue(String.class));
                }
                DBHelper.getInstance(App.getInstance()).saveKeywords(keywords);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getCouponsByCode(String code) {
        List<String> couponsCode = new ArrayList<String>();
        couponsCode.add(code);
        getCouponsList(couponsCode, true);
    }

    public static void getCoupons() {
        getCouponsList(DBHelper.getInstance(App.getInstance()).getCoupons(), false);
    }

    private static void getCouponsList(final List<String> couponsCode, final boolean addedCoupon) {
        final List<CouponExtension> newCoupons = new ArrayList<CouponExtension>();
        for (int i = 0; i < couponsCode.size(); i++) {
            Constants.DB_COUPON.child(couponsCode.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final CouponExtension coupon = dataSnapshot.getValue(CouponExtension.class);
                    if (dataSnapshot.exists() && coupon.getPlaceKey() != null) {
                        Constants.DB_PLACE.child(coupon.getPlaceKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Place place = dataSnapshot.getValue(Place.class);
                                    coupon.setPlaceName(place.getName());
                                    coupon.setType(place.getType());
                                    coupon.setGeoLat(place.getGeoLat());
                                    coupon.setGeoLon(place.getGeoLon());
                                    coupon.setCity(place.getCity());
                                    coupon.setKeywords(place.getKeywords());
                                    Constants.DB_COMPANY.child(coupon.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Company company = dataSnapshot.getValue(Company.class);
                                                coupon.setCompanyName(company.getName());
                                                coupon.setLogo(company.getLogo());
                                                Constants.DB_GIFT.child(coupon.getGiftKey()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            Gift gift = dataSnapshot.getValue(Gift.class);
                                                            coupon.setRules(gift.getRules());
                                                            coupon.setDescription(gift.getDescription());
                                                            newCoupons.add(coupon);

                                                            //if (newCoupons.size() > 0 && newCoupons.size() == couponsCode.size()) {
                                                            if (newCoupons.size() > 0) {
                                                                updateCoupons(newCoupons, addedCoupon);
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

    private static void updateCoupons(List<CouponExtension> coupons, boolean addedCoupon) {
        DBHelper.getInstance(App.getInstance()).saveCoupons(coupons, false);
        if (addedCoupon) {
            EventBus.getDefault().post(new Events.AddedCoupon());
        } else {
            EventBus.getDefault().post(new Events.UpdateCoupons());
        }
    }

    public static void getOffer() {
        Constants.DB_OFFER.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<CouponExtension> newCoupons = new ArrayList<CouponExtension>();
                for (final DataSnapshot dataOffer : dataSnapshot.getChildren()) {
                    CouponExtension baseCoupon = dataOffer.getValue(CouponExtension.class);
                    Constants.DB_PLACE.orderByChild("offer").equalTo(baseCoupon.getGiftKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                                    final CouponExtension coupon = dataOffer.getValue(CouponExtension.class);
                                    Place place = dataPlace.getValue(Place.class);
                                    coupon.setCouponType(Constants.COUPON_TYPE_OFFER);
                                    coupon.setPlaceName(place.getName());
                                    coupon.setType(place.getType());
                                    coupon.setGeoLat(place.getGeoLat());
                                    coupon.setGeoLon(place.getGeoLon());
                                    coupon.setCity(place.getCity());
                                    coupon.setPlaceKey(place.getId());
                                    Constants.DB_COMPANY.child(place.getCompanyKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Company company = dataSnapshot.getValue(Company.class);
                                                coupon.setCompanyName(company.getName());
                                                coupon.setLogo(company.getLogo());
                                                newCoupons.add(coupon);
                                                updateOffer(newCoupons);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            } else {
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

    private static void updateOffer(List<CouponExtension> coupons) {
        DBHelper.getInstance(App.getInstance()).saveCoupons(coupons, true);
        EventBus.getDefault().post(new Events.UpdateCoupons());
    }

    public static void getSpins() {
        Constants.DB_SPIN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Spin> spins = new ArrayList<Spin>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Spin spin = data.getValue(Spin.class);
                    if (CurrentUser.user != null) {
                        long timeShift = Rrule.getTimeStart(spin.getRrule());
                        Constants.DB_USER.child(CurrentUser.user.getId()).child("place")
                                .child(spin.getPlaceKey()).child(spin.getId())
                                .orderByChild("time").startAt(timeShift).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long countSpent = 0;

                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    UsedSpin usedSpin = data.getValue(UsedSpin.class);
                                    if (usedSpin.getType() == Constants.SPIN_TYPE_NORMAL) {
                                        countSpent++;
                                    }
                                    /*if (DateUtils.isToday(new Date(usedSpin.getTime())) && usedSpin.getType() == Constants.SPIN_TYPE_EXTRA) {
                                        spin.setExtraAvailable(false);
                                    }*/
                                }

                                spin.setSpent(countSpent);
                                spins.add(spin);
                                updateSpin(spins, count);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        spins.add(spin);
                        updateSpin(spins, count);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateSpin(ArrayList<Spin> spins, long count) {
        if (spins.size() == count) {
            SpinServiceLayer.saveSpins(spins);
        }
    }

    public static void getPlaces() {
        Constants.DB_PLACE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Place> places = new ArrayList<Place>();
                long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                    places.add(dataPlace.getValue(Place.class));
                    if (count == places.size()) {
                        PlaceServiceLayer.updatePlaces(places);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateGift(ArrayList<Gift> gifts, long count) {
        if (gifts.size() == count) {
            DBHelper.getInstance(App.getInstance()).saveGifts(gifts);
        }
    }

    public static void getGift() {
        Constants.DB_GIFT.orderByChild("spinKey").startAt("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Gift> gifts = new ArrayList<Gift>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot dataGift : dataSnapshot.getChildren()) {
                    final Gift gift = dataGift.getValue(Gift.class);
                    Constants.DB_SPIN.child(gift.getSpinKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Spin spin = dataSnapshot.getValue(Spin.class);
                            if (spin != null) {
                                long timeShift = Rrule.getTimeStart(spin.getRrule());
                                Constants.DB_LIMIT.child(gift.getCompanyKey()).child(gift.getId())
                                        .orderByChild("date").startAt(timeShift).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        long countSpent = 0;
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            long value = (long)data.child("value").getValue();
                                            countSpent += value;
                                        }
                                        long countAvailable = gift.getLimitGifts() - countSpent;

                                        if (countAvailable > 0) {
                                            gift.setActive(true);
                                        } else {
                                            gift.setActive(false);
                                            countAvailable = 0;
                                        }
                                        gift.setCountAvailable(countAvailable);
                                        gifts.add(gift);

                                        updateGift(gifts, count);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                gifts.add(gift);
                                updateGift(gifts, count);
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
