package com.spindealsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.binding.handler.DetailsHandler;
import com.spindealsapp.common.Helper;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.GiftServiceLayer;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.NetworkState;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityDetailsBinding;
import com.spindealsapp.util.Rrule;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends BaseActivity implements DetailsHandler {

    private ActivityDetailsBinding binding;

    private RotateAnimation rotateAnim;
    private ImageView detailsBtnPlay;
    private SliderLayout slider;
    private ExpandableLayout expandableLayout;
    private ExpandableLayout expandableLayoutOther;
    private ImageView detailsArrow;
    private ImageView detailsArrowOther;
    private boolean geoNotification;
    private int countCoupons;
    private HashMap<String, Place> otherPlacesCompany;
    private ArrayList<String> otherList;
    private ListView detailsOtherPlaces;
    private List<Box> boxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        geoNotification = getIntent().getBooleanExtra("geoNotification", false);

        binding = DataBindingUtil.setContentView(DetailsActivity.this, R.layout.activity_details);
        binding.setHandler(this);

        detailsOtherPlaces = (ListView)findViewById(R.id.detailsOtherPlaces);

        detailsBtnPlay = (ImageView) findViewById(R.id.detailsBtnPlay);
        detailsArrow = (ImageView)findViewById(R.id.detailsArrow);
        detailsArrowOther = (ImageView)findViewById(R.id.detailsArrowOther);
        rotateAnim = new RotateAnimation(0f, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setDuration(2000);

        slider = (SliderLayout) findViewById(R.id.detailsSlider);

        expandableLayoutOther = (ExpandableLayout) findViewById(R.id.expandableLayoutOther);
        expandableLayoutOther.collapse();
        expandableLayout = (ExpandableLayout) findViewById(R.id.expandableLayout);
        expandableLayout.collapse();

        if (isTaskRoot()) {
            loadData();
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        place = PlaceServiceLayer.getPlace(getIntent().getStringExtra(Constants.PLACE_KEY));
        boxes = place.getSpin().getBox();
        if (place.getCompanyKey() != null) {
            gifts = GiftServiceLayer.getGifts(place);
            Iterator<Box> iterator = boxes.iterator();
            while (iterator.hasNext()) {
                Box box = iterator.next();
                Gift gift = gifts.get(box.getGift());
                if (gift == null || !gift.isActive()) {
                    iterator.remove();
                }
            }

            binding.setCountGift(boxes.size());
            initSlider();
            company = DBHelper.getInstance(this).getCompany(place.getCompanyKey());
            countCoupons = DBHelper.getInstance(this).getCouponsByPlace(place.getId()).size();
            binding.setCountCoupons(countCoupons);
            binding.setPlace(place);
            binding.setCompany(company);
            if ((place.getSpin().isAvailable() || geoNotification) && place.getSpin().getBox().size() > 0) {
                detailsBtnPlay.startAnimation(rotateAnim);
            } else {
                detailsBtnPlay.clearAnimation();
            }
            initOther();
        } else {
            onBackPressed();
        }

    }

    private void initOther() {
        otherPlacesCompany = DBHelper.getInstance(this).getOtherPlacesCompany(place.getCompanyKey(), place.getId());
        otherList = new ArrayList<String>(otherPlacesCompany.keySet());

        ArrayAdapter arrayFilterAdapter = new ArrayAdapter<String>(
                this, R.layout.my_simple_list_item_1, otherList);
        detailsOtherPlaces.setAdapter(arrayFilterAdapter);
        Helper.getListViewSize(detailsOtherPlaces);
        detailsOtherPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String placeId = otherPlacesCompany.get(otherList.get(position)).getId();
                Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                intent.putExtra(Constants.PLACE_KEY, placeId );
                startActivity(intent);
                finish();
            }
        });
        binding.setOther(otherPlacesCompany.size() > 0);
    }

    @Override
    public void back(View view) {
        if (isTaskRoot()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            onBackPressed();
        }

    }

    @Override
    public void showGifts(View view) {
        if (boxes.size() > 0) {
            Intent intent = new Intent(this, LegendActivity.class);
            intent.putExtra(Place.class.getCanonicalName(), place);
            intent.putExtra(Company.class.getCanonicalName(), company);
            intent.putExtra(Gift.class.getCanonicalName(), gifts);
            startActivity(intent);
        }
    }

    @Override
    public void goToPlay(View view) {
        if (NetworkState.isOnline()) {
            if (place.getSpin().getStatus() != Constants.SPIN_STATUS_COMING || geoNotification) {
                if (boxes.size() > 0) {
                    startGame();
                } else {
                    Toast.makeText(this, R.string.gifts_over, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.spin_coming_message, Toast.LENGTH_LONG).show();
            }
        } else {
            startActivity(new Intent(this, NetworkActivity.class));
        }

    }

    private void startGame() {
        if (CurrentUser.user != null) {
            if (place.getSpin().isAvailable() || (geoNotification && Rrule.isAvailable(place.getSpin().getRrule()))) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(Company.class.getCanonicalName(), company);
                intent.putExtra(Gift.class.getCanonicalName(), gifts);

                int type = Constants.SPIN_TYPE_NORMAL;
                if (geoNotification) {
                    place.getSpin().setExtraAvailable(false);
                    type = Constants.SPIN_TYPE_EXTRA;
                    geoNotification = false;
                }
                intent.putExtra(Constants.SPIN_TYPE_KEY, type);
                intent.putExtra(Place.class.getCanonicalName(), place);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.spin_not_available, Toast.LENGTH_LONG).show();
            }
        } else {
            loginDialog();
        }
    }

    private void loginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.login_dialog_title))
                .setMessage(getString(R.string.login_dialog_message))
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(DetailsActivity.this, ChooseAccountActivity.class));
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
        if (binding.getPlace().isInfoChecked() || place.getInfoTimestamp() == 0) {
            Toast.makeText(this, R.string.info_empty_message, Toast.LENGTH_SHORT).show();
        } else {
            place.setInfoChecked(true);
            binding.setPlace(place);
            DBHelper.getInstance(this).updatePlace(place);

            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("info", place.getInfo());
            intent.putExtra("title", place.getName());
            startActivity(intent);
        }
    }

    @Override
    public void web(View view) {
        startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse(place.getUrl())));
    }

    @Override
    public void directions(View view) {
        if (CurrentLocation.lat != 0 && place.getGeoLat() != 0 && place.getGeoLon() != 0 && NetworkState.isOnline()) {

            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="
                    + place.getGeoLat() + ","
                    + place.getGeoLon() + "&daddr="
                    + CurrentLocation.lat +","
                    + CurrentLocation.lon;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(Intent.createChooser(intent, getString(R.string.select_maps_app)));

        } else {
            String uri = String.format(Locale.getDefault(), "geo:%f,%f", place.getGeoLat(), place.getGeoLon());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(Intent.createChooser(intent, getString(R.string.select_maps_app)));
            //Toast.makeText(this, R.string.unknown_location, Toast.LENGTH_LONG).show();
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
    public void showOther(View view) {
        expandableLayoutOther.toggle();
        if (expandableLayoutOther.isExpanded()) {
            detailsArrowOther.setImageResource(R.drawable.arrow_down);
        } else {
            detailsArrowOther.setImageResource(R.drawable.arrow);
        }
    }

    @Override
    public void favorites(View view) {
        super.favorites(view);
        binding.setPlace(place);
    }

    @Override
    public void getExtraSpin(View view) {
        if (NetworkState.isOnline()) {
            if (CurrentUser.user != null) {
                if (place.getSpin().getStatus() != Constants.SPIN_STATUS_COMING) {
                    if (place.getSpin().isExtraAvailable()) {
                        Intent intent = new Intent(this, ExtraSpinActivity.class);
                        intent.putExtra(Place.class.getCanonicalName(), place);
                        intent.putExtra(Company.class.getCanonicalName(), company);
                        intent.putExtra(Gift.class.getCanonicalName(), gifts);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.extra_spin_not_available, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.spin_coming_message, Toast.LENGTH_LONG).show();
                }
            } else {
                loginDialog();
            }
        } else {
            startActivity(new Intent(this, NetworkActivity.class));
        }
    }

    @Override
    public void showCoupons(View view) {
        if (countCoupons > 0) {
            Intent intent = new Intent(this, SlideCouponsActivity.class);
            intent.putExtra(Constants.PLACE_KEY, place.getId());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePlaces(Events.UpdatePlaces updatePlaces) {
        refreshData();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateSpinAvailable(Events.UpdateSpinAvailable updateSpinAvailable) {
        refreshData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
