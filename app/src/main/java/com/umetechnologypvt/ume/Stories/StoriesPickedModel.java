package com.umetechnologypvt.ume.Stories;

import android.net.Uri;

public class StoriesPickedModel {
    String id,uri, path, type;
    long time;

    public StoriesPickedModel(String id, String uri, String path, String type, long time) {
        this.id = id;
        this.uri = uri;
        this.path = path;
        this.type = type;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
