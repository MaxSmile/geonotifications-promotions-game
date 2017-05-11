package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Coupon;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends BaseActivity {

    private TextView couponCode;
    private Button buttonRedeem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int widthWindow = dm.widthPixels;
        int heightWindow = dm.heightPixels;

        getWindow().setLayout((int)(widthWindow * 0.9), (int)(heightWindow * 0.9));*/

        final Coupon coupon = getIntent().getParcelableExtra(Coupon.class.getCanonicalName());

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
        });
    }

    private void redeem(Coupon coupon) {
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


        dbRedeemed
                .child(coupon.getCompany())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(redeemCoupon);

        /*startActivity(new Intent(this, HomeActivity.class));
        finish();*/
    }

    public void onClickCoupon(View view) {
        switch (view.getId()) {
            case R.id.couponInfoClose:
                onBackPressed();
                break;
        }
    }
}
