package com.spindealsapp.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;

import com.spindealsapp.vmodel.ViewModel;

/**
 * Created by Volodymyr Kusenko on 30.11.2017.
 */

public abstract class BaseBindingActivity<B extends ViewDataBinding, VM extends ViewModel> extends BindingActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
