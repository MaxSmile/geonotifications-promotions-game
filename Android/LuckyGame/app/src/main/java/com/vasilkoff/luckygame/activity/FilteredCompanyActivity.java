package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.databinding.ActivityFilteredCompanyBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FilteredCompanyActivity extends BaseActivity {
    private int type;
    private RecyclerView companiesList;
    private ActivityFilteredCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_company);
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSpins();
    }

    @Override
    public void resultSpins(ArrayList<Spin> spins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        super.resultSpins(spins, places, companies);
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

        binding.setCountResult(spins.size() > 0);
        companiesList.setAdapter(new CompanyListAdapter(this, spins, places, companies));
    }
}
