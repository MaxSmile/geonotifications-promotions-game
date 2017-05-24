package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.CouponHandler;
import com.vasilkoff.luckygame.databinding.ActivityCouponBinding;

import com.vasilkoff.luckygame.entity.CouponExtension;


public class CouponActivity extends BaseActivity implements CouponHandler{

    private TextView couponCode;
    private Button buttonRedeem;
    private CouponExtension coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        coupon = getIntent().getParcelableExtra(CouponExtension.class.getCanonicalName());
        ActivityCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        binding.setCoupon(coupon);
        binding.setHandler(this);

        /*final CouponDB coupon = getIntent().getParcelableExtra(CouponDB.class.getCanonicalName());

        couponCode = (TextView) findViewById(R.id.couponCode);
        couponCode.setText(coupon.getCode());

        ((TextView) findViewById(R.id.couponName)).setText(coupon.getName());
        ((TextView) findViewById(R.id.couponDescription)).setText(coupon.getDescription());
        ((TextView) findViewById(R.id.couponExpire)).setText(coupon.getDateExpire());

        buttonRedeem = (Button)findViewById(R.id.couponRedeem);
        buttonRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) {
                    startActivity(new Intent(CouponActivity.this, ChooseAccountActivity.class));
                } else {
                    redeem(coupon);
                }
            }
        });*/
    }

   /* private void redeem(CouponDB coupon) {
        dbHelper.setInactive(coupon.getCode());
        couponCode.setVisibility(View.VISIBLE);
        buttonRedeem.setVisibility(View.GONE);

        Map<String, String> redeemCoupon = new HashMap<String, String>();
        redeemCoupon.put("date", String.valueOf(System.currentTimeMillis()));
        redeemCoupon.put("code", coupon.getCode());
        redeemCoupon.put("name", coupon.getName());

        if (objectFacebook != null) {
            try {
                redeemCoupon.put("userId", objectFacebook.getString("id"));
                redeemCoupon.put("userName", objectFacebook.getString("name"));
                redeemCoupon.put("userType", "facebook");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (accountGoogle != null) {
            redeemCoupon.put("userId", accountGoogle.getId());
            redeemCoupon.put("userName", accountGoogle.getDisplayName());
            redeemCoupon.put("userType", "google");
        }


        dbCoupons
                .child(coupon.getCompany())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(redeemCoupon);

        *//*startActivity(new Intent(this, HomeActivity.class));
        finish();*//*
    }*/


    @Override
    public void send(View view) {
        Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unlock(View view) {
        Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void more(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("company", coupon.getCompanyKey());
        startActivity(intent);
    }

    @Override
    public void redeem(View view) {
        Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
    }
}
