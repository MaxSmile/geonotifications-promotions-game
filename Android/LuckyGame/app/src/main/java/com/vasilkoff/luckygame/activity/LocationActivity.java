package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.NetworkHandler;
import com.vasilkoff.luckygame.databinding.ActivityLocationBinding;
import com.vasilkoff.luckygame.util.LocationState;

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
