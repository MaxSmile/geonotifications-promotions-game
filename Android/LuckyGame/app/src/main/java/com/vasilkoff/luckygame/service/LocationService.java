package com.vasilkoff.luckygame.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.activity.HomeActivity;
import com.vasilkoff.luckygame.database.DBHelper;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.receiver.ProximityIntentReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 23.02.2017.
 */

public class LocationService extends Service {

    private static final String DISCONNECT_VPN = "com.vasilkoff.luckygame.stop";

    private static final String PROX_ALERT_INTENT = "com.vasilkoff.luckygame.";

    private int requestCode = 100;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    private static final long POINT_RADIUS = 1000; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private LocationManager locationManager;



    private List<ProximityIntentReceiver> receivers;
    private List<PendingIntent> pendingIntents;

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

            LocationListener locationListener = new MyLocationListener();

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

            Intent intent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Intent disconnectVPN = new Intent(this, LocationService.class);
            disconnectVPN.setAction(DISCONNECT_VPN);
            PendingIntent disconnectPendingIntent = PendingIntent.getService(
                    this, 0, disconnectVPN, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .addAction(R.mipmap.ic_launcher, getString(R.string.location_service_stop), disconnectPendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Notification notification = builder.build();
            startForeground(777, notification);

            receivers = new ArrayList<ProximityIntentReceiver>();
            pendingIntents = new ArrayList<PendingIntent>();

            Log.i("Test", "Service: onCreate");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Test", "Service: onStartCommand");
        if (intent != null && DISCONNECT_VPN.equals(intent.getAction())) {
            Log.i("Test", "Service: STOP");
            stopSelf();
            return START_NOT_STICKY;
        }

        loadPlaces();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ProximityIntentReceiver receiver : receivers) {
            unregisterReceiver(receiver);
        }
        Log.i("Test", "Service: onDestroy");
    }

    private void loadPlaces() {
      /*  if (receivers != null) {
            for (int i = 0; i < receivers.size(); i++) {
                unregisterReceiver(receivers.get(i));
            }
        } else {
            receivers = new ArrayList<ProximityIntentReceiver>();
        }*/

       /* if (pendingIntents != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                for (PendingIntent pendingIntent : pendingIntents) {
                    locationManager.removeProximityAlert(pendingIntent);
                }
            }
        } else {
            pendingIntents = new ArrayList<PendingIntent>();
        }*/



        ArrayList<Place> uniquePlaces = new DBHelper(this).getPlacesList();
        for (int i = 0; i < uniquePlaces.size(); i++) {
            Place place = uniquePlaces.get(i);
            /*addProximityAlert(place.getLat(), place.getLon(), place.getNameCompany());
            Log.i("Test", "Service: place = " + place.getNameCompany());*/
        }

    }

    private void addProximityAlert(double latitude, double longitude, String id) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(PROX_ALERT_INTENT + id);
            intent.putExtra("company", id);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            locationManager.addProximityAlert(
                    latitude,
                    longitude,
                    POINT_RADIUS,
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
            Log.i("Test", "Service: location Latitude = " + location.getLatitude());
            Log.i("Test", "Service: location Longitude = " + location.getLongitude());
        }
        public void onStatusChanged(String s, int i, Bundle b) {
        }
        public void onProviderDisabled(String s) {
        }
        public void onProviderEnabled(String s) {
        }
    }

}
