package com.nsa.mapsspeech.Location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nsa.mapsspeech.Activities.RealTimeViewActivity;

public class LocationClass extends AsyncTask<String, Void, LatLng> {

    FusedLocationProviderClient providerClient;
    Context context;
    LocationClassListener locatioCallBack;


    LatLng location = null;

    public LocationClass(Context context, LocationClassListener locationClassListener) {
        this.context = context;
        this.locatioCallBack = locationClassListener;
        providerClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "No permission", Toast.LENGTH_SHORT).show();
          return null;
        }
        providerClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location myLocation = task.getResult();
                 if(myLocation==null){

                 }
                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                location = latLng;
                Log.e("location", "got location" + latLng);
            }
        });
        return location;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        if (locatioCallBack != null && latLng != null){
            locatioCallBack.onLocation(latLng);
    }
    else{
        Log.e("idEmpty",locatioCallBack+"  "+latLng);
    }
    }
}

