package com.nsa.mapsspeech.ExtraClasses;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.nsa.mapsspeech.Activities.MapsActivity.SEASON;
import static com.nsa.mapsspeech.Activities.MapsActivity.YEAR;


public class FireBase {
    FirebaseDatabase database;
    DatabaseReference referenceUsers,referenceLocationUpdate
            ,referenceDataPlace,referenceDataPolyLine,
            referenceDataArea,referenceCrops
            ,referenceWork;



    public FireBase(){
        database = FirebaseDatabase.getInstance();
        referenceUsers=database.getReference("TheMapThing").child("users");
        referenceLocationUpdate = database.getReference("TheMapThing").child("locationUpdate");
        referenceDataPlace=database.getReference("TheMapThing").child("data").child("places");
        referenceDataPolyLine=database.getReference("TheMapThing").child("data").child("polylines");
        referenceDataArea=database.getReference("TheMapThing").child("data").child("areas");
        referenceCrops=database.getReference("TheMapThing").child("crops").child(YEAR).child(SEASON);
        referenceWork=database.getReference("TheMapThing").child("work").child(YEAR).child(SEASON);
    }

    public DatabaseReference getReferenceWork() {
        return referenceWork;
    }

    public DatabaseReference getReferenceCrops() {
        return referenceCrops;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getReferenceUsers() {
        return referenceUsers;
    }

    public DatabaseReference getReferenceLocationUpdate() {
        return referenceLocationUpdate;
    }

    public DatabaseReference getReferenceDataPlace() {
        return referenceDataPlace;
    }

    public DatabaseReference getReferenceDataPolyLine() {
        return referenceDataPolyLine;
    }

    public DatabaseReference getReferenceDataArea() {
        return referenceDataArea;
    }
}
