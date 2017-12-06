package com.spindealsapp.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.database.service.CompanyServiceLayer;
import com.spindealsapp.database.service.GiftServiceLayer;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Count;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.entity.UsedSpin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.Rrule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kvm on 23.06.2017.
 */

public class FirebaseData {
    private static DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
    private static boolean firstConnection;

    private static Count countChildren;
    private static boolean initCompanies;
    private static long countCompanies;
    private static boolean initPlaces;
    private static long countPlaces;
    private static boolean initGifts;
    private static long countGifts;
    private static boolean initSpins;
    private static long countSpins;
    private static boolean initOffers;
    private static long countOffers;

    private static ArrayList<Gift> giftsList;
    private static ArrayList<Spin> spinsList;
    private static ArrayList<CouponExtension> offersList;

    private static void checkConnection() {
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (firstConnection && !connected) {
                    cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private static void init() {
        firstConnection = false;
        initCompanies = false;
        countCompanies = 0;
        initPlaces = false;
        countPlaces = 0;
        initGifts = false;
        countGifts = 0;
        initSpins = false;
        countSpins = 0;
        initOffers = false;
        countOffers = 0;
    }

    public static void loadData() {
        init();
        checkConnection();
        getKeywords();
        countChildren = new Count();
        getCountCompanies();
    }

    private static void cancel() {
        EventBus.getDefault().post(new Events.FinishLoadData());
    }

    private static void getCountCompanies() {
        Constants.DB_COMPANY.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countChildren.setCompanies(dataSnapshot.getChildrenCount());
                EventBus.getDefault().post(new Events.LoadingData(5));
                getCountPlaces();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void getCountPlaces() {
        Constants.DB_PLACE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countChildren.setPlaces(dataSnapshot.getChildrenCount());
                EventBus.getDefault().post(new Events.LoadingData(20));
                getCountGifts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void getCountGifts() {
        Constants.DB_GIFT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countChildren.setGifts(dataSnapshot.getChildrenCount());
                EventBus.getDefault().post(new Events.LoadingData(25));
                getCountSpins();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void getCountSpins() {
        Constants.DB_SPIN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countChildren.setSpins(dataSnapshot.getChildrenCount());
                EventBus.getDefault().post(new Events.LoadingData(45));
                getCountOffers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void getCountOffers() {
        Constants.DB_OFFER.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countChildren.setOffers(dataSnapshot.getChildrenCount());
                EventBus.getDefault().post(new Events.LoadingData(50));
                companyListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }


    private static void companyListener() {
        final ArrayList<Company> companies = new ArrayList<Company>();
        Constants.DB_COMPANY.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (initCompanies) {
                    insertCompany(dataSnapshot.getValue(Company.class));
                } else {
                    companies.add(dataSnapshot.getValue(Company.class));
                    countCompanies++;
                    if (countCompanies == countChildren.getCompanies()) {
                        initCompanies = true;
                        CompanyServiceLayer.add(companies);
                        //DBHelper.getInstance(App.getInstance()).saveCompanies(companies);
                        placeListener();
                        EventBus.getDefault().post(new Events.LoadingData(55));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                insertCompany(dataSnapshot.getValue(Company.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void insertCompany(Company company) {
        CompanyServiceLayer.add(company);
        PlaceServiceLayer.calculateData();
    }

    private static void placeListener() {
        final ArrayList<Place> places = new ArrayList<Place>();
        Constants.DB_PLACE.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (initPlaces) {
                    PlaceServiceLayer.insertPlace(dataSnapshot.getValue(Place.class));
                } else {
                    places.add(dataSnapshot.getValue(Place.class));
                    countPlaces++;
                    if (countPlaces == countChildren.getPlaces()) {
                        initPlaces = true;
                        PlaceServiceLayer.updatePlaces(places);
                        giftListener();
                        EventBus.getDefault().post(new Events.LoadingData(70));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PlaceServiceLayer.insertPlace(dataSnapshot.getValue(Place.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void giftListener() {
        giftsList = new ArrayList<Gift>();
        Constants.DB_GIFT.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateGift(dataSnapshot.getValue(Gift.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateGift(dataSnapshot.getValue(Gift.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    public static void refreshGifts(List<Gift> gifts) {
        initGifts = true;
        for (Gift gift : gifts) {
            updateGift(gift);
        }
    }

    private static void updateGift(final Gift gift) {
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
                            saveGift(gift);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            cancel();
                        }
                    });
                } else {
                    saveGift(gift);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void saveGift(Gift gift) {
        if (initGifts) {
            GiftServiceLayer.add(gift);
        } else {
            if (giftsList != null)
                giftsList.add(gift);

            countGifts++;
            if (countGifts == countChildren.getGifts()) {
                initGifts = true;
                if (giftsList != null)
                    GiftServiceLayer.add(giftsList);

                spinListener();
                EventBus.getDefault().post(new Events.LoadingData(70));
            }
        }
    }

    private static void spinListener() {
        spinsList = new ArrayList<Spin>();
        Constants.DB_SPIN.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateSpin(dataSnapshot.getValue(Spin.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateSpin(dataSnapshot.getValue(Spin.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void updateSpin(final Spin spin) {
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
                    }
                    spin.setSpent(countSpent);
                    saveSpin(spin);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    cancel();
                }
            });
        } else {
            saveSpin(spin);
        }
    }

    private static void saveSpin(Spin spin) {
        if (initSpins) {
            SpinServiceLayer.insertSpin(spin);
        } else {
            spinsList.add(spin);
            countSpins++;
            if (countSpins == countChildren.getSpins()) {
                initSpins = true;
                SpinServiceLayer.saveSpins(spinsList);
                EventBus.getDefault().post(new Events.LoadingData(90));
                if (countChildren.getOffers() > 0) {
                    if (!initOffers) {
                        offerListener();
                    }
                } else {
                    EventBus.getDefault().post(new Events.FinishLoadData());
                }
            }
        }
    }

    private static void offerListener() {
        offersList = new ArrayList<CouponExtension>();
        Constants.DB_OFFER.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateOffer(dataSnapshot.getValue(CouponExtension.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateOffer(dataSnapshot.getValue(CouponExtension.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void updateOffer(final CouponExtension coupon) {
        coupon.setCouponType(Constants.COUPON_TYPE_OFFER);
        coupon.setCode(coupon.getGiftKey());
        Constants.DB_PLACE.orderByChild("offer").equalTo(coupon.getGiftKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                        Place place = dataPlace.getValue(Place.class);
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
                                    saveOffer(coupon);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                cancel();
                            }
                        });
                    }
                } else {
                    saveOffer(coupon);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cancel();
            }
        });
    }

    private static void saveOffer(CouponExtension coupon) {
        if (initOffers) {
            CouponServiceLayer.insertCoupon(coupon);
        } else {
            offersList.add(coupon);
            countOffers++;
            if (countOffers == countChildren.getOffers()) {
                initOffers = true;
                CouponServiceLayer.saveCoupons(offersList, true);
                EventBus.getDefault().post(new Events.FinishLoadData());
            }
        }
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
                firstConnection = true;
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
}
