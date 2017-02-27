package com.vasilkoff.luckygame.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Company;

import java.util.List;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class AllCompanyListAdapter extends RecyclerView.Adapter<AllCompanyListAdapter.Holder>{

    private Context context;
    private List<Company> companiesList;

    public AllCompanyListAdapter(Context context, List<Company> companiesList) {
        this.context = context;
        this.companiesList = companiesList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_companies_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.name.setText(companiesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;

        public Holder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.companyName);
        }
    }
}
