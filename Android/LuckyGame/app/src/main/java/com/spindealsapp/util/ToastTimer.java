package com.spindealsapp.util;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Volodymyr Kusenko on 23.11.2017.
 */

public class ToastTimer {

    public static CountDownTimer countDownTimer;

    public static void start(final Context context, final Toast toast, long duration) {
        countDownTimer =  new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!((Activity) context).isFinishing()) {
                    toast.show();
                }
            }

            @Override
            public void onFinish() {
                if(!((Activity) context).isFinishing()) {
                    toast.cancel();
                }
            }
        };

        if(!((Activity) context).isFinishing()) {
            toast.show();
            countDownTimer.start();
        }
    }

    public static void cancel() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
