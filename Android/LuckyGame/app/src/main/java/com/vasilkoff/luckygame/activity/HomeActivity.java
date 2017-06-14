package com.vasilkoff.luckygame.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.fragment.ActiveCompaniesFragment;
import com.vasilkoff.luckygame.fragment.AllCompaniesFragment;
import com.vasilkoff.luckygame.fragment.CouponsFragment;
import com.vasilkoff.luckygame.fragment.DataBridge;
import com.vasilkoff.luckygame.service.LocationService;


import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends BaseActivity implements DataBridge {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActiveCompaniesFragment activeCompaniesFragment;
    private CouponsFragment couponsFragment;
    private AllCompaniesFragment allCompaniesFragment;

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ImageView logo;
    private ImageView logoSmall;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkNetwork();

        logo = (ImageView) findViewById(R.id.homeLogo);
        logoSmall = (ImageView) findViewById(R.id.homeLogoSmall);

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
       /* mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        activeCompaniesFragment.refreshList();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

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
                    "com.vasilkoff.luckygame",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        updateGeoPlaces();
    }

    @Override
    public void resultSpins(ArrayList<Spin> spins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        super.resultSpins(spins, places, companies);
        activeCompaniesFragment.refreshList(spins, places, companies);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (showPopUpLogin && !checkLogin()) {
            showPopUpLogin = false;
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
        getSpins();
    }

    private void updateGeoPlaces() {
        Constants.DB_PLACE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Place> placeList = new ArrayList<Place>();
                for (DataSnapshot dataPlace : dataSnapshot.getChildren()) {
                    Place place = dataPlace.getValue(Place.class);
                    if (place.getGeoTimeStart() < System.currentTimeMillis() && place.getGeoTimeFinish() > System.currentTimeMillis()) {
                        placeList.add(place);
                    }
                }
                dbHelper.savePlaces(placeList);
                startGeoService();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void onHomeClick(View view) {
        switch (view.getId()) {
            case R.id.homeSetting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.companyAll:
                goToCategory(-1);
                break;
            case R.id.companyFood:
                goToCategory(0);
                break;
            case R.id.companyNightlife:
                goToCategory(1);
                break;
            case R.id.companyCoffee:
                goToCategory(2);
                break;
            case R.id.companyEvents:
                goToCategory(3);
                break;
            case R.id.companyShops:
                goToCategory(4);
                break;
            case R.id.companyServices:
                goToCategory(5);
                break;
            case R.id.companyEShops:
                goToCategory(6);
                break;
            default:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
        }

    }

    private void goToCategory(int type) {
        Intent intent = new Intent(this, FilteredCompanyActivity.class);
        intent.putExtra(Constants.PLACE_TYPE_KEY, type);
        startActivity(intent);
    }


}
