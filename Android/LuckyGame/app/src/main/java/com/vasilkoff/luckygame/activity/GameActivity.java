package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class GameActivity extends BaseActivity implements Animation.AnimationListener {

    private PopupWindow popupWindow;
    private RelativeLayout parentLayout;
    private Map<String, Promotion> promotions;

    private ProgressBar progressBar;
    private int count = 0;
    private boolean flag;
    private Handler handler;
    private ImageView powerButton;

    final int prizes[] = {200,1000,200,1000,200,400,400,200,3000,400,1000,400};
    private long mSpinDuration;
    private float mSpinRevolutions;
    private ImageView pointerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        parentLayout = (RelativeLayout) findViewById(R.id.activity_game);

        progressBar = (ProgressBar)findViewById(R.id.powerBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress));

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                progressBar.setProgress(message.arg1);
                return false;
            }
        });

        powerButton = (ImageView)findViewById(R.id.powerButton);
        powerButton.setOnTouchListener(new PowerTouchListener());
        pointerImageView = (ImageView)findViewById(R.id.imageWheel);


        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        init(intent);
    }

    private void StartSpinner() {
        //mSpinDuration = (long)(2000 + Math.random() * 3000);//random: 2000-5000
        mSpinRevolutions = count*60;

        mSpinDuration = (long) (mSpinRevolutions*6);

        if (count >= 30) {
            mSpinDuration = (long) (mSpinRevolutions*4);
        }




//        if (count >= 60) {
//            mSpinRevolutions = 3600*3
//            mSpinDuration = 15000;
//        }


        // Final point of rotation defined right here:
        int end = (int)Math.floor(Math.random() * 360);//random: 0-360
        int numOfPrizes = prizes.length;// quantity of prizes
        int degreesPerPrize = 360/numOfPrizes;// size of sector per prize in degrees
        int shift = 0; //shit where the arrow points
        int prizeIndex = (shift + end/degreesPerPrize)%numOfPrizes;
        //prizeText = "Prize is: "+ prizes[prizeIndex];


        RotateAnimation rotateAnim = new RotateAnimation(0f, mSpinRevolutions + end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setRepeatCount(0);
        rotateAnim.setDuration(mSpinDuration);
        rotateAnim.setAnimationListener(this);
        rotateAnim.setFillAfter(true);

        pointerImageView.startAnimation(rotateAnim);
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

        Toast.makeText(this, "You got coupon!", Toast.LENGTH_LONG).show();
      /*  Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(Promotion.class.getCanonicalName(), winPromotion);
        startActivity(intent);*/
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class PowerTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    flag = true;
                    count = 0;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag) {
                                count+=2;
                                if (count >= 100) {
                                    //flag = false;
                                    count = 1;
                                }

                                Message msg = new Message();
                                msg.arg1 = count;
                                handler.sendMessage(msg);
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    return true;
                case MotionEvent.ACTION_UP:
                    flag = false;

                    StartSpinner();
                    return true;
            }
            return false;
        }
    }
}
