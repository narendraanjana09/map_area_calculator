package com.nsa.mapsspeech.ExtraClasses;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {
    FirebaseDatabase database;
    DatabaseReference referenceUsers,referenceLocationUpdate;


    public FireBase(){
        database = FirebaseDatabase.getInstance();
        referenceUsers=database.getReference("TheMapThing").child("users");
        referenceLocationUpdate = database.getReference("TheMapThing").child("locationUpdate");
    }

    public DatabaseReference getReferenceUsers() {
        return referenceUsers;
    }

    public DatabaseReference getReferenceLocationUpdate() {
        return referenceLocationUpdate;
    }
}
