package com.appsinventiv.ume.Models;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    String username,name,phone,picUrl,fcmKey;

    long time;
    String authId;
    boolean verified;
    String status;
    String gender,language,country;
    String dob;

    List<String> interests=new ArrayList<>();
    List<String> confirmFriends=new ArrayList<>();
    List<String> requestSent=new ArrayList<>();
    List<String> requestReceived=new ArrayList<>();
    List<String> blockedUsers=new ArrayList<>();
    List<String> learningLanguage=new ArrayList<>();



    public UserModel() {
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

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
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
}
