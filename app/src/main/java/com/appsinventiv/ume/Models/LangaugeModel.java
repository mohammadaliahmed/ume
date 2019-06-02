package com.appsinventiv.ume.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LangaugeModel {
    @SerializedName("languageName")
    @Expose
    String languageName;

    @SerializedName("picUrl")
    @Expose
    String picUrl;

    public LangaugeModel(String languageName, String picUrl) {
        this.languageName = languageName;
        this.picUrl = picUrl;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
