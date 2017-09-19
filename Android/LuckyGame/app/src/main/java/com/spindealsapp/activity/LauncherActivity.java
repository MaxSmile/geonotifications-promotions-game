package com.spindealsapp.activity;
import android.content.Intent;
import android.os.Bundle;

import com.spindealsapp.R;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (showPopUpLogin && !checkLogin()) {
            showPopUpLogin = false;
            startActivity(new Intent(this, ChooseAccountActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoaderActivity.class));
            finish();
        }
    }
}
