package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameActivity extends BaseActivity {

    private PopupWindow popupWindow;
    private RelativeLayout parentLayout;
    private Map<String, Promotion> promotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        parentLayout = (RelativeLayout) findViewById(R.id.activity_game);
        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        init(intent);
    }

    private void init(Intent intent) {
        String company = intent.getStringExtra("company");
        System.out.println("TEST company = " + company);
        if (company != null) {
            dbCompanies.child(company).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    updateData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateData(DataSnapshot dataSnapshot) {
        promotions = new HashMap<String, Promotion>();
        for (DataSnapshot promotion : dataSnapshot.child("promo").getChildren()) {
            if (promotion.child("active").getValue().equals(true)) {
                Promotion promotionValue = promotion.getValue(Promotion.class);
                promotions.put(promotion.getKey(), promotionValue);
            }
        }

        if (promotions.size() > 0) {
            System.out.println("TEST GAME ok");
        }
    }

    private void checkAccount() {
        if (AccessToken.getCurrentAccessToken() != null) {
            playGame();
        } else {
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
    }

    public void onClickGame(View view) {
        switch (view.getId()) {
            case R.id.gamePlay:
                checkAccount();
                break;
            case R.id.gameWin:
                createCoupon();
                break;
            case R.id.gameLose:
                chooseAction();
                break;
        }
    }

    private void playGame() {

    }

    private void createCoupon() {
        Random generator = new Random();
        Object[] values = companies.get(getIntent().getStringExtra("company")).values().toArray();
        Promotion winPromotion = (Promotion)values[generator.nextInt(values.length)];
        String coupon = UUID.randomUUID().toString();
        dbHelper.saveCoupon(coupon, getIntent().getStringExtra("company"), winPromotion);

        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(Promotion.class.getCanonicalName(), winPromotion);
        startActivity(intent);
        finish();
    }

    private void chooseAction() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choose_action, null);

        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }

}
