package com.vasilkoff.luckygame.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Promotion;
import com.vasilkoff.luckygame.fragment.ActiveCompaniesFragment;
import com.vasilkoff.luckygame.fragment.AllCompaniesFragment;
import com.vasilkoff.luckygame.fragment.CouponsFragment;
import com.vasilkoff.luckygame.service.LocationService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActiveCompaniesFragment activeCompaniesFragment;
    private CouponsFragment couponsFragment;
    private AllCompaniesFragment allCompaniesFragment;

    private List<Company> allCompanyList;
    private List<Company> activeCompanyListInfo;
    private static boolean showPopUpLogin = true;

    private TabLayout tabLayout;
    private String[] companyTypeNames;
    private AppBarLayout appBarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        companyTypeNames = getResources().getStringArray(R.array.company_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        activeCompaniesFragment = new ActiveCompaniesFragment();
        couponsFragment = new CouponsFragment();
        allCompaniesFragment = new AllCompaniesFragment();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        allCompaniesFragment.refreshList();
                        break;
                    case 1:
                        activeCompaniesFragment.refreshList();
                        break;
                    /*case 2:
                        couponsFragment.refreshList();
                        break;*/
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.customTabText))
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabTextSelected));
                if (tab.getPosition() == 1) {
                    appBarLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cover_small));
                    TextView customTabCount = (TextView) tab.getCustomView().findViewById(R.id.customTabCount);
                    customTabCount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabTextSelected));
                    customTabCount.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_count_active));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.customTabText))
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabText));
                if (tab.getPosition() == 1) {
                    appBarLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cover));
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

        /* try {
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

        dbData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    updateData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        if (!checkLogin() && showPopUpLogin) {
            showPopUpLogin = false;
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
        //startActivity(new Intent(this, ChooseAccountActivity.class));
    }

    private void setCountActiveCompanies(int count) {
        TextView customTabCount = (TextView) tabLayout.getTabAt(1).getCustomView().findViewById(R.id.customTabCount);
        customTabCount.setVisibility(View.VISIBLE);
        customTabCount.setText(String.valueOf(count));
        customTabCount.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_count));
    }

    private void updateData(DataSnapshot dataSnapshot) {
        companies = new HashMap<String, Map<String, Promotion>>();
        allCompanyList = new ArrayList<Company>();
        activeCompanyListInfo = new ArrayList<Company>();
        Set<String> uniquePlacesNames = new HashSet<>();

        for (DataSnapshot company : dataSnapshot.child("companies").getChildren()) {
            allCompanyList.add(
                    new Company(
                            company.getKey(),
                            company.child("name").exists() ? company.child("name").getValue().toString() : company.getKey(),
                            company.child("info").exists() ? company.child("info").getValue().toString() : null,
                            company.child("logo").exists() ? company.child("logo").getValue().toString() : null));
            Map<String, Promotion> promotions = new HashMap<String, Promotion>();
            for (DataSnapshot promotion : company.child("promo").getChildren()) {
                if (promotion.child("active").getValue().equals(true)) {
                    Promotion promotionValue = promotion.getValue(Promotion.class);
                    promotions.put(promotion.getKey(), promotionValue);

                    if (promotionValue.getListPlaces() != null) {
                        for (int i = 0; i < promotionValue.getListPlaces().size(); i++) {
                            uniquePlacesNames.add(promotionValue.getListPlaces().get(i));
                        }
                    }
                }
            }
            if (promotions.size() > 0) {
                companies.put(company.getKey(), promotions);
                activeCompanyListInfo.add(
                        new Company(
                                company.getKey(),
                                company.child("name").exists() ? company.child("name").getValue().toString() : company.getKey(),
                                company.child("info").exists() ? company.child("info").getValue().toString() : null,
                                company.child("logo").exists() ? company.child("logo").getValue().toString() : null));
            }
        }

        setCountActiveCompanies(companies.size());

        activeCompaniesFragment.setCompanies(companies, activeCompanyListInfo);
        if (activeCompaniesFragment.isVisible())
            activeCompaniesFragment.refreshList();

        allCompaniesFragment.setCompanies(allCompanyList);
        if (allCompaniesFragment.isVisible())
            allCompaniesFragment.refreshList();

        GenericTypeIndicator<Map<String, Place>> type = new GenericTypeIndicator<Map<String, Place>>() {};
        Map<String, Place> places = dataSnapshot.child("places").getValue(type);
        uniquePlaces = new ArrayList<Place>();

        for (String placeName : uniquePlacesNames) {
            uniquePlaces.add(places.get(placeName));
        }

        dbHelper.savePlaces(uniquePlaces);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        } else {
            startService(new Intent(this, LocationService.class));
        }

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
            default:
                Toast.makeText(this, R.string.next_version, Toast.LENGTH_SHORT).show();
        }

    }

}
