package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LikeButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.ExtraHandler;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;

/**
 * Created by Kvm on 07.06.2017.
 */

public abstract class BaseFacebookActivity extends BaseActivity implements ExtraHandler{

    public static CallbackManager callbackManager;
    private LikeView likeView;
    private ShareButton shareButton;
    public String name;

    public void initData() {
        callbackManager = CallbackManager.Factory.create();
        init();
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
        if (checkFb()) {
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
    }

    @Override
    public void share(View view) {
        if (checkFb()) {
            if (place != null) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle(place.getName())
                        .setContentDescription(place.getInfo())
                        .setContentUrl(Uri.parse(place.getUrl()))
                        .setImageUrl(Uri.parse(place.getGallery().get(0)))
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
    }

    @Override
    public void invite(View view) {
        if (checkFb()) {
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
                    if (result != null && result.equals("like")) {
                        socialSuccess();
                    }
                }
            }
        }
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }


}
