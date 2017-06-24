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
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.BaseHandler;
import com.vasilkoff.luckygame.database.DBHelper;
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

    static User user;

    private static final String G_TAG = "SignInActivity";

    public Company company;
    public Place place;
    public HashMap<String, Gift> gifts = new HashMap<String, Gift>();
    public Spin spinByPlace;

    public boolean result;
    public static final String TAG = "myTest";

    public boolean favorites;

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
            user = new User(
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
                        try {
                            user = new User(
                                    objectFacebook.getString("id"),
                                    objectFacebook.getString("name"),
                                    Constants.USER_TYPE_FACEBOOK);
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

    @Override
    public void favorites(View view) {
        favorites = !favorites;
        if (favorites) {
            DBHelper.getInstance(this).saveFavorites(place);
        } else {
            DBHelper.getInstance(this).removeFavorites(place);
        }
    }

    public void resultDataByPlace() {

    }

    public void getDataByPlace(String placeKey) {
        Constants.DB_PLACE.child(placeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                place = dataSnapshot.getValue(Place.class);
                place.setTypeName(Constants.COMPANY_TYPE_NAMES[place.getType()]);
                TypedArray iconArray = getResources().obtainTypedArray(R.array.company_type_icons);
                place.setTypeIcon(iconArray.getResourceId(place.getType(), 0));
                if (CurrentLocation.lat != 0 && place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                    place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                    place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                }
                iconArray.recycle();
                Constants.DB_SPIN.orderByChild("placeKey").equalTo(place.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Spin spin = data.getValue(Spin.class);

                            if (spin.getDateStart() <= System.currentTimeMillis() && spin.getDateFinish() >= System.currentTimeMillis()) {
                                spinByPlace = spin;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Constants.DB_COMPANY.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        company = dataSnapshot.getValue(Company.class);
                        final List<Box> boxes = place.getBox();
                        String limitKey = DateFormat.getDate("yyyyMMdd", System.currentTimeMillis());
                        Constants.DB_LIMIT.child(company.getId()).child(limitKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long countLimit = 0;
                                if (dataSnapshot.exists()) {
                                    countLimit = dataSnapshot.getValue(Long.class);
                                }
                                if (boxes != null && countLimit < company.getLimitGifts()) {
                                    for (int i = 0; i < boxes.size(); i++) {
                                        Box box = boxes.get(i);
                                        Constants.DB_GIFT.child(box.getGift()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Gift gift = dataSnapshot.getValue(Gift.class);
                                                if (gift.getDateStart() < System.currentTimeMillis() && gift.getDateFinish() > System.currentTimeMillis())
                                                    gifts.put(gift.getId(), gift);

                                                if (gifts.size() == boxes.size()) {
                                                    resultDataByPlace();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                } else {
                                    resultDataByPlace();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void resultSpins(TreeMap<String, Spin> mapSpins,HashMap<String, Place> places, HashMap<String, Company> companies) {

    }

    public void getSpins() {
        cities = new TreeMap<String, String>();
        Constants.DB_SPIN.orderByChild("dateFinish").startAt(System.currentTimeMillis()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final TreeMap<String, Spin> spins = new TreeMap<String, Spin>();
                final HashMap<String, Place> places = new HashMap<String, Place>();
                final HashMap<String, Company> companies = new HashMap<String, Company>();

                final String[] spinType = getResources().getStringArray(R.array.spin_type);

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final long spinCount = dataSnapshot.getChildrenCount();
                    final Spin spin = data.getValue(Spin.class);

                    if (spin.getDateStart() <= System.currentTimeMillis()) {
                        spin.setStatus(Constants.SPIN_STATUS_ACTIVE);
                    } else {
                        spin.setStatus(Constants.SPIN_STATUS_COMING);
                    }

                    spin.setTimeLeft(DateFormat.getDiff(spin.getDateFinish()));

                    Constants.DB_PLACE.child(spin.getPlaceKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Place spinPlace = dataSnapshot.getValue(Place.class);
                            cities.put(spinPlace.getCity(), spinPlace.getCity());
                            spinPlace.setTypeName(Constants.COMPANY_TYPE_NAMES[spinPlace.getType()]);
                            places.put(spinPlace.getId(), spinPlace);
                            if (user != null) {
                                long timeShift = System.currentTimeMillis() - Constants.DAY_TIME_SHIFT;
                                Constants.DB_USER.child(user.getId()).child("place").child(spinPlace.getId())
                                        .orderByChild("time").startAt(timeShift).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        boolean spinAvailable = true;
                                        boolean extraSpinAvailable = true;
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            UsedSpin usedSpin = data.getValue(UsedSpin.class);
                                            if (usedSpin.getType() == Constants.SPIN_TYPE_NORMAL) {
                                                spinAvailable = false;
                                            }

                                            if (usedSpin.getType() == Constants.SPIN_TYPE_EXTRA) {
                                                extraSpinAvailable = false;
                                            }
                                        }

                                        if (!spinAvailable) {
                                            spin.setStatus(Constants.SPIN_STATUS_EXTRA_AVAILABLE);
                                        }


                                        TypedArray spinIcon = getResources().obtainTypedArray(R.array.spin_type_icon);
                                        spin.setStatusIcon(spinIcon.getDrawable(spin.getStatus()));
                                        spin.setStatusString(spinType[spin.getStatus()]);
                                        spins.put(spinPlace.getName(), spin);
                                        spinIcon.recycle();

                                        getCompanies(spins, places, companies, spinCount, spinPlace);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                TypedArray spinIcon = getResources().obtainTypedArray(R.array.spin_type_icon);
                                spin.setStatusIcon(spinIcon.getDrawable(spin.getStatus()));
                                spin.setStatusString(spinType[spin.getStatus()]);
                                spins.put(spinPlace.getName(), spin);
                                spinIcon.recycle();
                                getCompanies(spins, places, companies, spinCount, spinPlace);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCompanies(final TreeMap<String, Spin> spins, final HashMap<String,
            Place> places, final HashMap<String, Company> companies,final long spinCount, final Place place) {
        Constants.DB_COMPANY.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                companies.put(dataSnapshot.getKey(), company);
                    resultSpins(spins, places, companies);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean checkFb() {
        if (user == null || user.getType() == Constants.USER_TYPE_GOOGLE) {
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
