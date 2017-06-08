package com.vasilkoff.luckygame.fragment;


import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.util.DateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class ActiveCompaniesFragment extends Fragment {

    private RecyclerView companiesList;
    private int count;
    private DataBridge dataBridge;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataBridge = (DataBridge) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.active_companies_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        companiesList = (RecyclerView) getActivity().findViewById(R.id.activeCompaniesList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        companiesList.setLayoutManager(llm);
        updateData();
    }



    private void updateData() {
        Constants.dbSpin.orderByChild("dateFinish").startAt(System.currentTimeMillis()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Spin> spins = new ArrayList<Spin>();
                final HashMap<String, Place> places = new HashMap<String, Place>();
                final HashMap<String, Company> companies = new HashMap<String, Company>();
                count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Spin spin = data.getValue(Spin.class);
                    if (spin.getDateStart() <= System.currentTimeMillis()) {
                        spin.setStatus(Constants.SPIN_STATUS_ACTIVE);
                        spin.setTimeLeft(DateFormat.getDiff(spin.getDateFinish()));
                        spins.add(spin);
                        count++;
                        Constants.dbPlace.child(spin.getPlaceKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Place place = dataSnapshot.getValue(Place.class);
                                place.setTypeName(Constants.companyTypeNames[place.getType()]);
                                places.put(place.getId(), place);
                                Constants.dbCompany.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Company company = dataSnapshot.getValue(Company.class);
                                        companies.put(dataSnapshot.getKey(), company);

                                        dataBridge.activeSpins(count);
                                        companiesList.setAdapter(new CompanyListAdapter(getContext(), spins, places, companies));
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
