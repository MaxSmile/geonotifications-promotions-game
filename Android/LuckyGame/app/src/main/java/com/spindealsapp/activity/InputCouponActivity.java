package com.spindealsapp.activity;
import com.spindealsapp.BR;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityInputCouponBinding;
import com.spindealsapp.vmodel.InputCouponVM;

public class InputCouponActivity extends BaseBindingActivity<ActivityInputCouponBinding, InputCouponVM> {

    @Override
    public InputCouponVM onCreate() {
        return new InputCouponVM(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_input_coupon;
    }
}
