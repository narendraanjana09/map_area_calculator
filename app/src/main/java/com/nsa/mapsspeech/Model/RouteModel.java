package com.nsa.mapsspeech.Model;

public class RouteModel {
    String name;
    String date;
    String points;

    public RouteModel() {
    }

    public RouteModel(String name, String date, String points) {
        this.name = name;
        this.date = date;
        this.points = points;
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
