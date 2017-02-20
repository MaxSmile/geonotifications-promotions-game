package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.vasilkoff.luckygame.R;

public class ShareActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        callbackManager = CallbackManager.Factory.create();

        ShareButton fbShareButton = (ShareButton) findViewById(R.id.fb_share_button);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Hello Guys")
                .setContentDescription(
                        "Food is good))")
                .setContentUrl(Uri.parse("http://google.com"))
                .setImageUrl(Uri.parse("http://depts.washington.edu/nwcenter/wp-content/uploads/2016/09/maxresdefault.jpg"))

                .build();
        fbShareButton.setShareContent(content);
        fbShareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("TEST Share+");
            }

            @Override
            public void onCancel() {
                System.out.println("TEST Share-");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}
