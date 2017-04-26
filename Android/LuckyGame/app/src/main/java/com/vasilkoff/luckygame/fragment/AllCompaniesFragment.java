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
import com.vasilkoff.luckygame.adapter.AllCompanyListAdapter;
import com.vasilkoff.luckygame.entity.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class AllCompaniesFragment extends Fragment {

    private RecyclerView companiesList;
    private List<Company> allCompanyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_companies_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        companiesList = (RecyclerView) getActivity().findViewById(R.id.allCompaniesList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        companiesList.setLayoutManager(llm);
        if (allCompanyList == null)
            allCompanyList = new ArrayList<Company>();
        refreshList();

    }

    public void setCompanies(List<Company> allCompanyList) {
        this.allCompanyList = allCompanyList;
    }

    public void refreshList() {
        companiesList.setAdapter(new AllCompanyListAdapter(getContext(), allCompanyList));
    }


}
