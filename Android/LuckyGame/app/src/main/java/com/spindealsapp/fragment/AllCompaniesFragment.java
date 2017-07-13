package com.spindealsapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spindealsapp.binding.handler.AllCompanyHandler;
import com.spindealsapp.R;
import com.spindealsapp.databinding.AllCompaniesFragmentBinding;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class AllCompaniesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AllCompaniesFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.all_companies_fragment, container, false);
        binding.setHandler(new AllCompanyHandler());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
