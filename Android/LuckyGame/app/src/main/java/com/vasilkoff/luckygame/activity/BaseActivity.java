package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.CurrentUser;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.BaseHandler;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.database.FirebaseData;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.entity.UsedSpin;
import com.vasilkoff.luckygame.entity.User;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;
import com.vasilkoff.luckygame.util.NetworkState;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by Kusenko on 20.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        BaseHandler {

    static DBHelper dbHelper;

    static GoogleApiClient mGoogleApiClient;
    static GoogleSignInAccount accountGoogle;
    static JSONObject objectFacebook;

    private static final String G_TAG = "SignInActivity";

    public Place place;
    public Company company;
    public HashMap<String, Gift> gifts = new HashMap<String, Gift>();


    public boolean result;
    public static final String TAG = "myTest";

    public static boolean showPopUpLogin = true;

    public static TreeMap<String, String> cities;


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

    public void loadData() {
        FirebaseData.getCoupons();
        FirebaseData.getPlaces();
        FirebaseData.getGift();
    }

    public void setListeners() {
        FirebaseData.placeListener();
        FirebaseData.companyListener();
        FirebaseData.spinListener();
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

    boolean checkResult() {
        if (!result)
            Toast.makeText(this, R.string.wait_for_update_data, Toast.LENGTH_LONG).show();

        return result;
    }

    void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            accountGoogle = result.getSignInAccount();
            CurrentUser.user = new User(
                    accountGoogle.getId(),
                    accountGoogle.getDisplayName(),
                    Constants.USER_TYPE_GOOGLE
            );

        }
    }

    public void checkNetwork() {
        if (!NetworkState.isOnline()) {
            startActivity(new Intent(this, NetworkActivity.class));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(G_TAG, "onConnectionFailed:" + connectionResult);
    }

    void getFacebookUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        objectFacebook = object;
                        if (objectFacebook != null) {
                            try {
                                CurrentUser.user = new User(
                                        objectFacebook.getString("id"),
                                        objectFacebook.getString("name"),
                                        Constants.USER_TYPE_FACEBOOK);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    @Override
    public void favorites(View view) {
        place.setFavorites(!place.isFavorites());
        DBHelper.getInstance(this).updatePlace(place);
    }

    public boolean checkFb() {
        if (CurrentUser.user == null || CurrentUser.user.getType() == Constants.USER_TYPE_GOOGLE) {
            Intent intent = new Intent(this, ChooseAccountActivity.class);
            intent.putExtra("fbAction", true);
            startActivity(intent);
        } else {
            return true;
        }
        return false;
    }

    public void inviteApp() {
        if (checkFb()) {
            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(getString(R.string.facebook_app_link))
                        .setPreviewImageUrl(getString(R.string.app_preview_image_url))
                        .build();
                AppInviteDialog appInviteDialog = new AppInviteDialog(this);
                appInviteDialog.show(content);
            }
        }
    }
}
