package com.nsa.mapsspeech.Services;


import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.nsa.mapsspeech.Activities.MapsActivity;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.Model.LocationModel;
import com.nsa.mapsspeech.R;

import static com.nsa.mapsspeech.Activities.MapsActivity.fuser;


public class BackgroundService extends Service {

    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "com.nsa.backgroundlocation" +
            ".started_from_notification";
    private static final String TAG ="BackGroundService" ;
  private final IBinder mbinder = new LocalBinder();

    private static final String CHANNEL_ID = "my_channel";
    private static final long UPDATE_INTERVAL_IN_MIL = 3000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL / 2;
    private static final int NOTI_ID = 1223;

    private NotificationManager notificationManager;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private Location mLocation;

    private int batteryLevel;
    private boolean isBatteryCharging;
    private BroadcastReceiver mReceiver;
    private DatabaseReference referenceLocation=new FireBase().getReferenceLocationUpdate();
    LocationModel locationModel;

    public BackgroundService() {
    }


    @Override
    public void onCreate() {
        Log.e("Bind","onCreate");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();


        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel=new NotificationChannel(CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        boolean startedFromNotification= intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,false);
        Log.e("Bind","onStartCommand "+startedFromNotification);
        if(startedFromNotification){
            removeLocationUpdates();
            stopSelf();
        }

        if(com.nsa.mapsspeech.Services.Common.requestingLocationUpdates(this)){
            startForeground(NOTI_ID,getNotification());
        }
        return START_NOT_STICKY;
    }




    public void removeLocationUpdates() {
        try{
            locationModel=new LocationModel(mLocation.getLatitude()+"",mLocation.getLongitude()+"",
                    "off",mLocation.getSpeed()+"",batteryLevel+"",isBatteryCharging);
            referenceLocation.child(fuser.getUid())
                    .setValue(locationModel);

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            com.nsa.mapsspeech.Services.Common.setRequestLocationUpdates(this,false);
            stopForeground(true);
            unregisterReceiver(mReceiver);
            stopSelf();
        } catch (SecurityException ex) {
            com.nsa.mapsspeech.Services.Common.setRequestLocationUpdates(this,true);
            Log.e(TAG,"lost location permission could not remove updates"+ex);
        }catch (Exception e){
            Log.e(TAG,"Exception"+e);
        }

    }

    private void getLastLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull  Task<Location> task) {

                            if(task.isSuccessful() && task.getResult()!=null){
                                mLocation=task.getResult();
                            }else{
                                Log.e(TAG,"Failed to get Location");
                            }
                        }
                    });
    } catch (SecurityException e) {
            Log.e(TAG,"lost location permission "+e);
        }

    }

    private void createLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MIL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MIL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location lastLocation) {
        mLocation=lastLocation;
        if(mLocation!=null){
            locationModel=new LocationModel(mLocation.getLatitude()+"",mLocation.getLongitude()+"",
                                "on",mLocation.getSpeed()+"",batteryLevel+"",isBatteryCharging);
                        referenceLocation.child(fuser.getUid())
                    .setValue(locationModel);
        }

//        if(serviceIsRunningInForeground(this)){
//            notificationManager.notify(NOTI_ID,getNotification());
//        }
    }

    private Notification getNotification() {
        Intent intent=new Intent(this,BackgroundService.class);
        String text= com.nsa.mapsspeech.Services.Common.getLocationText(mLocation);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION,true);
        PendingIntent servicePendingIntent=PendingIntent.getService(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent activityPendingIntent=PendingIntent.getActivity(this,0,
                new Intent(this, MapsActivity.class),0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .addAction(R.drawable.launch,"Launch",activityPendingIntent)
                .addAction(R.drawable.close_icon,"Remove",servicePendingIntent)
                .setContentText(text)
                .setContentTitle(com.nsa.mapsspeech.Services.Common.getLocationTitle(this))
                .setOngoing(true)
                .setNotificationSilent()
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        builder.setChannelId(CHANNEL_ID);
        }

        return builder.build();
    }

    private boolean serviceIsRunningInForeground(Context context) {
        ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(getClass().getName().equals(service.service.getClassName())){
                if(service.foreground){
                    return true;
                }
            }
        }
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind","onBind");

        return mbinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e("Bind","onRebind");

        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return true;
    }

    @Override
    public void onDestroy() {
        Log.e("Bind","onDestroy");
        super.onDestroy();
    }

    public void requestLocationUpdates() {

        com.nsa.mapsspeech.Services.Common.setRequestLocationUpdates(this,true);
        startService(new Intent(getApplicationContext(),BackgroundService.class));
        try {
            mReceiver = new BatteryBroadcastReceiver();
            registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        }catch (SecurityException ex){

            Log.e(TAG,"lost location permission could not request it "+ex);
        }catch (Exception e){
            Log.e(TAG,"Execption"+e);
        }
    }

    public class LocalBinder extends Binder {

        public BackgroundService getService(){
            return BackgroundService.this;
        }
    }
    private class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isBatteryCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
        }
    }
}
