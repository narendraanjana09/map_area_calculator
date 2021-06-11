package com.nsa.mapsspeech.Model;

import java.util.ArrayList;

public class PlaceModel {
    String key;
    String lat;
    String lng;
    String placeName;
    String date;
    private ArrayList<String> imageUriList;


    public PlaceModel() {
    }

    public PlaceModel(String key, String lat, String lng, String placeName, String date, ArrayList<String> imageUriList) {
        this.key = key;
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.date = date;
        this.imageUriList = imageUriList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(ArrayList<String> imageUriList) {
        this.imageUriList = imageUriList;
    }
}
