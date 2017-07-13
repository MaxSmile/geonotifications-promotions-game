package com.spindealsapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.spindealsapp.binding.handler.NetworkHandler;
import com.spindealsapp.util.NetworkState;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityNetworkBinding;

public class NetworkActivity extends BaseActivity implements NetworkHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        ActivityNetworkBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_network);
        binding.setHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkState.isOnline())
            onBackPressed();
    }

    @Override
    public void settings(View view) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }
}
