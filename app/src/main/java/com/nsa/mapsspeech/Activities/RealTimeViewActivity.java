package com.nsa.mapsspeech.Activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.nsa.mapsspeech.Directions.DirectionHelper;
import com.nsa.mapsspeech.Directions.DirectionPointListener;
import com.nsa.mapsspeech.Directions.GetPathFromLocation;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.Model.LocationModel;
import com.nsa.mapsspeech.Model.UserDataModel;
import com.nsa.mapsspeech.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RealTimeViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String id = null;
    Marker mylocationMarker= null,userLocationMarker=null;
    TextView batteryLevelTV, speedTV, statusTV, dataTV;

    LatLng myLocation = null, userLocation = null;
    private FusedLocationProviderClient locationProviderClient;
    FloatingActionButton fabStartNav,fabCloseNav;
    private boolean mapTypeChanged = false;
    Switch switchOnOff;
    private boolean follow = false;
    private String TAG = "RealTimeActivity";
    private Polyline routePloyline=null,currentPolyline=null;
    List<LatLng> currentList=null;
    private CountDownTimer timer;
    public static JSONArray jLegs=null;
    public static JSONArray jSteps=null;
    public static String routeData=null;
    private String string="";
    SharedPreferences sharedpreferences;
    String jLegsArray = "jLegs";
    String jStepsArray = "jLegs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_real_time_view);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }

            id="PIxL2Whuu9PrKfwL2go3mAPqZbG3";


        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        batteryLevelTV = findViewById(R.id.batteryLevelTextView);
        speedTV = findViewById(R.id.speedTextView);
        statusTV = findViewById(R.id.statusTextView);
        switchOnOff = findViewById(R.id.switchOnOff);
        fabCloseNav=findViewById(R.id.fabNavigationCancel);
        fabStartNav=findViewById(R.id.fabNavigationStart);
        dataTV = findViewById(R.id.data);
        Drawable img = speedTV.getContext().getResources().getDrawable(R.drawable.speed);
        speedTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);


        batteryLevelTV.setText("50");
        speedTV.setText(1200 + "");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchOnOff.setText("on");
                    follow = true;


                } else {
                    switchOnOff.setText("off");
                    follow = false;
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(true);


//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.style_json));
//
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e(TAG, "Can't find style. Error: ", e);
//        }
        if (!checkPermissions()){
            return;
        }

       getLastLocation();
        mapUISettings(true);



        if (id != null) {

            viewRealTimeLocation();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20);
                mMap.animateCamera(update, 2000, null);
                return true;
            }
        });

    }

    private void createDriveMarker() {
        MarkerOptions markerOptions = new MarkerOptions().position(myLocation)
                .anchor(0.5f, 0.5f)
                .icon(  BitmapDescriptorFactory.fromBitmap(
                        createCustomMarker(RealTimeViewActivity.this,
                                getResources().getDrawable(R.drawable.navigation_black ))));
         mylocationMarker = mMap.addMarker(markerOptions);
         mylocationMarker.setVisible(false);
         mylocationMarker.setFlat(true);

    }

    @SuppressLint("MissingPermission")
    private void mapUISettings(boolean b) {
        if(b){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        }else{
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData(true);
                        } else {
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            myLocation =latLng;
                            createDriveMarker();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
                    Toast.makeText(RealTimeViewActivity.this, "My Location = " + myLocation, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this, "Provide Location Permission To App First!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(boolean b) {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
            if(b){
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }else{
                locationProviderClient.removeLocationUpdates(mLocationCallback).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("locationCallback", "call back removed");
                    }
                });
            }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            myLocation=latLng;
            animateMarker(mylocationMarker,myLocation);
            Log.e("locationCallback", "call back "+latLng);
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void viewRealTimeLocation() {
        new FireBase().getReferenceUsers().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    UserDataModel model = snapshot.getValue(UserDataModel.class);
                    getRealTimeLocation(model);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    boolean markerCreated = false;

    private void getRealTimeLocation(UserDataModel model) {


        new FireBase().getReferenceLocationUpdate().child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    LocationModel locationModel = snapshot.getValue(LocationModel.class);

                    changeOtherUnits(locationModel);
                    LatLng latLng = new LatLng(Double.parseDouble(locationModel.getLat()),
                            Double.parseDouble(locationModel.getLng()));
                    userLocation = latLng;
                    if (!markerCreated) {
                        createCustomMarkerForUrl(RealTimeViewActivity.this,model);
                        markerCreated = true;
                    } else {
                        animateMarker(userLocationMarker,userLocation);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void changeOtherUnits(LocationModel locationModel) {

        String status = locationModel.getStatus();
        statusTV.setText(status);
        double speedInKm = (Double.parseDouble(locationModel.getSpeed())) * 3.6;
        speedTV.setText((int) speedInKm + "km/hr");
        int battryLevel = Integer.parseInt(locationModel.getChargingLevel());
        batteryLevelTV.setText(battryLevel + "%");
        if (battryLevel < 20) {
            Drawable img = batteryLevelTV.getContext().getResources().getDrawable(R.drawable.batter_low);
            batteryLevelTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        }
        if (locationModel.isCharging()) {

            Drawable img = batteryLevelTV.getContext().getResources().getDrawable(R.drawable.battery_charging);
            batteryLevelTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        } else {
            if (!(battryLevel < 20)) {
                Drawable img = batteryLevelTV.getContext().getResources().getDrawable(R.drawable.battery_std);
                batteryLevelTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            }
        }
        if (status.equals("on")) {
            Drawable img = statusTV.getContext().getResources().getDrawable(R.drawable.location_on);
            statusTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        } else {
            Drawable img = statusTV.getContext().getResources().getDrawable(R.drawable.location_off);
            statusTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        }

        changeDrawable(!mapTypeChanged);


    }

    public void animateMarker(Marker marker,final LatLng toPosition) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (follow) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 19);
                    mMap.animateCamera(update);
                }
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);

                }
            }
        });
    }

    public void createCustomMarkerForUrl(Context context,UserDataModel model) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);

        Picasso.get()
                .load(Uri.parse(model.getProfileUrl()))
                .into(markerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("Upon Marker","image add onSucces");
                        addImageToMarker((Activity) context, model, marker);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Upon Marker","image add OnError"+e);
                        markerImage.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
                        addImageToMarker((Activity) context, model, marker);
                    }

                });



    }

    private void addImageToMarker(Activity context, UserDataModel model, View marker) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        MarkerOptions markerOptions = new MarkerOptions().position(userLocation)
                .anchor(0.5f, 0.5f)
                .title(model.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        userLocationMarker = mMap.addMarker(markerOptions);
        Log.e("Uponmarker","Marker  added for profile");
    }

    public static Bitmap createCustomMarker(Context context,Drawable drawable) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageDrawable(drawable);
        markerImage.setRotation(-45);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        return bitmap;
    }
    public void zoomIn(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOut(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }


    public void changeMapType(View view) {
        changeDrawable(mapTypeChanged);
        if (mapTypeChanged) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            batteryLevelTV.setTextColor(getResources().getColor(R.color.black));
            speedTV.setTextColor(getResources().getColor(R.color.black));
            statusTV.setTextColor(getResources().getColor(R.color.black));
            mapTypeChanged = false;
        } else {
            batteryLevelTV.setTextColor(getResources().getColor(R.color.white));
            speedTV.setTextColor(getResources().getColor(R.color.white));
            statusTV.setTextColor(getResources().getColor(R.color.white));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapTypeChanged = true;
        }
    }

    private void changeDrawable(boolean b) {
        Drawable[] batteryDrawables = batteryLevelTV.getCompoundDrawables();
        Drawable[] speedDrawables = speedTV.getCompoundDrawables();
        Drawable[] statusDrawables = statusTV.getCompoundDrawables();


        Drawable battery = null, speed = null, status = null;
        for (int i = 0; i < speedDrawables.length; i++) {
            if (batteryDrawables[i] != null) {
                battery = DrawableCompat.wrap(batteryDrawables[i]);
            }
            if (speedDrawables[i] != null) {
                speed = DrawableCompat.wrap(speedDrawables[i]);
            }
            if (statusDrawables[i] != null) {
                status = DrawableCompat.wrap(statusDrawables[i]);
            }
        }


        if (b) {
            DrawableCompat.setTint(battery, getResources().getColor(R.color.black));
            batteryLevelTV.setCompoundDrawables(battery, null, null, null);
            DrawableCompat.setTint(speed, getResources().getColor(R.color.black));
            speedTV.setCompoundDrawables(speed, null, null, null);
            DrawableCompat.setTint(status, getResources().getColor(R.color.black));
            statusTV.setCompoundDrawables(status, null, null, null);
        } else {
            DrawableCompat.setTint(battery, getResources().getColor(R.color.white));
            batteryLevelTV.setCompoundDrawables(battery, null, null, null);
            DrawableCompat.setTint(speed, getResources().getColor(R.color.white));
            speedTV.setCompoundDrawables(speed, null, null, null);
            DrawableCompat.setTint(status, getResources().getColor(R.color.white));
            statusTV.setCompoundDrawables(status, null, null, null);
        }
    }

    public void getRoute() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String route = sh.getString("route", "");
        if(route.isEmpty()){
            Log.e(TAG,"route not exist");
           if(userLocation==null){
               Toast.makeText(this, "User Location Null", Toast.LENGTH_SHORT).show();
               return;
           }
        new GetPathFromLocation(myLocation, userLocation, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {

                routePloyline=mMap.addPolyline(polyLine);
                fabStartNav.setVisibility(View.VISIBLE);
                saveDataToSp();
                if(jLegs!=null){
                    try{
                    JSONObject legsObjects = jLegs.getJSONObject(0);
                    JSONObject distanceObject = legsObjects.getJSONObject("distance");
                    String distance = distanceObject.getString("text");

                    JSONObject time = legsObjects.getJSONObject("duration");
                    String duration = time.getString("text");

                    String startAddress = legsObjects.getString("start_address");
                    String endAddress = legsObjects.getString("end_address");
                    String text="Start Address = "+startAddress+"\n"
                            +"End Address = "+endAddress+"\n"
                            +"Total Distance = "+distance+"\n"
                            +"Total Duration = "+duration;
                        string=text;
                    dataTV.setText(string);

                } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(jSteps!=null){

                }


            }
        }).execute();
        }else{
            Log.e(TAG,"route exist");
              routeData=route;

            routePloyline=mMap.addPolyline(getSavedRoute(route));
            fabStartNav.setVisibility(View.VISIBLE);
                try{
                    JSONObject legsObjects = jLegs.getJSONObject(0);
                    JSONObject distanceObject = legsObjects.getJSONObject("distance");
                    String distance = distanceObject.getString("text");

                    JSONObject time = legsObjects.getJSONObject("duration");
                    String duration = time.getString("text");

                    String startAddress = legsObjects.getString("start_address");
                    String endAddress = legsObjects.getString("end_address");
                    String text="Start Address = "+startAddress+"\n"
                            +"End Address = "+endAddress+"\n"
                            +"Total Distance = "+distance+"\n"
                            +"Total Duration = "+duration;
                    string=text;
                    dataTV.setText(string);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


    }
    }

    private void saveDataToSp() {


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);


        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        if(routeData!=null){
            myEdit.putString("route", routeData);
            Log.e(TAG,"route saved");
        }else{
            Log.e(TAG,"route not saved");
        }
        myEdit.commit();
    }

    private void startMovingOnPolyline() {
        mylocationMarker.setVisible(true);
        List<LatLng> list=routePloyline.getPoints();


        int size=list.size();


        timer=new CountDownTimer(size*1000,500 ) {
            int count=0;

            LatLng oldLocation, newLocation;


            @Override
            public void onTick(long millisUntilFinished) {


                if(list.size()==count){
                    this.cancel();
                }else{

                    oldLocation=list.get(count);
                    newLocation=list.get(count++);
                    if(currentPolyline==null){
                        checkPath(newLocation);

                    }else{
                        if(currentList!=null) {

                        }

                    }

                animateMarker(mylocationMarker,newLocation);

                    float bearing = getBearing(oldLocation, newLocation);
                 //   upDatecamera(newLocation,90,10,bearing);
                    mylocationMarker.setRotation(bearing);


//                Log.e("count",count+"");
            }}

            @Override
            public void onFinish() {

            }
        };

    }

    private void checkPath(LatLng newLocation) {
        try{
        JSONObject jsonObject = new JSONObject(routeData);
        JSONArray jRoutes =jsonObject.getJSONArray("routes");
        for (int p = 0; p < jRoutes.length(); p++) {
            JSONArray legsArr = ((JSONObject) jRoutes.get(p)).getJSONArray("legs");
        for (int i = 0; i < legsArr.length(); i++) {
            // get array of steps
            JSONArray stepsArr = legsArr.getJSONObject(i).getJSONArray("steps");
            // for each step
            for (int j = 0; j < stepsArr.length(); j++) {
                // get step
                JSONObject step = stepsArr.getJSONObject(j);

                JSONObject distanceObject = step.getJSONObject("distance");
                JSONObject durationObject = step.getJSONObject("duration");

                String distance = distanceObject.getString("text");
                String duration = durationObject.getString("text");
                String text="Distance "+distance+"\n"+"Duration = "+duration;
                dataTV.setText(string+"\n"+text);
             // Log.e(TAG,text);


                JSONObject polyline = step.getJSONObject("polyline");
                String encodedPoints = polyline.getString("points");
                // decode encoded path to list of points LatLng
                List<LatLng> decodedPoints = PolyUtil.decode(encodedPoints);
                if (PolyUtil.isLocationOnPath(newLocation, decodedPoints, true, 100)) {
                    currentPolyline=null;
                   currentList=null;
                     currentList=decodedPoints;
                    currentPolyline=mMap.addPolyline(new PolylineOptions().addAll(decodedPoints).color(Color.RED).width(5));

                   break;
                }
            }
        }
        }} catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void upDatecamera(LatLng userPosition,float tilt,float zoom, float bearing ){

        LatLng mapCenter = userPosition;
        Projection projection = mMap.getProjection();
        Point centerPoint = projection.toScreenLocation(mapCenter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;

        centerPoint.y = centerPoint.y - (int) (displayHeight / 5);

        LatLng newCenterPoint = projection.fromScreenLocation(centerPoint);

//        mMap.animateCamera(CameraUpdateFactory.newLatLng(newCenterPoint));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newCenterPoint)
                .tilt(tilt)
                .zoom(zoom)
                .bearing(bearing)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    private float getBearing(LatLng begin, LatLng end) {
        double dLon = (end.longitude - begin.longitude);
        double x = Math.sin(Math.toRadians(dLon)) * Math.cos(Math.toRadians(end.latitude));
        double y = Math.cos(Math.toRadians(begin.latitude))*Math.sin(Math.toRadians(end.latitude))
                - Math.sin(Math.toRadians(begin.latitude))*Math.cos(Math.toRadians(end.latitude)) * Math.cos(Math.toRadians(dLon));
        double bearing = Math.toDegrees((Math.atan2(x, y)));
        return (float) bearing;
    }
    public void getWay(View view) {
        getRoute();

    }


    public void startNaviagtion(View view) {

        if(routePloyline!=null) {
            mapUISettings(false);
            fabStartNav.setVisibility(View.INVISIBLE);
            fabCloseNav.setVisibility(View.VISIBLE);
          //  requestNewLocationData(true);
            mylocationMarker.setVisible(true);
            startMovingOnPolyline();
            timer.start();
        }else{
            Toast.makeText(this, "Get Route First", Toast.LENGTH_SHORT).show();
        }


    }

    public void cancelNavigation(View view) {
        mapUISettings(true);
        fabStartNav.setVisibility(View.VISIBLE);
        fabCloseNav.setVisibility(View.INVISIBLE);
      //  requestNewLocationData(false);
        timer.cancel();
    }

    private PolylineOptions getSavedRoute(String data){
        try{
        JSONObject jsonObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jsonObject = new JSONObject(data);
            // Starts parsing data
            DirectionHelper helper = new DirectionHelper();
            routes = helper.parse(jsonObject);
            Log.e(TAG, "Executing Routes : "/*, routes.toString()*/);


            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = routes.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(13);
                lineOptions.color(Color.BLACK);

                Log.e(TAG, "PolylineOptions Decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                return lineOptions;
            } else {
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception in Executing Routes : " + e.toString());
            return null;
        }

    } catch (Exception e) {
        Log.e(TAG, "Background Task Exception : " + e.toString());
        return null;
    }
}


}