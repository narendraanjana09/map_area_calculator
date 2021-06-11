package com.nsa.mapsspeech.Model.Work;

public class OWModel { //OtherWorkModel
    private String key;
    private String workDes;
    private String date;

    public OWModel() {
    }

    public OWModel(String key, String workDes, String date) {
        this.key = key;
        this.workDes = workDes;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWorkDes() {
        return workDes;
    }

    public void setWorkDes(String workDes) {
        this.workDes = workDes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
