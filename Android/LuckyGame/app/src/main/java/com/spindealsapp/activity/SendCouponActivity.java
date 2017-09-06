package com.spindealsapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.spindealsapp.Constants;
import com.spindealsapp.binding.handler.SendHandler;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivitySendCouponBinding;

public class SendCouponActivity extends BaseActivity implements SendHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coupon);
        ActivitySendCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_send_coupon);
        binding.setHandler(this);

        switch (getIntent().getIntExtra("type", 0)) {
            case 0:
                binding.setTitle(R.string.send_title);
                binding.setText(R.string.send_text);
                binding.setBtnText(R.string.send_btn);
                return;
            case 1:
                binding.setTitle(R.string.important);
                binding.setText(R.string.get_coupon_text);
                binding.setBtnText(R.string.ok);
                return;
        }
    }

    @Override
    public void send(View view) {
        if (getIntent().getIntExtra("type", 0) == 0) {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(
                    Intent.EXTRA_TEXT,
                    getIntent().getStringExtra(Constants.COUPON_KEY));
            startActivity(Intent.createChooser(send, getString(R.string.send_chooser_title)));
        } else {
            Intent intent = new Intent(this, SlideCouponsActivity.class);
            intent.putExtra(Constants.COUPON_KEY, getIntent().getStringExtra(Constants.COUPON_KEY));
            startActivity(intent);
        }

        finish();
    }
}
