package com.vasilkoff.luckygame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CouponListAdapter;
import com.vasilkoff.luckygame.common.Filters;
import com.vasilkoff.luckygame.common.Properties;

import com.vasilkoff.luckygame.database.ServiceLayer;

import com.vasilkoff.luckygame.entity.CouponExtension;

import com.vasilkoff.luckygame.eventbus.Events;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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
        coupons = ServiceLayer.getCoupons();
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

        if (Filters.byZA) {
            if (coupons.size() > 0) {
                List<CouponExtension> sortedCoupons = new ArrayList<CouponExtension>();
                for (int k = coupons.size() - 1; k >= 0; k--) {
                    sortedCoupons.add(coupons.get(k));
                }
                coupons = new ArrayList<CouponExtension>(sortedCoupons);
            }
        }

        if (Filters.search && Filters.searchKeyWord != null && Filters.searchKeyWord.length() > 0) {
            Iterator<CouponExtension> iSearch = coupons.iterator();
            while (iSearch.hasNext()) {
                CouponExtension coupon = iSearch.next();
                if (!coupon.getPlaceName().toLowerCase().contains(Filters.searchKeyWord)) {
                    iSearch.remove();
                }
            }
        }

        couponsList.setAdapter(new CouponListAdapter(getContext(), coupons));
    }
}
