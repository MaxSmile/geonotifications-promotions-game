package com.vasilkoff.luckygame.fragment;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CouponListAdapter;
import com.vasilkoff.luckygame.common.Filters;
import com.vasilkoff.luckygame.common.Properties;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class CouponsFragment extends Fragment {

    private RecyclerView couponsList;
    private List<CouponExtension> coupons;
    private TreeMap<String, CouponExtension> newCoupons;
    private List<String> couponsCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coupons_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        couponsList = (RecyclerView) getActivity().findViewById(R.id.couponsList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        couponsList.setLayoutManager(llm);
        getCoupons();
    }

    public void refreshList() {
        if (newCoupons != null)
            updateData();
    }

    public void updateData() {
        //if (isAdded() && (newCoupons.size() == couponsCode.size())) {
        if (isAdded()) {
            coupons = new ArrayList<CouponExtension>(newCoupons.values());

            TypedArray ta = getResources().obtainTypedArray(R.array.coupon_type);
            for (int i = 0; i < coupons.size(); i++) {
                CouponExtension coupon = coupons.get(i);

                if (CurrentLocation.lat != 0) {
                    if (coupon.getGeoLat() != 0 && coupon.getGeoLon() != 0) {
                        coupon.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                                coupon.getGeoLat(), coupon.getGeoLon()));
                        coupon.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                                coupon.getGeoLat(), coupon.getGeoLon()));
                    }
                }

                coupon.setTypeString(Constants.COMPANY_TYPE_NAMES[(int)coupon.getType()]);

                if (coupon.getStatus() != Constants.COUPON_STATUS_REDEEMED) {
                    if (coupon.getExpired() < System.currentTimeMillis()) {
                        coupon.setStatus(Constants.COUPON_STATUS_EXPIRED);
                    } else if (coupon.getStatus() == Constants.COUPON_STATUS_ACTIVE && coupon.getLocks() < System.currentTimeMillis()
                            && coupon.getLocked() == Constants.COUPON_LOCK) {
                        coupon.setStatus(Constants.COUPON_STATUS_LOCK);
                        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_LOCK);
                    }
                }

                String locks = DateFormat.getDiff(coupon.getLocks());
                if (locks != null)
                    coupon.setLockDiff(locks);

                String expire = DateFormat.getDiff(coupon.getExpired());
                if (expire != null)
                    coupon.setExpiredDiff(expire);

                if (coupon.getStatus() >= Constants.COUPON_STATUS_LOCK) {
                    coupon.setStatusIcon(ta.getResourceId(coupon.getStatus(), 0));
                }
                coupon.setRedeemedString(DateFormat.getDate("dd/MM/yyyy", coupon.getRedeemed()));
            }
            ta.recycle();

            if (Filters.nearMe) {
                Iterator<CouponExtension> j = coupons.iterator();
                while (j.hasNext()) {
                    CouponExtension coupon = j.next();
                    if (coupon.getDistance() > Properties.getNearMeRadius()) {
                        j.remove();
                    }
                }
            }

            if (Filters.byCity) {
                Iterator<CouponExtension> iCity = coupons.iterator();
                while (iCity.hasNext()) {
                    CouponExtension coupon = iCity.next();
                    if (Filters.filteredCities.get(coupon.getCity()) == null) {
                        iCity.remove();
                    }
                }
            }

            if (Filters.byZA) {
                if (coupons.size() > 0) {
                    List<CouponExtension> sortedCoupons = new ArrayList<CouponExtension>();
                    for (int k = coupons.size() - 1; k >= 0; k--) {
                        sortedCoupons.add(coupons.get(k));
                    }
                    coupons = new ArrayList<CouponExtension>(sortedCoupons);
                }
            }

            if (Filters.search && Filters.searchKeyWord != null && Filters.searchKeyWord.length() > 0) {
                Iterator<CouponExtension> iSearch = coupons.iterator();
                while (iSearch.hasNext()) {
                    CouponExtension coupon = iSearch.next();
                    if (!coupon.getPlaceName().toLowerCase().contains(Filters.searchKeyWord)) {
                        iSearch.remove();
                    }
                }
            }

            couponsList.setAdapter(new CouponListAdapter(getContext(), coupons));

        }

    }

    private void getCoupons() {
        couponsCode = DBHelper.getInstance(getContext()).getCoupons();
        newCoupons = new TreeMap<String, CouponExtension>();
        for (int i = 0; i < couponsCode.size(); i++) {
            Constants.DB_COUPON.child(couponsCode.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final CouponExtension coupon = dataSnapshot.getValue(CouponExtension.class);
                    if (coupon != null) {
                        Constants.DB_PLACE.child(coupon.getPlaceKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Place place = dataSnapshot.getValue(Place.class);
                                coupon.setPlaceName(place.getName());
                                coupon.setType(place.getType());
                                coupon.setGeoLat(place.getGeoLat());
                                coupon.setGeoLon(place.getGeoLon());
                                coupon.setCity(place.getCity());
                                Constants.DB_COMPANY.child(coupon.getCompanyKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Company company = dataSnapshot.getValue(Company.class);
                                        coupon.setCompanyName(company.getName());
                                        coupon.setLogo(company.getLogo());
                                        newCoupons.put(coupon.getPlaceName(), coupon);
                                        updateData();
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
}
