package com.vasilkoff.luckygame.binding.handler;

import android.view.View;

/**
 * Created by Kvm on 24.05.2017.
 */

public interface CouponHandler extends BaseHandler {
    public void send(View view);
    public void unlock(View view);
    public void more(View view);
    public void redeem(View view);
}