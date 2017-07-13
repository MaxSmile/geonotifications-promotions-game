package com.spindealsapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebView;

import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityTermsConditionsBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TermsConditionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        ActivityTermsConditionsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_conditions);
        binding.setHandler(this);
        binding.setTitle(getIntent().getStringExtra("title"));


        WebView webview = (WebView)this.findViewById(R.id.termsConditions);
        webview.loadDataWithBaseURL("file:///android_asset/", getHtmlFromAsset(), "text/html", "UTF-8", "");
    }

    private String getHtmlFromAsset() {
        InputStream is;
        StringBuilder builder = new StringBuilder();
        String htmlString = null;
        try {
            is = getAssets().open(getIntent().getStringExtra("file"));
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                htmlString = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlString;
    }
}
