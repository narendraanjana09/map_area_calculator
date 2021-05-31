package com.nsa.mapsspeech.Activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import com.nsa.mapsspeech.ExtraClasses.StartSpeechRecognition;
import com.nsa.mapsspeech.R;
import com.nsa.mapsspeech.Services.BackgroundService;
import com.nsa.mapsspeech.Services.Common;
import com.squareup.picasso.Picasso;


import net.gotev.speech.ui.SpeechProgressView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        RecognitionListener,SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(Common.KEY_REQUESTING_LOCATION_UPDATES) ){
            setButtonState(sharedPreferences.getBoolean(Common.KEY_REQUESTING_LOCATION_UPDATES,false));
        }

    }
    private void setButtonState(boolean isRequestEnable) {
        if(isRequestEnable){
            switchLanguageChanger.setChecked(true);

        }else{
            switchLanguageChanger.setChecked(false);
        }
    }

    BackgroundService backgroundService=null;
    boolean mBounds=false;

    private final ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundService.LocalBinder binder=(BackgroundService.LocalBinder)service;
            backgroundService=binder.getService();
            mBounds=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            backgroundService=null;
            mBounds=false;
        }
    };

    CircleImageView profileImageView;
    public static FirebaseUser fuser;
    GoogleSignInAccount guser;

    static MapsActivity instance;
    LocationRequest mLocationRequest;

    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;

    public static MapsActivity getInstance() {
        return instance;
    }

    private boolean isNavigation = false;
    private boolean isDeepLink=false;
    private GoogleMap mMap;
    boolean isMapReady=false;
    ArrayList<String> commandList=new ArrayList<>();
    TextView commandTextView;
    private TextView selectPlaceTextView;
    List<AlertDialog> dialogList=new ArrayList<>();

    private TextView areaTextView, lengthTextView;

    ArrayList markerPoints= new ArrayList();


    EditText searchEditText;

    SpeechProgressView progressView;
    private FusedLocationProviderClient locationProviderClient;

    Dialog mBottomSheetDialog;
    private LocationRequest locationRequest;

    FloatingActionButton fabOtherOptions, fabMic,fabCurrentLocation,
            fabShareLocation, fabCancelLocationShare, fabNavigation;

    int isMapNumber = 1;

    boolean needMic = true;

    List<LatLng> latLngList = new ArrayList<>();
    LatLng userLocation, shareLocation = null;

    Polygon polygon;

    Polyline polyline;
    Switch switchLanguageChanger;

    private LinearLayout linksLayout, zoomLayout;
    AdView adView1, adView2;
    private InterstitialAd mInterstitialAd;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private String TAG = "MapsActivity";

    private static final int SPEECH_REQUEST = 10;
    private Animation animation_open, animation_close;
    Context context;
    Resources resources;
    public static final String default_area = "Default_Area";
    SharedPreferences sharedpreferences;
    private MapboxNavigation navigation;
    private DirectionsRoute currentRoute;

    TextToSpeech textToSpeech;


    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognition";
    boolean gotDeepLink=false;


    private void resetSpeechRecognizer() {

        if (speech != null)
            speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        if (SpeechRecognizer.isRecognitionAvailable(this))
            speech.setRecognitionListener(this);
        else
            finish();
    }

    private void setRecogniserIntent() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);

        instance=this;
        FirebaseApp.initializeApp(this);
        guser= GoogleSignIn.getLastSignedInAccount(this);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        profileImageView=findViewById(R.id.circlarImageView);
      Picasso.get().load(guser.getPhotoUrl()).into(profileImageView);


        setRecogniserIntent();
        gotDeepLink=true;

        checkForDynamicLink();

        handleIntent();

        resources = getResources();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

     
        Mapbox.getInstance(this, resources.getString(R.string.mapbox_access_token));
        navigation = new MapboxNavigation(MapsActivity.this, resources.getString(R.string.mapbox_access_token));
        mapFragment.getMapAsync(this);
        switchLanguageChanger = findViewById(R.id.changeLanguageSwitch);
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


        getPermissions();
        prepareLocationService();

        sharedpreferences = getSharedPreferences(default_area, Context.MODE_PRIVATE);
        setDefaultArea(resources.getString(R.string.default_area));

        fabMic = findViewById(R.id.fabMic);
        progressView = findViewById(R.id.progress);

        fabOtherOptions = findViewById(R.id.fabMoreOptions);
        selectPlaceTextView = findViewById(R.id.selectPlaceText);
        fabShareLocation = findViewById(R.id.fabShareLocation);
        fabCancelLocationShare = findViewById(R.id.fabCancelLocationShare);
        fabNavigation = findViewById(R.id.fabNaviagtion);
        fabCurrentLocation = findViewById(R.id.fabMyLocation);

        commandTextView = findViewById(R.id.textView);
        commandTextView.setVisibility(View.INVISIBLE);
        commandTextView.setMovementMethod(new ScrollingMovementMethod());
        areaTextView = findViewById(R.id.areaTextView);
        lengthTextView = findViewById(R.id.lengthTextView);

        searchEditText = findViewById(R.id.searchEditText);
        zoomLayout = findViewById(R.id.ZoomLayout);
        linksLayout = findViewById(R.id.links);
        setSearchListener();
        setSearchTextChangeListener();
        animation_open = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        animation_close = AnimationUtils.loadAnimation(this, R.anim.rotate_close);


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.US);

                }
            }
        });

        getCommandList();
        audioMute(true);
        // start speech recogniser
        resetSpeechRecognizer();



        setButtonState(Common.requestingLocationUpdates(MapsActivity.this));
        bindService(new Intent(MapsActivity.this, BackgroundService.class)
                , mServiceConnection
                , Context.BIND_AUTO_CREATE);


    }



    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        if(!gotDeepLink){

        checkForDynamicLink();
    }
    }

    private void checkForDynamicLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Log.i(TAG,"We have a dynamic link!");


                Uri deepLink=null;
                if(pendingDynamicLinkData!=null){
                    deepLink=pendingDynamicLinkData.getLink();
                }
                if(deepLink!=null){


                    Log.i(TAG,"Here's the deep link url "+deepLink.toString());
                    String type=deepLink.getQueryParameter("type");
                    Toast.makeText(MapsActivity.this, ""+type, Toast.LENGTH_SHORT).show();

                    if(type==null||type.equals("applink")){
                        return;
                    }
                    isDeepLink=true;
                    String lat,lng;
                    LatLng location;
                    onPause();
                    progressView.onEndOfSpeech();
                    if(type.equals("location")){
                         lat=deepLink.getQueryParameter("lat");
                         lng=deepLink.getQueryParameter("lng");
                        Log.e(TAG,"Extracting Link."+type+" "+lat+" "+lng);
                         location=getLanlngFromString(lat,lng);
                        setLinkMarkerOnMap(location);
                        resetSpeechRecognizer();
                        speech.startListening(recognizerIntent);
                    }else if(type.equals("navigation")){
                        lat=deepLink.getQueryParameter("lat");
                        lng=deepLink.getQueryParameter("lng");
                        Log.e(TAG,"Extracting Link."+type+" "+lat+" "+lng);
                        location=getLanlngFromString(lat,lng);
                        setLinkMarkerOnMap(location);
                        shareLocation=location;
                       startNavigationWhenNotValueNull();
                    }else if(type.equals("realtimelocation")){
                        String id=deepLink.getQueryParameter("userid");
                        if(!id.isEmpty()){
                      Intent intent=new Intent(MapsActivity.this,RealTimeViewActivity.class);
                      intent.putExtra("id",id);
                      startActivity(intent);
                    }}
                }

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e(TAG,"oop's cant get dynamic link data"+e.getMessage());
            }
        });
    }

    private void startNavigationWhenNotValueNull() {
        if(userLocation==null){
       new CountDownTimer(3000, 1000) {
           @Override
           public void onTick(long millisUntilFinished) {

           }

           @Override
           public void onFinish() {
            startNavigationWhenNotValueNull();
           }
       }.start();
        }else{
            startNavigate();
            resetSpeechRecognizer();
            speech.startListening(recognizerIntent);
        }
    }

    private void setLinkMarkerOnMap(LatLng latLng2) {
        if(isMapReady){

            addPlaceMarker(latLng2,"location");
            CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latLng2,10);
            mMap.animateCamera(update);
            isDeepLink=false;
        }else{
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    setLinkMarkerOnMap(latLng2);

                }
            }.start();
        }
    }

    private LatLng getLanlngFromString(String lat, String lng) {
        double latitude=Double.parseDouble(lat);
        double longitude=Double.parseDouble(lng);
        return new LatLng(latitude,longitude);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void getCommandList() {
        commandList.add("navigtion to 'city name' :- to get navigation/route details");
        commandList.add("start navigtion to 'city name' :- to start in app navigtion to a city" );
        commandList.add("start google map navigtion to 'city name' :- to start naviagtion in google map to a city");
        commandList.add("search 'city name' :- to search city");
        commandList.add("refresh map :- to refresh map");
        commandList.add("share application :- to share app link");
        commandList.add("calculate area :- to start calculating area on map");
        commandList.add("calcualte length :- to start calculating length on map");
        commandList.add("developer info :- to get developer information ");
        commandList.add("change map type :- to change map type");
        commandList.add("close dialog :- to close current view");
        commandList.add("close application :- to close app");
        commandList.add("zoom in :- to zoom in");
        commandList.add("zoom out :- to zoom out");
        commandList.add("zoom in 30% :- to zoom with percentage value(from 0 to 100 multiple of 10)");
        commandList.add("show current location or my location :- to get your location");
        commandList.add("restart or refresh :- to restart application");
        commandList.add("show all command :- to view all commands");
        commandList.add("more options :- to view other available options");

        String text="";
        int count=0;
        for(String str:commandList){
            count++;
            text+=count+"."+str+"\n";
        }
        commandTextView.setText(text);
    }

    private void getPermissions() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE
                        ,Manifest.permission.ACCESS_COARSE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }

        }).check();
    }

    private void audioMute(boolean mute) {
        if (mute) {
            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        } else {
            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "restart");
        resetSpeechRecognizer();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onResume() {
        Log.i(LOG_TAG, "resume");
        super.onResume();
        resetSpeechRecognizer();
        speech.startListening(recognizerIntent);

    }

    @Override
    protected void onPause() {
        gotDeepLink=false;
        speech.stopListening();
        Log.i(LOG_TAG, "pause");
        super.onPause();

    }

    @Override
    protected void onStop() {

        audioMute(false);
        Log.i(LOG_TAG, "stop");
        if(mBounds){
            unbindService(mServiceConnection);
            mBounds=false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        if (speech != null) {
            speech.destroy();
        }
        super.onStop();
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressView.onBeginningOfSpeech();

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }


    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");

            progressView.onEndOfSpeech();

      //  speech.stopListening();
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


        String text = matches.get(0);
        text=text.toLowerCase();
       getInfoFromText(text);
        speech.startListening(recognizerIntent);

        progressView.onResultOrOnError();

    }

    private void getInfoFromText(String text) {

       // Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
        if (text.equals("tiger")) {
            speech.destroy();

            new StartSpeechRecognition(MapsActivity.this).listenToUser();
            return;
        }else if(text.contains("other")){
               fabOtherOptions.callOnClick();
        }else if(text.contains("show all command")){
            commandTextView.setVisibility(View.VISIBLE);
        }else if(text.contains("naviga")||text.contains("search")||text.contains("show")){
            getTextInfo(text);
        }else if(text.equals("refresh map")){
            Toast.makeText(this, "Map Refreshed", Toast.LENGTH_SHORT).show();
            clearAll();
        }else if(text.contains("real time")){
            switchLanguageChanger.setChecked(true);
        }else if(text.contains("share app")){
            Toast.makeText(this, "Select An App To Share App Link", Toast.LENGTH_SHORT).show();
            shareAppLink();
        }else if(text.equals("calculate area")){
            Toast.makeText(this, "Tap on Map to calculate area", Toast.LENGTH_SHORT).show();
            addMarkerOnMap(true,false);

        }else if(text.equals("calculate length")){
            Toast.makeText(this, "Tap on Map to calculate length", Toast.LENGTH_SHORT).show();
            addMarkerOnMap(false,false);

        }else if(text.contains("developer")){
            showDeveloperInfo();

        }else if(text.contains("change map")){
            Toast.makeText(this, "Map Type Changed", Toast.LENGTH_SHORT).show();
            changeMap();
        }else if(text.contains("close dialog")){
            commandTextView.setVisibility(View.INVISIBLE);
                   removeAllDialog();
                   if(mBottomSheetDialog!=null&&mBottomSheetDialog.isShowing()){
                       mBottomSheetDialog.dismiss();
                   }
            fabCancelLocationShare.callOnClick();
        }else if(text.contains("close app")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }else{
                finish();
                System.exit(0);
            }
        }else if(text.equals("zoom in")||text.equals("zoomin")){
           
            zoomIn();
            
        }else if(text.equals("zoom out")||text.equals("zoomout")){
           
            zoomOut();
            
        }else if(text.contains("zoom")||text.contains("%")){
            if(text.contains("%")||text.contains("percent")){
                String percentage = text.replaceAll("[^0-9]", "");
                int f;
                try {
                    f=Integer.valueOf(percentage);

                    switch (f){
                        case 0: zoomWithPercent(2f);
                        break;
                        case 10: zoomWithPercent(4f);
                            break;
                        case 20: zoomWithPercent(6f);
                            break;
                        case 30: zoomWithPercent(8f);
                            break;
                        case 40: zoomWithPercent(10f);
                            break;
                        case 50: zoomWithPercent(12f);
                            break;
                        case 60: zoomWithPercent(14f);
                            break;
                        case 70: zoomWithPercent(16f);
                            break;
                        case 80: zoomWithPercent(18f);
                            break;
                        case 90: zoomWithPercent(20f);
                            break;
                        case 100: zoomWithPercent(22f);
                            break;
                    }

                } catch (NumberFormatException e) {
                    zoomWithPercent(22f);
                    Log.e(TAG,e.getMessage());
                }
            }
        }else if(text.contains("current location")||text.contains("my location")){
            fabCurrentLocation.callOnClick();
        }
//        else if(text.contains("open here")){
//            if(isNavigation) {
//                startNavigationHere();
//            }
//        }else if(text.contains("google map")){
//            if(isNavigation) {
//                openInGoogleMaps();
//            }
//        }
        else if(text.contains("restart")||text.contains("refresh")){
            removeAllDialog();
            startActivity(new Intent(MapsActivity.this,MapsActivity.class));

            finish();
        }else{
            searchEditText.setText(text);
            searchAddress(false,"",null);
        }





    }
    private void zoomWithPercent(float val){
        LatLng coordinate= mMap.getCameraPosition().target;
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, val);
        mMap.animateCamera(location);
    }

    private void removeAllDialog() {
        for(AlertDialog dialog:dialogList){
            dialog.dismiss();

        }
    }



    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.e(LOG_TAG, "FAILED " + errorMessage);
        // rest voice recogniser
        resetSpeechRecognizer();
        progressView.onResultOrOnError();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        progressView.onRmsChanged(rmsdB);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    private void changeSwitchText(boolean isChecked) {
        if (isChecked) {
            switchLanguageChanger.setText("on");
        } else {
            switchLanguageChanger.setText("off");

        }
    }
    public void changeLanguage() {
        switchLanguageChanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(backgroundService!=null) {
                    if (isChecked) {
                        getRealtimeLocationSharePermission();
                    } else {
                        backgroundService.removeLocationUpdates();
                    }
                }
                changeSwitchText(isChecked);
            }
        });
    }

    private void getRealtimeLocationSharePermission() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Do you want to share your real time location?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shareRealTimeLocationLink();
                        backgroundService.requestLocationUpdates();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                                 switchLanguageChanger.setChecked(false);
                    }
                })
                .create();
        dialog.show();
        dialogList.add(dialog);

    }

    private void loadRewardedInterstitialAds() {
        RewardedInterstitialAd.load(MapsActivity.this, getString(R.string.rewardedInterstitialAdUnitId),
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.show(MapsActivity.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                Log.i(TAG, "reward = " + rewardItem);
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

        InterstitialAd.load(this, getString(R.string.interstitialAdUnitId), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MapsActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
                String name = editable.toString();
                if (name.length() == 0) {
                    needMic = true;
                    changeMicDrawable();
                } else {
                    needMic = false;
                    changeMicDrawable();

                }

            }
        });
    }

    private void changeMicDrawable() {
        if (needMic) {
            fabMic.setImageDrawable(getResources().getDrawable(R.drawable.mic));
            return;
        }
        fabMic.setImageDrawable(getResources().getDrawable(R.drawable.close_icon));
    }

    private void setSearchListener() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    searchAddress(false, "",null);
                    return true;
                }
                return false;
            }
        });
    }


    private void searchAddress(boolean navigateTo, String txt,String command) {

        // on below line we are getting the
        // location name from search view.
        String location ="";
        if(navigateTo){
            location=txt;
        }else{
            location=searchEditText.getText().toString();
        }


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
                location=address.getFeatureName();
                searchEditText.setText(location);

                // on below line we are creating a variable for our location
                // where we will add our locations latitude and longitude.
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                shareLocation = latLng;
                mMap.clear();
                // on below line we are adding marker to that position.
                isNavigation = true;
//                        speakOut(location+" Is Available On Map");
                navigatioFabsVisisbe(true);
                addPlaceMarker(latLng, location);

                // below line is to animate camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                if(navigateTo){
                    if(command!=null){
                        if(command.equals("g")){
                            openInGoogleMaps();
                        }else{
                            startNavigationHere();
                        }
                    }else{
                    startNavigate();
                }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException exception) {
                Toast.makeText(MapsActivity.this, resources.getString(R.string.no_place_toast), Toast.LENGTH_SHORT).show();
            }
            // on below line we are getting the location
            // from our list a first position.


        }
    }

    private void speakOut(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setNavigationDrawable(boolean b) {
        if (b) {

            fabOtherOptions.setImageDrawable(getResources().getDrawable(R.drawable.navigation));
        } else {
            fabOtherOptions.setImageDrawable(getResources().getDrawable(R.drawable.add));
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
        isMapReady = true;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        getRoute();
        speech.startListening(recognizerIntent);
        if (isLocationEnabled(MapsActivity.this)) {
            showMeUserCurrentLoaction();


        } else {
            showLocationEnableDialog();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13);
                mMap.animateCamera(update);
                return true;
            }
        });



    }

    private void getRoute() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = (LatLng) markerPoints.get(0);
                    LatLng dest = (LatLng) markerPoints.get(1);
                    getDirections(origin,dest);


                }

            }
        });

    }

    private void getDirections(LatLng origin, LatLng dest) {
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyC1rU8F0fBtYFA3Vsj28v3w_025sLGHX0I")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(
                context, origin.latitude+","+origin.longitude,dest.latitude+","+dest.longitude);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                Toast.makeText(this, "got routes", Toast.LENGTH_SHORT).show();
                com.google.maps.model.DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline

        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.WHITE).width(5);
            mMap.addPolyline(opts);
        }

    }

    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
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
        dialogList.add(dialog);
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
        dialogList.add(dialog);
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
                String text=wordsSpeaked.get(0);
                getInfoFromText(text);

            }

        }
    }

    private void getTextInfo(String text) {

            text.toLowerCase();
            String[] strings = text.split(" ");
            int a=strings.length;
            if(strings[0].contains("navigat")){


                if(a>1){
                    String txt=strings[1];
                    for(int i=2;i<a;i++){
                        txt+=" "+strings[i];
                    }


                    searchAddress(true,txt,null);
                }else{
                    Toast.makeText(this, "Command Not Available!", Toast.LENGTH_SHORT).show();
                }

            }else if(strings[0].contains("search")||strings[0].contains("show")){
                searchEditText.setText(text);
                searchAddress(false,"",null);

            }else if(strings[0].contains("start")){
                 String cityName=strings[a-1];
                 if(text.contains("google")){
                     searchAddress(true,cityName,"g");
                 }else{
                     searchAddress(true,cityName,"h");
                 }

            }else{
                Toast.makeText(this, "Command Not Available!", Toast.LENGTH_SHORT).show();
            }

    }

    int count=0;
    private void showMeUserCurrentLoaction() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            getPermissions();
        }else{
            mMap.getUiSettings().setCompassEnabled(false);
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
                    if(!isDeepLink) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, 16);
                        mMap.animateCamera(update);


                    }
                }
                }
            });



        }

    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
         mLocationRequest= new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);

        // setting LocationRequest
        // on FusedLocationClient
        locationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
       locationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();

            requestNewLocationData();
            if(location!=null) {
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e("locationdata", " location = " + location + "speed = " + location.getSpeed() + " time = " + location.getTime()
                            + " getSpeedAccuracyMetersPerSecond = " + location.getSpeedAccuracyMetersPerSecond()
                            + "\n getVerticalAccuracyMeters = " + location.getVerticalAccuracyMeters());
                }

            }
        }
    };
    private void prepareLocationService(){
      locationProviderClient= LocationServices.getFusedLocationProviderClient(this);
    }

    public void zoomIn(View view) {
        zoomIn();
    }
    private void zoomIn(){

        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }
    private void zoomOut(){
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    public void zoomOut(View view) {
       zoomOut();
    }

    public void search(View view) {
        loadBannerAds();
         if(needMic){

             speech.destroy();
        new StartSpeechRecognition(MapsActivity.this).listenToUser();
        return;
    }
         isNavigation=false;
         needMic=true;
         shareLocation=null;
         mMap.clear();
         searchEditText.setText("");
         changeMicDrawable();

    }

    public void changeMapType(View view) {

        loadInterstialAds();
        loadBannerAds();
       changeMap();

    }
    public void changeMap(){
        if(isMapNumber==1){
            isMapNumber=2;
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        }else{
            isMapNumber=1;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
        if(mBottomSheetDialog==null){
            return;
        }
        if(mBottomSheetDialog.isShowing()){
        mBottomSheetDialog.dismiss();
        }

    }

    public void refreshMap(View view) {
        mBottomSheetDialog.dismiss();
        loadInterstialAds();
        clearAll();

    }
    public void clearAll(){
        showLinks(false);
        polygon=null;
        polyline=null;
        commandTextView.setVisibility(View.INVISIBLE);
        lengthTextView.setText("");
        lengthTextView.setVisibility(View.INVISIBLE);
        areaTextView.setText("");
        areaTextView.setVisibility(View.INVISIBLE);
        mMap.clear();

        latLngList.clear();

    }

    public void calculateArea(View view) {
        mBottomSheetDialog.dismiss();
        loadBannerAds();
        getWhatUserWantToCalculate();


           

    }
    String lengthText;
    private void getWhatUserWantToCalculate() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(resources.getString(R.string.check_calculate_title))
                .setPositiveButton(resources.getString(R.string.area_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        addMarkerOnMap(true,false);
                    }
                })
                .setNegativeButton(resources.getString(R.string.length_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        addMarkerOnMap(false,false);
                    }
                })
                .create();
        dialog.show();
        dialogList.add(dialog);
        
    }
    private void addMarkerOnMap(boolean area,boolean isLocation){
        clearAll();
        if(area){
            lengthText=resources.getString(R.string.perimeter_text)+"👇";
        }else{
             lengthText=resources.getString(R.string.length_text)+"👇";
        }
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


        if(isNavigation){
            if(shareLocation==null){
                Toast.makeText(this, "Please Select A Place First!", Toast.LENGTH_SHORT).show();
            }else{
                startNavigate();
            }


        }else{
          mBottomSheetDialog = new Dialog(MapsActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.more_options_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();



}}

    private void startNavigate() {



        if(userLocation==null || shareLocation==null){
            Toast.makeText(MapsActivity.this, "Try After Some Time!", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setPositiveButton("Open Here", (dialogInterface, i) -> {
                    startNavigationHere();
                })
                .setNegativeButton("Google Maps",(dialogInterface, i) -> {
                    openInGoogleMaps();
                })
                .create();
        dialog.show();
        dialogList.add(dialog);




    }

    private void startNavigationHere() {
        Point origin = Point.fromLngLat(userLocation.longitude,userLocation.latitude);
        Point destination = Point.fromLngLat(shareLocation.longitude,shareLocation.latitude);
        NavigationRoute.builder(MapsActivity.this)
                .accessToken(resources.getString(R.string.mapbox_access_token ))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Toast.makeText(MapsActivity.this, "No routes found, make sure you set the right place...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Toast.makeText(MapsActivity.this, "No routes found", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);
                        Log.e("route",currentRoute.legs()+"");

                        if(currentRoute.distance()<2.0){
                            Toast.makeText(MapsActivity.this, "Distance Too Less", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(true)
                                .build();
// Call this method with Context from within an Activity


                        NavigationLauncher.startNavigation(MapsActivity.this, options);


                    }


                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    private void openInGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+shareLocation.latitude+","+shareLocation.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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
        mBottomSheetDialog.dismiss();
        loadInterstialAds();
        loadRewardedInterstitialAds();

        showDeveloperInfo();
    }
    public void showDeveloperInfo(){
        showLinks(true);
        LatLng developerLocation = new LatLng(23.60094623768234, 75.45045677572489);
        mMap.addMarker(new MarkerOptions().position(developerLocation).
                icon(BitmapDescriptorFactory.fromBitmap(
                        createCustomMarker(MapsActivity.this,
                                R.drawable.profileimage))))
                .setTitle("Narendra Singh Aanjna");
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(developerLocation,15);
        mMap.animateCamera(update,4000,null);
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
        mBottomSheetDialog.dismiss();
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
        dialogList.add(dialog);
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
                        Toast.makeText(MapsActivity.this, resources.getString(R.string.change_default_area_toast), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(resources.getString(R.string.cancel_text), (dialogInterface, i) -> {

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
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
        mBottomSheetDialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Share...")

                .setPositiveButton("Location", (dialogInterface, i) -> {
                   checkLocationShare();

                })
                .setNegativeButton("App", (dialogInterface, i) -> {
                    shareAppLink();
                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    private void checkLocationShare() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Location Type?")

                .setPositiveButton("Real Time", (dialogInterface, i) -> {

                    switchLanguageChanger.setChecked(true);
                })
                .setNegativeButton("Point", (dialogInterface, i) -> {
                    clearAll();

                    isNavigation=false;
                    setMarker();
                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    private void shareRealTimeLocationLink() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.themapthing.com/?type=realtimelocation&userid="+fuser.getUid()))
                .setDomainUriPrefix("https://themapthing.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.nsa.mapsspeech")
                                .build())
                .buildDynamicLink();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String link="Real Time Location :- "+shortLink.toString();
                            shareLinkToOtherApp(link);

                        } else {
                            Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e(TAG,"Link Exception = "+e.getMessage());
                    }
                });
    }



    private void shareAppLink() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.themapthing.com/?type=applink"))
                .setDomainUriPrefix("https://themapthing.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.nsa.mapsspeech")
                                .build())
                .buildDynamicLink();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String link="Check out The Map Thing App :- "+shortLink.toString();
                            shareLinkToOtherApp(link);

                        } else {
                            Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e(TAG,"Link Exception = "+e.getMessage());
                    }
                });

    }

    private void setMarker() {


        Toast.makeText(MapsActivity.this, "Click On The Map To Select A Location!", Toast.LENGTH_SHORT).show();

        shareLocationFabVisisble(true);
                    addMarkerOnMap(false,true);

    }
    private void shareLocationFabVisisble(boolean visible){
        if(visible){
            fabOtherOptions.setVisibility(View.INVISIBLE);
            selectPlaceTextView.setVisibility(View.VISIBLE);
            fabShareLocation.setVisibility(View.VISIBLE);
            fabCancelLocationShare.setVisibility(View.VISIBLE);
        }else{
            fabOtherOptions.setVisibility(View.VISIBLE);
            selectPlaceTextView.setVisibility(View.INVISIBLE);
            fabShareLocation.setVisibility(View.INVISIBLE);
            fabCancelLocationShare.setVisibility(View.INVISIBLE);
        }

    }
    private void addPlaceMarker(LatLng latLng,String title){


            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(false);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);

    }

    public void cancelLocationShare(View view) {
        mMap.setOnMapClickListener(null);
        mMap.clear();
        shareLocation=null;

        if(isNavigation){
            isNavigation=false;
            navigatioFabsVisisbe(false);
            setNavigationDrawable(false);
            return;
        }
        shareLocationFabVisisble(false);

    }

    public void shareLocation(View view) {

        if(shareLocation==null){
            Toast.makeText(MapsActivity.this, "Please Select A Location By Click On It First!", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setTitle("Share This Location For?")
                    .setPositiveButton("Location", (dialogInterface, i) -> {
                        shareLocationIntent("location");
                    })
                    .setNegativeButton("Navigation",(dialogInterface, i) -> {
                        shareLocationIntent("navigation");
                    })
                    .create();
            dialog.show();
            dialogList.add(dialog);
        }

    }

    private void shareLocationIntent(String type) {

//        String uri="https://www.google.com/maps/dir/?api=1&destination="+shareLocation.latitude+"%2C"+shareLocation.longitude;


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.themapthing.com/?type="+type+"&lat="+shareLocation.latitude+"&lng="+shareLocation.longitude))
                .setDomainUriPrefix("https://themapthing.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.nsa.mapsspeech")
                                .setMinimumVersion(4)
                                .build())
                .buildDynamicLink();

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            shareLinkToOtherApp(shortLink.toString());
                           } else {
                            Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e("LinkException",e.getMessage());
                    }
                });





    }

    private void shareLinkToOtherApp(String uri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void startNavigationProcess(View view) {
        mMap.setOnMapClickListener(null);

       mBottomSheetDialog.dismiss();
        Toast.makeText(MapsActivity.this, "Select A Place To Navigate", Toast.LENGTH_LONG).show();
        isNavigation=true;
        navigatioFabsVisisbe(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                shareLocation=latLng;
                addPlaceMarker(latLng,"");

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBounds){
            unbindService(mServiceConnection);
            mBounds=false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        removeAllDialog();
        audioMute(false);
        navigation.onDestroy();
    }

    public void startNavigate(View view) {
        if(shareLocation!=null){
            startNavigate();
        }else{
            Toast.makeText(MapsActivity.this, "Location Not Selected!", Toast.LENGTH_SHORT).show();
        }
    }
    private void navigatioFabsVisisbe(boolean visible){
        if(visible){
            fabOtherOptions.setVisibility(View.INVISIBLE);
            fabNavigation.setVisibility(View.VISIBLE);
            fabCancelLocationShare.setVisibility(View.VISIBLE);
            selectPlaceTextView.setVisibility(View.VISIBLE);
        }else{
            fabOtherOptions.setVisibility(View.VISIBLE);
            fabNavigation.setVisibility(View.INVISIBLE);
            fabCancelLocationShare.setVisibility(View.INVISIBLE);
            selectPlaceTextView.setVisibility(View.INVISIBLE);
        }
    }


}