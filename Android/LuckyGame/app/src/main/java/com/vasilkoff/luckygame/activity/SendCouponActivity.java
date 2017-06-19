package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.SendHandler;
import com.vasilkoff.luckygame.databinding.ActivitySendCouponBinding;

public class SendCouponActivity extends BaseActivity implements SendHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coupon);

        ActivitySendCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_send_coupon);
        binding.setHandler(this);
    }

    @Override
    public void send(View view) {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(
                Intent.EXTRA_TEXT,
                getIntent().getStringExtra("couponCode"));
        startActivity(Intent.createChooser(send, getString(R.string.send_chooser_title)));
        finish();
    }
}
