package com.spindealsapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebView;

import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityInfoBinding;

public class InfoActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        binding.setHandler(this);
        binding.setTitle(getIntent().getStringExtra("title"));

        webView = (WebView)findViewById(R.id.infoWeb);
        String html = getIntent().getStringExtra("info");
        String mime = "text/html";
        String encoding = "utf-8";
        webView.loadDataWithBaseURL(null, html, mime, encoding, null);
    }
}
