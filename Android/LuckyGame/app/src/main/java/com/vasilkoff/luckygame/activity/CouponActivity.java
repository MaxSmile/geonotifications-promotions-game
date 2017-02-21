package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Coupon;

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
                dbHelper.setInactive(coupon.getCode());
            }
        });
    }
}
