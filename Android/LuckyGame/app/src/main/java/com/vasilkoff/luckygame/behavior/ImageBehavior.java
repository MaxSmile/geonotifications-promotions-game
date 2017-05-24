package com.vasilkoff.luckygame.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.vasilkoff.luckygame.R;

/**
 * Created by Kvm on 17.05.2017.
 */

public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private int startWidth;
    private int startHeight;
    private int minHeight;
    private int minWidth;
    private int minTop;
    private float startAppBarY;
    private float startY;


    public ImageBehavior() {
    }

    public ImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageBehavior);
            minHeight = (int)a.getDimension(R.styleable.ImageBehavior_minHeight, 0);
            minWidth = (int)a.getDimension(R.styleable.ImageBehavior_minWidth, 0);
            minTop = (int)a.getDimension(R.styleable.ImageBehavior_minTop, 0);

            a.recycle();
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        if (startWidth == 0) {
            startWidth = child.getWidth();
            startHeight = child.getHeight();
            startY = child.getY();
        }

        AppBarLayout appBarLayout = (AppBarLayout) dependency;
        int range = appBarLayout.getTotalScrollRange();
        float factor = -appBarLayout.getY() / range;

        int width = (int)(startWidth - (startWidth*factor));
        int height = (int)(startHeight - (startHeight*factor));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (height >= minHeight) {
            lp.width = width;
            lp.height = height;
            child.setLayoutParams(lp);
        } else {
            if (startAppBarY == 0)
                startAppBarY = appBarLayout.getY();

            float diff = startAppBarY + (-1*appBarLayout.getY());
            startAppBarY = appBarLayout.getY();

            float top = child.getY() - diff;
            child.setY(top);
        }

        if (appBarLayout.getY() == 0) {
            child.setY(startY);
        }

        if (range == -appBarLayout.getY()) {
            child.setY(minTop);
            lp.width = minWidth;
            lp.height = minHeight;
            child.setLayoutParams(lp);
        }

        return true;
    }
}