package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.databinding.ActivityFilteredCompanyBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.eventbus.Events;
import com.vasilkoff.luckygame.util.LocationDistance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FilteredCompanyActivity extends BaseActivity {
    private int type;
    private RecyclerView companiesList;
    private ActivityFilteredCompanyBinding binding;
    private RelativeLayout preloader;

    private ArrayList<Spin> spins;
    private HashMap<String, Place> places;
    private HashMap<String, Company> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_company);
        checkNetwork();
        type = getIntent().getIntExtra(Constants.PLACE_TYPE_KEY, -1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filtered_company);
        binding.setHandler(this);
        binding.setCountResult(true);

        String typeString;
        if (type >= 0) {
            typeString = Constants.COMPANY_TYPE_NAMES[type];
        } else {
            typeString = getString(R.string.all);
        }
        binding.setType(typeString);

        companiesList = (RecyclerView) findViewById(R.id.filteredCompanyList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        companiesList.setLayoutManager(llm);

        preloader = (RelativeLayout) findViewById(R.id.preloader);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preloader.setVisibility(View.VISIBLE);
        getSpins();
    }

    @Override
    public void resultSpins(ArrayList<Spin> spins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        super.resultSpins(spins, places, companies);
        preloader.setVisibility(View.GONE);
        if (type >= 0) {
            Iterator<Spin> i = spins.iterator();
            while (i.hasNext()) {
                Spin spin = i.next();
                if (places.get(spin.getPlaceKey()).getType() != type) {
                    places.remove(spin.getPlaceKey());
                    i.remove();
                }
            }
        }

        this.spins = spins;
        this.places = places;
        this.companies = companies;
        updateData();
    }

    private void updateData() {
        binding.setCountResult(spins.size() > 0);
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
        companiesList.setAdapter(new CompanyListAdapter(this, spins, places, companies));
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCurrentLocation(Events.UpdateLocation updateLocation) {
        if (!CurrentLocation.check) {
            CurrentLocation.check = true;
            updateData();
        }
    }
}
