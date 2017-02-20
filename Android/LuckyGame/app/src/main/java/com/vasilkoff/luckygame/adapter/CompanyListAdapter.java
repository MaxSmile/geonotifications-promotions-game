package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.GameActivity;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kusenko on 17.02.2017.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.Holder>{
    private Context context;
    private Map<String, Map<String, Promotion>> companies;
    private List<String> companiesList;

    public CompanyListAdapter(Context context, Map<String, Map<String, Promotion>> companies) {
        this.context = context;
        this.companies = companies;
        companiesList = new ArrayList<String>(companies.keySet());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.name.setText(companiesList.get(position));
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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putExtra("company", companiesList.get(getAdapterPosition()));

                    context.startActivity(intent);
                }
            });
        }
    }
}
