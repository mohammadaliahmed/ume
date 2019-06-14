package com.appsinventiv.ume.Models;

public class NotificationModel {
    String id,username,hisusername,picUrl,title,type;
    long time;

    public NotificationModel(String id, String username, String hisusername, String picUrl, String title, String type, long time) {
        this.id = id;
        this.username = username;
        this.hisusername = hisusername;
        this.picUrl = picUrl;
        this.title = title;
        this.type = type;
        this.time = time;
    }

    public NotificationModel() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHisusername() {
        return hisusername;
    }

    public void setHisusername(String hisusername) {
        this.hisusername = hisusername;
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
}
