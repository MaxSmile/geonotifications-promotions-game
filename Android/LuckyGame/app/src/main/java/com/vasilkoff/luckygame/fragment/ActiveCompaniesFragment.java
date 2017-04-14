package com.vasilkoff.luckygame.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.adapter.CompanyListAdapter;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class ActiveCompaniesFragment extends Fragment {

    private RecyclerView companiesList;
    private Map<String, Map<String, Promotion>> companies;
    private List<Company> activeCompanyListInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.active_companies_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        companiesList = (RecyclerView) getActivity().findViewById(R.id.activeCompaniesList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        companiesList.setLayoutManager(llm);
        if (companies == null) {
            activeCompanyListInfo = new ArrayList<Company>();
            companies = new HashMap<String, Map<String, Promotion>>();
        }
        refreshList();
    }

    public void setCompanies(Map<String, Map<String, Promotion>> companies, List<Company> activeCompanyListInfo) {
        this.companies = companies;
        this.activeCompanyListInfo = activeCompanyListInfo;
    }

    public void refreshList() {
        companiesList.setAdapter(new CompanyListAdapter(getContext(), companies, activeCompanyListInfo));
    }
}
