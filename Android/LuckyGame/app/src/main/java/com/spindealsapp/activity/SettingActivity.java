package com.spindealsapp.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.binding.handler.SettingHandler;
import com.spindealsapp.R;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity implements SettingHandler {

    private boolean disconnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setUser(CurrentUser.user);
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
        if (CurrentUser.user != null) {
            LoginManager.getInstance().logOut();
            logoutGoogle();
            CurrentUser.user = null;
            Properties.setUser(null);
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
        startActivity(new Intent(this, TutorialActivity.class));
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
        final String appPackageName = getPackageName();
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

    private void sendMail(String address) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",address, null));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.mail_chooser)));
    }


    @Override
    public void clear(View view) {
        if (CurrentUser.user != null) {
            Constants.DB_USER.child(CurrentUser.user.getId()).getRef().removeValue();
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
