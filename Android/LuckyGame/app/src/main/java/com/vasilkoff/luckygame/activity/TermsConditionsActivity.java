package com.vasilkoff.luckygame.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.vasilkoff.luckygame.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TermsConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        WebView webview = (WebView)this.findViewById(R.id.termsConditions);
        webview.loadDataWithBaseURL("file:///android_asset/", getHtmlFromAsset(), "text/html", "UTF-8", "");
    }

    private String getHtmlFromAsset() {
        InputStream is;
        StringBuilder builder = new StringBuilder();
        String htmlString = null;
        try {
            is = getAssets().open("terms_conditions.html");
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
