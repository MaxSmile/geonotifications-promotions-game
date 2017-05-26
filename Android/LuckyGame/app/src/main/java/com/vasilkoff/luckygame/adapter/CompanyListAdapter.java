package com.vasilkoff.luckygame.adapter;

import android.content.Context;
import android.content.Intent;
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
        Company company = activeCompanyListInfo.get(position);
        company.setCountPromo(companies.get(activeCompanyListInfo.get(position).getId()).size());
        holder.bind(company);
    }

    @Override
    public int getItemCount() {
        return activeCompanyListInfo.size();
    }

    class Holder extends RecyclerView.ViewHolder implements CompanyRowHandler {
        private CompaniesRowBinding binding;
        private Company company;

        public Holder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
           /* name = (TextView) v.findViewById(R.id.companyName);
            countPromo = (TextView) v.findViewById(R.id.textCountPromo);
            logo = (ImageView) v.findViewById(R.id.companyLogo);
*/
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("company", company.getId());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Company company) {
            this.company = company;
            binding.setCompany(company);
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
