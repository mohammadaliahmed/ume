package com.umetechnologypvt.ume.Models;

public class NotificationModel {
    String notifcationId,username,id,picUrl,title,type;
    long time;

    public NotificationModel(String notifcationId, String username, String id, String picUrl, String title, String type, long time) {
        this.notifcationId = notifcationId;
        this.username = username;
        this.id = id;
        this.picUrl = picUrl;
        this.title = title;
        this.type = type;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getNotifcationId() {
        return notifcationId;
    }

    public void setNotifcationId(String notifcationId) {
        this.notifcationId = notifcationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
