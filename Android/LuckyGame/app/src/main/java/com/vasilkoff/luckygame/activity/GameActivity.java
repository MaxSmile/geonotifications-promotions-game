package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.databinding.ActivityGameBinding;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.Coupon;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Spin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    private String winKey;
    private Company company;
    private Place place;
    private Spin spin;
    private HashMap<String, Gift> gifts;
    private boolean social = false;

    private SoundPool sp;
    private int soundIdLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        place = getIntent().getParcelableExtra(Place.class.getCanonicalName());
        company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
        gifts = (HashMap<String, Gift>)(getIntent().getSerializableExtra(Gift.class.getCanonicalName()));
        spin = getIntent().getParcelableExtra(Spin.class.getCanonicalName());

        ActivityGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setCompany(company);
        binding.setPlace(place);

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


        initSound();
        //init(getIntent());
        initData();
    }

    private void initSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        try {
            soundIdLose = sp.load(getAssets().openFd("loser.wav"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Arrays.fill(colorBox, -1);
        int i = 0;
        List<Box> boxes = place.getBox();
        for (int j = 0; j < boxes.size(); j++) {
            Box box = boxes.get(j);
            for (int k = 0; k < box.getCount(); k++) {
                if (i < prizes.length) {
                    colorBox[i] = box.getColor();
                }
                i++;
            }
        }
        List<Integer> lst = Arrays.asList(colorBox);
        Collections.shuffle(lst);
        colorBox = lst.toArray(colorBox);

        for (int j = 0; j < boxes.size(); j++) {
            Box box = boxes.get(j);
            for (int k = 0; k < colorBox.length; k++) {
                if (box.getColor() == colorBox[k]) {
                    prizes[k] = box.getGift();
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

    private void checkAccount() {
        if (AccessToken.getCurrentAccessToken() != null) {
        } else {
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
    }

    public void onClickGame(View view) {
        switch (view.getId()) {
            case R.id.gameBack:
                onBackPressed();
                break;
            case R.id.gameShowDetailsGifts:
                Intent intent = new Intent(this, LegendActivity.class);
                intent.putExtra(Place.class.getCanonicalName(), place);
                intent.putExtra(Company.class.getCanonicalName(), company);
                intent.putExtra(Gift.class.getCanonicalName(), gifts);
                startActivity(intent);
                break;
        }
    }

    private void createCoupon(Gift gift) {
        StringBuilder couponCodeBuilder = new StringBuilder();
        couponCodeBuilder.append(company.getName().substring(0, 2).toLowerCase());
        couponCodeBuilder.append(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        String couponCode = couponCodeBuilder.toString();

        int status = Constants.COUPON_STATUS_ACTIVE;
        long lockTime = System.currentTimeMillis() + gift.getTimeLock();

        if (!social && lockTime > System.currentTimeMillis()) {
            status = Constants.COUPON_STATUS_LOCK;
        }

        Coupon coupon = new Coupon(
                status,
                couponCode,
                place.getCompanyKey(),
                gift.getId(),
                place.getId(),
                gift.getDescription(),
                "userId",
                System.currentTimeMillis(),
                gift.getDateFinish(),
                lockTime
        );

        CouponExtension couponExtension = new CouponExtension(
                status,
                couponCode,
                place.getCompanyKey(),
                gift.getId(),
                place.getId(),
                gift.getDescription(),
                "userId",
                System.currentTimeMillis(),
                gift.getDateFinish(),
                lockTime,
                company.getName(),
                place.getName(),
                company.getLogo(),
                place.getType(),
                place.getTypeName()
        );


        Constants.dbCoupon.child(couponCode).setValue(coupon);
        dbHelper.saveCoupon(coupon);



        Intent intent = new Intent(this, CouponActivity.class);
        intent.putExtra(CouponExtension.class.getCanonicalName(), couponExtension);
        intent.putExtra("userPrize", true);
        startActivity(intent);



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


        dbCoupon
                .child(coupon.getCompanyKey())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(redeemCoupon);*/

        //Toast.makeText(this, "You got coupon!", Toast.LENGTH_LONG).show();
      /*  Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(Promotion.class.getCanonicalName(), winPromotion);
        startActivity(intent);*/
        finish();
    }

    private void gameLose() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_lose, null);
        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        ((ImageButton) view.findViewById(R.id.losePopUpClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ((LinearLayout) view.findViewById(R.id.losePopUpGetSpin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Toast.makeText(GameActivity.this, R.string.next_version, Toast.LENGTH_SHORT).show();
            }
        });
        ((LinearLayout) view.findViewById(R.id.losePopUpNotify)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Toast.makeText(GameActivity.this, R.string.next_version, Toast.LENGTH_SHORT).show();
            }
        });

        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }

    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (winKey != null) {
            createCoupon(gifts.get(winKey));
        } else {
            sp.play(soundIdLose, 1, 1, 0, 0, 1);
            gameLose();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onDestroy() {
        sp.release();
        sp = null;
        super.onDestroy();
    }

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
