package com.nsa.mapsspeech.Model.Work;

public class WorkModel {
    private String key;
    private String labourCount;
    private String labourNames;
    private String workDescription;
    private String cost;
    private String date;

    public WorkModel() {
    }

    public WorkModel(String key,String labourCount, String labourNames, String workDescription, String cost,String date) {
        this.key=key;
        this.labourCount = labourCount;
        this.labourNames = labourNames;
        this.workDescription = workDescription;
        this.cost = cost;
        this.date=date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLabourCount() {
        return labourCount;
    }

    public void setLabourCount(String labourCount) {
        this.labourCount = labourCount;
    }

    public String getLabourNames() {
        return labourNames;
    }

    public void setLabourNames(String labourNames) {
        this.labourNames = labourNames;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
