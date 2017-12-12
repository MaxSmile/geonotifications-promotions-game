package com.spindealsapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.spindealsapp.adapter.CompanyListAdapter;
import com.spindealsapp.common.Filters;
import com.spindealsapp.common.PlaceDistanceComparator;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.database.SpinServiceLayer;
import com.spindealsapp.database.service.CompanyServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class ActiveCompaniesFragment extends Fragment {

    private RecyclerView companiesList;
    private DataBridge dataBridge;
    private RelativeLayout networkUnavailable;

    private ArrayList<Place> newPlaces = new ArrayList<Place>();
    private CompanyListAdapter adapter;

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
        networkUnavailable = (RelativeLayout) getActivity().findViewById(R.id.networkUnavailable);
        adapter = new CompanyListAdapter(getContext(), newPlaces, CompanyServiceLayer.getCompanies());
        companiesList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePlaces(Events.UpdatePlaces updatePlaces) {
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateFilter(Events.UpdateFilter updateFilter) {
        if (Filters.nearMe) {
            refreshData();
        } else {
            filterData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void refreshData() {
        newPlaces = new ArrayList<>(PlaceServiceLayer.getPlaces().values());

        removeInactive();
        filterData();
    }

    private void removeInactive() {
        Iterator<Place> iterator = newPlaces.iterator();
        while (iterator.hasNext()) {
            Place place = iterator.next();
            if (!place.getSpin().isAvailable() || place.getSpin().getBox().size() == 0) {
                iterator.remove();
            }
        }
    }

    private void filterData() {
        ArrayList<Place> places = new ArrayList<Place>(newPlaces);

        if (Filters.nearMe) {
            Iterator<Place> iNearMe = places.iterator();
            while (iNearMe.hasNext()) {
                if (iNearMe.next().getDistance() > Properties.getNearMeRadius()) {
                    iNearMe.remove();
                }
            }
        }

        if (Filters.byCity) {
            Iterator<Place> iCity = places.iterator();
            while (iCity.hasNext()) {
                if (Filters.filteredCities.get(iCity.next().getCity()) == null) {
                    iCity.remove();
                }
            }
        }

        if (Filters.byKeywords) {
            Iterator<Place> iKeywords = places.iterator();
            while (iKeywords.hasNext()) {
                Place place = iKeywords.next();
                boolean exist = false;
                List<String> keywords = Arrays.asList(place.getKeywords().split(","));
                for (int i = 0; i < keywords.size(); i++) {
                    if (Filters.filteredKeywords.get(keywords.get(i).toLowerCase()) != null) {
                        exist = true;
                    }
                }
                if (!exist) {
                    iKeywords.remove();
                }
            }
        }

        if (Filters.byZA) {
            if (places.size() > 0) {
                List<Place> sortedPlaces = new ArrayList<Place>();
                for (int k = places.size() - 1; k >= 0; k--) {
                    sortedPlaces.add(places.get(k));
                }
                places = new ArrayList<Place>(sortedPlaces);
            }
        }

        places = groupByCompany(places);

        if (Filters.nearMe) {
            Collections.sort(places, new PlaceDistanceComparator());
        }

        dataBridge.activeSpins(places.size());
        adapter.updateData(places, CompanyServiceLayer.getCompanies());
    }

    private ArrayList<Place> groupByCompany(ArrayList<Place> places) {
        HashMap<String, Place> orderPlaces = new HashMap<String, Place>();
        for (Place place : places) {
            if (orderPlaces.get(place.getCompanyKey()) == null ||
                    orderPlaces.get(place.getCompanyKey()).getDistance() > place.getDistance()) {
                orderPlaces.put(place.getCompanyKey(), place);
            }
        }
        return new ArrayList<Place>(orderPlaces.values());
    }
}
