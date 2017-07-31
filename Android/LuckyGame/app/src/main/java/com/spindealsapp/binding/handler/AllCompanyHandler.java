package com.spindealsapp.binding.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.spindealsapp.Constants;
import com.spindealsapp.R;
import com.spindealsapp.activity.FilteredCompanyActivity;

/**
 * Created by Kvm on 25.06.2017.
 */

public class AllCompanyHandler {

    public void onItemClicked(View v, int category) {
        Context context = v.getContext();
        if (category == Constants.CATEGORY_E_SHOPS) {
            Toast.makeText(context, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, FilteredCompanyActivity.class);
            intent.putExtra(Constants.PLACE_TYPE_KEY, category);
            context.startActivity(intent);
        }
    }
}