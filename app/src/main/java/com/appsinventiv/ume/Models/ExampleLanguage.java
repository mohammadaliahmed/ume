
package com.appsinventiv.ume.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExampleLanguage {

    @SerializedName("Languages")
    @Expose
    private List<LangaugeModel> languages = null;

    public List<LangaugeModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LangaugeModel> languages) {
        this.languages = languages;
    }
}
