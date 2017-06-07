package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import com.vasilkoff.luckygame.binding.handler.BaseHandler;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.User;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Kusenko on 20.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        BaseHandler {

    static DBHelper dbHelper;

    static GoogleApiClient mGoogleApiClient;
    static GoogleSignInAccount accountGoogle;
    static JSONObject objectFacebook;

    static User user;

    private static final String TAG = "SignInActivity";


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        dbHelper = DBHelper.getInstance(this);

        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    boolean checkLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            getFacebookUserInfo();

            return true;
        } else {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
                return true;
            }
        }
        return false;
    }

    void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            accountGoogle = result.getSignInAccount();
            user = new User(
                    accountGoogle.getId(),
                    accountGoogle.getDisplayName(),
                    "google"
            );
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    void getFacebookUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        objectFacebook = object;
                        try {
                            user = new User(
                                    objectFacebook.getString("id"),
                                    objectFacebook.getString("name"),
                                    "facebook");
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

    @Override
    public void back(View view) {
        onBackPressed();
    }
}
