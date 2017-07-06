package com.vasilkoff.luckygame.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.DetailsActivity;
import com.vasilkoff.luckygame.common.Properties;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Place;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering && Properties.getNotifications()) {
            Place place = DBHelper.getInstance(context).getPlace(intent.getStringExtra(Constants.PLACE_KEY));

            long lastNotification = DBHelper.getInstance(context).getTimeNotification(intent.getStringExtra(Constants.PLACE_KEY));
            if (lastNotification > 0) {
                if ((System.currentTimeMillis() - lastNotification) > place.getGeoTimeFrequency()) {
                    sentNotification(context, intent, place);
                }
            } else {
                sentNotification(context, intent, place);
            }
        }
    }

    private void sentNotification(Context context, Intent intent, Place place) {
        DBHelper.getInstance(context).saveTimeNotification(place.getId());

        Intent activityIntent = new Intent(context, DetailsActivity.class);
        activityIntent.putExtra(Constants.PLACE_KEY, intent.getStringExtra(Constants.PLACE_KEY));
        activityIntent.putExtra("geoNotification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentText(place.getGeoMessage())
                .setContentTitle(place.getName())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setOngoing(false)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification);
    }
}
