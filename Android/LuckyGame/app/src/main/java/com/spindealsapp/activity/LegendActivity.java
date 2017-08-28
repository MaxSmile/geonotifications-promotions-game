package com.spindealsapp.activity;


import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityLegendBinding;

import java.util.HashMap;
import java.util.List;


public class LegendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);

        Place place = getIntent().getParcelableExtra(Place.class.getCanonicalName());
        Company company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
        HashMap<String, Gift> gifts = (HashMap<String, Gift>)(getIntent().getSerializableExtra(Gift.class.getCanonicalName()));

        List<Box> boxes = place.getSpin().getBox();

        ActivityLegendBinding binding = DataBindingUtil.setContentView(LegendActivity.this, R.layout.activity_legend);
        binding.setCompany(company);
        binding.setPlace(place);

        final LinearLayout containerLayout = (LinearLayout) findViewById(R.id.legendContainer);

        TypedArray images = getResources().obtainTypedArray(R.array.box_type);
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);

            Gift gift = gifts.get(box.getGift());

            LinearLayout rowLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.gift_row, null);
            ((ImageView) rowLayout.findViewById(R.id.legendRowImg))
                    .setImageResource(images.getResourceId(box.getColor(), -1));
            ((TextView)rowLayout.findViewById(R.id.legendRowText))
                    .setText(gift.getDescription());

            String subText;
            if (gift.getCountAvailable() < 10) {
                subText = String.format(getResources().getString(R.string.gifts_left),
                        gift.getCountAvailable());
            } else {
                subText = String.format(getResources().getString(R.string.finish_on),
                        DateFormat.getDate("dd/MM/yyyy", System.currentTimeMillis() + gift.getExpirationTime()));
            }

            ((TextView)rowLayout.findViewById(R.id.legendRowSubText))
                    .setText(subText);
            containerLayout.addView(rowLayout);


        }
        images.recycle();

        ((ImageButton)findViewById(R.id.legendClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
