package com.nsa.mapsspeech;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.nsa.mapsspeech.ExtraClasses.StartSpeechRecognition;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;

    private TextView areaTextView,lengthTextView;
    EditText searchEditText;
    private static  final int FINE_LOCATION_REQUEST_CODE=12;

    private FusedLocationProviderClient locationProviderClient;

    private LocationRequest locationRequest;

    FloatingActionButton fabMapType,fabOtherOptions,fabMic;

    int isMapNumber=1;
    boolean isOtherOptionsVisible=false;
    boolean needMic=true;

    List<LatLng> markersList=new ArrayList<>();

    Polygon polygon;
    Polyline polyline;

    private LinearLayout zoomLayout,calculatorLayout,linksLayout;



    private static final int SPEECH_REQUEST=10;
    private Animation animation_open,animation_close;

    public static final String default_area = "Default_Area";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedpreferences = getSharedPreferences(default_area, Context.MODE_PRIVATE);
        setDefaultArea("2327.0579");
        fabMapType=findViewById(R.id.fabMapType);
        fabMic=findViewById(R.id.fabMic);
        fabOtherOptions=findViewById(R.id.fabMoreOptions);
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



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prepareLocationService();



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
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException exception){
                        Toast.makeText(MapsActivity.this, "No Place Found", Toast.LENGTH_SHORT).show();
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

        showMeUserCurrentLoaction();



    }



    private void getLocationPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == FINE_LOCATION_REQUEST_CODE || requestCode == 101){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showMeUserCurrentLoaction();
            }else{
               giveLocationAlert();

            }
        }
    }

    private void giveLocationAlert() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("We Don't Have Acces To Your Location!")
                .setMessage("Would You Like Give Us Access?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MapsActivity.this, "Location Access Denied!", Toast.LENGTH_SHORT).show();
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
                    LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                    CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latLng,16);
                    mMap.animateCamera(update);
                }
            });



        }

    }
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
         if(needMic){
        new StartSpeechRecognition(MapsActivity.this).listenToUser();
        return;
    }
         needMic=true;
         searchEditText.setText("");
         changeMicDrawable();

    }

    public void changeMapType(View view) {
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

        clearAll();
        showLinks(false);
    }
    public void clearAll(){
        lengthTextView.setText("");
        lengthTextView.setVisibility(View.INVISIBLE);
        areaTextView.setText("");
        areaTextView.setVisibility(View.INVISIBLE);
        YoYo.with(Techniques.RollOut)
                .duration(300)
                .playOn(areaTextView);
        mMap.clear();
        markersList.clear();

    }

    public void calculateArea(View view) {
        getWhatUserWantToCalculate();
        clearAll();

           

    }

    private void getWhatUserWantToCalculate() {
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("What You Want To Calculaye?")
                .setPositiveButton("Area", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addMarkerOnMap(true);
                    }
                })
                .setNegativeButton("Length", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addMarkerOnMap(false);
                    }
                })
                .create();
        dialog.show();
        
    }
    private void addMarkerOnMap(boolean area){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                markersList.add(latLng);
                addMarker(latLng);

                if(area){

                        calculateAreaofPolygon();

                }else{
                    calculateLengthOfPolyline();
                }


            }
        });
    }
    private void calculateLengthOfPolyline() {
        PolylineOptions polylineOptions=new PolylineOptions();
                for(LatLng latLng:markersList){
                    polylineOptions.add(latLng);
                }

         polyline = mMap.addPolyline(polylineOptions);
        stylePolyLine();
        showLengthToUser(true);

    }


    private void showLengthToUser(boolean isLength) {
        double doublePeri=0.0;
        if(!isLength){
            doublePeri=SphericalUtil.computeDistanceBetween(markersList.get(0),
                    markersList.get(markersList.size()-1));
        }
        double computerLength=SphericalUtil.computeLength(markersList)+doublePeri;
        lengthTextView.setVisibility(View.VISIBLE);
        double inMeter=getRoundValue(computerLength);           // 1metre =  3.281 feet
        double inKiloMeter=getRoundValue((computerLength/1000)); // 1 acre = 4,049 sqmetre
        double inFeet=getRoundValue(computerLength*3.281);
        lengthTextView.setText("KM = "+inKiloMeter+"\nMeter = "+inMeter
                +"\nFeet = "+inFeet);
        YoYo.with(Techniques.BounceIn)
                .duration(300)
                .playOn(areaTextView);
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



    private void addMarker(LatLng latLng) {

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title(markersList.size()+"");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);
    }

    private void calculateAreaofPolygon() {
      PolygonOptions polygonOptions=new PolygonOptions();
       for(LatLng latLng:markersList){
           polygonOptions.add(latLng);
       }
         polygon = mMap.addPolygon(polygonOptions);
       showLengthToUser(false);
        showAreaToUser(SphericalUtil.computeArea(markersList));
                stylePolygon();
    }
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

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
        areaTextView.setText("Biga = "+inBiga+"\nSqMeter = "+inSqMeter
                             +"\nSqFeet = "+inSqFeet+"\nAcres = "+inAcres);
        YoYo.with(Techniques.BounceIn)
                .duration(300)
                .playOn(areaTextView);

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
            YoYo.with(Techniques.FadeInDown)
                    .duration(1200)
                    .playOn(zoomLayout);
            YoYo.with(Techniques.FadeInLeft)
                    .duration(600)
                    .playOn(calculatorLayout);
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
        showMeUserCurrentLoaction();
    }



    public void developerInfo(View view) {
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

        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Default Area Info")
                .setMessage("We Have A Default Value "+getDefaultAreaValue()+" m² per Bigha. \nIt Can Vary In Differnet Area.\nWould You Like To Change It? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeDefaultAreaDialog();
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

    private void changeDefaultAreaDialog() {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Change Default Area Value!")
                .setMessage("Allright Give us the value of 1 Bigha to Meter Square:-")
                .setView(editText)
                .setPositiveButton("Done", (dialogInterface, i) -> {

                    String editTextInput = editText.getText().toString();
                    double val=Double.parseDouble(editTextInput);

                    if(val>1500 && val<3500){
                        setDefaultArea(editTextInput);
                }else {
                        Toast.makeText(MapsActivity.this, "Sorry! But It Seems That This\nMuch Difference Is Not Possible at all", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Exit", (dialogInterface, i) -> {

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
}