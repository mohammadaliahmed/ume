package com.umetechnologypvt.ume.Stories;

public class StoryViewsModel {
   String storyId,seenByName,seenByPicUrl;
   long time;

    public StoryViewsModel() {
    }

    public StoryViewsModel(String storyId, String seenByName, String seenByPicUrl, long time) {
        this.storyId = storyId;
        this.seenByName = seenByName;
        this.seenByPicUrl = seenByPicUrl;
        this.time = time;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getSeenByName() {
        return seenByName;
    }

    public void setSeenByName(String seenByName) {
        this.seenByName = seenByName;
    }

    public String getSeenByPicUrl() {
        return seenByPicUrl;
    }

    public void setSeenByPicUrl(String seenByPicUrl) {
        this.seenByPicUrl = seenByPicUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
