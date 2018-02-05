package com.spindealsapp.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentUser;
import com.spindealsapp.binding.handler.GameHandler;
import com.spindealsapp.common.FasterAnimationsContainer;
import com.spindealsapp.common.MyRotateAnimation;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.service.CouponServiceLayer;
import com.spindealsapp.database.FirebaseData;
import com.spindealsapp.database.service.GiftServiceLayer;
import com.spindealsapp.database.service.SpinServiceLayer;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Coupon;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.GiftLimit;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.UsedSpin;
import com.spindealsapp.R;
import com.spindealsapp.databinding.ActivityGameBinding;
import com.spindealsapp.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity implements GameHandler, Animation.AnimationListener {

    private PopupWindow popupWindow;
    private RelativeLayout parentLayout;

    private ProgressBar progressBar;
    private int count = 0;
    private boolean flag;
    private Handler handler;
    private Handler animateHandler;
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

    private HashMap<String, Gift> gifts;
    private boolean social = false;

    private SoundPool sp;
    private int soundIdLose;
    private int soundIdTick;
    private int soundIdWin;

    private ImageView imagePointer;

    private TimerTask timerTask;
    private Timer timer;
    private float lastRotation = 0;

    private boolean gameAvailable = true;
    private ActivityGameBinding binding;
    private ImageView losePopUpFavorites;
    private int countCoupons;

    private FasterAnimationsContainer mFasterAnimationsContainer;
    private static final int ANIMATION_INTERVAL = 80;
    private ImageView animationBox;

    private Coupon coupon;
    private int prizeIndex;
    private boolean isGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (place == null) {
            startActivity(new Intent(this, LoaderActivity.class));
            finish();
        } else {
            company = getIntent().getParcelableExtra(Company.class.getCanonicalName());
            gifts = GiftServiceLayer.getGifts(place);
            FirebaseData.refreshGifts(new ArrayList<Gift>(gifts.values()));

            countCoupons = CouponServiceLayer.getCouponsByPlace(place.getId()).size();

            binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
            binding.setCompany(company);
            binding.setPlace(place);
            binding.setCountGift(place.getSpin().getBox().size());
            binding.setCountCoupons(countCoupons);

            binding.setHandler(this);

            parentLayout = (RelativeLayout) findViewById(R.id.gameWheel);
            imagePointer = (ImageView)findViewById(R.id.imagePointer);

            progressBar = (ProgressBar)findViewById(R.id.powerBar);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress));

            animationBox = (ImageView)findViewById(R.id.animationBox);

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

            animateHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    float rotation = msg.arg1%30;
                    if (lastRotation > rotation) {
                        if (Properties.getSoundGame()) {
                            sp.play(soundIdTick, 1, 1, 0, 0, 1);
                        }
                        imagePointer.setRotation(-1*10);
                    } else {
                        imagePointer.setRotation(0);
                    }
                    lastRotation = rotation;
                    return false;
                }
            });


            initSound();
            initData();
        }
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
            soundIdLose = sp.load(getAssets().openFd(getString(R.string.loser_sound)), 1);
            soundIdTick = sp.load(getAssets().openFd(getString(R.string.tick_sound)), 1);
            //soundIdWin = sp.load(getAssets().openFd(getString(R.string.winning_sound)), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Arrays.fill(colorBox, -1);
        int i = 0;
        List<Box> boxes = place.getSpin().getBox();
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
        gameAvailable = true;
    }

    private void startSpinner() {
        isGame = true;
        updateSpin();
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
        prizeIndex = (int)((all/degreesPerPrize)%numOfPrizes);
        winKey = prizes[prizeIndex];

        final MyRotateAnimation rotateAnim = new MyRotateAnimation(0f, all,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setRepeatCount(0);
        rotateAnim.setDuration(mSpinDuration);
        rotateAnim.setAnimationListener(this);
        rotateAnim.setFillAfter(true);

        pointerImageView.startAnimation(rotateAnim);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = (int) rotateAnim.getDegrees();
                animateHandler.sendMessage(message);
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,50);
    }

    private void createCoupon(Gift gift) {
        StringBuilder couponCodeBuilder = new StringBuilder();
        couponCodeBuilder.append(company.getName().substring(0, 2).toLowerCase().replaceAll("[^a-z]", ""));
        couponCodeBuilder.append(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        String couponCode = couponCodeBuilder.toString();

        int lock = Constants.COUPON_UNLOCK;
        long lockTime = System.currentTimeMillis() + gift.getTimeLock();

        if (!social && lockTime > System.currentTimeMillis()) {
            lock = Constants.COUPON_LOCK;
        }

        coupon = new Coupon(
                Constants.COUPON_STATUS_ACTIVE,
                couponCode,
                place.getCompanyKey(),
                gift.getId(),
                place.getId(),
                gift.getDescription(),
                CurrentUser.user.getId(),
                System.currentTimeMillis(),
                System.currentTimeMillis() + gift.getExpirationTime(),
                lockTime,
                lock,
                place.getSpin().getRrule()
        );

        CouponExtension couponExtension = new CouponExtension(
                Constants.COUPON_STATUS_ACTIVE,
                couponCode,
                place.getCompanyKey(),
                gift.getId(),
                place.getId(),
                gift.getDescription(),
                CurrentUser.user.getId(),
                System.currentTimeMillis(),
                System.currentTimeMillis() + gift.getExpirationTime(),
                lockTime,
                company.getName(),
                place.getName(),
                company.getLogo(),
                place.getType(),
                place.getTypeName(),
                place.getGeoLat(),
                place.getGeoLon(),
                lock,
                0,
                place.getCity(),
                gift.getRules(),
                Constants.COUPON_TYPE_NORMAL,
                place.getKeywords(),
                place.getSpin().getRrule()
        );

        Constants.DB_COUPON.child(couponCode).setValue(coupon);
        CouponServiceLayer.add(couponExtension);
        setLimit(gift);
    }

    private void goToCoupon() {
        Intent intent = new Intent(this, SlideCouponsActivity.class);
        intent.putExtra(Constants.COUPON_KEY, coupon.getCode());
        intent.putExtra("userPrize", true);
        startActivity(intent);
        finish();
    }

    private void startAnimation(String nameBox)  {
        animationBox.setVisibility(View.VISIBLE);
        int[] imageResources = new int[62];
        for (int i = 0; i < 62; i++) {
            imageResources[i] = getResources().getIdentifier(nameBox + i, "drawable", getPackageName());
        }

        mFasterAnimationsContainer = new FasterAnimationsContainer(animationBox);
        mFasterAnimationsContainer.addAllFrames(imageResources, ANIMATION_INTERVAL);
        mFasterAnimationsContainer.setOnAnimationStoppedListener(new FasterAnimationsContainer.OnAnimationStoppedListener() {
            @Override
            public void onAnimationStopped() {
                goToCoupon();
            }
        });

        mFasterAnimationsContainer.start();
    }



    private void setLimit(final Gift gift) {
        gift.setCountAvailable(gift.getCountAvailable() - 1);
        GiftServiceLayer.add(gift);
        final String timeKey = String.valueOf(DateUtils.getStart(new Date()).getTime());
        Constants.DB_LIMIT.child(gift.getCompanyKey()).child(gift.getId())
                .child(timeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GiftLimit giftLimit = new GiftLimit(DateUtils.getStart(new Date()).getTime(), 1);
                if (dataSnapshot.exists()) {
                    giftLimit = dataSnapshot.getValue(GiftLimit.class);
                    giftLimit.setValue(giftLimit.getValue() + 1);
                }
                Constants.DB_LIMIT.child(gift.getCompanyKey()).child(gift.getId())
                        .child(timeKey).setValue(giftLimit);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((ImageButton) view.findViewById(R.id.losePopUpClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popupWindow.dismiss();
                finish();
            }
        });
        ((LinearLayout) view.findViewById(R.id.losePopUpGetSpin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                getExtra();
            }
        });

        losePopUpFavorites = (ImageView)view.findViewById(R.id.losePopUpFavorites);
        losePopUpFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorites(v);
            }
        });
        setImage();

        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }

    private void setImage() {
        if (place.isFavorites()) {
            losePopUpFavorites.setImageResource(R.drawable.heart_filled);
        } else {
            losePopUpFavorites.setImageResource(R.drawable.heart);
        }
    }

    public void getExtra() {
        if (place.getSpin().isExtraAvailable()) {
            Intent intent = new Intent(this, ExtraSpinActivity.class);
            intent.putExtra(Place.class.getCanonicalName(), place);
            intent.putExtra(Company.class.getCanonicalName(), company);
            intent.putExtra(Gift.class.getCanonicalName(), gifts);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.extra_spin_not_available, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        timerTask.cancel();
        timer.cancel();
        timer.purge();
        imagePointer.setRotation(0);
        gifts = GiftServiceLayer.getGifts(place);
        if (winKey != null) {
            if (gifts.size() > 0) {
                Gift gift = gifts.get(winKey);
                if (gift.isActive()) {
                    createCoupon(gift);
                    setLog(Constants.GAME_WIN);
                    //startAnimation(getResources().getStringArray(R.array.box_name_type)[colorBox[prizeIndex]]);
                    goToCoupon();
                } else {
                    Toast.makeText(this, R.string.gifts_over, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.gifts_over, Toast.LENGTH_LONG).show();
            }
        } else {
            if (Properties.getSoundGame()) {
                sp.play(soundIdLose, 1, 1, 0, 0, 1);
            }
            gameLose();
            setLog(Constants.GAME_LOSE);
        }
        isGame = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void updateSpin() {
        int typeSpin = getIntent().getIntExtra(Constants.SPIN_TYPE_KEY, Constants.SPIN_TYPE_EXTRA);
        if (typeSpin == Constants.SPIN_TYPE_EXTRA) {
            place.getSpin().setExtra(false);
            place.getSpin().setAvailable(false);
        } else {
            long spent = place.getSpin().getSpent() + 1;
            place.getSpin().setSpent(spent);
            long limit = place.getSpin().getLimit();
            if (spent < limit) {
                gameAvailable = true;
            }
            if (spent < limit || place.getSpin().isExtra()) {
                place.getSpin().setAvailable(true);
            } else {
                place.getSpin().setAvailable(false);
            }
        }
    }

    private void setLog(int result) {
        int typeSpin = getIntent().getIntExtra(Constants.SPIN_TYPE_KEY, Constants.SPIN_TYPE_EXTRA);
        UsedSpin usedSpin = new UsedSpin(System.currentTimeMillis(), typeSpin , result);

        Constants.DB_USER.child(CurrentUser.user.getId()).child("userInfo").setValue(CurrentUser.user);
        Constants.DB_USER.child(CurrentUser.user.getId()).child("place").child(place.getId())
                .child(place.getSpin().getId()).child(String.valueOf(System.currentTimeMillis())).setValue(usedSpin);

        usedSpin.setCompanyKey(place.getCompanyKey());
        usedSpin.setPlaceKey(place.getId());
        usedSpin.setUserKey(CurrentUser.user.getId());
        usedSpin.setSpinKey(place.getSpin().getId());
        sendLog(usedSpin);

        SpinServiceLayer.updateSpin(place.getSpin());
    }

    private void sendLog(UsedSpin usedSpin) {
        Gson gson = new Gson();
        String json = gson.toJson(usedSpin);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            AndroidNetworking.post(getString(R.string.server_log_url))
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG , response.toString());
                        }
                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG , error.toString());
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        if (sp != null) {
            sp.release();
            sp = null;
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (mFasterAnimationsContainer != null) {
            mFasterAnimationsContainer.stop();
        }
        super.onStop();
    }

    @Override
    public void showGifts(View view) {
        Intent intent = new Intent(this, LegendActivity.class);
        intent.putExtra(Place.class.getCanonicalName(), place);
        intent.putExtra(Company.class.getCanonicalName(), company);
        intent.putExtra(Gift.class.getCanonicalName(), gifts);
        startActivity(intent);
    }

    @Override
    public void getExtraSpin(View view) {
        getExtra();
    }

    @Override
    public void showCoupons(View view) {
        if (countCoupons > 0) {
            Intent intent = new Intent(this, SlideCouponsActivity.class);
            intent.putExtra(Constants.PLACE_KEY, place.getId());
            startActivity(intent);
        }
    }

    @Override
    public void favorites(View view) {
        super.favorites(view);
        binding.setPlace(place);
        if (losePopUpFavorites != null)
            setImage();
    }

    @Override
    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!isGame)
            super.onBackPressed();
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
                            while (flag && gameAvailable) {
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
                    if (gameAvailable) {
                        startSpinner();
                    }
                    gameAvailable = false;
                    return true;
            }
            return false;
        }
    }
}
