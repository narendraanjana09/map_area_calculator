package com.nsa.mapsspeech;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.SphericalUtil;
import com.nsa.mapsspeech.ExtraClasses.LanguageHelper;
import com.nsa.mapsspeech.ExtraClasses.StartSpeechRecognition;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView areaTextView,lengthTextView;
    EditText searchEditText;
    private static  final int FINE_LOCATION_REQUEST_CODE=12;

    private FusedLocationProviderClient locationProviderClient;


    private LocationRequest locationRequest;

    FloatingActionButton fabMapType,fabOtherOptions,fabMic,fabShareLocation,fabCancelLocationShare;

    int isMapNumber=1;
    boolean isOtherOptionsVisible=false;
    boolean needMic=true;

    List<LatLng> latLngList=new ArrayList<>();
    LatLng userLocation,shareLocation=null;

    Polygon polygon;
    Polyline polyline;
    Switch switchLanguageChanger;

    private LinearLayout zoomLayout,calculatorLayout,linksLayout;
    AdView adView1,adView2;
    private InterstitialAd mInterstitialAd;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private String TAG="MapsActivity";

    private static final int SPEECH_REQUEST=10;
    private Animation animation_open,animation_close;
    Context context;
    Resources resources;
    public static final String default_area = "Default_Area";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        resources=getResources();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        switchLanguageChanger=findViewById(R.id.changeLanguageSwitch);
        changeLanguage();
        adView1 = findViewById(R.id.adView1);
        adView2 = findViewById(R.id.adView2);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBannerAds();
                loadInterstialAds();
                loadRewardedInterstitialAds();
            }
        });


        prepareLocationService();

        sharedpreferences = getSharedPreferences(default_area, Context.MODE_PRIVATE);
        setDefaultArea(resources.getString(R.string.default_area));
        fabMapType=findViewById(R.id.fabMapType);
        fabMic=findViewById(R.id.fabMic);
        fabOtherOptions=findViewById(R.id.fabMoreOptions);
        fabShareLocation=findViewById(R.id.fabShareLocation);
        fabCancelLocationShare=findViewById(R.id.fabCancelLocationShare);
        zoomLayout=findViewById(R.id.ZoomLayout);
        areaTextView=findViewById(R.id.areaTextView);
        lengthTextView=findViewById(R.id.lengthTextView);
        calculatorLayout=findViewById(R.id.CalculatorLayout);
        searchEditText=findViewById(R.id.searchEditText);
        linksLayout=findViewById(R.id.links);
        setSearchListener();
        setSearchTextChangeListener();
        animation_open= AnimationUtils.loadAnimation(this,R.anim.rotate_open);
        animation_close= AnimationUtils.loadAnimation(this,R.anim.rotate_close);






    }

    public void changeLanguage(){
        switchLanguageChanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    context = LanguageHelper.setLocale(MapsActivity.this, "hi");
                    resources = context.getResources();
                    switchLanguageChanger.setText("hi");

                }else{
                    context = LanguageHelper.setLocale(MapsActivity.this, "en");
                    resources = context.getResources();
                    switchLanguageChanger.setText("en");

                }
            }
        });
    }
    private void loadRewardedInterstitialAds() {
        RewardedInterstitialAd.load(MapsActivity.this, getString(R.string.rewardedInterstitialAdUnitId),
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.show(MapsActivity.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                Log.i(TAG, "reward = "+rewardItem);
                            }
                        });
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.i(TAG, "onAdFailedToShowFullScreenContent");
                            }

                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.i(TAG, "onAdShowedFullScreenContent");
                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.i(TAG, "onAdDismissedFullScreenContent");
                            }
                        });
                        Log.e(TAG, "onAdLoaded");
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e(TAG, "onAdFailedToLoad");
                    }
                });
    }

    private void loadInterstialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.interstitialAdUnitId), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MapsActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }



    private void loadBannerAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
        adView2.loadAd(adRequest);
    }

    private void setDefaultArea(String s) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(default_area, s);
        editor.apply();
    }

    private void setSearchTextChangeListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name=editable.toString();
                if(name.length()==0){
                     needMic=true;
                    changeMicDrawable();
                }else{
                   needMic=false;
                    changeMicDrawable();

                }

            }
        });
    }

    private void changeMicDrawable() {
        if(needMic){
            fabMic.setImageDrawable(getResources().getDrawable(R.drawable.mic));
            return;
        }
        fabMic.setImageDrawable(getResources().getDrawable(R.drawable.close_icon));
    }

    private void setSearchListener() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id== EditorInfo.IME_ACTION_SEARCH){
                    searchAddress();
                    return true;
                }
                return false;
            }
        });
    }


    private void searchAddress() {

                // on below line we are getting the
                // location name from search view.
                String location = searchEditText.getText().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);

                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        // on below line we are adding marker to that position.
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                        // below line is to animate camera to that position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException exception){
                        Toast.makeText(MapsActivity.this, resources.getString(R.string.no_place_toast), Toast.LENGTH_SHORT).show();
                    }
                    // on below line we are getting the location
                    // from our list a first position.


    }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if(isLocationEnabled(MapsActivity.this)){
            showMeUserCurrentLoaction();
        }else{
            showLocationEnableDialog();
        }



    }




    private void getLocationPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST_CODE);
    }
    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == FINE_LOCATION_REQUEST_CODE || requestCode == 101){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(isLocationEnabled(MapsActivity.this)){
                showMeUserCurrentLoaction();
                }else{
                    showLocationEnableDialog();
                }
            }else{
               giveLocationAlert();

            }
        }
    }

    private void showLocationEnableDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Your Loaction Service Is Disabled!")
                .setMessage("Enable It To Get Your current Location!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
    }

    private void giveLocationAlert() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(resources.getString(R.string.location_access_title))
                .setMessage(resources.getString(R.string.location_access_message))
                .setPositiveButton(resources.getString(R.string.access_grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(resources.getString(R.string.access_deny), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SPEECH_REQUEST&&resultCode==RESULT_OK){
            ArrayList<String> wordsSpeaked=data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            float[] confidLevels=data.getFloatArrayExtra(
                    RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
            if(wordsSpeaked.size()>0) {
                searchEditText.setText(wordsSpeaked.get(0));
                searchAddress();
            }

        }
    }
    int count=0;
    private void showMeUserCurrentLoaction() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            getLocationPermission();
        }else{


            mMap.setMyLocationEnabled(true);


           Task<Location> currentLocation=locationProviderClient.getLastLocation();
            currentLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location==null){
                        if(count==0) {
                            count=1;
                            prepareLocationService();
                        }
                        new CountDownTimer(3000,1000){

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                showMeUserCurrentLoaction();
                            }
                        };


                    }else{
                    userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    CameraUpdate update=CameraUpdateFactory.newLatLngZoom(userLocation,16);
                    mMap.animateCamera(update);
                }
                }
            });



        }

    }
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData() {
//
//        // Initializing LocationRequest
//        // object with appropriate methods
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(5);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        // setting LocationRequest
//        // on FusedLocationClient
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            location = locationResult.getLastLocation();
//
//
//        }
//    };
    private void prepareLocationService(){
      locationProviderClient= LocationServices.getFusedLocationProviderClient(this);
    }

    public void zoomIn(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOut(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    public void search(View view) {
        loadBannerAds();
         if(needMic){
        new StartSpeechRecognition(MapsActivity.this).listenToUser();
        return;
    }
         needMic=true;
         searchEditText.setText("");
         changeMicDrawable();

    }

    public void changeMapType(View view) {
        loadInterstialAds();
        loadBannerAds();
        YoYo.with(Techniques.Tada)
                .duration(500)
                .repeat(2)
                .playOn(fabMapType);
       changeMap();

    }
    public void changeMap(){
        if(isMapNumber==1){
            isMapNumber=2;
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            fabMapType.setImageDrawable(getResources().getDrawable(R.drawable.satellite));
        }else if(isMapNumber==2){
            isMapNumber=3;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            fabMapType.setImageDrawable(getResources().getDrawable(R.drawable.terrain));
        }else{
            isMapNumber=1;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            fabMapType.setImageDrawable(getResources().getDrawable(R.drawable.map));

        }
    }

    public void refreshMap(View view) {
        loadInterstialAds();
        clearAll();
        showLinks(false);
    }
    public void clearAll(){

        polygon=null;
        polyline=null;
        lengthTextView.setText("");
        lengthTextView.setVisibility(View.INVISIBLE);
        areaTextView.setText("");
        areaTextView.setVisibility(View.INVISIBLE);
        mMap.clear();

        latLngList.clear();

    }

    public void calculateArea(View view) {
        loadBannerAds();
        getWhatUserWantToCalculate();
        clearAll();

           

    }
    String lengthText;
    private void getWhatUserWantToCalculate() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(resources.getString(R.string.check_calculate_title))
                .setPositiveButton(resources.getString(R.string.area_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lengthText=resources.getString(R.string.perimeter_text)+"👇";
                        addMarkerOnMap(true,false);
                    }
                })
                .setNegativeButton(resources.getString(R.string.length_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lengthText=resources.getString(R.string.length_text)+"👇";
                        addMarkerOnMap(false,false);
                    }
                })
                .create();
        dialog.show();
        
    }
    private void addMarkerOnMap(boolean area,boolean isLocation){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {



                if(isLocation){
                    mMap.clear();
                    shareLocation=latLng;
                    addMarker(latLng,"");
                }else {
                    addMarker(latLng,latLngList.size()+"");
                    latLngList.add(latLng);
                    if (area) {

                        calculateAreaofPolygon();

                    } else {
                        calculateLengthOfPolyline();
                    }

                }
            }
        });
    }
    private void calculateLengthOfPolyline() {
        PolylineOptions polylineOptions=new PolylineOptions();
                for(LatLng latLng:latLngList){
                    polylineOptions.add(latLng);
                }

         polyline = mMap.addPolyline(polylineOptions);
        stylePolyLine();
        showLengthToUser(true);

    }


    private void showLengthToUser(boolean isLength) {
        double doublePeri=0.0;
        if(!isLength){
            doublePeri=SphericalUtil.computeDistanceBetween(latLngList.get(0),
                    latLngList.get(latLngList.size()-1));
        }
        double computerLength=SphericalUtil.computeLength(latLngList)+doublePeri;
        lengthTextView.setVisibility(View.VISIBLE);
        double inMeter=getRoundValue(computerLength);           // 1metre =  3.281 feet
        double inKiloMeter=getRoundValue((computerLength/1000)); // 1 acre = 4,049 sqmetre
        double inFeet=getRoundValue(computerLength*3.281);
        lengthTextView.setText(lengthText+"\nKM = "+inKiloMeter+"\nMeter = "+inMeter
                +"\nFeet = "+inFeet);

    }
    private double getRoundValue(double value){
        DecimalFormat df = new DecimalFormat("###.#");
        return Double.parseDouble(df.format(value));
    }


    private void stylePolyLine() {
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(10);
        polyline.setColor(getResources().getColor(R.color.black));
        polyline.setJointType(JointType.ROUND);
    }



    private void addMarker(LatLng latLng,String title) {

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);
    }

    private void calculateAreaofPolygon() {
      PolygonOptions polygonOptions=new PolygonOptions();
       for(LatLng latLng:latLngList){
           polygonOptions.add(latLng);
       }
         polygon = mMap.addPolygon(polygonOptions);
       showLengthToUser(false);
        showAreaToUser(SphericalUtil.computeArea(latLngList));
                stylePolygon();
    }




            public double getDefaultAreaValue(){
                String s1 = sharedpreferences.getString(default_area, "");
                return Double.parseDouble(s1);
            }
    private void showAreaToUser(double computeArea) {

        areaTextView.setVisibility(View.VISIBLE);
        double inSqMeter=getRoundValue(computeArea);           // 1metre =  3.281 feet
        double inBiga=getRoundValue((computeArea/getDefaultAreaValue())); // 1 acre = 4,049 sqmetre
        double inSqFeet=getRoundValue(computeArea*3.281*3.281);
        double inAcres=getRoundValue(computeArea/4049);
        areaTextView.setText(resources.getString(R.string.area_text)+"👇\n"+resources.getString(R.string.bigha_text)+" = "+inBiga+"\nSqMeter = "+inSqMeter
                             +"\nSqFeet = "+inSqFeet+"\nAcres = "+inAcres);


    }


    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;

    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    private void stylePolygon() {
        polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(getResources().getColor(R.color.dark));
        polygon.setFillColor(getResources().getColor(R.color.teal_700));
    }


    public void openOtherOptions(View view) {

        if(isOtherOptionsVisible){
            isOtherOptionsVisible=false;
            fabOtherOptions.startAnimation(animation_close);
            showLinks(false);
            zoomLayout.setVisibility(View.INVISIBLE);
            calculatorLayout.setVisibility(View.INVISIBLE);

        }else{
            isOtherOptionsVisible=true;
          fabOtherOptions.startAnimation(animation_open);
        zoomLayout.setVisibility(View.VISIBLE);
        calculatorLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeInUp)
                .duration(1200)
                .playOn(zoomLayout);
        YoYo.with(Techniques.FadeInRight)
                .duration(600)
                .playOn(calculatorLayout);
    }
}


    public void moveCameraToMyLocation(View view) {
        loadRewardedInterstitialAds();
        loadBannerAds();
        if(isLocationEnabled(MapsActivity.this)){
            showMeUserCurrentLoaction();
        }else{
            showLocationEnableDialog();
        }
    }



    public void developerInfo(View view) {
        loadInterstialAds();
        loadRewardedInterstitialAds();
        showLinks(true);
        LatLng developerLocation = new LatLng(23.60094623768234, 75.45045677572489);
        mMap.addMarker(new MarkerOptions().position(developerLocation).
                icon(BitmapDescriptorFactory.fromBitmap(
                        createCustomMarker(MapsActivity.this,
                                R.drawable.profileimage))))
                .setTitle("Narendra Singh Aanjna");
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(developerLocation,15);
        mMap.animateCamera(update);
    }

    private void showLinks(boolean b) {
        if(b){
        linksLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(600)
                .playOn(linksLayout);
    }else{
            linksLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void showDefaultArea(View view) {
        loadRewardedInterstitialAds();
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(resources.getString(R.string.default_area_title))
                .setMessage(resources.getString(R.string.default_area_message1)+"\n"+resources.getString(R.string.default_area_message2))
                .setPositiveButton(resources.getString(R.string.yes_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeDefaultAreaDialog();
                    }
                })
                .setNegativeButton(resources.getString(R.string.no_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
    }

    private void changeDefaultAreaDialog() {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(resources.getString(R.string.change_default_area_title))
                .setMessage(resources.getString(R.string.change_default_area_message))
                .setView(editText)
                .setPositiveButton(resources.getString(R.string.ok_text), (dialogInterface, i) -> {

                    String editTextInput = editText.getText().toString();
                    double val=Double.parseDouble(editTextInput);
                    loadInterstialAds();
                    if(val>=1500 && val<=3500){
                        setDefaultArea(editTextInput);
                }else {
                        Toast.makeText(MapsActivity.this, resources.getString(R.string.change_default_area_toast), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(resources.getString(R.string.cancel_text), (dialogInterface, i) -> {

                })
                .create();
        dialog.show();
    }
    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);

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

    public void linkLinkedIn(View view) {

        Uri uri = Uri.parse("https://www.linkedin.com/in/narendra-singh-aanjna-454bb6190");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void linkInstagram(View view) {
        Uri uri = Uri.parse("https://www.instagram.com/narendra_aanjna_09");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void share(View view) {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Share...")
                .setPositiveButton("Location", (dialogInterface, i) -> {
                    setMarker();
                })
                .setNegativeButton("App", (dialogInterface, i) -> {
                    shareAppLink();
                })
                .create();
        dialog.show();
    }

    private void shareAppLink() {
        final String appPackageName = getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Area Calculator On Google Play Store: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
      startActivity(sendIntent);
    }

    private void setMarker() {

        Toast.makeText(MapsActivity.this, "You can Change This Marker\nBy Clicking Over The Map!", Toast.LENGTH_LONG).show();
                    addMarker(userLocation,"Your Location");
                    shareLocation=userLocation;
                    fabOtherOptions.callOnClick();
                    fabShareLocation.setVisibility(View.VISIBLE);
                    fabCancelLocationShare.setVisibility(View.VISIBLE);
                    fabOtherOptions.setVisibility(View.INVISIBLE);
                    addMarkerOnMap(false,true);

    }

    public void cancelLocationShare(View view) {
        mMap.setOnMapClickListener(null);
        mMap.clear();
        shareLocation=null;
        fabOtherOptions.setVisibility(View.VISIBLE);
        fabShareLocation.setVisibility(View.INVISIBLE);
        fabCancelLocationShare.setVisibility(View.INVISIBLE);
    }

    public void shareLocation(View view) {
        if(shareLocation==null){
            Toast.makeText(MapsActivity.this, "Please Select A Location By Click On It First!", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setTitle("Do you Want To Share This Location?")
                    .setPositiveButton(resources.getString(R.string.yes_text), (dialogInterface, i) -> {
                        shareLocationIntent();
                    })
                    .setNegativeButton(resources.getString(R.string.no_text),(dialogInterface, i) -> {
                    })
                    .create();
            dialog.show();
        }
    }

    private void shareLocationIntent() {

        String uri="https://www.google.com/maps/dir/?api=1&destination="+shareLocation.latitude+"%2C"+shareLocation.longitude;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
}