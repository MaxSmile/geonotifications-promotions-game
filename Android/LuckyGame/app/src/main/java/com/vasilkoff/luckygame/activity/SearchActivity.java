package com.vasilkoff.luckygame.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.binding.handler.SearchHandler;
import com.vasilkoff.luckygame.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity implements SearchHandler {

    private String searchText;
    private  ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setHandler(this);
        binding.setSearchText(searchText);
    }

    @Override
    public void search(View view) {
        System.out.println(TAG + " searchText= " + binding.getSearchText());
    }

    @Override
    public void test(View view, String arg) {
        System.out.println(TAG + " arg= " + arg);
    }
}
