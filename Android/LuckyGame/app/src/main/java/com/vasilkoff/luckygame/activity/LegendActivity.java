package com.vasilkoff.luckygame.activity;


import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityLegendBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Promotion;
import com.vasilkoff.luckygame.util.DateFormat;

import java.util.HashMap;
import java.util.Map;

public class LegendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);

        HashMap<String, Promotion> promotions = (HashMap<String, Promotion>)getIntent().getSerializableExtra(Promotion.class.getCanonicalName());
        Company company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
        TypedArray images = getResources().obtainTypedArray(R.array.box_type);

        ActivityLegendBinding binding = DataBindingUtil.setContentView(LegendActivity.this, R.layout.activity_legend);
        binding.setCompany(company);

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.legendContainer);

        for (Map.Entry<String, Promotion> promotion : promotions.entrySet()) {
            LinearLayout rowLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.gift_row, null);
            ((ImageView) rowLayout.findViewById(R.id.legendRowImg))
                    .setImageResource(images.getResourceId(promotion.getValue().getColorBox(), -1));
            ((TextView)rowLayout.findViewById(R.id.legendRowText))
                    .setText(promotion.getValue().getName());

            String dateFinish = String.format(getResources().getString(R.string.finish_on),
                    DateFormat.getDate("dd/MM/yyyy", promotion.getValue().getDateFinish()));
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
