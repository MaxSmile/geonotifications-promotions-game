package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.CouponActivity;
import com.vasilkoff.luckygame.entity.Coupon;

import java.util.List;

/**
 * Created by Kusenko on 20.02.2017.
 */

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.Holder>{
    private Context context;
    private List<Coupon> couponsList;

    public CouponListAdapter(Context context, List<Coupon> couponsList) {
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
        holder.name.setText(couponsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;

        public Holder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.couponName);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CouponActivity.class);
                    intent.putExtra(Coupon.class.getCanonicalName(), couponsList.get(getAdapterPosition()));

                    context.startActivity(intent);
                }
            });
        }
    }
}