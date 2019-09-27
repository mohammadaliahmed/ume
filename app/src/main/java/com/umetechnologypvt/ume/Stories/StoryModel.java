package com.umetechnologypvt.ume.Stories;

public class StoryModel {
    String id,storyByName,storyByUsername,storyByPicUrl,imageUrl,videoUrl,storyType;
    long time;
    String countryCode;
    String proxyUrl;

    public StoryModel(String id, String storyByName, String storyByUsername,
                      String storyByPicUrl, String imageUrl, String videoUrl, String storyType, long time,String countryCode) {
        this.id = id;
        this.storyByName = storyByName;
        this.storyByUsername = storyByUsername;
        this.storyByPicUrl = storyByPicUrl;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.storyType = storyType;
        this.time = time;
        this.countryCode = countryCode;
    }

    public StoryModel() {
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryByName() {
        return storyByName;
    }

    public void setStoryByName(String storyByName) {
        this.storyByName = storyByName;
    }

    public String getStoryByUsername() {
        return storyByUsername;
    }

    public void setStoryByUsername(String storyByUsername) {
        this.storyByUsername = storyByUsername;
    }

    public String getStoryByPicUrl() {
        return storyByPicUrl;
    }

    public void setStoryByPicUrl(String storyByPicUrl) {
        this.storyByPicUrl = storyByPicUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
