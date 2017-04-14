package com.vasilkoff.luckygame.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                    case 2:
                        couponsFragment.refreshList();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, LocationService.class));
        }
    }

}
