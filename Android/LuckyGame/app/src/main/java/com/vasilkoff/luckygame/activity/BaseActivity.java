package com.vasilkoff.luckygame.activity;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kusenko on 20.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbVersion = reference.child("ver1");
    DatabaseReference dbData = dbVersion.child("data");
    DatabaseReference dbCompanies = dbData.child("companies");
    DatabaseReference dbPlaces = dbData.child("places");
    DatabaseReference dbRedeemed = dbVersion.child("redeemed");

    static Map<String, Map<String, Promotion>> companies;
    static ArrayList<Place> uniquePlaces;

    static DBHelper dbHelper;

    static boolean isLogin;
    static GoogleApiClient mGoogleApiClient;
    static GoogleSignInAccount accountGoogle;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        if (dbHelper == null)
            dbHelper = new DBHelper(this);

        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    boolean checkLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            return true;
        } else {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                //GoogleSignInResult result = opr.get();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
