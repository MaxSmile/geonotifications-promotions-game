package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityDetailsBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

public class DetailsActivity extends BaseActivity {

    private Company company;
    private Place place;
    private Spin spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());

        final ActivityDetailsBinding binding = DataBindingUtil.setContentView(DetailsActivity.this, R.layout.activity_details);

        Constants.dbPlace.child(getIntent().getStringExtra(Place.class.getCanonicalName())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                place = dataSnapshot.getValue(Place.class);
                place.setTypeName(Constants.companyTypeNames[place.getType()]);
                TypedArray iconArray = getResources().obtainTypedArray(R.array.company_type_icons);
                place.setTypeIcon(iconArray.getResourceId(place.getType(), 0));
                iconArray.recycle();
                Constants.dbCompany.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        company = dataSnapshot.getValue(Company.class);
                        binding.setCompany(company);
                        binding.setPlace(place);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  dbCompanies.child(getIntent().getStringExtra("company")).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });*/
    }

    public void onClickDetails(View view) {
        switch (view.getId()) {
            case R.id.detailsBack:
                onBackPressed();
                break;
            case R.id.detailsBtnPlay:
                if (spin != null) {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra(Place.class.getCanonicalName(), place);
                    intent.putExtra(Spin.class.getCanonicalName(), spin);
                    intent.putExtra(Company.class.getCanonicalName(), company);
                    startActivity(intent);
                }
                break;
            case R.id.companyShowDetailsGifts:
                Intent intent = new Intent(this, LegendActivity.class);
                intent.putExtra(Place.class.getCanonicalName(), place);
                intent.putExtra(Company.class.getCanonicalName(), company);
                startActivity(intent);
                break;
            case R.id.companyDetailsCall:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsInfo:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsDirections:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsWeb:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
