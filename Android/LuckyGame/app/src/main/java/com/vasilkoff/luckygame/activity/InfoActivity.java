package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.vasilkoff.luckygame.R;

public class InfoActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        webView = (WebView)findViewById(R.id.infoWeb);
        String html = getIntent().getStringExtra("info");
        String mime = "text/html";
        String encoding = "utf-8";
        webView.loadDataWithBaseURL(null, html, mime, encoding, null);
    }
}
