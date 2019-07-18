package com.umetechnologypvt.ume.Models;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    String username,name,phone,picUrl,fcmKey;

    long time;
    String authId;
    boolean verified;
    String status;
    String gender,language,country,currentLocation;
    String dob;

    List<String> interests=new ArrayList<>();
    List<String> learningLanguage=new ArrayList<>();

    List<String> confirmFriends=new ArrayList<>();
    List<String> requestSent=new ArrayList<>();
    List<String> requestReceived=new ArrayList<>();

    String password;
    String email;

    private int age;

    String countryNameCode;

    String about;

    double latitude,longitude;

    public UserModel() {
    }

    public UserModel(String gender, String language, String country, String currentLocation,
                     List<String> interests, List<String> learningLanguage) {
        this.gender = gender;
        this.language = language;
        this.country = country;
        this.currentLocation = currentLocation;
        this.interests = interests;
        this.learningLanguage = learningLanguage;
    }

    public UserModel(String username, String phone, String fcmKey, long time, String authId, boolean verified, String status) {
        this.username = username;
        this.phone = phone;
        this.fcmKey = fcmKey;
        this.time = time;
        this.authId = authId;
        this.verified = verified;
        this.status = status;
    }

    public UserModel(String username, String name, String email,  String password,long time) {
        this.username = username;
        this.name = name;
        this.time = time;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCountryNameCode() {
        return countryNameCode;
    }

    public void setCountryNameCode(String countryNameCode) {
        this.countryNameCode = countryNameCode;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<String> getRequestReceived() {
        return requestReceived;
    }

    public void setRequestReceived(List<String> requestReceived) {
        this.requestReceived = requestReceived;
    }

    public List<String> getConfirmFriends() {
        return confirmFriends;
    }

    public void setConfirmFriends(List<String> confirmFriends) {
        this.confirmFriends = confirmFriends;
    }

    public List<String> getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(List<String> requestSent) {
        this.requestSent = requestSent;
    }



    public List<String> getLearningLanguage() {
        return learningLanguage;
    }

    public void setLearningLanguage(List<String> learningLanguage) {
        this.learningLanguage = learningLanguage;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age=age;
    }
}
