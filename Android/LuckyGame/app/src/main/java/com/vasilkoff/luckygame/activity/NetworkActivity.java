package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.NetworkHandler;
import com.vasilkoff.luckygame.databinding.ActivityNetworkBinding;
import com.vasilkoff.luckygame.util.NetworkState;

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
