package com.vasilkoff.luckygame.activity;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kusenko on 17.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbVersion = reference.child("ver1");
    DatabaseReference dbCompanies = dbVersion.child("companies");
    DatabaseReference dbPlaces = dbVersion.child("places");
}
