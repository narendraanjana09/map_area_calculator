package com.nsa.mapsspeech.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nsa.mapsspeech.Adapter.ViewCropsAdapter;
import com.nsa.mapsspeech.Adapter.ViewPagerAdapter;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.ProgressBar;
import com.nsa.mapsspeech.ExtraClasses.Storage;
import com.nsa.mapsspeech.Model.CropModel;
import com.nsa.mapsspeech.Model.PlaceModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.OnBalloonClickListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.nsa.mapsspeech.Activities.MapsActivity.MAP_VIEW_BUNDLE_KEY;
import static com.nsa.mapsspeech.Activities.MapsActivity.getRoundValue;
import static com.nsa.mapsspeech.ExtraClasses.BalloonText.getBalloon;

public class AddOnMap extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener, ChipGroup.OnCheckedChangeListener {

    private static final String TAG = "ADDOnMap";
    private static final String TASK_LOCATION = "location";
    private static final String TASK_ROUTE = "polyline";
    private static final String TASK_AREA = "area";
    List<AlertDialog> dialogList;

    static ArrayList<String> imagesList;
    Uri imageUri;
    ViewPager2 imagesPager;
    ImageView addImageButton;
    static ViewPagerAdapter imagesAdapter;
    TextView addImagesTextTV;


    private GoogleMap mMap;
    private MapView mapView;
    private LatLng currentLatLng = null, selectedLocation = null;
    FusedLocationProviderClient providerClient;
    String task = "";
     FloatingActionButton fabDone, fabCancel,
           fabViewData,fabPlayPause
                       ,fabAddPlace,fabAddRoute,fabAddArea;
     boolean fabViewDataBulloonShowed=false,fabPlayPauseBullooneShowed=false
         ,fabAddPlaceBulloonShowed=false,fabAddRouteBullooneShowed=false,
                fabAddAreaBalloonShowed=false;
    TextView placesCountTV, routesCountTv, areaCountTv, infoTV;
    LinearLayout zoomLayout, addLayout;
    RelativeLayout images_layout;
    ProgressBar progressBar;

     public static List<PlaceModel> placeModelList;
    private String placeName = "";
    private String placeDate = "";
    public static FirebaseUser fUser;
    String date;

    private long UPDATE_INTERVAL = 1000;  /* 1 secs */
    private long FASTEST_INTERVAL = 500;
    private LocationRequest mLocationRequest;


    public static List<CropModel> cropList=new ArrayList<>();
    HorizontalScrollView kharifScrollView;
    ChipGroup kharifChips;




    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_on_map);

        providerClient = getFusedLocationProviderClient(AddOnMap.this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_vieww);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        getDrawer();

        placeModelList = new ArrayList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fabDone = findViewById(R.id.fabDone);

        kharifChips=findViewById(R.id.kharif_chips);
        kharifScrollView=findViewById(R.id.kharifCropsView);
        kharifChips.setOnCheckedChangeListener(this);

        images_layout = findViewById(R.id.images_layout);
        addImagesTextTV = findViewById(R.id.addImagesTxt);

        imagesList = new ArrayList<>();
        addImageButton = findViewById(R.id.addImageButton);
        imagesPager = findViewById(R.id.imagesViewPager);
        imagesPager.setClipToPadding(false);
        imagesPager.setClipChildren(false);
        imagesPager.setOffscreenPageLimit(3);
        setTransformer();
        imagesAdapter = new ViewPagerAdapter(getApplicationContext(), imagesList, false);
        imagesPager.setAdapter(imagesAdapter);

        areaCountTv = findViewById(R.id.areaCountTextView);
        placesCountTV = findViewById(R.id.placeCountTextView);
        routesCountTv = findViewById(R.id.routesCountTextView);
        fabPlayPause = findViewById(R.id.fabPlayPause);

        fabAddPlace=findViewById(R.id.fabAddPlace);
        fabAddRoute=findViewById(R.id.fabAddRoute);
        fabAddArea=findViewById(R.id.fabAddArea);

        fabAddPlace.setOnLongClickListener(this);
        fabAddRoute.setOnLongClickListener(this);
        fabAddArea.setOnLongClickListener(this);

        infoTV = findViewById(R.id.infoTextView);

        fabCancel = findViewById(R.id.fabCancel);
        fabViewData = findViewById(R.id.fabViewData);
        dialogList = new ArrayList<>();
        progressBar = new ProgressBar(AddOnMap.this, "");

        zoomLayout = findViewById(R.id.zoomLayout);
        addLayout = findViewById(R.id.addLayout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        getFromFirebase();
    }

    private void getFromFirebase() {
        getUploadedCropsList();
        getUploadedLocations(false);
        getUploadedRoutes(true,false);
        getUploadedRoutes(false,false);
    }

    private void getUploadedCropsList() {
        Log.e(TAG," getting Crops data"+cropList);
        new FireBase().getReferenceCrops().getRef().child(fUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            cropList.clear();
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                CropModel model=dataSnapshot.getValue(CropModel.class);
                                cropList.add(model);
                            }
                            Log.e(TAG,"Crops List "+cropList);
                        }else{
                            Log.e(TAG," not Crops data"+cropList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    DrawerLayout drawer;
    NavigationView navigationView;
    private void getDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar );
         drawer= findViewById(R.id.addonmap_drawer_layout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string. navigation_drawer_open ,
                R.string. navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        navigationView= findViewById(R.id.addonmap_navigation_view ) ;
        navigationView.setNavigationItemSelectedListener( this ) ;

    }
    @Override
    public void onBackPressed () {

        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            super .onBackPressed() ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addonmap_menu_drawer , menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId() ;

        return super .onOptionsItemSelected(item) ;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    RequestQueue mRequestQueue;
    public RequestQueue getRequestQueue() {
        //requestQueue is used to stack your request and handles your cache.
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    protected void onStart() {
        mapView.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }



    private void setTransformer() {
        CompositePageTransformer transformer=new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v=1-Math.abs(position);
                page.setScaleY(0.8f+v*0.2f);
            }
        });
        imagesPager.setPageTransformer(transformer);
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

        Log.e(TAG, "on map ready");
        getLastLocation();



    }

    private void getLastLocation() {
        Log.e(TAG, "getting location ready");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        providerClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location myLocation = task.getResult();
                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                currentLatLng = latLng;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
                Log.e("location", "got location" + latLng);
            }
        });
