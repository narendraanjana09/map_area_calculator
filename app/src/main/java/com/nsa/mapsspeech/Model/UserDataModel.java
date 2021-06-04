package com.nsa.mapsspeech.Model;

public class UserDataModel {
    private String name;
    private String email;
    private String profileUrl;
    private String token;

    public UserDataModel() {
    }

    public UserDataModel(String name, String email, String profileUrl, String token) {
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
