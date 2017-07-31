package com.spindealsapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LikeButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.spindealsapp.binding.handler.ExtraHandler;
import com.spindealsapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kvm on 07.06.2017.
 */

public abstract class BaseFacebookActivity extends BaseActivity implements ExtraHandler {

    public static CallbackManager callbackManager;
    private LikeView likeView;
    private ShareButton shareButton;
    public String name;
    private LoginButton loginFb;
    private boolean unLike;


    public void initData() {
        callbackManager = CallbackManager.Factory.create();
        /*if (CurrentUser.user == null || CurrentUser.user.getType() == Constants.USER_TYPE_GOOGLE) {
            loginFb();
        }*/
        init();
    }

    private void loginFb() {
        loginFb = (LoginButton)findViewById(R.id.loginFbButton);
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookUserInfo();
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
        loginFb.performClick();
    }

    private void init() {
        ((TextView)findViewById(R.id.unlockLikeText))
                .setText(String.format(getString(R.string.unlock_facebook_like), name));
        ((TextView)findViewById(R.id.unlockCheckInText))
                .setText(String.format(getString(R.string.unlock_facebook_share), name));

        likeView = (LikeView) findViewById(R.id.likeView);
        shareButton = (ShareButton) findViewById(R.id.fb_share_button);
    }

    protected void socialSuccess() {

    }

    @Override
    public void like(View view) {
        if (company.getFacebookUrl() != null) {
            likeView.setObjectIdAndType(
                    company.getFacebookUrl(),
                    LikeView.ObjectType.PAGE);
            final LikeButton button = (LikeButton) ((ViewGroup) likeView.getChildAt(0)).getChildAt(0);
            button.post(new Runnable() {
                @Override
                public void run() {
                    button.performClick();
                }
            });
        }
    }

    @Override
    public void share(View view) {
        String image = getString(R.string.app_preview_image_url);
        if (place != null) {
            if (place.getGallery().size() > 0) {
                image = place.getGallery().get(0);
            }
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(place.getName())
                    .setContentDescription(place.getInfo())
                    .setContentUrl(Uri.parse(place.getUrl()))
                    .setImageUrl(Uri.parse(image))
                    .build();
            shareButton.setShareContent(content);
            shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    socialSuccess();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            shareButton.callOnClick();
        }
    }

    @Override
    public void invite(View view) {
        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(getString(R.string.facebook_app_link))
                    .setPreviewImageUrl(getString(R.string.app_preview_image_url))
                    .build();
            AppInviteDialog appInviteDialog = new AppInviteDialog(this);
            appInviteDialog.registerCallback(callbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                @Override
                public void onSuccess(AppInviteDialog.Result result) {
                    socialSuccess();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            appInviteDialog.show(content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (responseCode == RESULT_OK && intent != null) {
            if (intent.hasExtra("com.facebook.platform.protocol.RESULT_ARGS") ) {
                Bundle bundle = intent.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS");
                if (bundle != null) {
                    bundle.getBoolean("object_is_liked"); // liked/unliked
                    bundle.getInt("didComplete");
                    bundle.getInt("like_count"); // object like count
                    bundle.getString("like_count_string");
                    bundle.getString("social_sentence");
                    bundle.getString("completionGesture"); // liked/cancel/unliked

                    String result = bundle.getString("completionGesture");
                    System.out.println(TAG + " result =" + result);
                    if (result != null) {
                        if (result.equals("unlike")) {
                            unLike = true;
                        }
                        System.out.println(TAG + " unLike =" + unLike);
                        if (result.equals("like")) {
                            if (unLike) {
                                Toast.makeText(this, R.string.cheat_message, Toast.LENGTH_SHORT).show();
                            } else {
                                socialSuccess();
                            }
                        }
                    }
                }
            }
        }
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

}
