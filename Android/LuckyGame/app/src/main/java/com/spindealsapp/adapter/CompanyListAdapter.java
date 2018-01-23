package com.spindealsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spindealsapp.Constants;
import com.spindealsapp.activity.DetailsActivity;
import com.spindealsapp.activity.SlideCouponsActivity;
import com.spindealsapp.binding.handler.CompanyRowHandler;
import com.spindealsapp.database.service.PlaceServiceLayer;
import com.spindealsapp.database.service.CouponServiceLayer;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Place;
import com.spindealsapp.R;
import com.spindealsapp.databinding.CompaniesRowBinding;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Kusenko on 17.02.2017.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.Holder>{
    private Context context;
    private ArrayList<Place> places;
    private Map<String, Company> companies;


    public CompanyListAdapter(Context context, ArrayList<Place> places, Map<String, Company> companies) {
        this.context = context;
        this.companies = companies;
        this.places = places;
    }

    public void updateData(ArrayList<Place> places, Map<String, Company> companies) {
        this.companies = companies;
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
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
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Place place, Company company) {
            this.place = place;
            binding.setCompany(company);
            binding.setPlace(place);
            binding.setHandler(this);

            countCoupons = CouponServiceLayer.getCouponsByPlace(place.getId()).size();
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
            PlaceServiceLayer.update(place);
        }
    }
}
