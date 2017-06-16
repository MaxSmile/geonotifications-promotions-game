package com.vasilkoff.luckygame.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.DetailsHandler;
import com.vasilkoff.luckygame.databinding.ActivityDetailsBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.entity.UsedSpin;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class DetailsActivity extends BaseActivity implements DetailsHandler {

    private Spin spin;
    private ActivityDetailsBinding binding;


    private boolean spinAvailable;
    private boolean extraSpinAvailable;

    private RotateAnimation rotateAnim;
    private ImageView detailsBtnPlay;
    private SliderLayout slider;
    private ExpandableLayout expandableLayout;
    private ImageView detailsArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        checkNetwork();

        result = false;

        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());

        binding = DataBindingUtil.setContentView(DetailsActivity.this, R.layout.activity_details);
        binding.setSpin(spin);
        binding.setHandler(this);

        getDataByPlace(getIntent().getStringExtra(Constants.PLACE_KEY));

        detailsBtnPlay = (ImageView) findViewById(R.id.detailsBtnPlay);
        detailsArrow = (ImageView)findViewById(R.id.detailsArrow);
        rotateAnim = new RotateAnimation(0f, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setDuration(2000);

        slider = (SliderLayout) findViewById(R.id.detailsSlider);
        expandableLayout
                = (ExpandableLayout) findViewById(R.id.expandableLayout);
        expandableLayout.collapse();

    }

    private void initSlider() {
        slider.removeAllSliders();
        List<String> gallery = place.getGallery();
        if (gallery.size() > 0) {
            for (int i = 0; i < gallery.size(); i++) {
                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView
                        .image(gallery.get(i))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                slider.addSlider(sliderView);
            }

            slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.startAutoCycle();
            slider.setSliderTransformDuration(1000, new LinearInterpolator());
            slider.setDuration(10000);
        }
    }

    @Override
    protected void onDestroy() {
        if (slider != null)
            slider.stopAutoCycle();

        super.onDestroy();
    }

    private void checkSpinAvailable() {
        if (user != null) {
            long timeShift = System.currentTimeMillis() - Constants.DAY_TIME_SHIFT;
            Constants.DB_USER.child(user.getId()).child("place").child(place.getId())
                    .orderByChild("time").startAt(timeShift).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    spinAvailable = true;
                    extraSpinAvailable = true;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        UsedSpin usedSpin = data.getValue(UsedSpin.class);
                        if (usedSpin.getType() == Constants.SPIN_TYPE_NORMAL) {
                            spinAvailable = false;
                        }

                        if (usedSpin.getType() == Constants.SPIN_TYPE_EXTRA) {
                            extraSpinAvailable = false;
                        }
                    }
                    if (spinAvailable && gifts.size() > 0) {
                        detailsBtnPlay.startAnimation(rotateAnim);
                    } else {
                        detailsBtnPlay.clearAnimation();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (result) {
            checkSpinAvailable();
        }
    }

    @Override
    public void resultDataByPlace() {
        binding.setCompany(company);
        binding.setPlace(place);
        binding.setCountGift(gifts.size());
        checkSpinAvailable();
        result = true;
        initSlider();
    }

    @Override
    public void showGifts(View view) {
        if (gifts.size() > 0) {
            Intent intent = new Intent(this, LegendActivity.class);
            intent.putExtra(Place.class.getCanonicalName(), place);
            intent.putExtra(Company.class.getCanonicalName(), company);
            intent.putExtra(Gift.class.getCanonicalName(), gifts);
            startActivity(intent);
        }
    }

    @Override
    public void goToPlay(View view) {
        checkNetwork();
        if (spin.getStatus() != Constants.SPIN_STATUS_COMING) {
            if (user != null) {
                if (checkResult()) {
                    if (spinAvailable || getIntent().getBooleanExtra("geoNotification", false)) {
                        Intent intent = new Intent(this, GameActivity.class);
                        intent.putExtra(Place.class.getCanonicalName(), place);
                        intent.putExtra(Spin.class.getCanonicalName(), spin);
                        intent.putExtra(Company.class.getCanonicalName(), company);
                        intent.putExtra(Gift.class.getCanonicalName(), gifts);
                        intent.putExtra("extraSpinAvailable", extraSpinAvailable);
                        intent.putExtra(Constants.SPIN_TYPE_KEY, Constants.SPIN_TYPE_NORMAL);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.spin_not_available, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                startActivity(new Intent(this, ChooseAccountActivity.class));
            }
        } else {
            Toast.makeText(this, R.string.spin_coming_message, Toast.LENGTH_LONG).show();
        }

    }

    @SuppressWarnings({"MissingPermission"})
    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + place.getTel()));
        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCall();
        }
    }

    @Override
    public void call(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 100);
            }
        } else {
            startCall();
        }
    }

    @Override
    public void info(View view) {
        /*Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("info", place.getInfo());
        intent.putExtra("title", place.getName());
        startActivity(intent);*/
        Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void web(View view) {
        startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse(place.getUrl())));
    }

    @Override
    public void directions(View view) {
        if (CurrentLocation.lat != 0 && place.getGeoLat() != 0 && place.getGeoLon() != 0) {
            String destination = place.getGeoLat() + "," + place.getGeoLon();
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("destination", destination);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.unknown_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void moreInfo(View view) {
        expandableLayout.toggle();
        if (expandableLayout.isExpanded()) {
            detailsArrow.setImageResource(R.drawable.arrow_down);
        } else {
            detailsArrow.setImageResource(R.drawable.arrow);
        }
    }

    @Override
    public void getExtraSpin(View view) {
        if (spin.getStatus() != Constants.SPIN_STATUS_COMING) {
            if (extraSpinAvailable) {
                Intent intent = new Intent(this, ExtraSpinActivity.class);
                intent.putExtra(Constants.PLACE_KEY, place.getId());
                intent.putExtra(Spin.class.getCanonicalName(), spin);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.extra_spin_not_available, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.spin_coming_message, Toast.LENGTH_LONG).show();
        }

    }
}
