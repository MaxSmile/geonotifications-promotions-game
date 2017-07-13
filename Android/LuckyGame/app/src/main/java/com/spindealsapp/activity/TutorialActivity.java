package com.spindealsapp.activity;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.spindealsapp.fragment.TutorialFragment;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityTutorialBinding;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends BaseActivity {

    private List<Integer> listImage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fabPrev;
    private FloatingActionButton fabNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ActivityTutorialBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial);
        binding.setHandler(this);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                checkPosition(position);
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

        listImage = new ArrayList<Integer>();
        TypedArray ta = getResources().obtainTypedArray(R.array.tutorial);
        for (int i = 0; i < ta.length(); i++) {
            listImage.add(ta.getResourceId(i, 0));
        }
        ta.recycle();

        initFragments();
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (listImage.size() == 1) {
            fabNext.setVisibility(View.GONE);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TutorialFragment tutorialFragment = new TutorialFragment();
            Bundle args = new Bundle();
            args.putInt("image", listImage.get(position));
            tutorialFragment.setArguments(args);
            return tutorialFragment;
        }

        @Override
        public int getCount() {
            return listImage.size();
        }
    }
}
