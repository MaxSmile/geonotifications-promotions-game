package com.spindealsapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.spindealsapp.R;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.NetworkState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoaderActivity extends BaseActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        progressBar = (ProgressBar)findViewById(R.id.loadProgress);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        if (!NetworkState.isOnline()) {
            loadDataSuccess();
        } else {
            FirebaseData.loadData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishLoadData(Events.FinishLoadData finishLoadData) {
        progressBar.setProgress(100);
        loadDataSuccess();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoadingData(Events.LoadingData loadingData) {
        progressBar.setProgress(loadingData.getPercent());
    }

    private void loadDataSuccess() {
        if (getIntent().getBooleanExtra("loginForGame", false)) {
            onBackPressed();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
