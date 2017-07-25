package com.spindealsapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.adapter.CompanyListAdapter;
import com.spindealsapp.binding.handler.FilteredHandler;
import com.spindealsapp.common.Filters;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityFilteredCompanyBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FilteredCompanyActivity extends BaseActivity implements FilteredHandler {
    private int type;
    private RecyclerView companiesList;
    private ActivityFilteredCompanyBinding binding;

    private boolean fromFilter;
    private ArrayList<Place> newPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_company);
        type = getIntent().getIntExtra(Constants.PLACE_TYPE_KEY, Constants.CATEGORY_ALL);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filtered_company);
        binding.setHandler(this);
        binding.setBack(getResources().getIdentifier("back", "drawable", getPackageName()));
        binding.setColorTitle(ContextCompat.getColor(this, R.color.categoryTitle));
        binding.setCountResult(true);
        binding.setFavourites(type == Constants.CATEGORY_FAVORITES);


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
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (!fromFilter) {
            refreshData();
        }

        fromFilter = false;
        binding.setFilterNearMe(Filters.nearMe);
        binding.setFiltersCount(Filters.count);
    }

    private void refreshData() {
        newPlaces = PlaceServiceLayer.getPlaces();
        removeOtherCategory();
        filter();
    }

    private void removeOtherCategory() {
        if (type >= 0) {
            Iterator<Place> i = newPlaces.iterator();
            while (i.hasNext()) {
                if (type == Constants.CATEGORY_FAVORITES) {
                    if (!i.next().isFavorites()) {
                        i.remove();
                    }
                } else {
                    if (i.next().getType() != type) {
                        i.remove();
                    }
                }
            }
        }
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

    private void filter() {
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

        binding.setCountResult(places.size() > 0);
        companiesList.setAdapter(new CompanyListAdapter(this, places, DBHelper.getInstance(this).getCompanies()));
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

    @Override
    public void filterNearMe(View view) {
        if (CurrentLocation.lat != 0 ) {
            Filters.nearMe = !Filters.nearMe;
            binding.setFilterNearMe(Filters.nearMe);
            filter();
        } else {
            Toast.makeText(this, R.string.unknown_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void filters(View view) {
         startActivityForResult(new Intent(this, FilterActivity.class), Filters.FILTER_CODE);
    }

    @Override
    public void search(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

}