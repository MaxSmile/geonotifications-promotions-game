package com.vasilkoff.luckygame.activity;


import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;

import com.vasilkoff.luckygame.common.Properties;
import com.vasilkoff.luckygame.database.FirebaseData;
import com.vasilkoff.luckygame.database.ServiceLayer;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.eventbus.Events;
import com.vasilkoff.luckygame.fragment.CouponFragment;
import com.vasilkoff.luckygame.util.DateFormat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;


public class SlideCouponsActivity extends BaseActivity implements SoundPool.OnLoadCompleteListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FloatingActionButton fabPrev;
    private FloatingActionButton fabNext;
    private List<CouponExtension> coupons;

    private TextView slideCount;
    private TextView slideCurrent;
    private LinearLayout slidePagination;

    private boolean userPrize = false;
    private SoundPool sp;
    private int soundIdWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_coupons);

        slideCount = (TextView)findViewById(R.id.slideCount);
        slideCurrent = (TextView)findViewById(R.id.slideCurrent);
        slidePagination = (LinearLayout)findViewById(R.id.slidePagination);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                checkPosition(position);
                slideCurrent.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fabPrev = (FloatingActionButton) findViewById(R.id.fabPrev);
        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
            }
        });

        fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() < mSectionsPagerAdapter.getCount()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
            }
        });

        userPrize = getIntent().getBooleanExtra("userPrize", false);
        initSound();

    }

    private void initSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sp.setOnLoadCompleteListener(this);

        try {
            soundIdWin = sp.load(getAssets().openFd(getString(R.string.winning_sound)), 1);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void checkPosition(int position) {
        if (position == mSectionsPagerAdapter.getCount() - 1) {
            fabNext.setVisibility(View.GONE);
        } else {
            fabNext.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            fabPrev.setVisibility(View.GONE);
        } else {
            fabPrev.setVisibility(View.VISIBLE);
        }
    }

    private void initFragments() {
        if (getIntent().getStringExtra(Constants.PLACE_KEY) != null) {
            coupons = ServiceLayer.getCouponsByPlace(getIntent().getStringExtra(Constants.PLACE_KEY));
        }

        if (getIntent().getStringExtra(Constants.COUPON_KEY) != null) {
            coupons = ServiceLayer.getCouponsByCode(getIntent().getStringExtra(Constants.COUPON_KEY));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (coupons.size() == 1) {
            fabNext.setVisibility(View.GONE);
            slidePagination.setVisibility(View.GONE);
        }

        slideCount.setText(String.valueOf(coupons.size()));
    }

    /*private void updateFragments() {
        coupons = ServiceLayer.getCoupons();
        System.out.println("myTest name---------- = " + coupons.get(0).getPlaceName());
        System.out.println("myTest size = " + coupons.size());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
       *//* for (int i = 0; i <= mSectionsPagerAdapter.getCount(); i++) {
            CouponFragment couponFragment = (CouponFragment)mSectionsPagerAdapter.getItem(i);
            System.out.println("myTest name---------- = " + coupons.get(i).getPlaceName());
            System.out.println("myTest name----------i = " + i);
            couponFragment.updateFragment(coupons.get(i));
        }
        mSectionsPagerAdapter.notifyDataSetChanged();*//*
    }*/



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvents(Events.UpdateCoupons updateCoupons) {
        //System.out.println("myTest UpdateCoupons+");
       // updateFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFragments();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slide_coupons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (userPrize && Properties.getSoundGame()) {
            sp.play(soundIdWin, 1, 1, 0, 0, 1);
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CouponFragment couponFragment = new CouponFragment();
            Bundle args = new Bundle();
            args.putParcelable("coupon", coupons.get(position));
            couponFragment.setArguments(args);
            return couponFragment;
        }

        @Override
        public int getCount() {
            return coupons.size();
        }
    }
}
