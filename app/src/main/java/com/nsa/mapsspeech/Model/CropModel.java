package com.nsa.mapsspeech.Model;

public class CropModel {
    private String key;
    private String fieldName;
    private String cropName;
    private String cropSpecification;
    private String latlngList;
    private String cropPlantingDate;

    public CropModel() {
    }

    public CropModel(String key,String fieldName, String cropName, String cropSpecification, String latlngList, String cropPlantingDate) {
        this.key=key;
        this.fieldName = fieldName;
        this.cropName = cropName;
        this.cropSpecification = cropSpecification;
        this.latlngList = latlngList;
        this.cropPlantingDate = cropPlantingDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropSpecification() {
        return cropSpecification;
    }

    public void setCropSpecification(String cropSpecification) {
        this.cropSpecification = cropSpecification;
    }

    public String getLatlngList() {
        return latlngList;
    }

    public void setLatlngList(String latlngList) {
        this.latlngList = latlngList;
    }

    public String getCropPlantingDate() {
        return cropPlantingDate;
    }

    public void setCropPlantingDate(String cropPlantingDate) {
        this.cropPlantingDate = cropPlantingDate;
    }
}
