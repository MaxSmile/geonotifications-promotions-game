package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.binding.handler.FilteredHandler;
import com.vasilkoff.luckygame.common.Filters;
import com.vasilkoff.luckygame.common.Properties;
import com.vasilkoff.luckygame.database.DBHelper;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilteredCompanyActivity extends BaseActivity implements FilteredHandler {
    private int type;
    private RecyclerView companiesList;
    private ActivityFilteredCompanyBinding binding;
    private RelativeLayout preloader;


    private ArrayList<Spin> newSpins;
    private HashMap<String, Place> places;
    private HashMap<String, Company> companies;
    private boolean fromFilter;
    private EditText searchEditText;
    private boolean showSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_company);
        checkNetwork();
        type = getIntent().getIntExtra(Constants.PLACE_TYPE_KEY, Constants.CATEGORY_ALL);
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
        initSearch();
    }

    private void initSearch() {
        searchEditText = (EditText)findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Filters.searchKeyWord = s.toString();
                filter();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!fromFilter) {
            preloader.setVisibility(View.VISIBLE);
            getSpins();
        }

        fromFilter = false;
        binding.setFilterSearch(Filters.search);
        binding.setFilterNearMe(Filters.nearMe);
        binding.setFiltersCount(Filters.count);
        binding.setShowSearch(showSearch);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Filters.FILTER_CODE) {
            fromFilter = true;
            if (resultCode == RESULT_OK)
                filter();
        }
    }

    @Override
    public void resultSpins(TreeMap<String, Spin> mapSpins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        result = true;
        newSpins = new ArrayList<Spin>(mapSpins.values());
        preloader.setVisibility(View.GONE);
        if (newSpins.size() > 0 && newSpins.size() == places.size()) {
            HashMap<String, String> list = DBHelper.getInstance(this).getFavorites();
            if (type >= 0) {
                Iterator<Spin> i = newSpins.iterator();
                while (i.hasNext()) {
                    Spin spin = i.next();
                    if (type == Constants.CATEGORY_FAVORITES && list.size() > 0) {
                        if (list.get(spin.getPlaceKey()) == null) {
                            i.remove();
                        }
                    } else {
                        if (places.get(spin.getPlaceKey()).getType() != type) {
                            i.remove();
                        }
                    }
                }
            }

            this.places = places;
            this.companies = companies;
            updateData();
        }

    }

    private void updateData() {
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

        filter();
    }

    private void filter() {
        ArrayList<Spin> spins = new ArrayList<Spin>(newSpins);

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

        if (Filters.search && Filters.searchKeyWord != null && Filters.searchKeyWord.length() > 0) {
            Iterator<Spin> iSearch = spins.iterator();
            while (iSearch.hasNext()) {
                Spin spin = iSearch.next();
                if (!places.get(spin.getPlaceKey()).getName().toLowerCase().contains(Filters.searchKeyWord)) {
                    iSearch.remove();
                }
            }
        }

        binding.setCountResult(spins.size() > 0);
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

    @Override
    public void filterNearMe(View view) {
        if (CurrentLocation.lat != 0 ) {
            if (checkResult()) {
                Filters.nearMe = !Filters.nearMe;
                binding.setFilterNearMe(Filters.nearMe);
                filter();
            }
        } else {
            Toast.makeText(this, R.string.unknown_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void filters(View view) {
        if (checkResult())
            startActivityForResult(new Intent(this, FilterActivity.class), Filters.FILTER_CODE);
    }

    @Override
    public void search(View view) {
        if (checkResult()) {
            Filters.search = true;
            showSearch = true;
            binding.setShowSearch(showSearch);
            binding.setFilterSearch(Filters.search);
            searchEditText.setText(Filters.searchKeyWord);
        }
    }

    @Override
    public void hideSearch(View view) {
        showSearch = false;
        binding.setShowSearch(showSearch);
        if (searchEditText.getText().length() == 0) {
            Filters.search = false;
            binding.setFilterSearch(Filters.search);
        }
    }

    @Override
    public void offSearch(View view) {
        Filters.search = false;
        binding.setFilterSearch(Filters.search);
        showSearch = false;
        binding.setShowSearch(showSearch);
        Filters.searchKeyWord = null;
        searchEditText.setText(null);
    }
}
