package com.spindealsapp.common;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Kvm on 06.06.2017.
 */

public class MyRotateAnimation extends Animation {
    private float degrees;

    private float mFromDegrees;
    private float mToDegrees;

    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;

    private float mPivotX;
    private float mPivotY;


    public MyRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue,
                           int pivotYType, float pivotYValue) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;

        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    /**
     * Called at the end of constructor methods to initialize, if possible, values for
     * the pivot point. This is only possible for ABSOLUTE pivot values.
     */
    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        degrees = mFromDegrees + ((mToDegrees - mFromDegrees) * interpolatedTime);
        float scale = getScaleFactor();

        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            t.getMatrix().setRotate(degrees);
        } else {
            t.getMatrix().setRotate(degrees, mPivotX * scale, mPivotY * scale);
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    public float getDegrees() {
        return degrees;
    }
}