//        new LocationClass(AddOnMap.this, new LocationClassListener() {
//            @Override
//            public void onLocation(LatLng latLng) {
//                currentLatLng=latLng;
//                Log.e(TAG,"got location");
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
//            }
//        });
    }
    public void zoomIn(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOut(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    boolean mapTypeChanged=false,fabMapTypeChangeBalloonShowed=false;

    public void changeMapType(View view) {
          if(!fabMapTypeChangeBalloonShowed) {
            getBalloon("change map type", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.RIGHT)
                    .setOnBalloonClickListener(new OnBalloonClickListener() {
                        @Override
                        public void onBalloonClick(@NotNull View view) {
                            fabMapTypeChangeBalloonShowed=true;
                            changeMapType(null);
                            Log.e(TAG,"BaloonCLicked");
                        }
                    }).build().showAlignLeft(findViewById(R.id.fabChangeMapType));
        }else{
        if (mapTypeChanged) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapTypeChanged = false;
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapTypeChanged = true;
        }
          }

    }


    public void moveCameraToMyLocation(View view) {
        getLastLocation();
    }

    boolean secondTime=false;
    public void uploadData(View view) {
        if(task.equals(TASK_LOCATION)){

            if(selectedLocation==null){
                Toast.makeText(this, "Please Select a place!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(secondTime){
                            if(imagesList.size()==0){
                             checkForGetImages();
                            }else{
                                uploadPlaceData();
                            }
            }else {
                secondTime = true;
                Toast.makeText(AddOnMap.this, "Add Image Of The Place!", Toast.LENGTH_SHORT).show();
                images_layout.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
                addImagesTextTV.setText("Add Images");
            }



        }else if(task.equals(TASK_ROUTE)){
            if(polyLineList.size()<2){
                Toast.makeText(this, "Please select more than one point", Toast.LENGTH_SHORT).show();
                return;
            }else{
                checkForRouteUpload(true);
            }
        }else{
            if(polyLineList.size()<2){
                Toast.makeText(this, "Please select more than two points", Toast.LENGTH_SHORT).show();
                return;
            }else{
                checkForRouteUpload(false);
            }
        }


    }

    private void checkForRouteUpload(boolean isRoute) {
        String title="";String message="";
        if(isRoute){
            title="Route!";
            message="Do You Want To Upload This Route?";
        }else{
            title="Area!";
            message="Do You Want To Upload This Area?";
        }

        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.yes_text), (dialogInterface, i) -> {
                  progressBar.setTitle("Uploading...");
                  progressBar.show();
                    uploadRoute(isRoute);
                })
                .setNegativeButton(getResources().getString(R.string.no_text), (dialogInterface, i) -> {

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    private void uploadRoute(boolean isRoute) {
        date =new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String route=PolyUtil.encode(polyLineList);
        RouteModel model=new RouteModel(date,placeName,placeDate,route);
        DatabaseReference reference=null;
        if(isRoute){
            reference=new FireBase().getReferenceDataPolyLine();
        }else{
            reference=new FireBase().getReferenceDataArea();
        }
        reference.child(fUser.getUid()).child(date).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.hide();
                        if(isRoute){
                            routeModelList.add(model);
                        }else {
                            areaModelList.add(model);
                        }
                        Toast.makeText(AddOnMap.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        fabCancel.callOnClick();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.hide();
                Toast.makeText(AddOnMap.this, "try again later", Toast.LENGTH_SHORT).show();
                fabCancel.callOnClick();
            }
        });
    }

    private void checkForGetImages() {
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Images!")
                .setMessage("Do You Want To Add Images")
                .setPositiveButton(getResources().getString(R.string.yes_text), (dialogInterface, i) -> {

                })
                .setNegativeButton(getResources().getString(R.string.no_text), (dialogInterface, i) -> {
                    askForUploadPlaceData();
                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }



    private void getPlaceName(String message) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextColor(Color.WHITE);
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Place Name!")
                .setMessage(message)
                .setView(editText)
                .setPositiveButton(getResources().getString(R.string.ok_text), (dialogInterface, i) -> {


                     placeName = editText.getText().toString();
                    if(placeName.isEmpty()){
                        getPlaceName("Please Give A Name!");


                    }else{
                        mMap.clear();
                        changeButtonsVisibility();
                        placeDate=getDayMonthYear();
                        fabViewData.setVisibility(View.INVISIBLE);
                        mMap.setOnMapClickListener(AddOnMap.this);
                        Toast.makeText(AddOnMap.this, "Now Click on map to select a place!", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_text), (dialogInterface, i) -> {
                        placeName="";
                    fabCancel.callOnClick();
                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }
    private void askForUploadPlaceData() {
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Upload Place?")
                .setPositiveButton(getResources().getString(R.string.yes_text), (dialogInterface, i) -> {
                    uploadPlaceData();
                })
                .setNegativeButton(getResources().getString(R.string.no_text), (dialogInterface, i) -> {

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    private void uploadPlaceData() {
        date =new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        boolean noImages=false;
        if(imagesList.size()==0){
            noImages=true;
            imagesList.add("null");
        }
        PlaceModel model=new PlaceModel(date,selectedLocation.latitude+"",selectedLocation.longitude+""
                                    ,placeName,placeDate,imagesList);
        if(noImages){
            progressBar.setTitle("Uploading...");
            progressBar.show();

            uploadPlaceModel(model);
        }else{
            uploadImages(model);
        }

    }
    String filename;
    int uploadCount=0;
    private void uploadImages(PlaceModel model)
    {
        if (model != null) {

            // Code for showing progressDialog while uploading
            filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            int count=uploadCount+1;
            progressBar.setTitle("Uploading "+count+"/"+model.getImageUriList().size());
            progressBar.setMessage("Uploading...");
            progressBar.show();

            // Defining the child of storageReference
            StorageReference ref
                    = new Storage().getStorageReference()
                    .child(
                            "placeImages/"
                                    + fUser.getUid()+"/"+date+"/"+filename);

            UploadTask uploadTask = ref.putFile(Uri.parse(imagesList.get(uploadCount)));
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());

                    progressBar.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        progressBar.hide();
                        Log.e("AddPostActivity",task.getException().getMessage());
                        Toast.makeText(AddOnMap.this, "task upload Failed", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {



                    if (task.isSuccessful()) {


                        Uri downloadUri = task.getResult();
                        model.getImageUriList().remove(uploadCount);
                        model.getImageUriList().add(uploadCount,downloadUri.toString());
                        if(model.getImageUriList().size()==(uploadCount+1)){

                            uploadPlaceModel(model);

                        }else{
                            uploadCount++;

                            uploadImages(model);
                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });


        }
    }

    private void uploadPlaceModel(PlaceModel model) {
        new FireBase().getReferenceDataPlace().child(fUser.getUid()).child(date).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                placeModelList.add(model);
                Toast.makeText(AddOnMap.this, "Place Data Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.hide();
                fabCancel.callOnClick();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(AddOnMap.this, "Place Data Upload Failed\nTry Again Later!", Toast.LENGTH_SHORT).show();
                progressBar.hide();
                fabCancel.callOnClick();
            }
        });
    }


    boolean zoomControlsShowing=true;
    public void cancelUpload(View view) {
        changeButtonsVisibility();
        fabViewData.setVisibility(View.VISIBLE);
        clearAll();

    }

    private void clearAll(){
        mMap.clear();
        secondTime=false;
        selectedLocation=null;
        images_layout.setVisibility(View.INVISIBLE);
        requestNewLocationData(false);
        started=false;
        fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
        fabPlayPause.setVisibility(View.INVISIBLE);
        mMap.setOnMapClickListener(null);
        mMap.setOnMarkerClickListener(null);
        if(polyLineMarker!=null) {
            polyLineMarker.remove();
        }
        if(areaPolygon!=null){
            areaPolygon.remove();
        }
        polyLineList.clear();
        if(polylineRoute!=null){
        polylineRoute.remove();
        }
        infoTV.setText("");
        infoTV.setVisibility(View.INVISIBLE);
        task="";
        placeName="";
        placeDate="";
        imagesList.clear();
        if(imagesAdapter!=null){
        imagesAdapter.setList(imagesList);
        imagesAdapter.notifyDataSetChanged();
        }
        imageUri=null;

    }


    public void animateZoomLayout(float trans){

     zoomLayout.animate().translationY(trans).setDuration(300);



    }
    private void changeButtonsVisibility() {

        if(zoomControlsShowing){
            zoomControlsShowing=false;
            addLayout.setVisibility(View.INVISIBLE);
            animateZoomLayout((float) (-fabDone.getMeasuredHeight()-5));
            fabCancel.setVisibility(View.VISIBLE);
            fabDone.setVisibility(View.VISIBLE);
        }else{
            addLayout.setVisibility(View.VISIBLE);
            animateZoomLayout(0);
            fabCancel.setVisibility(View.INVISIBLE);
            fabDone.setVisibility(View.INVISIBLE);
            zoomControlsShowing=true;
        }
    }

    public void addLocation(View view) {
         if(!fabAddPlaceBulloonShowed) {
            getBalloon("add or view places", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setOnBalloonClickListener(new OnBalloonClickListener() {
                        @Override
                        public void onBalloonClick(@NotNull View view) {
                            fabAddPlaceBulloonShowed=true;
                            addLocation(null);
                            Log.e(TAG,"BaloonCLicked");
                        }
                    }).build().showAlignTop(fabAddPlace);
        }else{
        task=TASK_LOCATION;
        if(viewData){
            mMap.setOnMarkerClickListener(AddOnMap.this);
            showPlacesOnMap();
            return;
        }

        getPlaceName("What is the name of selected place?");

        Log.e(TAG,"task "+task);
         }
    }

    private void getUploadedLocations(boolean refresh) {
        progressBar.setTitle("Checking...");
        progressBar.show();
        new FireBase().getReferenceDataPlace()
                .child(fUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            placeModelList.clear();
                            Log.e(TAG,"Data Available");
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Log.e(TAG,"Data Available datasnapshot");
                                PlaceModel model=dataSnapshot.getValue(PlaceModel.class);
                                placeModelList.add(model);
                            }
                            mMap.setOnMarkerClickListener(AddOnMap.this);
                            placesCountTV.setText(placeModelList.size()+"");

                        }else{
                            Log.e(TAG,"no data available");
                        }
                        progressBar.hide();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(AddOnMap.this, "Technical Error\ntry again later", Toast.LENGTH_SHORT).show();

                        progressBar.hide();
                    }
                });
    }


    private void showPlacesOnMap() {
        mMap.clear();
        infoTV.setVisibility(View.INVISIBLE);
        if(placeModelList.size()==0){
            Toast.makeText(this, "No Places Were uploaded!", Toast.LENGTH_SHORT).show();
            return;
        }

        getListSnackBar("places");
        for(PlaceModel model:placeModelList){
            selectedLocation=new LatLng(Double.parseDouble(model.getLat()),Double.parseDouble(model.getLng()));
            placeName=model.getPlaceName();
            placeDate=model.getDate();
            addMarker(selectedLocation,placeName,placeDate);




        }
    }

    public void addRoute(View view) {


        if(!fabAddRouteBullooneShowed) {
            getBalloon("add or view routes", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setOnBalloonClickListener(new OnBalloonClickListener() {
                        @Override
                        public void onBalloonClick(@NotNull View view) {
                            fabAddRouteBullooneShowed=true;
                            addRoute(null);
                            Log.e(TAG,"BaloonCLicked");
                        }
                    }).build().showAlignTop(fabAddRoute);

        }else{
            task=TASK_ROUTE;
        if(viewData){
            mMap.setOnMarkerClickListener(AddOnMap.this);

            showRoutesOnMap();
            return;
        }
       getPolylineName("Give This Route A Name");

        Log.e(TAG,"task "+task);
        }
    }

    private void getPolylineName(String message) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextColor(Color.WHITE);
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Route Name!")
                .setMessage(message)
                .setView(editText)
                .setPositiveButton(getResources().getString(R.string.ok_text), (dialogInterface, i) -> {


                    placeName = editText.getText().toString();
                    if(placeName.isEmpty()){
                        getPolylineName("Please Give Route A Name!");


                    }else{
                        mMap.clear();
                        placeDate=getDayMonthYear();
                        fabViewData.setVisibility(View.INVISIBLE);
                      getHowPolyLineCreated();
                        changeButtonsVisibility();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_text), (dialogInterface, i) -> {
                    placeName="";
                    placeDate="";
                    mMap.setOnMapClickListener(null);

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    private void getHowPolyLineCreated() {
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Create A Route By?")
                .setPositiveButton("Walking", (dialogInterface, i) -> {
                    fabPlayPause.setVisibility(View.VISIBLE);
                })
                .setNegativeButton("Map", (dialogInterface, i) -> {
                    mMap.setOnMapClickListener(AddOnMap.this);
                    Toast.makeText(this, "All Right Click on map to select route!", Toast.LENGTH_SHORT).show();

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }


    public static List<RouteModel> routeModelList=new ArrayList<>();
   public static List<RouteModel> areaModelList=new ArrayList<>();
    private void getUploadedRoutes(boolean isRoute,boolean refresh) {
        progressBar.setTitle("Checking...");
        progressBar.show();
        DatabaseReference reference=null;
        if(isRoute){
            reference=new FireBase().getReferenceDataPolyLine();
        }else{
            reference=new FireBase().getReferenceDataArea();
        }
        reference.child(fUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if(isRoute){
                                routeModelList.clear();
                            }else{
                                areaModelList.clear();
                            }
                            Log.e(TAG,"routes Available");
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Log.e(TAG,"Data Available datasnapshot");
                                RouteModel model=dataSnapshot.getValue(RouteModel.class);
                                if(isRoute){
                                routeModelList.add(model);
                               }else{
                                    areaModelList.add(model);
                                }

                            }

                            if(isRoute){
                                routesCountTv.setText(routeModelList.size()+"");
                                if(refresh){
                                    task=TASK_ROUTE;
                                    showRoutesOnMap();
                                }

                            }else{
                                if(refresh){
                                    task=TASK_AREA;
                                    showAreaOnMap();
                                }

                                areaCountTv.setText(areaModelList.size()+"");



                            }

                        }else{
                            Log.e(TAG, "No Data Available!");
                        }
                        progressBar.hide();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(AddOnMap.this, "Technical Error\ntry again later", Toast.LENGTH_SHORT).show();

                        progressBar.hide();
                    }
                });
    }

    private void showAreaOnMap(){
        mMap.clear();
        SqMeter=0;
        Biga=0;
        if(areaModelList.size()==0){
            Toast.makeText(this, "No Area Uploaded", Toast.LENGTH_SHORT).show();
            return;
        }
        getListSnackBar("areas");
        infoTV.setVisibility(View.VISIBLE);
        infoTV.setText("");
        for(RouteModel model:areaModelList){
            List<LatLng> points=PolyUtil.decode(model.getPoints());
            addMarker(points.get(0),model.getName(),model.getDate());
            String text=getStringAreaText(getArea(points));
            addMarker(points.get(points.size()-1),model.getName(),text);
            showPolygon(points);
        }
    }
    public static double getArea(List<LatLng> points){
        return SphericalUtil.computeArea(points);
    }
    public static double getAreaBigha(List<LatLng> list){
        double computeArea=getArea(list);
        double default_area= 2327.0579;
        return  getRoundValue((computeArea/default_area));
    }

    double SqMeter=0;
    double Biga=0;
    private String getStringAreaText(double computeArea) {
        double inSqMeter=getRoundValue(computeArea);           // 1metre =  3.281 feet
        double default_area= Double.parseDouble(getResources().getString(R.string.default_area));
        double inBiga=getRoundValue((computeArea/default_area)); // 1 acre = 4,049 sqmetre
//        double inSqFeet=getRoundValue(computeArea*3.281*3.281);
//        double inAcres=getRoundValue(computeArea/4049);
        SqMeter+=inSqMeter;
        Biga+=inBiga;
        infoTV.setText("Total Uploaded Area👇\n"+"Bigha = "+(int)Biga+"\n"+"SqMeter = "+(int)SqMeter);
        return inBiga+" "+getResources().getString(R.string.bigha_text)
                +","+inSqMeter+" sq/meter";
    }

    private void showPolygon(List<LatLng> points) {
         PolygonOptions polygonOptions=new PolygonOptions();
        for(LatLng latLng:points){
            polygonOptions.add(latLng);
        }

        areaPolygon = mMap.addPolygon(polygonOptions);
        stylePolygon(areaPolygon);
    }



    double km=0;
    double meter=0;
    double feet=0;
    private void showRoutesOnMap() {
        mMap.clear();
         km=0;
         meter=0;
         feet=0;
         if(routeModelList.size()==0){
             Toast.makeText(this, "No Routes Uploaded!", Toast.LENGTH_SHORT).show();
             return;
         }

        getListSnackBar("routes");


        infoTV.setVisibility(View.VISIBLE);
        infoTV.setText("");
        for(RouteModel model:routeModelList){
            List<LatLng> points=PolyUtil.decode(model.getPoints());
            addMarker(points.get(0),model.getName(),model.getDate());
            double distance=SphericalUtil.computeLength(points);
            double inMeter=getRoundValue(distance);           // 1metre =  3.281 feet
            double inKiloMeter=getRoundValue((distance/1000)); // 1 acre = 4,049 sqmetre
            double inFeet=getRoundValue(distance*3.281);
            km+=inKiloMeter;
            meter+=inMeter;
            feet+=inFeet;
            infoTV.setText("Total Uploaded Length👇\n"+"Km = "+(int)km+"\n"+
                    "Meter = "+(int)meter+"\n"+"Feet = "+(int)feet);

            String text=inKiloMeter+" km,"+inMeter+" m,"+inFeet+" ft";
            addMarker(points.get(points.size()-1),model.getName(),text);
            showPolyLine(points);
        }
    }

    private void getListSnackBar(String routes) {
        Snackbar snackbar
                = Snackbar
                .make(drawer, "Show List", Snackbar.LENGTH_LONG)
                .setAction("open list",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AddOnMap.this, ViewListActivity.class);
                                intent.putExtra("query", routes);
                                startActivity(intent);
                            }
                        });
        snackbar.setDuration(5000);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.BLACK);//getResources().getColor(R.color.teal_200));
        snackbar.show();
    }


    private void showPolyLine(List<LatLng> points) {
        PolylineOptions options=new PolylineOptions();
        options.width(5);
        options.color(Color.BLACK);
        options.addAll(points);
        options.clickable(true);
        polylineRoute=mMap.addPolyline(options);
    }


    public void addArea(View view) {
          if(!fabAddAreaBalloonShowed) {
            getBalloon("add or view area", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setOnBalloonClickListener(new OnBalloonClickListener() {
                        @Override
                        public void onBalloonClick(@NotNull View view) {
                            fabAddAreaBalloonShowed=true;
                            addArea(null);
                            Log.e(TAG,"BaloonCLicked");

                        }
                    }).build().showAlignTop(fabAddArea);
        }else{
        task=TASK_AREA;
        if(viewData){
            mMap.setOnMarkerClickListener(AddOnMap.this);

            showAreaOnMap();
            return;
        }
        getAreaName("Give This Area A Name!");
        Log.e(TAG,"task "+task);}
    }

    private void getAreaName(String message) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextColor(Color.WHITE);
        AlertDialog dialog = new AlertDialog.Builder(AddOnMap.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Area Name!")
                .setMessage(message)
                .setView(editText)
                .setPositiveButton(getResources().getString(R.string.ok_text), (dialogInterface, i) -> {


                    placeName = editText.getText().toString();
                    if(placeName.isEmpty()){
                        getAreaName("Please Give Area A Name!");


                    }else{
                        mMap.clear();
                        placeDate=getDayMonthYear();
                        fabViewData.setVisibility(View.INVISIBLE);
                        mMap.setOnMapClickListener(AddOnMap.this);
                        Toast.makeText(this, "All Right Click on Boundries On Map to get the area!", Toast.LENGTH_SHORT).show();
                        changeButtonsVisibility();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_text), (dialogInterface, i) -> {
                    placeName="";
                    placeDate="";
                    mMap.setOnMapClickListener(null);

                })
                .create();
        dialog.show();
        dialogList.add(dialog);
    }

    List<LatLng> polyLineList=new ArrayList<>();
    @Override
    public void onMapClick(@NonNull @NotNull LatLng latLng) {
        if(task.equals(TASK_LOCATION)) {
            mMap.clear();
            selectedLocation = latLng;
            addMarker(selectedLocation, placeName, placeDate);
        }else if(task.equals(TASK_ROUTE)){
            addMarker(latLng, placeName, "");
//            if(polyLineList.size()!=0){
//                addFlagMarker(polyLineList.get(polyLineList.size()-1));
//            }
             polyLineList.add(latLng);
             showLengthToUser(polyLineList);
             getPoyLine();
        }else if(task.equals(TASK_AREA)){
            addMarker(latLng, "", "");
//            if(polyLineList.size()!=0){
//                addFlagMarker(polyLineList.get(polyLineList.size()-1));
//            }
            polyLineList.add(latLng);
            calculateAreaofPolygon();
            getPoyLine();

        }
    }

    private void addFlagMarker(LatLng latLng) {
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.anchor(0.3f, 0.3f);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(RealTimeViewActivity.createCustomMarker(AddOnMap.this,getResources().getDrawable(R.drawable.flag))));
        mMap.addMarker(markerOptions);
    }

    Polygon areaPolygon=null;
    int count=0;
    private void calculateAreaofPolygon() {
        PolygonOptions polygonOptions=new PolygonOptions();
        if(count<1){
            getBalloon("click to hide or view shape", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setDismissWhenClicked(true)
                    .build().showAlignTop(infoTV);
            count=1;
        }
        for(LatLng latLng:polyLineList){
            polygonOptions.add(latLng);
        }
        if(areaPolygon!=null){
            areaPolygon.remove();
        }
        areaPolygon = mMap.addPolygon(polygonOptions);
        if(!showPolgon){
            areaPolygon.setVisible(false);
        }
        showAreaToUser(SphericalUtil.computeArea(polyLineList));
        stylePolygon(areaPolygon);
    }

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;

    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    private void stylePolygon(Polygon areaPolygon) {
        areaPolygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
        areaPolygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        areaPolygon.setStrokeColor(getResources().getColor(R.color.dark));
        areaPolygon.setFillColor(Color.GREEN);
    }



    private void showAreaToUser(double computeArea) {

        infoTV.setText("");
        infoTV.setVisibility(View.VISIBLE);
        double inSqMeter=getRoundValue(computeArea);           // 1metre =  3.281 feet
        double default_area= Double.parseDouble(getResources().getString(R.string.default_area));
        double inBiga=getRoundValue((computeArea/default_area)); // 1 acre = 4,049 sqmetre
        double inSqFeet=getRoundValue(computeArea*3.281*3.281);
        double inAcres=getRoundValue(computeArea/4049);
        infoTV.setText(getResources().getString(R.string.area_text)
                +"👇\n"+getResources().getString(R.string.bigha_text)
                +" = "+inBiga+"\nSqMeter = "+inSqMeter
                +"\nSqFeet = "+inSqFeet+"\nAcres = "+inAcres);


    }

    private void showLengthToUser(List<LatLng> list) {
        double computerLength=SphericalUtil.computeLength(list);
        infoTV.setVisibility(View.VISIBLE);
        double inMeter=getRoundValue(computerLength);           // 1metre =  3.281 feet
        double inKiloMeter=getRoundValue((computerLength/1000)); // 1 acre = 4,049 sqmetre
        double inFeet=getRoundValue(computerLength*3.281);
        infoTV.setText("Distance"+"\nKM = "+inKiloMeter+"\nMeter = "+inMeter
                +"\nFeet = "+inFeet);
    }

    Polyline polylineRoute=null;
    private void getPoyLine() {
        if(polylineRoute!=null){
            polylineRoute.remove();
        }
        PolylineOptions options=new PolylineOptions();
        options.width(10);
        options.color(getResources().getColor(R.color.black));
        options.addAll(polyLineList);

        polylineRoute=mMap.addPolyline(options);
        Log.e(TAG,placeName+" distance = "+ (int)SphericalUtil.computeLength(polyLineList)+"meter");

    }


    Marker polyLineMarker=null;
    private void addMarker(LatLng latLng, String title, String placeDate) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        if(placeDate.isEmpty()){
            //for length
            if(polyLineMarker!=null){
                polyLineMarker.remove();
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        }else if(title.isEmpty()){
            //for area
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        }else{
            //for place
            markerOptions.title(title);
            markerOptions.snippet(placeDate);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        }

        polyLineMarker=mMap.addMarker(markerOptions);
    }

    public void addImage(View view) {
        if(imagesList.size()==4){
            Toast.makeText(AddOnMap.this, "You can only Add 4 Images!", Toast.LENGTH_SHORT).show();
            return;
        }
        getImage();

    }

    private void getImage() {
        ImagePicker.Companion.with(AddOnMap.this)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .crop(4,3) //Crop image(Optional), Check Customization for more option
                .compress(1024)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {

            imageUri=data.getData();
            imagesList.add(imageUri.toString());

            imagesAdapter.notifyDataSetChanged();


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.EXTRA_ERROR, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "TaskCancelled", Toast.LENGTH_SHORT).show();
        }

    }
    public static void deleteFromList(int position){
        imagesList.remove(position);
        imagesAdapter.notifyDataSetChanged();

    }
    public static  String getDayMonthYear()
    {

        String dt =new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String dateParts[] = dt.split("-");

        int day = Integer.parseInt(dateParts[0]);
        String month = getMonthName(Integer.parseInt(dateParts[1]));
        String year = dateParts[2];

        return day+" "+month+" "+year;
    }
    public static String getMonthName(int month){
        String monthString;
        switch (month) {
            case 1:  monthString = "Jan";       break;
            case 2:  monthString = "Feb";      break;
            case 3:  monthString = "Mar";         break;
            case 4:  monthString = "Apr";         break;
            case 5:  monthString = "May";           break;
            case 6:  monthString = "June";          break;
            case 7:  monthString = "July";          break;
            case 8:  monthString = "Aug";        break;
            case 9:  monthString = "Sep";     break;
            case 10: monthString = "Oct";       break;
            case 11: monthString = "Nov";      break;
            case 12: monthString = "Dec";      break;
            default: monthString = month+""; break;
        }
        return monthString;
    }

    boolean viewData=false;
    public void ViewData(View view) {
        if(!fabViewDataBulloonShowed) {
            getBalloon("enable view mode", AddOnMap.this)
                    .setArrowOrientation(ArrowOrientation.RIGHT)
                    .setOnBalloonClickListener(new OnBalloonClickListener() {
                        @Override
                        public void onBalloonClick(@NotNull View view) {
                            fabViewDataBulloonShowed=true;
                            ViewData(null);
                            Log.e(TAG,"BaloonCLicked");

                        }
                    }).build().showAlignLeft(fabViewData);
        }else {
            YoYo.with(Techniques.Shake).duration(1000).playOn(addLayout);


            if (viewData) {
                viewData = false;
                clearAll();
                fabViewData.setImageDrawable(getResources().getDrawable(R.drawable.eye_off));
            } else {
                viewData = true;
                fabViewData.setImageDrawable(getResources().getDrawable(R.drawable.eye_on));
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        LatLng latLng=marker.getPosition();
        Log.e(TAG,"on marker click "+marker.getTitle());


        if(task.equals(TASK_LOCATION)) {
            for (PlaceModel model : placeModelList) {
                Log.e(TAG, "inside for loop click " + latLng + "  //// " + model.getLat() + "," + model.getLng());
                if (model.getLat().equals((latLng.latitude) + "")
                        && model.getLng().equals((latLng.longitude) + "")) {
                    Log.e(TAG, "inside equal ");
                    if (model.getImageUriList().get(0).equals("null")) {
                        Toast.makeText(AddOnMap.this, "No Images were uploaded!", Toast.LENGTH_SHORT).show();
                    } else {
                        images_layout.setVisibility(View.VISIBLE);
                        addImageButton.setVisibility(View.INVISIBLE);
                        addImagesTextTV.setText("Images");
                        imagesAdapter.setGetFromNet(true);
                        imagesAdapter.setList(model.getImageUriList());
                        imagesAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }else if(task.equals(TASK_ROUTE)){
            Toast.makeText(this, "OnMArker", Toast.LENGTH_SHORT).show();
        }else if(task.equals(TASK_AREA)){
            Toast.makeText(this, "onmarkerTASK_AREA", Toast.LENGTH_SHORT).show();
        }


        return false;
    }

//
@SuppressLint("MissingPermission")
private void requestNewLocationData(boolean b) {

    // Initializing LocationRequest
    // object with appropriate methods
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setInterval(3000);
    mLocationRequest.setFastestInterval(1000);
    if(b){

        providerClient = LocationServices.getFusedLocationProviderClient(this);

        providerClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }else{
        providerClient.removeLocationUpdates(mLocationCallback).addOnSuccessListener(new OnSuccessListener<Void>() {
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

            if(task.equals(TASK_ROUTE)){
                addMarker(latLng, placeName, "");
                polyLineList.add(latLng);
                showLengthToUser(polyLineList);
                getPoyLine();
            }else if(task.equals(TASK_AREA)){
                addMarker(latLng, "", "");
                polyLineList.add(latLng);
                calculateAreaofPolygon();

            }
            Log.e("locationCallback", "call back "+latLng);
        }};


    boolean started=false;
    public void playPauseLocationUpdate(View view) {

        if(started){
            requestNewLocationData(false);
            started=false;
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
        }else{
            requestNewLocationData(true);
            started=true;
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewData(null);
        if(v.equals(fabAddPlace)){
            clearAll();
            getUploadedLocations(true);
        }else if(v.equals(fabAddRoute)){
            clearAll();
            getUploadedRoutes(true,true);
        }else{
            clearAll();
            getUploadedRoutes(false,true);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_crop:
                if(areaModelList.size()==0){
                    Toast.makeText(this, "add Some fileds First", Toast.LENGTH_SHORT).show();
                    addArea(null);
                    break;
                }
                startActivity(new Intent(AddOnMap.this,CropsActivity.class));
                break;
            case R.id.view_crop:
                if(cropList.size()==0){
                    Toast.makeText(this, "No Crops Were Added For Current Season!", Toast.LENGTH_SHORT).show();
                }else {
                    setBottomSheet();
                }
                break;
            case R.id.view_area:  fabViewDataBulloonShowed=true;
                                  viewData = false;
                                  ViewData(null);
                                  fabAddArea.callOnClick();

            break;
            case R.id.view_places:  fabViewDataBulloonShowed=true;
                viewData = false;
                ViewData(null);
                fabAddPlace.callOnClick();

                break;
            case R.id.view_routes:  fabViewDataBulloonShowed=true;
                viewData = false;
                ViewData(null);
                fabAddRoute.callOnClick();

                break;
            case R.id.add_area:    fabViewDataBulloonShowed=true;
                                  viewData = true;
                                ViewData(null);
                                fabAddArea.callOnClick();
                                break;
            case R.id.add_places:    fabViewDataBulloonShowed=true;
                viewData = true;
                ViewData(null);
                fabAddPlace.callOnClick();
                break;
            case R.id.add_routes:    fabViewDataBulloonShowed=true;
                viewData = true;
                ViewData(null);
                fabAddRoute.callOnClick();
                break;
            case R.id.add_work:
                Intent intent=new Intent(this,ViewListActivity.class);
                intent.putExtra("query","work");
                startActivity(intent);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private FloatingActionButton fabMoreLess;
    private RecyclerView fieldsRv;
    private  LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private void setBottomSheet() {
        kharifScrollView.setVisibility(View.VISIBLE);

        setUpReferences();


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        fabMoreLess.setImageResource(R.drawable.more);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        fabMoreLess.setImageResource(R.drawable.less);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        sheetBehavior.setHideable(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setUpReferences() {
         layoutBottomSheet = findViewById(R.id.bottom_sheet);
        fabMoreLess=findViewById(R.id.fabMoreLess);
        fieldsRv=findViewById(R.id.fieldRV);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
    }

    List<CropModel> currentViewCropList=new ArrayList<>();
    public void searchForCropInList(String crop){
        currentViewCropList.clear();
        boolean gotCrop=false;
        for(CropModel model:cropList){
            if(model.getCropName().contains(crop)){
                gotCrop=true;
                currentViewCropList.add(model);
                setCropsPolygon(model);
                break;
            }
        }
        if(gotCrop){
            layoutBottomSheet.setVisibility(View.VISIBLE);
        showCropDetailsOnBottomSheet();
        }else{
            layoutBottomSheet.setVisibility(View.GONE);
            Toast.makeText(this, "No Crop Uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    ViewCropsAdapter viewCropsAdapter;
    private void showCropDetailsOnBottomSheet() {
        viewCropsAdapter=new ViewCropsAdapter(AddOnMap.this,currentViewCropList);
        fieldsRv.setAdapter(viewCropsAdapter);
    }

    private void setCropsPolygon(CropModel model) {
        mMap.clear();
        List<LatLng> list=PolyUtil.decode(model.getLatlngList());
        MarkerOptions markerOptions=new MarkerOptions();
        if(list==null){
            Toast.makeText(this, "Error try later!", Toast.LENGTH_SHORT).show();
            return;
        }
        markerOptions.position(list.get(0));
        markerOptions.title(model.getFieldName());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
        showPolygon(list);
    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
        Chip chip=group.findViewById(checkedId);
        if(group==kharifChips){

            if (chip != null) {
                String cropType = chip.getText().toString();
                searchForCropInList(cropType);
//                switch (cropType){
//                    case "Soybean": searchForCropInList(cropType);
//                        break;
//                    case "Sesame": searchForCropInList(cropType);
//                        break;
//                    case "Mung bean": searchForCropInList(cropType);
//                        break;
//                    case "Guar": searchForCropInList(cropType);
//                        break;
//                    case "Arhar": searchForCropInList(cropType);
//                        break;
//                    case "Groundnut": searchForCropInList(cropType);
//                        break;
//                    case "Green gram": searchForCropInList(cropType);
//                        break;
//                        default:
//                }




            }
            else{
                layoutBottomSheet.setVisibility(View.GONE);

            }

        }
    }

    public void viewBottomSheet(View view) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            fabMoreLess.setImageResource(R.drawable.more);
        }else{
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fabMoreLess.setImageResource(R.drawable.less);
        }
    }

    public void refresh(View view) {
        startActivity(new Intent(this,AddOnMap.class));
        finish();
    }

    boolean showPolgon=false;
    public void showPolygon(View view) {
        if(areaPolygon!=null){
            if(areaPolygon.isVisible()){
                areaPolygon.setVisible(false);
                showPolgon=false;
            }else{
                areaPolygon.setVisible(true);
                showPolgon=true;
            }
        }
    }
}