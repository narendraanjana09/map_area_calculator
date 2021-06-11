package com.nsa.mapsspeech.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.nsa.mapsspeech.Adapter.CropsAdapter;
import com.nsa.mapsspeech.Model.CropModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import static com.nsa.mapsspeech.Activities.AddOnMap.areaModelList;
import static com.nsa.mapsspeech.Activities.AddOnMap.cropList;
import static com.nsa.mapsspeech.Activities.MapsActivity.SEASON;

public class CropsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView crops_rv;
    CropsAdapter cropsAdapter;

    public void finishOnClick() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops);
        crops_rv=findViewById(R.id.crops_rv);
        crops_rv.setLayoutManager(new LinearLayoutManager(this));
        List<RouteModel> list=new ArrayList<>();
        List<Integer> index=new ArrayList<>();

        for(int i=0;i<areaModelList.size();i++){
            for(int j=0;j<cropList.size();j++){
                if(cropList.get(j).getKey().equals(areaModelList.get(i).getKey())){
                 if(!index.contains(i)){
                     index.add(i);
                 }
                }
            }
        }
        for(int i=0;i<areaModelList.size();i++){
            if(!index.contains(i)){
                list.add(areaModelList.get(i));
            }
        }
        if(list.size()==0){
            Toast.makeText(this, "Upload Another Field!", Toast.LENGTH_SHORT).show();
            finish();
        }

        cropsAdapter=new CropsAdapter(CropsActivity.this,list);
        crops_rv.setAdapter(cropsAdapter);

        getDrawer();
    }

    private void getDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar );
        toolbar.setTitle(SEASON+" Season");
        drawer= findViewById(R.id.crop_activity_drawer ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string. navigation_drawer_open ,
                R.string. navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        navigationView= findViewById(R.id.cropActivity_nav ) ;
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
        getMenuInflater().inflate(R.menu.crops_activity_menu , menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId() ;

        return super .onOptionsItemSelected(item) ;
    }

    public void finishFromAdapter(){
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}