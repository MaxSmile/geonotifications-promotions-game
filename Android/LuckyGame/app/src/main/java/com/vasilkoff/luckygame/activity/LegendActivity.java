package com.vasilkoff.luckygame.activity;


import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityLegendBinding;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.util.DateFormat;


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

        List<Box> boxes = place.getBox();

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

            String dateFinish = String.format(getResources().getString(R.string.finish_on),
                    DateFormat.getDate("dd/MM/yyyy", gift.getDateFinish()));
            ((TextView)rowLayout.findViewById(R.id.legendRowSubText))
                    .setText(dateFinish);
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
