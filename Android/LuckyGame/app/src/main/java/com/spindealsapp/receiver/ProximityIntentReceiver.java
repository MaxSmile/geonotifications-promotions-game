package com.spindealsapp.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.activity.DetailsActivity;
import com.spindealsapp.common.Properties;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.service.PlaceServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.R;
import com.spindealsapp.util.LocationDistance;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        if (entering && Properties.getNotifications()) {
            Place place = PlaceServiceLayer.getSimplePlace(intent.getStringExtra(Constants.PLACE_KEY));
            if (place != null) {
                double distance = getDistance(CurrentLocation.lat, CurrentLocation.lon, place.getGeoLat(), place.getGeoLon());
                long lastNotification = DBHelper.getInstance(context).getTimeNotification(intent.getStringExtra(Constants.PLACE_KEY));
                if (distance <= (place.getGeoRadius() + 10)) {
                    if (lastNotification > 0) {
                        if ((System.currentTimeMillis() - lastNotification) > place.getGeoTimeFrequency()) {
                            sentNotification(context, intent, place);
                        }
                    } else {
                        sentNotification(context, intent, place);
                    }
                }
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

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        return LocationDistance.calculateDistance(lat1, lon1, lat2, lon2);
    }
}
