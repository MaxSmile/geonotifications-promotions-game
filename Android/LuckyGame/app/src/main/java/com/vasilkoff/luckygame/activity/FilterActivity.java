package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.vasilkoff.luckygame.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterActivity extends BaseActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        listView = (ListView)findViewById(R.id.filterList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_multiple_choice, cities);
        listView.setAdapter(arrayAdapter);

        if (checkedCitiesArray!= null) {
            for (int i = 0; i < checkedCitiesArray.size(); i++) {
                int key = checkedCitiesArray.keyAt(i);
                if (checkedCitiesArray.get(key)) {
                    listView.setItemChecked(key, true);
                }
            }
        }

        ((Button)findViewById(R.id.filterApply)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredCities = new HashMap<String, String>();
                checkedCitiesArray = listView.getCheckedItemPositions();
                for (int i = 0; i < checkedCitiesArray.size(); i++) {
                    int key = checkedCitiesArray.keyAt(i);
                    if (checkedCitiesArray.get(key)) {
                        filteredCities.put(cities.get(key), cities.get(key));
                    }
                }

                for (int i = 0; i < filteredCities.size(); i++) {
                    System.out.println(TAG + " city = " + filteredCities.get(i));
                }
            }
        });
    }
}
