package com.vasilkoff.luckygame.activity;


import android.databinding.DataBindingUtil;

import android.os.Bundle;

import com.vasilkoff.luckygame.R;

import com.vasilkoff.luckygame.databinding.ActivityExtraSpinBinding;


public class ExtraSpinActivity extends BaseFacebookActivity {

    private ActivityExtraSpinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_spin);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_extra_spin);
        getDataByPlace(getIntent().getStringExtra(PLACE_KEY));
    }

    @Override
    public void resultDataByPlace() {
        super.resultDataByPlace();
        binding.setCompany(company);
        binding.setPlace(place);
        binding.setHandler(this);
    }

    @Override
    protected void socialSuccess() {

    }
}
