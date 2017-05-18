package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityDetailsBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.HashMap;

public class DetailsActivity extends BaseActivity {

    private HashMap<String, Promotion> promotions;
    private String[] companyTypeNames;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        companyTypeNames = getResources().getStringArray(R.array.company_type);


        dbCompanies.child(getIntent().getStringExtra("company")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                promotions = new HashMap<String, Promotion>();
                for (DataSnapshot promotion : dataSnapshot.child("promo").getChildren()) {
                    Promotion promotionValue = promotion.getValue(Promotion.class);
                    promotions.put(promotion.getKey(), promotionValue);
                }

                int type = ((Long)dataSnapshot.child("type").getValue()).intValue();
                TypedArray iconArray = getResources().obtainTypedArray(R.array.company_type_icons);

                company = new Company(
                        dataSnapshot.getKey(),
                        dataSnapshot.child("name").getValue().toString(),
                        dataSnapshot.child("info").getValue().toString(),
                        dataSnapshot.child("logo").getValue().toString(),
                        companyTypeNames[type],
                        type,
                        iconArray.getResourceId(type, 0),
                        promotions.size());

                ActivityDetailsBinding binding = DataBindingUtil.setContentView(DetailsActivity.this, R.layout.activity_details);
                binding.setCompany(company);

                iconArray.recycle();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickDetails(View view) {
        switch (view.getId()) {
            case R.id.companyShowDetailsGifts:
                if (company.getCountPromo() > 0) {
                    Intent intent = new Intent(this, LegendActivity.class);
                    intent.putExtra(Promotion.class.getCanonicalName(), promotions);
                    intent.putExtra(Company.class.getCanonicalName(), company);
                    startActivity(intent);
                }
                break;
        }
    }


}
