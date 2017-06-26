package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
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
        binding.setTitle(getString(R.string.setting_title));
        binding.setBack(getResources().getIdentifier("back_blue", "drawable", getPackageName()));
        binding.setColorTitle(ContextCompat.getColor(this, android.R.color.tab_indicator_text));
        binding.setHandler(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionNumber = pInfo.versionName + "." + pInfo.versionCode;
            binding.setVersion(String.format(getString(R.string.version), versionNumber));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void settings(View view) {
        startActivity(new Intent(this, PreferencesActivity.class));
    }

    @Override
    public void termsConditions(View view) {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        intent.putExtra("title", getString(R.string.terms_conditions));
        intent.putExtra("file", getString(R.string.terms_conditions_file));
        startActivity(intent);
    }

    @Override
    public void tutorial(View view) {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        intent.putExtra("title", getString(R.string.tutorial));
        intent.putExtra("file", getString(R.string.tutorial_file));
        startActivity(intent);
    }

    @Override
    public void visitWebsite(View view) {
        startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url))));
    }

    @Override
    public void visitFacebook(View view) {
        startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_page_url))));
    }

    @Override
    public void invite(View view) {
        inviteApp();
    }

    @Override
    public void rating(View view) {
        //final String appPackageName = getPackageName();
        final String appPackageName = "com.vasilkoff.easyvpnfree";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void forPartner(View view) {
        sendMail(getString(R.string.mail_for_partner));
    }

    @Override
    public void forImprove(View view) {
        sendMail(getString(R.string.mail_for_improve));
    }

    @Override
    public void addCoupon(View view) {
        startActivity(new Intent(this, InputCouponActivity.class));
    }

    private void sendMail(String address) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",address, null));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.mail_chooser)));
    }


    @Override
    public void clear(View view) {
        if (user != null) {
            Constants.DB_USER.child(user.getId()).getRef().removeValue();
        } else {
            Toast.makeText(this, "Unknown user, need login", Toast.LENGTH_SHORT).show();
        }

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
