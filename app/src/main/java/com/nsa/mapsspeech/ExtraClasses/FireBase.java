package com.nsa.mapsspeech.ExtraClasses;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {
    FirebaseDatabase database;
    DatabaseReference referenceUsers,referenceLocationUpdate
            ,referenceDataPlace,referenceDataPolyLine,referenceDataArea;


    public FireBase(){
        database = FirebaseDatabase.getInstance();
        referenceUsers=database.getReference("TheMapThing").child("users");
        referenceLocationUpdate = database.getReference("TheMapThing").child("locationUpdate");
        referenceDataPlace=database.getReference("TheMapThing").child("data").child("places");
        referenceDataPolyLine=database.getReference("TheMapThing").child("data").child("polylines");
        referenceDataArea=database.getReference("TheMapThing").child("data").child("areas");
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
