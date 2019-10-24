package com.umetechnologypvt.ume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("uploaded")
    @Expose
    private Boolean uploaded;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("value2")
    @Expose
    private String finalUrl;

    public Boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }
}
