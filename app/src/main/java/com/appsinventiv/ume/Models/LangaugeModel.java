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

    String langCode;

    int picDrawable;

    public LangaugeModel(String languageName, String langCode, int picDrawable) {
        this.languageName = languageName;
        this.langCode = langCode;
        this.picDrawable = picDrawable;
    }

    public String getLangCode() {
        return langCode;
    }

    public int getPicDrawable() {
        return picDrawable;
    }

    public void setPicDrawable(int picDrawable) {
        this.picDrawable = picDrawable;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
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
