package com.spindealsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.spindealsapp.common.Filters;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends BaseActivity {

    private ListView filterListByCity;
    private ListView filterList;
    private ListView filterListByKeywords;
    private List<String> citiesList;
    private List<String> keywordsList;
    private LinearLayout filterLayoutByKeywords;

    private ArrayAdapter<String> arrayFilterAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayKeywordsAdapter;

    private static int FILTER_INDEX_ZA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterLayoutByKeywords = (LinearLayout)findViewById(R.id.filterLayoutByKeywords);
        filterListByKeywords = (ListView)findViewById(R.id.filterListByKeywords);
        filterListByKeywords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        keywordsList = DBHelper.getInstance(this).getKeywords();
        if (keywordsList.size() > 0) {
            filterLayoutByKeywords.setVisibility(View.VISIBLE);
        } else {
            filterListByKeywords.clearChoices();
            Filters.byKeywords = false;
            Filters.count--;
        }
        arrayKeywordsAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_multiple_choice, keywordsList);

        filterListByKeywords.setAdapter(arrayKeywordsAdapter);

        filterList = (ListView)findViewById(R.id.filterList);
        filterList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        List<String> filtersList = new ArrayList<String>();
        filtersList.add(getString(R.string.filter_za));
        arrayFilterAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_multiple_choice, filtersList);
        filterList.setAdapter(arrayFilterAdapter);

        filterListByCity = (ListView)findViewById(R.id.filterListByCity);
        filterListByCity.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        citiesList = DBHelper.getInstance(this).getCities();
        arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_multiple_choice, citiesList);

        filterListByCity.setAdapter(arrayAdapter);

        if (Filters.checkedCitiesArray != null) {
            for (int i = 0; i < Filters.checkedCitiesArray.size(); i++) {
                int key = Filters.checkedCitiesArray.keyAt(i);
                if (Filters.checkedCitiesArray.get(key)) {
                    filterListByCity.setItemChecked(key, true);
                }
            }
        }

        if (Filters.checkedKeywordsArray != null) {
            for (int i = 0; i < Filters.checkedKeywordsArray.size(); i++) {
                int key = Filters.checkedKeywordsArray.keyAt(i);
                if (Filters.checkedKeywordsArray.get(key)) {
                    filterListByKeywords.setItemChecked(key, true);
                }
            }
        }

        if (Filters.checkedFiltersArray != null) {
            for (int i = 0; i < Filters.checkedFiltersArray.size(); i++) {
                int key = Filters.checkedFiltersArray.keyAt(i);
                if (Filters.checkedFiltersArray.get(key)) {
                    filterList.setItemChecked(key, true);
                }
            }
        }

        ((Button)findViewById(R.id.filterApply)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply();
            }
        });
        ((Button)findViewById(R.id.filterClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((Button)findViewById(R.id.filterClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterListByCity.clearChoices();
                filterList.clearChoices();
                filterListByKeywords.clearChoices();

                arrayFilterAdapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetChanged();
                arrayKeywordsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void apply() {
        updateData();

        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void updateData() {
        Filters.filteredCities = new HashMap<String, String>();
        Filters.filteredKeywords = new HashMap<String, String>();
        Filters.count = 0;
        Filters.byZA = false;
        Filters.checkedCitiesArray = filterListByCity.getCheckedItemPositions();
        Filters.checkedFiltersArray = filterList.getCheckedItemPositions();
        Filters.checkedKeywordsArray = filterListByKeywords.getCheckedItemPositions();

        for (int i = 0; i < Filters.checkedCitiesArray.size(); i++) {
            int key = Filters.checkedCitiesArray.keyAt(i);
            if (Filters.checkedCitiesArray.get(key)) {
                Filters.filteredCities.put(citiesList.get(key), citiesList.get(key));
            }
        }

        for (int i = 0; i < Filters.checkedKeywordsArray.size(); i++) {
            int key = Filters.checkedKeywordsArray.keyAt(i);
            if (Filters.checkedKeywordsArray.get(key)) {
                Filters.filteredKeywords.put(keywordsList.get(key).toLowerCase(), keywordsList.get(key));
            }
        }

        for (int i = 0; i < Filters.checkedFiltersArray.size(); i++) {
            int key = Filters.checkedFiltersArray.keyAt(i);
            if (Filters.checkedFiltersArray.get(key)) {
                if (key == FILTER_INDEX_ZA) {
                    Filters.count++;
                    Filters.byZA = true;
                }

            }
        }


        if (Filters.filteredCities.size() > 0) {
            Filters.count++;
            Filters.byCity = true;
        } else {
            Filters.byCity = false;
        }

        if (Filters.filteredKeywords.size() > 0) {
            Filters.count++;
            Filters.byKeywords = true;
        } else {
            Filters.byKeywords = false;
        }
    }

}
