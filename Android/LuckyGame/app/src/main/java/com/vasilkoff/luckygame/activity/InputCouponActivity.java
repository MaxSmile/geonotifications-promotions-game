package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.InputCouponHandler;
import com.vasilkoff.luckygame.database.FirebaseData;
import com.vasilkoff.luckygame.databinding.ActivityInputCouponBinding;
import com.vasilkoff.luckygame.eventbus.Events;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class InputCouponActivity extends BaseActivity implements InputCouponHandler {

    private ActivityInputCouponBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_coupon);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_coupon);
        binding.setTitle(getString(R.string.add_coupon_title));
        binding.setBack(getResources().getIdentifier("back_blue", "drawable", getPackageName()));
        binding.setColorTitle(ContextCompat.getColor(this, android.R.color.tab_indicator_text));
        binding.setResult(true);
        binding.setCouponCheck(true);
        //binding.setCode("fr1497963301");
        binding.setHandler(this);
    }

    @Override
    public void add(View view) {
        FirebaseData.checkCouponsByCode(binding.getCode());
        binding.setResult(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckCoupon(Events.CheckCoupons checkCoupons) {
        binding.setResult(true);
        binding.setCouponCheck(checkCoupons.isExist());
        if (checkCoupons.isExist()) {
            Intent intent = new Intent(this, SlideCouponsActivity.class);
            intent.putExtra(Constants.COUPON_KEY, binding.getCode());
            startActivity(intent);
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
