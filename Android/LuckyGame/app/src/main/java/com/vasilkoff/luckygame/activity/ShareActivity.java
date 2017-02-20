package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Promotion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class ShareActivity extends BaseActivity {

    private CallbackManager callbackManager;
    private Promotion randomPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Random generator = new Random();
        Object[] values = companies.get(getIntent().getStringExtra("company")).values().toArray();
        randomPromotion = (Promotion)values[generator.nextInt(values.length)];

        String title = String.format(getResources().getString(R.string.share_title),
                randomPromotion.getName());
        ((TextView) findViewById(R.id.shareTitle)).setText(title);

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
                getUserInfo();
            }

            @Override
            public void onCancel() {
                startActivity(new Intent(ShareActivity.this, HomeActivity.class));
                finish();
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

    private void createCoupon(String id) {
        String coupon = id + String.valueOf(System.currentTimeMillis());
        dbHelper.saveCoupon(coupon, randomPromotion);
        startActivity(new Intent(ShareActivity.this, ListCouponsActivity.class));
        finish();
    }

    private void getUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                          /*  String gender = object.getString("gender");
                            String birthday = object.getString("birthday");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            System.out.println("TEST = " + name + id);*/
                            createCoupon(object.getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,gender,name,birthday,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

    }

}
