package com.spindealsapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.activity.ChooseAccountActivity;
import com.spindealsapp.activity.DetailsActivity;
import com.spindealsapp.activity.NetworkActivity;
import com.spindealsapp.activity.SendCouponActivity;
import com.spindealsapp.activity.UnlockActivity;
import com.spindealsapp.binding.handler.CouponHandler;
import com.spindealsapp.database.service.CouponServiceLayer;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.LocationDistance;
import com.spindealsapp.util.NetworkState;
import com.spindealsapp.R;
import com.spindealsapp.databinding.FragmentCouponBinding;
import com.spindealsapp.util.Rrule;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Kvm on 22.06.2017.
 */

public class CouponFragment extends Fragment implements CouponHandler {

    private CouponExtension coupon;
    private PopupWindow popupWindow;
    private LinearLayout parentLayout;
    private FragmentCouponBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_coupon, container, false);
        View view = binding.getRoot();
        coupon = getArguments().getParcelable("coupon");
        updateData();
        return view;
    }

    private void updateData() {
        String locks = DateFormat.getDiff(coupon.getLocks());
        if (locks != null)
            coupon.setLockDiff(locks);

        String expire = DateFormat.getDiff(coupon.getExpired());
        if (expire != null)
            coupon.setExpiredDiff(expire);

        if (CurrentLocation.lat != 0) {
            if (coupon.getGeoLat() != 0 && coupon.getGeoLon() != 0) {
                coupon.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                        coupon.getGeoLat(), coupon.getGeoLon()));
                coupon.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        coupon.getGeoLat(), coupon.getGeoLon()));
            }
        }

        binding.setCoupon(coupon);
        binding.setHandler(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentLayout = (LinearLayout) getActivity().findViewById(R.id.couponParentLayout);
    }

    @Override
    public void back(View view) {
        getActivity().onBackPressed();
    }

    @Override
    public void favorites(View view) {

    }


    @Override
    public void send(View view) {
        Intent intent = new Intent(getContext(), SendCouponActivity.class);
        intent.putExtra(Constants.COUPON_KEY, String.format(getString(R.string.send_coupon_text), coupon.getCode()));
        startActivity(intent);
    }

    @Override
    public void more(View view) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(Constants.PLACE_KEY, coupon.getPlaceKey());
        startActivity(intent);
    }

    @Override
    public void redeem(View view) {
        if (NetworkState.isOnline()) {
            if (CurrentUser.user != null) {
                if (coupon.getStatus() == Constants.COUPON_STATUS_LOCK) {
                    Intent intent = new Intent(getContext(), UnlockActivity.class);
                    intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
                    startActivity(intent);
                } else if (coupon.getStatus() == Constants.COUPON_STATUS_ACTIVE) {
                    if (Rrule.isAvailable(coupon.getRrule())) {
                        if(getActivity() != null && !getActivity().isFinishing()) {
                            showPopUp();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.coupon_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                startActivity(new Intent(getActivity(), ChooseAccountActivity.class));
            }
        } else {
            startActivity(new Intent(getActivity(), NetworkActivity.class));
        }
    }

    @Override
    public void share(View view) {

    }

    private void redeem() {
        coupon.setStatus(Constants.COUPON_STATUS_REDEEMED);
        coupon.setRedeemed(System.currentTimeMillis());
        coupon.setRedeemedString(DateFormat.getDate("dd/MM/yyyy", coupon.getRedeemed()));
        binding.setCoupon(coupon);
        CouponServiceLayer.add(coupon);
        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_REDEEMED);
        Constants.DB_COUPON.child(coupon.getCode()).child("redeemed").setValue(System.currentTimeMillis());
        Constants.DB_COUPON.child(coupon.getCode()).child("redeemUser").setValue(CurrentUser.user.getId());
    }

    private void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_redeem, null);

        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        ((ImageButton) view.findViewById(R.id.couponPopUpClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ((TextView) view.findViewById(R.id.couponPopUpNot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ((Button) view.findViewById(R.id.couponPopUpRedeem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                redeem();
            }
        });

        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }
}
