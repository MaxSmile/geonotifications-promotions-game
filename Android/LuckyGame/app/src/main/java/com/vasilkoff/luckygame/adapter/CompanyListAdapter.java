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

import com.vasilkoff.luckygame.binding.handler.CompanyRowHandler;
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
    private ArrayList<Spin> spins = new ArrayList<Spin>();
    private HashMap<String, Place> places = new HashMap<String, Place>();
    private HashMap<String, Company> companies = new HashMap<String, Company>();


    public CompanyListAdapter(Context context, ArrayList<Spin> spins, HashMap<String, Place> places, HashMap<String, Company> companies) {
        this.context = context;
        this.companies = companies;
        this.places = places;
        this.spins = spins;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Spin spin = spins.get(position);
        holder.bind(spin, places.get(spin.getPlaceKey()), companies.get(spin.getCompanyKey()));
    }

    @Override
    public int getItemCount() {
        return spins.size();
    }

    class Holder extends RecyclerView.ViewHolder implements CompanyRowHandler {
        private CompaniesRowBinding binding;
        private Spin spin;
        private Place place;

        public Holder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(Constants.PLACE_KEY, place.getId());
                    intent.putExtra(Spin.class.getCanonicalName(), spin);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Spin spin, Place place, Company company) {
            this.spin = spin;
            this.place = place;
            binding.setCompany(company);
            binding.setPlace(place);
            binding.setSpin(spin);
            binding.setHandler(this);
        }

        @Override
        public void showCoupons(View view) {

        }

        @Override
        public void getSpin(View view) {

        }
    }
}
