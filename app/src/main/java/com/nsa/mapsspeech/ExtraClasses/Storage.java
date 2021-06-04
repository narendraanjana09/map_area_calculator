package com.nsa.mapsspeech.ExtraClasses;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {
    FirebaseStorage storage;
    StorageReference storageReference;
    public Storage(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("TheMapThing");
    }

    public FirebaseStorage getStorage() {
        return storage;
    }



    public StorageReference getStorageReference() {
        return storageReference;
    }

}
