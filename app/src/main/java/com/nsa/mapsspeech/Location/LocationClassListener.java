package com.nsa.mapsspeech.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public interface LocationClassListener {
    public void onLocation(LatLng latLng);
}
