package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.DetailsActivity;

import com.vasilkoff.luckygame.activity.SlideCouponsActivity;
import com.vasilkoff.luckygame.binding.handler.CompanyRowHandler;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.databinding.CompaniesRowBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;

import com.vasilkoff.luckygame.entity.Spin;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kusenko on 17.02.2017.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.Holder>{
    private Context context;
    private ArrayList<Place> places;
    private HashMap<String, Company> companies;


    public CompanyListAdapter(Context context, ArrayList<Place> places, HashMap<String, Company> companies) {
        this.context = context;
        this.companies = companies;
        this.places = places;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Place place = places.get(position);
        holder.bind(place, companies.get(place.getCompanyKey()));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class Holder extends RecyclerView.ViewHolder implements CompanyRowHandler {
        private CompaniesRowBinding binding;
        private Place place;
        private int countCoupons;

        public Holder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(Constants.PLACE_KEY, place.getId());
                    //intent.putExtra(Spin.class.getCanonicalName(), spin);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Place place, Company company) {
            this.place = place;
            binding.setCompany(company);
            binding.setPlace(place);
            binding.setHandler(this);

            countCoupons = DBHelper.getInstance(context).getCouponsByPlace(place.getId()).size();
            binding.setCountCoupons(countCoupons);
        }

        @Override
        public void showCoupons(View view) {
            if (countCoupons > 0) {
                Intent intent = new Intent(context, SlideCouponsActivity.class);
                intent.putExtra(Constants.PLACE_KEY, place.getId());
                context.startActivity(intent);
            }
        }

        @Override
        public void getSpin(View view) {

        }

        @Override
        public void favorites(View view) {
            place.setFavorites(!place.isFavorites());
            binding.setPlace(place);
            DBHelper.getInstance(context).updatePlace(place);
        }
    }
}
