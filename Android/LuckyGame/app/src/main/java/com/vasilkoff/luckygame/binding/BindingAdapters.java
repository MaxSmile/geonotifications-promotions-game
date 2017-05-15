package com.vasilkoff.luckygame.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Kvm on 15.05.2017.
 */

public class BindingAdapters {

    private BindingAdapters() { throw new AssertionError(); }

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .into(view);
    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView view, int resource){
        view.setImageResource(resource);
    }
}
