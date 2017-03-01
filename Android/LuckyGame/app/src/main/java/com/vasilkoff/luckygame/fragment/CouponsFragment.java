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
import com.vasilkoff.luckygame.database.DBHelper;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class CouponsFragment extends Fragment {

    private RecyclerView couponsList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coupons_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper = new DBHelper(getContext());
        couponsList = (RecyclerView) getActivity().findViewById(R.id.couponsList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        couponsList.setLayoutManager(llm);
        refreshList();
    }

    public void refreshList() {
        couponsList.setAdapter(new CouponListAdapter(getContext(), dbHelper.getCouponsList()));
    }
}
