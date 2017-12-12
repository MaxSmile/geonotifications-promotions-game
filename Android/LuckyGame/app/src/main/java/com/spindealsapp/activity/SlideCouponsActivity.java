package com.spindealsapp.activity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spindealsapp.Constants;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.service.CouponServiceLayer;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.fragment.CouponFragment;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;


public class SlideCouponsActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FloatingActionButton fabPrev;
    private FloatingActionButton fabNext;
    private List<CouponExtension> coupons;

    private TextView slideCount;
    private TextView slideCurrent;
    private LinearLayout slidePagination;

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

        if (getIntent().getBooleanExtra("userPrize", false) && Properties.getSoundGame()) {
            initSound();
        }
    }

    private void initSound() {
        try {
            AssetFileDescriptor afd = getAssets().openFd(getString(R.string.winning_sound));
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
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
            if (getIntent().getStringExtra(Constants.COUPON_GIFT_KEY) != null) {
                coupons = CouponServiceLayer.getCouponsByPlaceGift(
                        getIntent().getStringExtra(Constants.COUPON_GIFT_KEY),
                        getIntent().getStringExtra(Constants.PLACE_KEY));
            } else {
                coupons = CouponServiceLayer.getCouponsByPlace(getIntent().getStringExtra(Constants.PLACE_KEY));
            }
        }

        if (getIntent().getStringExtra(Constants.COUPON_KEY) != null) {
            coupons = CouponServiceLayer.getCouponsByCode(getIntent().getStringExtra(Constants.COUPON_KEY));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (coupons.size() == 1) {
            fabNext.setVisibility(View.GONE);
            slidePagination.setVisibility(View.GONE);
        }

        slideCount.setText(String.valueOf(coupons.size()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvents(Events.UpdateCoupons updateCoupons) {
        initFragments();
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

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
