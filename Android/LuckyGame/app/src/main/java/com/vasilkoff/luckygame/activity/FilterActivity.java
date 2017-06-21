package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.common.Filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends BaseActivity {

    private ListView filterListByCity;
    private ListView filterList;
    private List<String> citiesList;

    private ArrayAdapter<String> arrayFilterAdapter;
    private ArrayAdapter<String> arrayAdapter;

    private static int FILTER_INDEX_ZA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterList = (ListView)findViewById(R.id.filterList);
        filterList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        List<String> filtersList = new ArrayList<String>();
        filtersList.add(getString(R.string.filter_za));
        arrayFilterAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_multiple_choice, filtersList);
        filterList.setAdapter(arrayFilterAdapter);

        filterListByCity = (ListView)findViewById(R.id.filterListByCity);
        filterListByCity.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        citiesList = new ArrayList<String>(cities.values());
        arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_multiple_choice, citiesList);

        filterListByCity.setAdapter(arrayAdapter);

        if (Filters.checkedCitiesArray!= null) {
            for (int i = 0; i < Filters.checkedCitiesArray.size(); i++) {
                int key = Filters.checkedCitiesArray.keyAt(i);
                if (Filters.checkedCitiesArray.get(key)) {
                    filterListByCity.setItemChecked(key, true);
                }
            }
        }

        if (Filters.checkedFiltersArray!= null) {
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
                arrayFilterAdapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void apply() {
        Filters.filteredCities = new HashMap<String, String>();
        Filters.count = 0;
        Filters.byZA = false;
        Filters.checkedCitiesArray = filterListByCity.getCheckedItemPositions();
        Filters.checkedFiltersArray = filterList.getCheckedItemPositions();

        for (int i = 0; i < Filters.checkedCitiesArray.size(); i++) {
            int key = Filters.checkedCitiesArray.keyAt(i);
            if (Filters.checkedCitiesArray.get(key)) {
                Filters.filteredCities.put(citiesList.get(key), citiesList.get(key));
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

        setResult(RESULT_OK, new Intent());
        finish();
    }
}
