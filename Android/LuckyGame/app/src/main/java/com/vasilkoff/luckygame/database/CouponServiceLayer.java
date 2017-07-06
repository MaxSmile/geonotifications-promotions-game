package com.vasilkoff.luckygame.database;

import android.content.res.TypedArray;

import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kvm on 23.06.2017.
 */

public class CouponServiceLayer {

    public static List<CouponExtension> getCoupons() {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsExtension();
        return updateData(coupons, false);
    }

    public static List<CouponExtension> getCouponsByPlace(String key) {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsByPlace(key);
        return updateData(coupons, false);
    }

    public static List<CouponExtension> getCouponsByCode(String key) {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsByCode(key);
        return updateData(coupons, true);
    }

    public static List<CouponExtension> getCouponsByPlaceGift(String giftKey, String placeKey) {
        List<CouponExtension> coupons  = DBHelper.getInstance(App.getInstance()).getCouponsByPlaceGift(giftKey, placeKey);
        return updateData(coupons, true);
    }


    private static List<CouponExtension> updateData(List<CouponExtension> coupons, boolean sort) {
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

            if (coupon.getCouponType() == Constants.COUPON_TYPE_NORMAL) {
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

        }
        ta.recycle();

        if (sort) {
            Collections.sort(coupons, new CouponComparator());
        }

        return coupons;
    }

    private static class CouponComparator implements Comparator<CouponExtension> {

        @Override
        public int compare(CouponExtension o1, CouponExtension o2) {
            String name1 = o1.getPlaceName().toLowerCase();
            String name2 = o2.getPlaceName().toLowerCase();
            return name1.compareTo(name2);
        }
    }
}
