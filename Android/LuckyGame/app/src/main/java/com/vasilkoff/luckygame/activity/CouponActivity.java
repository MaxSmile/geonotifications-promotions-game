package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.CouponHandler;
import com.vasilkoff.luckygame.databinding.ActivityCouponBinding;

import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.io.IOException;


public class CouponActivity extends BaseActivity implements SoundPool.OnLoadCompleteListener, CouponHandler{

    private CouponExtension coupon;
    private PopupWindow popupWindow;
    private LinearLayout parentLayout;
    private boolean userPrize = false;
    private SoundPool sp;
    private int soundIdWin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        coupon = getIntent().getParcelableExtra(CouponExtension.class.getCanonicalName());
        userPrize = getIntent().getBooleanExtra("userPrize", false);
        initSound();

        String locks = DateFormat.getDiff(coupon.getLocks());
        if (locks != null)
            coupon.setLockDiff(locks);

        String expire = DateFormat.getDiff(coupon.getExpired());
        if (expire != null)
            coupon.setExpiredDiff(expire);

        if (CurrentLocation.lat != 0) {
            if (coupon.getGeoLat() != 0 && coupon.getGeoLon() != 0) {
                coupon.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                        coupon.getGeoLat(), coupon.getGeoLon()));
                coupon.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        coupon.getGeoLat(), coupon.getGeoLon()));
            }
        }

        ActivityCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        binding.setCoupon(coupon);
        binding.setHandler(this);

        parentLayout = (LinearLayout) findViewById(R.id.couponParentLayout);


    }

    private void initSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sp.setOnLoadCompleteListener(this);

        try {
            soundIdWin = sp.load(getAssets().openFd("winning.mp3"), 1);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (sp != null) {
            sp.release();
            sp = null;
        }
        super.onDestroy();
    }

    private void redeem() {
        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_REDEEMED);
        Constants.DB_COUPON.child(coupon.getCode()).child("redeemed").setValue(System.currentTimeMillis());
        onBackPressed();
    }

    private void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_redeem, null);

        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        ((ImageButton) view.findViewById(R.id.couponPopUpClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ((TextView) view.findViewById(R.id.couponPopUpNot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ((Button) view.findViewById(R.id.couponPopUpRedeem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                redeem();
            }
        });

        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }


    @Override
    public void send(View view) {
        Intent intent = new Intent(this, SendCouponActivity.class);
        intent.putExtra("couponCode", String.format(getString(R.string.send_coupon_text), coupon.getCode()));
        startActivity(intent);
    }

    @Override
    public void unlock(View view) {

    }

    @Override
    public void more(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.PLACE_KEY, coupon.getPlaceKey());
        startActivity(intent);
    }

    @Override
    public void redeem(View view) {
        if (!checkLogin()) {
            startActivity(new Intent(this, ChooseAccountActivity.class));
        } else {
            if (coupon.getStatus() == 0) {
                Intent intent = new Intent(this, UnlockActivity.class);
                intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
                startActivity(intent);
                finish();
            } else {
                showPopUp();
            }
        }
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (userPrize) {
            sp.play(soundIdWin, 1, 1, 0, 0, 1);
        }
    }
}
