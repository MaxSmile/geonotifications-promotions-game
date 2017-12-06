package com.spindealsapp.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.activity.HomeActivity;
import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.PlaceServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.receiver.ProximityIntentReceiver;
import com.spindealsapp.util.LocationDistance;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class LocationService extends Service {

    private static final String STOP_LOCATION_SERVICE = "com.spindealsapp.stop";

    private static final String PROX_ALERT_INTENT = "com.spindealsapp.";

    private int requestCode = 100;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATE = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 5000; // in Milliseconds
    private static final int TIME_DIFFERENCE_THRESHOLD = 60000; // in Milliseconds

    private static final long POINT_RADIUS = 1000; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private LocationManager locationManager;

    private List<ProximityIntentReceiver> receivers;
    private List<PendingIntent> pendingIntents;
    private LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationListener = new MyLocationListener();

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATE,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATE,
                    locationListener
            );

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATE,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATE,
                    locationListener
            );

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);

            Intent intent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Intent stopIntent = new Intent(this, LocationService.class);
            stopIntent.setAction(STOP_LOCATION_SERVICE);
            PendingIntent disconnectPendingIntent = PendingIntent.getService(
                    this, 0, stopIntent, 0);
            contentView.setOnClickPendingIntent(R.id.notificationClose, disconnectPendingIntent);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDeleteIntent(disconnectPendingIntent)
                    .setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Notification notification = builder.build();
            startForeground(777, notification);

            receivers = new ArrayList<ProximityIntentReceiver>();
            pendingIntents = new ArrayList<PendingIntent>();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && STOP_LOCATION_SERVICE.equals(intent.getAction())) {
            if (pendingIntents != null) {
                for (int i = 0; i < pendingIntents.size(); i++) {
                    locationManager.removeProximityAlert(pendingIntents.get(i));
                }
                pendingIntents = new ArrayList<PendingIntent>();
            }
            locationManager.removeUpdates(locationListener);
            stopSelf();
            return START_NOT_STICKY;
        }

        if (pendingIntents != null) {
            for (int i = 0; i < pendingIntents.size(); i++) {
                locationManager.removeProximityAlert(pendingIntents.get(i));
            }
            pendingIntents = new ArrayList<PendingIntent>();
            loadPlaces();
        } else {
            loadPlaces();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ProximityIntentReceiver receiver : receivers) {
            unregisterReceiver(receiver);
        }
    }

    private void loadPlaces() {
        ArrayList<Place> places = new ArrayList<Place>(DBHelper.getInstance(App.getInstance()).getPlaces().values());
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            if (place.getGeoTimeStart() < System.currentTimeMillis() && place.getGeoTimeFinish() > System.currentTimeMillis()) {
                addProximityAlert(place.getGeoLat(), place.getGeoLon(), place.getId(), place.getGeoRadius());
            }
        }
    }

    private void addProximityAlert(double latitude, double longitude, String id, int pointRadius) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(PROX_ALERT_INTENT + id);
            intent.putExtra(Constants.PLACE_KEY, id);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (pointRadius < 1) {
                pointRadius = 1;
            }

            locationManager.addProximityAlert(
                    latitude,
                    longitude,
                    pointRadius,
                    PROX_ALERT_EXPIRATION,
                    proximityIntent
            );

            pendingIntents.add(proximityIntent);

            ProximityIntentReceiver receiver = new ProximityIntentReceiver();
            registerReceiver(receiver, new IntentFilter(PROX_ALERT_INTENT + id));
            receivers.add(receiver);
            requestCode++;
        }
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, CurrentLocation.location)) {
                CurrentLocation.location = location;
                changeLocation();
            }
        }
        public void onStatusChanged(String s, int i, Bundle b) {
        }
        public void onProviderDisabled(String s) {
        }
        public void onProviderEnabled(String s) {
        }
    }

    private void changeLocation() {
        CurrentLocation.lat = CurrentLocation.location.getLatitude();
        CurrentLocation.lon = CurrentLocation.location.getLongitude();
        if (!CurrentLocation.check && !PlaceServiceLayer.isBusy()) {
            CurrentLocation.check = true;
            PlaceServiceLayer.calculateData();
            EventBus.getDefault().post(new Events.UpdateLocation());
           /* ArrayList<Place> places = new ArrayList<Place>(DBHelper.getInstance(App.getInstance()).getPlaces().values());
            for (int i = 0; i < places.size(); i++) {
                Place place = places.get(i);
                if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                    place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                    place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                }
            }

            if (DBHelper.getInstance(App.getInstance()).updatePlaces(places)) {
                EventBus.getDefault().post(new Events.UpdateLocation());
            }*/
        }
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_DIFFERENCE_THRESHOLD;
        boolean isSignificantlyOlder = timeDelta < -TIME_DIFFERENCE_THRESHOLD;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
