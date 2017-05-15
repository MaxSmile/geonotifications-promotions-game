package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.vasilkoff.luckygame.entity.Company;
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
    private List<Company> activeCompanyListInfo;

    public CompanyListAdapter(Context context, Map<String, Map<String, Promotion>> companies, List<Company> activeCompanyListInfo) {
        this.context = context;
        this.companies = companies;
        this.activeCompanyListInfo = activeCompanyListInfo;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int count = companies.get(activeCompanyListInfo.get(position).getId()).size();
        holder.countPromo.setText(String.valueOf(count));
        holder.name.setText(activeCompanyListInfo.get(position).getName());
        String logoUri = activeCompanyListInfo.get(position).getLogo();
        if (logoUri != null)
            Picasso.with(context).load(logoUri).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return activeCompanyListInfo.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView countPromo;
        ImageView logo;

        public Holder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.companyName);
            countPromo = (TextView) v.findViewById(R.id.textCountPromo);
            logo = (ImageView) v.findViewById(R.id.companyLogo);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("company", activeCompanyListInfo.get(getAdapterPosition()).getId());

                    context.startActivity(intent);
                }
            });
        }
    }
}
