package com.vasilkoff.luckygame.fragment;

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

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.ChooseAccountActivity;
import com.vasilkoff.luckygame.activity.DetailsActivity;
import com.vasilkoff.luckygame.activity.SendCouponActivity;
import com.vasilkoff.luckygame.activity.UnlockActivity;
import com.vasilkoff.luckygame.binding.handler.CouponHandler;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.databinding.FragmentCouponBinding;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

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
        intent.putExtra("couponCode", String.format(getString(R.string.send_coupon_text), coupon.getCode()));
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
        if (coupon.getStatus() == Constants.COUPON_STATUS_LOCK) {
            Intent intent = new Intent(getContext(), UnlockActivity.class);
            intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
            startActivity(intent);
        } else if (coupon.getStatus() == Constants.COUPON_STATUS_ACTIVE){
            showPopUp();
        }
    }

    private void redeem() {
        coupon.setStatus(Constants.COUPON_STATUS_REDEEMED);
        coupon.setRedeemed(System.currentTimeMillis());
        coupon.setRedeemedString(DateFormat.getDate("dd/MM/yyyy", coupon.getRedeemed()));
        binding.setCoupon(coupon);
        DBHelper.getInstance(getContext()).saveCoupon(coupon);
        Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_REDEEMED);
        Constants.DB_COUPON.child(coupon.getCode()).child("redeemed").setValue(System.currentTimeMillis());

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
