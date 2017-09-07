package com.spindealsapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.spindealsapp.adapter.CompanyListAdapter;
import com.spindealsapp.binding.handler.SearchHandler;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivitySearchBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends BaseActivity implements SearchHandler {

    private EditText searchEditText;
    private RecyclerView companiesList;
    private ArrayList<Place> newPlaces;

    private ActivitySearchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setTitle(getString(R.string.search));
        binding.setHandler(this);
        binding.setCountResult(true);
        binding.setShowBg(true);
        binding.setBack(getResources().getIdentifier("back", "drawable", getPackageName()));
        binding.setColorTitle(ContextCompat.getColor(this, R.color.categoryTitle));
        initSearch();

        companiesList = (RecyclerView) findViewById(R.id.filteredCompanyList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        companiesList.setLayoutManager(llm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
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
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePlaces(Events.UpdatePlaces updatePlaces) {
        refreshData();
    }

    private void refreshData() {
        newPlaces = PlaceServiceLayer.getPlaces();
    }

    @Override
    public void clear(View view) {
        searchEditText.setText("");
    }

    private void search(String keyword) {
        keyword = keyword.trim().toLowerCase();
        HashMap<String, Place> spinsSearch = new HashMap<String, Place>();
        //String[] arraySearch = keyword.split("\\s++", -1);
        if (keyword.length() > 0) {
            for (int j = 0; j < newPlaces.size(); j++) {
                Place place = newPlaces.get(j);

                if (place.getName().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getCity().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getTypeName().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getAddress().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getTel().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getAbout().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getAboutMore().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }

                if (place.getKeywords().toLowerCase().contains(keyword)) {
                    spinsSearch.put(place.getId(), place);
                }
            }
        }

        ArrayList<Place> spinsResult = new ArrayList<Place>(spinsSearch.values());
        if (keyword.length() > 0) {
            binding.setCountResult(spinsResult.size() > 0);
            binding.setShowBg(false);
        } else {
            binding.setCountResult(true);
            binding.setShowBg(true);
        }

        companiesList.setAdapter(new CompanyListAdapter(this, spinsResult, DBHelper.getInstance(this).getCompanies()));
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
