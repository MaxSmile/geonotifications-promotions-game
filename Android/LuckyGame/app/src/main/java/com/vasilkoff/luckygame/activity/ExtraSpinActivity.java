package com.vasilkoff.luckygame.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;

import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.databinding.ActivityExtraSpinBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

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
        place.setExtraSpinAvailable(false);
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
