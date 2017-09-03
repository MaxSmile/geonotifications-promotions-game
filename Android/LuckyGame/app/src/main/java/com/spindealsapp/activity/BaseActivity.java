package com.spindealsapp.activity;

import android.content.Intent;
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
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.binding.handler.BaseHandler;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.User;
import com.spindealsapp.util.NetworkState;
import com.spindealsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
        FirebaseData.getOffer();
        FirebaseData.getKeywords();
        FirebaseData.getSpins();
    }

    public void setListeners() {
        FirebaseData.placeListener();
        FirebaseData.companyListener();
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

   /* public boolean checkFb() {
        if (CurrentUser.user == null || CurrentUser.user.getType() == Constants.USER_TYPE_GOOGLE) {
            Intent intent = new Intent(this, ChooseAccountActivity.class);
            intent.putExtra("fbAction", true);
            startActivity(intent);
        } else {
            return true;
        }
        return false;
    }*/

    public void inviteApp() {
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
