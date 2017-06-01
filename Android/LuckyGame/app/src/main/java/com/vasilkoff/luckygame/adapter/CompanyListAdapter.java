package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.DetailsActivity;
import com.vasilkoff.luckygame.activity.GameActivity;
import com.vasilkoff.luckygame.activity.InfoActivity;
import com.vasilkoff.luckygame.binding.handler.CompanyRowHandler;
import com.vasilkoff.luckygame.databinding.CompaniesRowBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Promotion;
import com.vasilkoff.luckygame.entity.Spin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        TypedArray ta = context.getResources().obtainTypedArray(R.array.spin_type);
        spin.setStatusIcon(ta.getResourceId(spin.getStatus(), 0));
        ta.recycle();
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
                    intent.putExtra(Place.class.getCanonicalName(), place.getId());
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
