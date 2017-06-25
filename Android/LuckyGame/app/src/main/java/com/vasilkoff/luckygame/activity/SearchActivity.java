package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.binding.handler.SearchHandler;

import com.vasilkoff.luckygame.databinding.ActivitySearchBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SearchActivity extends BaseActivity implements SearchHandler {

    private EditText searchEditText;
    private RelativeLayout preloader;
    private RecyclerView companiesList;

    private ArrayList<Spin> newSpins;
    private HashMap<String, Place> places;
    private HashMap<String, Company> companies;

    private ActivitySearchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setTitle(getString(R.string.search));
        binding.setHandler(this);
        binding.setCountResult(true);
        initSearch();
        preloader = (RelativeLayout) findViewById(R.id.preloader);

        companiesList = (RecyclerView) findViewById(R.id.filteredCompanyList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        companiesList.setLayoutManager(llm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preloader.setVisibility(View.VISIBLE);
        getSpins();
    }

    @Override
    public void resultSpins(TreeMap<String, Spin> mapSpins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        result = true;
        newSpins = new ArrayList<Spin>(mapSpins.values());
        preloader.setVisibility(View.GONE);
        if (newSpins.size() > 0 && newSpins.size() == places.size()) {
            this.places = places;
            this.companies = companies;
            updateData();
        }

    }

    @Override
    public void clear(View view) {
        searchEditText.setText("");
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
    }

    private void search(String keyword) {
        keyword = keyword.trim().toLowerCase();
        ArrayList<Spin> spins = new ArrayList<Spin>(newSpins);
        HashMap<String, Spin> spinsSearch = new HashMap<String, Spin>();

        if (keyword.length() > 0) {
            String[] arraySearch = keyword.split("\\s++", -1);

            for (int i = 0; i < arraySearch.length; i++) {
                for (int j = 0; j < spins.size(); j++) {
                    Spin spin = spins.get(j);

                    if (places.get(spin.getPlaceKey()).getName().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getCity().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getTypeName().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getAddress().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getTel().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getAbout().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }

                    if (places.get(spin.getPlaceKey()).getAboutMore().toLowerCase().contains(arraySearch[i])) {
                        spinsSearch.put(spin.getPlaceKey(), spin);
                    }
                }
            }
        }

        ArrayList<Spin> spinsResult = new ArrayList<Spin>(spinsSearch.values());
        if (keyword.length() > 0) {
            binding.setCountResult(spinsResult.size() > 0);
        } else {
            binding.setCountResult(true);
        }

        companiesList.setAdapter(new CompanyListAdapter(this, spinsResult, places, companies));
    }

    private void initSearch() {
        searchEditText = (EditText)findViewById(R.id.searchEditText);
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_small, 0, 0, 0);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_small, 0, 0, 0);
                }
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
