package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.CouponActivity;
import com.vasilkoff.luckygame.activity.UnlockActivity;
import com.vasilkoff.luckygame.binding.handler.CouponsRowHandler;
import com.vasilkoff.luckygame.databinding.CouponsRowBinding;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.util.DateFormat;

import java.util.List;

/**
 * Created by Kusenko on 20.02.2017.
 */

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.Holder>{
    private Context context;
    private List<CouponExtension> couponsList;

    public CouponListAdapter(Context context, List<CouponExtension> couponsList) {
        this.context = context;
        this.couponsList = couponsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CouponExtension coupon = couponsList.get(position);

        coupon.setDistance(context.getString(R.string.distance));
        coupon.setTypeString(Constants.COMPANY_TYPE_NAMES[(int)coupon.getType()]);

        if (coupon.getStatus() != Constants.COUPON_STATUS_REDEEMED) {
            if (coupon.getExpired() < System.currentTimeMillis()) {
                coupon.setStatus(Constants.COUPON_STATUS_EXPIRED);
            } else if (coupon.getStatus() == Constants.COUPON_STATUS_LOCK && coupon.getLocks() < System.currentTimeMillis()) {
                coupon.setStatus(Constants.COUPON_STATUS_ACTIVE);
            }
        }

        String locks = DateFormat.getDiff(coupon.getLocks());
        if (locks != null)
            coupon.setLockDiff(locks);

        String expire = DateFormat.getDiff(coupon.getExpired());
        if (expire != null)
            coupon.setExpiredDiff(expire);

        if (coupon.getStatus() >= Constants.COUPON_STATUS_LOCK) {
            TypedArray ta = context.getResources().obtainTypedArray(R.array.coupon_type);
            coupon.setStatusIcon(ta.getResourceId(coupon.getStatus(), 0));
            ta.recycle();
        }
        coupon.setRedeemedString(DateFormat.getDate("dd/MM/yyyy", coupon.getRedeemed()));

        holder.bind(coupon);
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements CouponsRowHandler {
        private CouponsRowBinding binding;
        private CouponExtension coupon;

        public Holder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (coupon.getStatus() < Constants.COUPON_STATUS_REDEEMED) {
                        Intent intent = new Intent(context, CouponActivity.class);
                        intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(CouponExtension coupon) {
            this.coupon = coupon;
            binding.setCoupon(coupon);
            binding.setHandler(this);
        }

        @Override
        public void send(View view) {
            Toast.makeText(context, R.string.next_version, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void unlock(View view) {
            Intent intent = new Intent(context, UnlockActivity.class);
            intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
            context.startActivity(intent);
        }
    }
}