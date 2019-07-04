package com.umetechnologypvt.ume.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("countryName")
    @Expose

    private String countryName;
    @SerializedName("picUrl")
    @Expose
    private String picUrl;

    @SerializedName("countryCode")
    @Expose
    String countryCode;

    boolean section;

    public Country(String countryName, String picUrl, String countryCode, boolean section) {
        this.countryName = countryName;
        this.picUrl = picUrl;
        this.countryCode = countryCode;
        this.section = section;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
