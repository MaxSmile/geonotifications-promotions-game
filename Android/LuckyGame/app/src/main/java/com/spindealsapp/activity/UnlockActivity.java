package com.spindealsapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.spindealsapp.Constants;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.database.service.CompanyServiceLayer;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityUnlockBinding;

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
//        company = DBHelper.getInstance(this).getCompany(coupon.getCompanyKey());
        company = CompanyServiceLayer.getCompany(coupon.getCompanyKey());
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
