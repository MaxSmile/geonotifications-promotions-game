package com.vasilkoff.luckygame.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.CouponExtension;

public class UnlockActivity extends BaseActivity {

    private CouponExtension coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        coupon = getIntent().getParcelableExtra(CouponExtension.class.getCanonicalName());
        ((TextView)findViewById(R.id.unlockExpires)).setText(coupon.getExpiredDiff());
    }

    private void unlock() {
        dbCoupons.child(coupon.getCode()).child("locks").setValue(System.currentTimeMillis());
        onBackPressed();
    }

    public void onClickUnlock(View view) {
        switch (view.getId()) {
            case R.id.unlockClose:
                onBackPressed();
                break;
            case R.id.unlockUnlock:
                unlock();
                break;
        }
    }
}
