package com.vasilkoff.luckygame.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.Map;

/**
 * Created by Kusenko on 20.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbVersion = reference.child("ver1");
    DatabaseReference dbCompanies = dbVersion.child("companies");
    DatabaseReference dbPlaces = dbVersion.child("places");
    DatabaseReference dbRedeemed = dbVersion.child("redeemed");

    static Map<String, Map<String, Promotion>> companies;

    static DBHelper dbHelper;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        if (dbHelper == null)
            dbHelper = new DBHelper(this);
    }
}
