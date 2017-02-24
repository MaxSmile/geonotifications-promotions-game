package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Promotion;
import com.vasilkoff.luckygame.service.LocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends BaseActivity {

    //private Set<String> uniquePlacesNames;
    private RecyclerView companiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vasilkoff.luckygame",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        companiesList = (RecyclerView) findViewById(R.id.homeCompanyList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        companiesList.setLayoutManager(llm);

        dbData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    updateData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*dbCompanies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                companies = new HashMap<String, Map<String, Promotion>>();
                uniquePlacesNames = new HashSet<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                        Map<String, Promotion> promotions = new HashMap<String, Promotion>();
                        for (DataSnapshot promotion : company.getChildren()) {
                            if (promotion.child("active").getValue().equals(true)) {
                                Promotion promotionValue = promotion.getValue(Promotion.class);
                                promotions.put(promotion.getKey(), promotionValue);

                                for (int i = 0; i < promotionValue.getListPlaces().size(); i++) {
                                    uniquePlacesNames.add(promotionValue.getListPlaces().get(i));
                                }

                            }
                        }
                        if (promotions.size() > 0) {
                            companies.put(company.getKey(), promotions);
                        }
                    }
                }
                companiesList.setAdapter(new CompanyListAdapter(HomeActivity.this, companies));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */

        ((Button) findViewById(R.id.homeToCoupons)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ListCouponsActivity.class));
            }
        });
    }

    private void updateData(DataSnapshot dataSnapshot) {
        companies = new HashMap<String, Map<String, Promotion>>();
        Set<String> uniquePlacesNames = new HashSet<>();

        for (DataSnapshot company : dataSnapshot.child("companies").getChildren()) {
            Map<String, Promotion> promotions = new HashMap<String, Promotion>();
            for (DataSnapshot promotion : company.getChildren()) {
                if (promotion.child("active").getValue().equals(true)) {
                    Promotion promotionValue = promotion.getValue(Promotion.class);
                    promotions.put(promotion.getKey(), promotionValue);

                    for (int i = 0; i < promotionValue.getListPlaces().size(); i++) {
                        uniquePlacesNames.add(promotionValue.getListPlaces().get(i));
                    }
                }
            }
            if (promotions.size() > 0) {
                companies.put(company.getKey(), promotions);
            }
        }

        companiesList.setAdapter(new CompanyListAdapter(HomeActivity.this, companies));

        GenericTypeIndicator<Map<String, Place>> type = new GenericTypeIndicator<Map<String, Place>>() {};
        Map<String, Place> places = dataSnapshot.child("places").getValue(type);
        uniquePlaces = new ArrayList<Place>();

        for (String placeName : uniquePlacesNames) {
            uniquePlaces.add(places.get(placeName));
        }

        Intent intent = new Intent(this, LocationService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("uniquePlaces", uniquePlaces);
        intent.putExtras(bundle);
        startService(intent);

    }
}
