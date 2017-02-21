package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Coupon;

import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        final Coupon coupon = getIntent().getParcelableExtra(Coupon.class.getCanonicalName());

        ((TextView) findViewById(R.id.couponCode)).setText(coupon.getCode());
        ((TextView) findViewById(R.id.couponName)).setText(coupon.getName());
        ((TextView) findViewById(R.id.couponDescription)).setText(coupon.getDescription());
        ((TextView) findViewById(R.id.couponExpire)).setText(coupon.getDateExpire());

        ((Button)findViewById(R.id.couponRedeem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeem(coupon);
            }
        });
    }

    private void redeem(Coupon coupon) {
        dbHelper.setInactive(coupon.getCode());

        Map<String, String> redeemCoupon = new HashMap<String, String>();
        redeemCoupon.put("date", String.valueOf(System.currentTimeMillis()));
        redeemCoupon.put("code", coupon.getCode());
        redeemCoupon.put("name", coupon.getName());
        redeemCoupon.put("userId", coupon.getUserId());

        dbRedeemed
                .child(coupon.getCompany())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(redeemCoupon);

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
