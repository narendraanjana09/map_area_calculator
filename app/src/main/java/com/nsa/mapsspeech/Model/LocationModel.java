package com.nsa.mapsspeech.Model;

public class LocationModel {
    String lat;
    String lng;
    String status;
    String speed;
    String chargingLevel;
    boolean isCharging;

    public LocationModel() {
    }

    public LocationModel(String lat, String lng, String status, String speed, String chargingLevel, boolean isCharging) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.speed = speed;
        this.chargingLevel = chargingLevel;
        this.isCharging = isCharging;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getChargingLevel() {
        return chargingLevel;
    }

    public void setChargingLevel(String chargingLevel) {
        this.chargingLevel = chargingLevel;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }
}
