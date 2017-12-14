package com.spindealsapp.binding.handler;

import android.view.View;

/**
 * Created by Kvm on 08.06.2017.
 */

public interface SettingHandler extends BaseHandler {
    public void logout(View view);
    public void settings(View view);
    public void termsConditions(View view);
    public void tutorial(View view);
    public void visitWebsite(View view);
    public void visitFacebook(View view);
    public void rating(View view);
    public void forPartner(View view);
    public void forImprove(View view);

    public void clear(View view);
}
