package com.spindealsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spindealsapp.adapter.CouponListAdapter;
import com.spindealsapp.common.Filters;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.CouponServiceLayer;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Kusenko on 27.02.2017.
 */

public class CouponsFragment extends Fragment {

    private RecyclerView couponsList;
    private List<CouponExtension> coupons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coupons_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        couponsList = (RecyclerView) getActivity().findViewById(R.id.couponsList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        couponsList.setLayoutManager(llm);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCoupons(Events.UpdateCoupons updateCoupons) {
        refreshList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateLocatio(Events.UpdateLocation updateLocation) {
        refreshList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateFilter(Events.UpdateFilter updateFilter) {
        refreshList();
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

    private void refreshList() {
        coupons = CouponServiceLayer.getCoupons();
        if (coupons != null)
            filterData();
    }

    private void filterData() {

        if (Filters.nearMe) {
            Iterator<CouponExtension> j = coupons.iterator();
            while (j.hasNext()) {
                CouponExtension coupon = j.next();
                if (coupon.getDistance() > Properties.getNearMeRadius()) {
                    j.remove();
                }
            }
        }

        if (Filters.byCity) {
            Iterator<CouponExtension> iCity = coupons.iterator();
            while (iCity.hasNext()) {
                CouponExtension coupon = iCity.next();
                if (Filters.filteredCities.get(coupon.getCity()) == null) {
                    iCity.remove();
                }
            }
        }

        if (Filters.byKeywords) {
            Iterator<CouponExtension> iKeywords = coupons.iterator();
            while (iKeywords.hasNext()) {
                CouponExtension coupon = iKeywords.next();
                boolean exist = false;
                List<String> keywords = Arrays.asList(coupon.getKeywords().split(","));
                for (int i = 0; i < keywords.size(); i++) {
                    if (Filters.filteredKeywords.get(keywords.get(i).toLowerCase()) != null) {
                        exist = true;
                    }
                }
                if (!exist) {
                    iKeywords.remove();
                }
            }
        }

        if (Filters.byZA) {
            if (coupons.size() > 0) {
                List<CouponExtension> sortedCoupons = new ArrayList<CouponExtension>();
                for (int k = coupons.size() - 1; k >= 0; k--) {
                    sortedCoupons.add(coupons.get(k));
                }
                coupons = new ArrayList<CouponExtension>(sortedCoupons);
            }
        }

        couponsList.setAdapter(new CouponListAdapter(getContext(), coupons));
    }
}
