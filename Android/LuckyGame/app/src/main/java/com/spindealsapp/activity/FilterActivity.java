package com.spindealsapp.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.spindealsapp.Constants;
import com.spindealsapp.common.Filters;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.R;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.database.service.KeywordServiceLayer;
import com.spindealsapp.util.Locales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
    private int categoryType;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        categoryType = getIntent().getIntExtra(Constants.PLACE_TYPE_KEY, Constants.CATEGORY_ALL);
        position = getIntent().getIntExtra("position", -1);
        filterLayoutByKeywords = (LinearLayout)findViewById(R.id.filterLayoutByKeywords);
        filterListByKeywords = (ListView)findViewById(R.id.filterListByKeywords);
        filterListByKeywords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        keywordsList = KeywordServiceLayer.getKeywords();
        if (keywordsList.size() > 0 && categoryType >= 0) {
            updateKeywords();
        }
        if (keywordsList.size() > 0) {
            filterLayoutByKeywords.setVisibility(View.VISIBLE);
        } else {
            filterListByKeywords.clearChoices();
            Filters.byKeywords = false;
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

        citiesList = PlaceServiceLayer.getCities();
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

        if (Filters.filteredKeywords != null && Filters.filteredKeywords.size() > 0) {
            for (int i = 0; i < keywordsList.size(); i++) {
                if (Filters.filteredKeywords.get(keywordsList.get(i).toLowerCase()) != null) {
                    filterListByKeywords.setItemChecked(i, true);
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

    private void updateKeywords() {
        Resources localizedResources = Locales.getLocalizedResources(this, Locale.US);
        String[] typesName = localizedResources.getStringArray(R.array.company_type);
        String typeName = typesName[categoryType].toLowerCase();
        Iterator<String> iKeywords = keywordsList.iterator();
        while (iKeywords.hasNext()) {
            boolean exist = false;
            String keyword = iKeywords.next();
            List<String> keywords = Arrays.asList(keyword.split("-"));
            if (keywords.size() > 1) {
                String type = keywords.get(0).toLowerCase().trim();
                if (type.equals(typeName)) {
                    exist = true;
                }
            }
            if (!exist) {
                iKeywords.remove();
            }
        }
    }

    private void apply() {
        updateData();
        if (position == 0) {
            startActivity(new Intent(this, FilteredCompanyActivity.class));
        } else {
            setResult(RESULT_OK, new Intent());
        }
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
                    Filters.byZA = true;
                }

            }
        }

        if (Filters.filteredCities.size() > 0) {
            Filters.count = Filters.filteredCities.size();
            Filters.byCity = true;
        } else {
            Filters.byCity = false;
        }

        if (Filters.filteredKeywords.size() > 0) {
            Filters.count += Filters.filteredKeywords.size();
            Filters.byKeywords = true;
        } else {
            Filters.byKeywords = false;
        }
    }

}
