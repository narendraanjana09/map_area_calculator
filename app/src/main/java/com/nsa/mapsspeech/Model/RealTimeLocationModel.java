package com.nsa.mapsspeech.Model;

public class RealTimeLocationModel {
    String location;
    String status;
    String speed;
    String chargingLevel;
    boolean isCharging;

    public RealTimeLocationModel() {
    }

    public RealTimeLocationModel(String location, String status, String speed, String chargingLevel, boolean isCharging) {
        this.location = location;
        this.status = status;
        this.speed = speed;
        this.chargingLevel = chargingLevel;
        this.isCharging = isCharging;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
