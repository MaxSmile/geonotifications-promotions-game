package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.database.PlaceServiceLayer;
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
        place = PlaceServiceLayer.getPlace(coupon.getPlaceKey());
        company = DBHelper.getInstance(this).getCompany(coupon.getCompanyKey());
        binding.setCompany(company);
        binding.setCoupon(coupon);
        binding.setHandler(this);
        name = coupon.getPlaceName();
        initData();
    }

    @Override
    protected void socialSuccess() {
        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_ACTIVE);
        Constants.DB_COUPON.child(coupon.getCode()).child("locks").setValue(System.currentTimeMillis());
        Constants.DB_COUPON.child(coupon.getCode()).child("locked").setValue(Constants.COUPON_UNLOCK);
        coupon.setStatus(Constants.COUPON_STATUS_ACTIVE);
        coupon.setLocks(System.currentTimeMillis());
        coupon.setLocked(Constants.COUPON_UNLOCK);
        DBHelper.getInstance(this).saveCoupon(coupon);

        onBackPressed();
    }
}
