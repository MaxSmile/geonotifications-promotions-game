package com.vasilkoff.luckygame.fragment;


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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CouponListAdapter;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Coupon;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class CouponsFragment extends Fragment {

    private RecyclerView couponsList;
    private DBHelper dbHelper;
    private DatabaseReference dbCoupons = Constants.dbCoupons;
    private DatabaseReference dbCompanies = Constants.dbCompanies;
    private List<CouponExtension> coupons;
    private CouponExtension coupon;
    private List<String> couponsCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coupons_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper = new DBHelper(getContext());
        couponsList = (RecyclerView) getActivity().findViewById(R.id.couponsList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        couponsList.setLayoutManager(llm);
        refreshList();
    }

    public void refreshList() {
        getCoupons();
    }

    private void getCoupons() {
        couponsCode = dbHelper.getCoupons();
        coupons = new ArrayList<CouponExtension>();
        for (int i = 0; i < couponsCode.size(); i++) {
            dbCoupons.child(couponsCode.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    coupon = dataSnapshot.getValue(CouponExtension.class);
                    if (coupon != null) {
                        dbCompanies.child(coupon.getCompanyKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Promotion promotion = dataSnapshot.child("promo").child(coupon.getPromoKey()).getValue(Promotion.class);
                                coupon.setCompanyName(dataSnapshot.child("name").getValue().toString());
                                coupon.setLogo(dataSnapshot.child("logo").getValue().toString());
                                coupon.setType((long)dataSnapshot.child("type").getValue());
                                coupon.setPromoName(promotion.getName());

                                coupons.add(coupon);

                                if (couponsCode.size() == coupons.size()) {

                                    couponsList.setAdapter(new CouponListAdapter(getContext(), coupons));
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
}
