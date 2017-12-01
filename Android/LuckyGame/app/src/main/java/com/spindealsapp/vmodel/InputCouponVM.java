package com.spindealsapp.vmodel;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.spindealsapp.Constants;
import com.spindealsapp.R;
import com.spindealsapp.activity.InputCouponActivity;
import com.spindealsapp.activity.NetworkActivity;
import com.spindealsapp.activity.SendCouponActivity;
import com.spindealsapp.binding.handler.InputCouponHandler;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.NetworkState;
import com.spindealsapp.util.ValidatorUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Volodymyr Kusenko on 24.11.2017.
 */

public class InputCouponVM extends BaseViewModel<InputCouponActivity> implements InputCouponHandler {

    private String code;
    private String title;
    private int colorTitle;
    private int backIcon;
    private boolean couponCheck;

    public InputCouponVM(InputCouponActivity activity) {
        super(activity);
        title = activity.getString(R.string.add_coupon_title);
        colorTitle = ContextCompat.getColor(activity, android.R.color.tab_indicator_text);
        backIcon = activity.getResources().getIdentifier("back_blue", "drawable", activity.getPackageName());
        couponCheck = true;
    }

    public int getColorTitle() {
        return colorTitle;
    }

    public String getTitle() {
        return title;
    }

    public int getBackIcon() {
        return backIcon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCouponCheck() {
        return couponCheck;
    }

    public void setCouponCheck(boolean couponCheck) {
        this.couponCheck = couponCheck;
        notifyChange();
    }

    @Override
    public void back(View view) {
        activity.onBackPressed();
    }

    @Override
    public void favorites(View view) {

    }

    @Override
    public void add(View view) {
        if (NetworkState.isOnline()) {
            if (code != null) {
                code = code.toLowerCase().trim();
                if (code.length() > 0 && ValidatorUtil.validateCouponCode(code)) {
                    FirebaseData.checkCouponsByCode(code);
                    isLoading.set(true);
                } else {
                    setCouponCheck(false);
                }
            } else {
                setCouponCheck(false);
            }
        } else {
            activity.startActivity(new Intent(activity, NetworkActivity.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckCoupon(Events.CheckCoupons checkCoupons) {
        setCouponCheck(checkCoupons.isExist());
        if (!checkCoupons.isExist()) {
            isLoading.set(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddedCoupon(Events.AddedCoupon addedCoupon) {
        isLoading.set(false);
        if (code != null) {
            Intent intent = new Intent(activity, SendCouponActivity.class);
            intent.putExtra(Constants.COUPON_KEY, code);
            intent.putExtra("type", 1);
            activity.startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
