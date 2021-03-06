package com.spindealsapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spindealsapp.Constants;
import com.spindealsapp.activity.SendCouponActivity;
import com.spindealsapp.activity.SlideCouponsActivity;
import com.spindealsapp.activity.UnlockActivity;
import com.spindealsapp.binding.handler.CouponsRowHandler;
import com.spindealsapp.database.service.CouponServiceLayer;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.R;
import com.spindealsapp.databinding.CouponsRowBinding;

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

    public void updateData(List<CouponExtension> couponsList) {
        this.couponsList.clear();
        this.couponsList.addAll(couponsList);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(couponsList.get(position));
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
                    if (coupon.getCouponType() == Constants.COUPON_TYPE_OFFER) {
                        Intent intent = new Intent(context, SlideCouponsActivity.class);
                        intent.putExtra(Constants.COUPON_GIFT_KEY, coupon.getGiftKey());
                        intent.putExtra(Constants.PLACE_KEY, coupon.getPlaceKey());
                        context.startActivity(intent);
                    } else {
                        if (coupon.getStatus() < Constants.COUPON_STATUS_REDEEMED) {
                            Intent intent = new Intent(context, SlideCouponsActivity.class);
                            intent.putExtra(Constants.COUPON_KEY, coupon.getCode());
                            context.startActivity(intent);
                        } else {
                            deleteDialog();
                        }
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
            Intent intent = new Intent(context, SendCouponActivity.class);
            intent.putExtra(Constants.COUPON_KEY, String.format(context.getString(R.string.send_coupon_text), coupon.getCode()));
            context.startActivity(intent);
        }

        @Override
        public void unlock(View view) {
            Intent intent = new Intent(context, UnlockActivity.class);
            intent.putExtra(CouponExtension.class.getCanonicalName(), coupon);
            context.startActivity(intent);
        }

        private void deleteDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.delete_coupon_dialog_title))
                    .setMessage(context.getString(R.string.delete_coupon_dialog_message))
                    .setNegativeButton(context.getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton(context.getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    CouponServiceLayer.remove(coupon);
                                    couponsList.remove(coupon);
                                    notifyDataSetChanged();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}