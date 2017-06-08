package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.SettingHandler;
import com.vasilkoff.luckygame.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity implements SettingHandler {

    private boolean disconnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setUser(user);
        binding.setHandler(this);

    }

    @Override
    public void logout(View view) {
        if (user != null) {
            LoginManager.getInstance().logOut();
            logoutGoogle();
            user = null;
        }

        startActivity(new Intent(this, ChooseAccountActivity.class));
        finish();
    }

    private void logoutGoogle() {
        disconnect = true;
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (disconnect) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect = false;
    }
}
