package com.vasilkoff.luckygame.binding.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.activity.FilteredCompanyActivity;

/**
 * Created by Kvm on 25.06.2017.
 */

public class AllCompanyHandler {

    public void onItemClicked(View v, int category) {
        Context context = v.getContext();
        Intent intent = new Intent(context, FilteredCompanyActivity.class);
        intent.putExtra(Constants.PLACE_TYPE_KEY, category);
        context.startActivity(intent);
    }
}