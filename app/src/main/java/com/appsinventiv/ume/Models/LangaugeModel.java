package com.appsinventiv.ume.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LangaugeModel {

    String languageName;


    int picUrl;

    String langCode;
    String countryCode;

    int picDrawable;
    boolean section;

    public LangaugeModel(String languageName, int picUrl) {
        this.languageName = languageName;
        this.picUrl = picUrl;
    }

    public LangaugeModel(String languageName, String langCode, String countryCode, int picDrawable) {
        this.languageName = languageName;
        this.langCode = langCode;
        this.picDrawable = picDrawable;
        this.countryCode = countryCode;
    }

    public LangaugeModel(String languageName, String langCode, String countryCode, int picDrawable,boolean section) {
        this.languageName = languageName;
        this.langCode = langCode;
        this.picDrawable = picDrawable;
        this.countryCode = countryCode;
        this.section=section;
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

    public int getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(int picUrl) {
        this.picUrl = picUrl;
    }
}
