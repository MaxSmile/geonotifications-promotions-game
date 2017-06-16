package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityUnlockBinding;
import com.vasilkoff.luckygame.entity.CouponExtension;

public class UnlockActivity extends BaseFacebookActivity {

    private CouponExtension coupon;
    private ActivityUnlockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        coupon = getIntent().getParcelableExtra(CouponExtension.class.getCanonicalName());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_unlock);
        getDataByPlace(coupon.getPlaceKey());

    }

    @Override
    public void resultDataByPlace() {
        super.resultDataByPlace();
        binding.setCompany(company);
        binding.setCoupon(coupon);
        binding.setHandler(this);
    }

    @Override
    protected void socialSuccess() {
        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_ACTIVE);
        Constants.DB_COUPON.child(coupon.getCode()).child("locks").setValue(System.currentTimeMillis());
        Constants.DB_COUPON.child(coupon.getCode()).child("locked").setValue(Constants.COUPON_UNLOCK);

        onBackPressed();
    }
}
