package com.vasilkoff.luckygame.database;

import android.content.res.TypedArray;

import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.List;

/**
 * Created by Kvm on 23.06.2017.
 */

public class ServiceLayer {

    public static List<CouponExtension> getCoupons() {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsExtension();
        return updateData(coupons);
    }

    public static List<CouponExtension> getCouponsByPlace(String key) {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsByPlace(key);
        return updateData(coupons);
    }


    private static List<CouponExtension> updateData(List<CouponExtension> coupons) {
        TypedArray ta = App.getInstance().getResources().obtainTypedArray(R.array.coupon_type);
        for (int i = 0; i < coupons.size(); i++) {
            CouponExtension coupon = coupons.get(i);

            if (CurrentLocation.lat != 0) {
                if (coupon.getGeoLat() != 0 && coupon.getGeoLon() != 0) {
                    coupon.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                            coupon.getGeoLat(), coupon.getGeoLon()));
                    coupon.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                            coupon.getGeoLat(), coupon.getGeoLon()));
                }
            }

            coupon.setTypeString(Constants.COMPANY_TYPE_NAMES[(int)coupon.getType()]);

            if (coupon.getStatus() != Constants.COUPON_STATUS_REDEEMED) {
                if (coupon.getExpired() < System.currentTimeMillis()) {
                    coupon.setStatus(Constants.COUPON_STATUS_EXPIRED);
                } else if (coupon.getStatus() == Constants.COUPON_STATUS_ACTIVE && coupon.getLocks() < System.currentTimeMillis()
                        && coupon.getLocked() == Constants.COUPON_LOCK) {
                    coupon.setStatus(Constants.COUPON_STATUS_LOCK);
                    Constants.DB_COUPON.child(coupon.getCode()).child("status").setValue(Constants.COUPON_STATUS_LOCK);
                }
            }

            String locks = DateFormat.getDiff(coupon.getLocks());
            if (locks != null)
                coupon.setLockDiff(locks);

            String expire = DateFormat.getDiff(coupon.getExpired());
            if (expire != null)
                coupon.setExpiredDiff(expire);

            if (coupon.getStatus() >= Constants.COUPON_STATUS_LOCK) {
                coupon.setStatusIcon(ta.getResourceId(coupon.getStatus(), 0));
            }
            coupon.setRedeemedString(DateFormat.getDate("dd/MM/yyyy", coupon.getRedeemed()));
        }
        ta.recycle();
        return coupons;
    }
}
