package com.spindealsapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.spindealsapp.binding.handler.NetworkHandler;
import com.spindealsapp.util.LocationState;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityLocationBinding;

public class LocationActivity extends BaseActivity implements NetworkHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        binding.setHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocationState.isEnabled())
            onBackPressed();
    }

    @Override
    public void settings(View view) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
