package com.vasilkoff.luckygame.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.FragmentTutorialBinding;

/**
 * Created by Kvm on 27.06.2017.
 */

public class TutorialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTutorialBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_tutorial, container, false);
        binding.setSrc(getArguments().getInt("image"));
        View view = binding.getRoot();

        return view;
    }
}
