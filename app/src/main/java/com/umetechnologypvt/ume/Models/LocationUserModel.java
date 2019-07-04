package com.umetechnologypvt.ume.Models;

public class LocationUserModel {
    UserModel userModel;
    double distance;

    public LocationUserModel(UserModel userModel, double distance) {
        this.userModel = userModel;
        this.distance = distance;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
