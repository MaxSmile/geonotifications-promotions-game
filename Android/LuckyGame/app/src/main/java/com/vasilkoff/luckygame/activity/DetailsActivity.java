package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.DetailsHandler;
import com.vasilkoff.luckygame.databinding.ActivityDetailsBinding;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailsActivity extends BaseActivity implements DetailsHandler {

    private Company company;
    private Place place;
    private Spin spin;
    private ActivityDetailsBinding binding;
    private HashMap<String, Gift> gifts = new HashMap<String, Gift>();
    private ImageButton detailsBtnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());

        binding = DataBindingUtil.setContentView(DetailsActivity.this, R.layout.activity_details);
        binding.setHandler(this);

        Constants.dbPlace.child(getIntent().getStringExtra(Place.class.getCanonicalName())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                place = dataSnapshot.getValue(Place.class);
                place.setTypeName(Constants.companyTypeNames[place.getType()]);
                TypedArray iconArray = getResources().obtainTypedArray(R.array.company_type_icons);
                place.setTypeIcon(iconArray.getResourceId(place.getType(), 0));
                iconArray.recycle();
                setGift();
                setBindingData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  detailsBtnPlay = (ImageButton) findViewById(R.id.detailsBtnPlay);
        RotateAnimation rotateAnim = new RotateAnimation(0f, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setFillAfter(true);

        detailsBtnPlay.startAnimation(rotateAnim);*/
    }

    private void setGift() {
        List<Box> boxes = place.getBox();
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            Constants.dbGift.child(box.getGift()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Gift gift = dataSnapshot.getValue(Gift.class);
                    if (gift.getDateStart() < System.currentTimeMillis() && gift.getDateFinish() > System.currentTimeMillis())
                        gifts.put(gift.getId(), gift);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setBindingData() {
        Constants.dbCompany.child(place.getCompanyKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                company = dataSnapshot.getValue(Company.class);
                binding.setCompany(company);
                binding.setPlace(place);
                binding.setCountGift(gifts.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickDetails(View view) {
        switch (view.getId()) {
            case R.id.companyDetailsCall:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsInfo:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsDirections:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
            case R.id.companyDetailsWeb:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void showGifts(View view) {
        Intent intent = new Intent(this, LegendActivity.class);
        intent.putExtra(Place.class.getCanonicalName(), place);
        intent.putExtra(Company.class.getCanonicalName(), company);
        intent.putExtra(Gift.class.getCanonicalName(), gifts);
        startActivity(intent);
    }

    @Override
    public void goToPlay(View view) {
        if (!checkLogin()) {
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
        if (spin != null) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(Place.class.getCanonicalName(), place);
            intent.putExtra(Spin.class.getCanonicalName(), spin);
            intent.putExtra(Company.class.getCanonicalName(), company);
            intent.putExtra(Gift.class.getCanonicalName(), gifts);
            startActivity(intent);
        }
    }

    @Override
    public void getExtraSpin(View view) {
        Intent intent = new Intent(this, ExtraSpinActivity.class);
        intent.putExtra(Place.class.getCanonicalName(), place);
        intent.putExtra(Spin.class.getCanonicalName(), spin);
        intent.putExtra(Company.class.getCanonicalName(), company);
        startActivity(intent);
    }
}
