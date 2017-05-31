package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityGameBinding;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Coupon;
import com.vasilkoff.luckygame.util.Properties;
import com.vasilkoff.luckygame.entity.Promotion;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameActivity extends BaseActivity implements Animation.AnimationListener {

    private PopupWindow popupWindow;
    private RelativeLayout parentLayout;

    private ProgressBar progressBar;
    private int count = 0;
    private boolean flag;
    private Handler handler;
    private ImageView powerButton;

    private static final float D_TO_R = 0.0174532925f;
    private String prizes[] = new String[12];
    private Integer colorBox[] = new Integer[12];
    private float angleDelay = 360f/prizes.length;
    private float startAngle = 0;
    private long mSpinDuration;
    private float mSpinRevolutions;
    private ImageView pointerImageView;

    private boolean spinActive = false;

   // private MediaPlayer player;

    private Company company;
    private HashMap<String, Promotion> promotions;
    private Promotion winPromotion;
    private String winKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        promotions = (HashMap<String, Promotion>)getIntent().getSerializableExtra(Promotion.class.getCanonicalName());
        company = getIntent().getParcelableExtra(Company.class.getCanonicalName());

        ActivityGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setCompany(company);

        parentLayout = (RelativeLayout) findViewById(R.id.gameWheel);

        progressBar = (ProgressBar)findViewById(R.id.powerBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress));



        powerButton = (ImageView)findViewById(R.id.powerButton);
        powerButton.setOnTouchListener(new PowerTouchListener());
        pointerImageView = (ImageView)findViewById(R.id.imageWheel);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                progressBar.setProgress(message.arg1);
                return false;
            }
        });
        //init(getIntent());
        initData();
    }

    private void initData() {
        Arrays.fill(colorBox, -1);
        int i = 0;
        for (HashMap.Entry<String, Promotion> promo : promotions.entrySet()) {
            Promotion promotion = promo.getValue();
            for (int j = 0; j < promotion.getCapacityBox(); j++) {
                if (i < prizes.length) {
                    colorBox[i] = promotion.getColorBox();
                }
                i++;
            }
        }

        List<Integer> lst = Arrays.asList(colorBox);
        Collections.shuffle(lst);
        colorBox = lst.toArray(colorBox);

        for (HashMap.Entry<String, Promotion> promo : promotions.entrySet()) {
            Promotion promotion = promo.getValue();
            for (int j = 0; j < colorBox.length; j++) {
                if (colorBox[j] == promotion.getColorBox()) {
                    prizes[j] = promo.getKey();
                }
            }
        }

        initWheel();
    }

    private void initWheel() {
        Bitmap bitmapWheel = BitmapFactory.decodeResource(getResources(),
                R.drawable.wheel);

        Bitmap bitmap = Bitmap.createBitmap(bitmapWheel.getWidth(), bitmapWheel.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmapWheel, 0, 0, null);


        int diameter = bitmapWheel.getHeight();
        int centerX =  diameter / 2;
        int centerY = diameter / 2;
        int radius = diameter / 2;
        float medianAngle = angleDelay / 2;

        radius *= 0.75;

        TypedArray ta = getResources().obtainTypedArray(R.array.game_gift_type);

        canvas.rotate(-90, centerX, centerX);
        for (int i = colorBox.length - 1; i >= 0; i--) {
            startAngle += angleDelay;
            float angle = (startAngle - medianAngle) * D_TO_R;
            float x = (float)(centerX + (radius * Math.cos(angle)));
            float y = (float)(centerY + (radius * Math.sin(angle)));

            canvas.save();
            float rotateAngle = (startAngle - medianAngle) + 90;
            canvas.rotate(rotateAngle, x, y);
            if (colorBox[i] >= 0) {
                Bitmap bitmapGift = BitmapFactory.decodeResource(getResources(),
                        ta.getResourceId(colorBox[i], 0));

                int widthGift = bitmapGift.getWidth() / 2;
                float xGift =  x - widthGift;
                float yGift =  y - widthGift;
                canvas.drawBitmap(bitmapGift, xGift, yGift, null);
            }

            canvas.restore();
        }
        ta.recycle();

        pointerImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //init(intent);
    }

    private void StartSpinner() {
        initWheel();

        mSpinRevolutions = count*60;

        mSpinDuration = (long) (mSpinRevolutions*6);

        if (count >= 30) {
            mSpinDuration = (long) (mSpinRevolutions*4);
        }

        int end = (int)Math.floor(Math.random() * 360);//random: 0-360
        int numOfPrizes = prizes.length;// quantity of prizes
        int degreesPerPrize = 360/numOfPrizes;// size of sector per prize in degrees

        float all = mSpinRevolutions + end;
        int prizeIndex = (int)((all/degreesPerPrize)%numOfPrizes);
        winKey = prizes[prizeIndex];


        RotateAnimation rotateAnim = new RotateAnimation(0f, all,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setRepeatCount(0);
        rotateAnim.setDuration(mSpinDuration);
        rotateAnim.setAnimationListener(this);
        rotateAnim.setFillAfter(true);

        pointerImageView.startAnimation(rotateAnim);
        //player.start();
    }

    private void init(Intent intent) {
        //initPlayer();
        String company = intent.getStringExtra("company");
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

    private void checkSpin() {
        if (!Properties.getSpinActive()) {
            if (Properties.getSpinTimeCreate() == 0) {
                Properties.setSpinActive(true);
                Properties.setSpinTimeCreate(System.currentTimeMillis());
                spinActive = true;
            } else {
                long diff = System.currentTimeMillis() - Properties.getSpinTimeCreate();
                if (TimeUnit.MILLISECONDS.toMinutes(diff) <= 3) {

                }
            }
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
            case R.id.gameBack:
                onBackPressed();
                break;
            case R.id.gameShowDetailsGifts:
                if (company.getCountPromo() > 0) {
                    Intent intent = new Intent(this, LegendActivity.class);
                    intent.putExtra(Promotion.class.getCanonicalName(), promotions);
                    intent.putExtra(Company.class.getCanonicalName(), company);
                    startActivity(intent);
                }
                break;
        }
    }

    private void playGame() {

    }

    private void createCoupon() {
        /*Random generator = new Random();
        Object[] values = companies.get(getIntent().getStringExtra("company")).values().toArray();
        Promotion winPromotion = (Promotion)values[generator.nextInt(values.length)];
        String coupon = UUID.randomUUID().toString();
        dbHelper.saveCoupon(coupon, getIntent().getStringExtra("company"), winPromotion);*/

        StringBuilder couponCodeBuilder = new StringBuilder();
        couponCodeBuilder.append(company.getName().substring(0, 2).toLowerCase());
        couponCodeBuilder.append(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        String couponCode = couponCodeBuilder.toString();

        Promotion promotion = promotions.get("promo1");

        boolean social = false;
        int status = Constants.COUPON_STATUS_ACTIVE;
        long lockTime = System.currentTimeMillis() + promotion.getTimeLock();

       /* if (!social && lockTime > System.currentTimeMillis()) {
            status = 0;
        }*/

        Coupon coupon = new Coupon(
                status,
                couponCode,
                company.getId(),
                promotion.getId(),
                "user",
                System.currentTimeMillis(),
                promotion.getDateFinish(),
                lockTime
        );

        dbCoupons.child(couponCode).setValue(coupon);
        dbHelper.saveCoupon(coupon);


       /* Map<String, String> redeemCoupon = new HashMap<String, String>();
        redeemCoupon.put("date", String.valueOf(System.currentTimeMillis()));
        redeemCoupon.put("code", coupon.getCode());
        redeemCoupon.put("name", coupon.getName());

        if (objectFacebook != null) {
            try {
                redeemCoupon.put("userId", objectFacebook.getString("id"));
                redeemCoupon.put("userName", objectFacebook.getString("name"));
                redeemCoupon.put("userType", "facebook");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (accountGoogle != null) {
            redeemCoupon.put("userId", accountGoogle.getId());
            redeemCoupon.put("userName", accountGoogle.getDisplayName());
            redeemCoupon.put("userType", "google");
        }


        dbCoupons
                .child(coupon.getCompanyKey())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(redeemCoupon);*/

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
        if (winKey != null) {
            winPromotion = promotions.get(winKey);
            System.out.println("TEST win= " + winPromotion.getName());
        } else {
            System.out.println("TEST win=lose");
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

 /*   private void initPlayer() {
        AssetFileDescriptor afd;
        try {
            afd = getAssets().openFd("spiniii.wav");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    private class PowerTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    flag = true;
                    count = 0;
                    powerButton.setImageResource(R.drawable.wheel_button_pressed);
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
                    powerButton.setImageResource(R.drawable.wheel_button);
                    flag = false;

                    StartSpinner();
                    return true;
            }
            return false;
        }
    }
}
