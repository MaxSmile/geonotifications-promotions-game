package com.spindealsapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spindealsapp.App;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.binding.handler.HomeHandler;
import com.spindealsapp.common.Filters;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.fragment.ActiveCompaniesFragment;
import com.spindealsapp.fragment.AllCompaniesFragment;
import com.spindealsapp.fragment.CouponsFragment;
import com.spindealsapp.fragment.DataBridge;
import com.spindealsapp.service.LocationService;
import com.spindealsapp.util.LocationState;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends BaseActivity implements DataBridge, HomeHandler {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActiveCompaniesFragment activeCompaniesFragment;
    private CouponsFragment couponsFragment;
    private AllCompaniesFragment allCompaniesFragment;

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ImageView logo;
    private ImageView logoSmall;
    private ImageView addCouponBtn;
    private ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setHandler(this);

        logo = (ImageView) findViewById(R.id.homeLogo);
        logoSmall = (ImageView) findViewById(R.id.homeLogoSmall);
        addCouponBtn = (ImageView) findViewById(R.id.addCouponBtn);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        activeCompaniesFragment = new ActiveCompaniesFragment();
        couponsFragment = new CouponsFragment();
        allCompaniesFragment = new AllCompaniesFragment();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.customTabText))
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabTextSelected));
                if (tab.getPosition() == 1) {
                    TextView customTabCount = (TextView) tab.getCustomView().findViewById(R.id.customTabCount);
                    customTabCount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabTextSelected));
                    customTabCount.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_count_active));
                }

                if (tab.getPosition() == 0) {
                    appBarLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cover));

                    logoSmall.setVisibility(View.GONE);
                    logo.setVisibility(View.VISIBLE);
                } else {
                    appBarLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cover_small));
                    logo.setVisibility(View.GONE);
                    logoSmall.setVisibility(View.VISIBLE);
                }

                if (tab.getPosition() == 2) {
                    addCouponBtn.setVisibility(View.VISIBLE);
                } else {
                    addCouponBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.customTabText))
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabText));
                if (tab.getPosition() == 1) {
                    TextView customTabCount = (TextView) tab.getCustomView().findViewById(R.id.customTabCount);
                    customTabCount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabText));
                    customTabCount.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_count));
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }

         /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.spindealsapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        if (DBHelper.getInstance(App.getInstance()).getPlaces().size() == 0) {
            checkNetwork();
        }

        FirebaseData.loadData();

        if (Properties.getShowTutorial()) {
            Properties.setShowTutorial(false);
            startActivity(new Intent(this, TutorialActivity.class));
        }

        startGeoService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseData.getCoupons();

        if (showPopUpLogin && !checkLogin()) {
            showPopUpLogin = false;
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }

        binding.setFilterNearMe(Filters.nearMe);
        binding.setFiltersCount(Filters.count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Filters.FILTER_CODE) {
            if (resultCode == RESULT_OK)
                filterData();
        }
    }

    private void startGeoService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        } else {
            startService(new Intent(this, LocationService.class));
        }
    }

    @Override
    public void activeSpins(int count) {
        TextView customTabCount = (TextView) tabLayout.getTabAt(1).getCustomView().findViewById(R.id.customTabCount);
        customTabCount.setVisibility(View.VISIBLE);
        customTabCount.setText(String.valueOf(count));
    }

    @Override
    public void filterNearMe(View view) {
        if (!LocationState.isEnabled()) {
            startActivity(new Intent(this, LocationActivity.class));
        }
        if (CurrentLocation.lat != 0 ) {
                Filters.nearMe = !Filters.nearMe;
                binding.setFilterNearMe(Filters.nearMe);
                filterData();
        } else {
            Toast.makeText(this, R.string.unknown_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void filters(View view) {
        startActivityForResult(new Intent(this, FilterActivity.class), Filters.FILTER_CODE);
    }

    @Override
    public void search(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void settings(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void addCoupon(View view) {
        startActivity(new Intent(this, InputCouponActivity.class));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return allCompaniesFragment;
                case 1:
                    return activeCompaniesFragment;
                case 2:
                    return couponsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.fragment_companies_title);
                case 1:
                    return getString(R.string.fragment_active_companies_title);
                case 2:
                    return getString(R.string.fragment_coupons_title);
            }
            return null;
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(HomeActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.customTabText);
            if (position == 0)
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabTextSelected));
            tv.setText(getPageTitle(position));
            return tab;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, LocationService.class));
        }
    }

    private void filterData() {
        EventBus.getDefault().post(new Events.UpdateFilter());
    }
}