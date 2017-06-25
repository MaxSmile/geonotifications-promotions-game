package com.vasilkoff.luckygame.fragment;


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

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.common.Filters;
import com.vasilkoff.luckygame.common.Properties;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.util.LocationDistance;
import com.vasilkoff.luckygame.util.NetworkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class ActiveCompaniesFragment extends Fragment {

    private RecyclerView companiesList;
    private DataBridge dataBridge;
    private RelativeLayout preloader;
    private RelativeLayout networkUnavailable;

    private ArrayList<Spin> spins;
    private HashMap<String, Place> places;
    private HashMap<String, Company> companies;

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
        preloader = (RelativeLayout) getActivity().findViewById(R.id.preloader);
        preloader.setVisibility(View.VISIBLE);
        networkUnavailable = (RelativeLayout) getActivity().findViewById(R.id.networkUnavailable);
        if (!NetworkState.isOnline())
            networkUnavailable.setVisibility(View.VISIBLE);
    }

    public void refreshList(ArrayList<Spin> spins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        if (isAdded() && preloader != null) {
            this.spins = spins;
            this.places = places;
            this.companies = companies;
            preloader.setVisibility(View.GONE);
            networkUnavailable.setVisibility(View.GONE);
            updateData();
        }
    }

    public void updateData() {
        if (CurrentLocation.lat != 0) {
            for (HashMap.Entry <String, Place> spinPlace : places.entrySet()) {
                Place place = spinPlace.getValue();
                if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                    place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                    place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                }
            }
        }

        Iterator<Spin> i = spins.iterator();
        while (i.hasNext()) {
            Spin spin = i.next();
            if (spin.getStatus() != Constants.SPIN_STATUS_ACTIVE) {
                i.remove();
            }
        }

        if (Filters.nearMe) {
            Iterator<Spin> j = spins.iterator();
            while (j.hasNext()) {
                Spin spin = j.next();
                if (places.get(spin.getPlaceKey()).getDistance() > Properties.getNearMeRadius()) {
                    j.remove();
                }
            }
        }

        if (Filters.byCity) {
            Iterator<Spin> iCity = spins.iterator();
            while (iCity.hasNext()) {
                Spin spin = iCity.next();
                if (Filters.filteredCities.get(places.get(spin.getPlaceKey()).getCity()) == null) {
                    iCity.remove();
                }
            }
        }

        if (Filters.byZA) {
            if (spins.size() > 0) {
                List<Spin> sortedSpins = new ArrayList<Spin>();
                for (int k = spins.size() - 1; k >= 0; k--) {
                    sortedSpins.add(spins.get(k));
                }
                spins = new ArrayList<Spin>(sortedSpins);
            }
        }

        dataBridge.activeSpins(spins.size());
        companiesList.setAdapter(new CompanyListAdapter(getContext(), spins, places, companies));

    }
}
