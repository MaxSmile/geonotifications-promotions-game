package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    private Map<String, Map<String, Promotion>> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final RecyclerView companiesList = (RecyclerView) findViewById(R.id.homeCompanyList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        companiesList.setLayoutManager(llm);

        dbCompanies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Map<String, Promotion>>> type = new GenericTypeIndicator<Map<String, Map<String, Promotion>>>() {};
                companies = dataSnapshot.getValue(type);
                if (companies == null) {
                    companies = new HashMap<String, Map<String, Promotion>>();
                }

                companiesList.setAdapter(new CompanyListAdapter(HomeActivity.this, companies));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
