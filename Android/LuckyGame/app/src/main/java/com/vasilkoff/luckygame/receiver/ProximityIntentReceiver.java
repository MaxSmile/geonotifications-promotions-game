package com.vasilkoff.luckygame.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.GameActivity;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Intent activityIntent = new Intent(context, GameActivity.class);
            activityIntent.putExtra("company", intent.getStringExtra("company"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentText(intent.getStringExtra("company"))
                    .setContentTitle("title")
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Notification notification = builder.build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification);
        }
    }
}
