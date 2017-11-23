package com.spindealsapp.util;

import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Volodymyr Kusenko on 23.11.2017.
 */

public class ToastTimer {

    public static CountDownTimer countDownTimer;

    public static void start(final Toast toast, long duration) {
        countDownTimer =  new CountDownTimer(duration, 1000)
        {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();

            }
        };
        toast.show();
        countDownTimer.start();
    }

    public static void cancel() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
