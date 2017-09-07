package com.spindealsapp.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.spindealsapp.Constants;
import com.spindealsapp.database.SpinServiceLayer;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityExtraSpinBinding;

import java.util.HashMap;


public class ExtraSpinActivity extends BaseFacebookActivity {

    private ActivityExtraSpinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_spin);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_extra_spin);
        place = getIntent().getParcelableExtra(Place.class.getCanonicalName());
        company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
        gifts = (HashMap<String, Gift>)(getIntent().getSerializableExtra(Gift.class.getCanonicalName()));
        binding.setCompany(company);
        binding.setPlace(place);
        binding.setHandler(this);
        name = place.getName();
        initData();
    }

    @Override
    protected void socialSuccess() {
        Intent intent = new Intent(this, GameActivity.class);
        place.getSpin().setExtraAvailable(false);
        place.getSpin().setExtra(true);
        place.getSpin().setExtraCreateTime(System.currentTimeMillis());
        SpinServiceLayer.updateSpin(place.getSpin());

        intent.putExtra(Place.class.getCanonicalName(), place);
        intent.putExtra(Company.class.getCanonicalName(), company);
        intent.putExtra(Gift.class.getCanonicalName(), gifts);
        intent.putExtra(Constants.SPIN_TYPE_KEY, Constants.SPIN_TYPE_EXTRA);
        startActivity(intent);
        finish();
    }

    @Override
    public void favorites(View view) {
        super.favorites(view);
        binding.setPlace(place);
    }
}
