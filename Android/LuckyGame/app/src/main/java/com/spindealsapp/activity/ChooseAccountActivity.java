package com.spindealsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.R;

import java.util.Arrays;
import java.util.List;

public class ChooseAccountActivity extends BaseActivity {

    public static CallbackManager callbackManager;
    private int RC_SIGN_IN = 100;
    private LoginButton loginFb;
    private Button btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);
        checkNetwork();

        btnGoogle = (Button)findViewById(R.id.btnGoogle);



        callbackManager = CallbackManager.Factory.create();
        loginFb = (LoginButton)findViewById(R.id.loginFbButton);
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookUserInfo();
                showLoginSuccessMessage();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        List<String> permissionNeeds = Arrays.asList("user_friends","email","user_birthday");
        loginFb.setReadPermissions(permissionNeeds);
        if (getIntent().getBooleanExtra("fbAction", false)) {
            btnGoogle.setVisibility(View.GONE);
            loginFb.performClick();
        }
    }

    private void showLoginSuccessMessage() {
        FirebaseData.getPlaces();
        onBackPressed();
    }

    @Override
    void handleSignInResult(GoogleSignInResult result) {
        super.handleSignInResult(result);
        if (result.isSuccess())
            showLoginSuccessMessage();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    public void onClickLogin(View v) {
        switch (v.getId()) {
            case R.id.btnFacebook:
                loginFb.performClick();
                break;
            case R.id.btnGoogle:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.btnLoginSkip:
                showPopUpLogin = false;
                onBackPressed();
                break;
            case R.id.chooseAccountTermsConditions:
                Intent intent = new Intent(this, TermsConditionsActivity.class);
                intent.putExtra("title", getString(R.string.terms_conditions));
                intent.putExtra("file", getString(R.string.terms_conditions_file));
                startActivity(intent);
                break;
        }
    }
}
