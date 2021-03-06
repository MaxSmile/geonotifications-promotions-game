package com.spindealsapp.binding;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.spindealsapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Kvm on 15.05.2017.
 */

public class BindingAdapters {

    private BindingAdapters() { throw new AssertionError(); }

    @BindingAdapter({"android:src"})
    public static void loadImage(final ImageView view, final String url) {
        Picasso.with(view.getContext())
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(view.getContext())
                                .load(url)
                                .error(R.mipmap.ic_launcher)
                                .into(view);
                    }
                });
    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView view, int resource){
        try {
            view.setImageResource(resource);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
