package com.spindealsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.binding.handler.BaseHandler;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.service.PlaceServiceLayer;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.User;
import com.spindealsapp.util.NetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;


/**
 * Created by Kusenko on 20.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        BaseHandler {

    static GoogleApiClient mGoogleApiClient;
    static GoogleSignInAccount accountGoogle;
    static JSONObject objectFacebook;

    private static final String G_TAG = "SignInActivity";

    public static Place place;
    public Company company;
    public HashMap<String, Gift> gifts = new HashMap<String, Gift>();


    public boolean result;
    public static final String TAG = "myTest";

    public static boolean showPopUpLogin = true;

    private boolean disconnect = false;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

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
        User user = Properties.getUser();
        if (user != null && user.getToken() != null) {
            CurrentUser.user = user;
            return true;
        } else {
            logoutAccount();
            return false;
        }
        /*
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
        }*/
    }



    void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            accountGoogle = result.getSignInAccount();
            CurrentUser.user = new User(
                    accountGoogle.getId(),
                    accountGoogle.getIdToken(),
                    accountGoogle.getDisplayName(),
                    Constants.USER_TYPE_GOOGLE
            );
            Properties.setUser(CurrentUser.user);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect = false;
    }

    void getFacebookUserInfo() {
        System.out.println("myTest exp= " + AccessToken.getCurrentAccessToken().getExpires());
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
                                        AccessToken.getCurrentAccessToken().getToken(),
                                        objectFacebook.getString("name"),
                                        Constants.USER_TYPE_FACEBOOK);
                                Properties.setUser(CurrentUser.user);
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
        PlaceServiceLayer.update(place);
    }

    public void logoutAccount() {
        LoginManager.getInstance().logOut();
        logoutGoogle();
        CurrentUser.user = null;
        Properties.setUser(null);
    }

    private void logoutGoogle() {
        disconnect = true;
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (disconnect) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }

    public void reloadData() {
        startActivity(new Intent(this, LoaderActivity.class));
        finish();
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
}
