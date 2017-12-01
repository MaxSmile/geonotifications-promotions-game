package com.spindealsapp.vmodel;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.spindealsapp.binding.handler.Handler;

/**
 * Created by Volodymyr Kusenko on 30.11.2017.
 */

public abstract class BaseViewModel<A extends AppCompatActivity> extends ViewModel implements Handler {

    public BaseViewModel(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void back(View view) {
        activity.onBackPressed();
    }

}
