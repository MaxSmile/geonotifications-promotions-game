package com.vasilkoff.luckygame.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;

import android.os.Bundle;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;

import com.vasilkoff.luckygame.databinding.ActivityExtraSpinBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;


public class ExtraSpinActivity extends BaseFacebookActivity {

    private ActivityExtraSpinBinding binding;
    private Spin spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_spin);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_extra_spin);
        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());
        getDataByPlace(getIntent().getStringExtra(Constants.PLACE_KEY));
    }

    @Override
    public void resultDataByPlace() {
        super.resultDataByPlace();
        binding.setCompany(company);
        binding.setPlace(place);
        binding.setSpin(spin);
        binding.setHandler(this);
    }

    @Override
    protected void socialSuccess() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Place.class.getCanonicalName(), place);
        intent.putExtra(Company.class.getCanonicalName(), company);
        intent.putExtra(Gift.class.getCanonicalName(), gifts);
        intent.putExtra("extraSpinAvailable", false);
        intent.putExtra(Constants.SPIN_TYPE_KEY, Constants.EXTRA_SPIN_TYPE);
        startActivity(intent);
        finish();
    }
}
