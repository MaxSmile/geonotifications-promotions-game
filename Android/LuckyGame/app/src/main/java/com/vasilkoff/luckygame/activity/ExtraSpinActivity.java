package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.ExtraHandler;
import com.vasilkoff.luckygame.databinding.ActivityExtraSpinBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

public class ExtraSpinActivity extends BaseActivity implements ExtraHandler {

    private Company company;
    private Place place;
    private Spin spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_spin);

        place = getIntent().getParcelableExtra(Place.class.getCanonicalName());
        company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());

        ActivityExtraSpinBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_extra_spin);
        binding.setCompany(company);
        binding.setPlace(place);
        binding.setHandler(this);

        ((TextView)findViewById(R.id.unlockLikeText))
                .setText(String.format(getString(R.string.unlock_facebook_like), place.getName()));
        ((TextView)findViewById(R.id.unlockCheckInText))
                .setText(String.format(getString(R.string.unlock_facebook_check_in), place.getName()));
    }
}
