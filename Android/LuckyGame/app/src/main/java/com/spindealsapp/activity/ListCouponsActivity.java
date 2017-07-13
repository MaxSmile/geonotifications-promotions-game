package com.spindealsapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.spindealsapp.R;

public class ListCouponsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_coupons);

        final RecyclerView couponsList = (RecyclerView) findViewById(R.id.couponsList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        couponsList.setLayoutManager(llm);
        //couponsList.setAdapter(new CouponListAdapter(this, dbHelper.getCouponsList()));
    }
}
