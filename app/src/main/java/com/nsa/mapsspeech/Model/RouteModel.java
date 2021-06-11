package com.nsa.mapsspeech.Model;

public class RouteModel {
    String key;
    String name;
    String date;
    String points;

    public RouteModel() {
    }


    public RouteModel(String key,String name, String date, String points) {
        this.key=key;
        this.name = name;
        this.date = date;
        this.points = points;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
