package com.umetechnologypvt.ume.Models;

import android.net.Uri;

import java.util.List;

public class PostsModel {
    String id, postBy, postByName, userPicUrl, comment, commentBy, commentByName,
            commentByPicUrl, pictureUrl, videoUrl, videoThumbnailUrl, location;
    long time;
    long commentsCount, likesCount;
    String type;

    int postFor;
    long videoViews;

    Uri mediaUri;
    private boolean isMute;
    String countryCode;

    List<String> multiImages;

    int userAge;
    String gender;

    boolean repost;
    String proxyUrl;

    public PostsModel() {
    }

    //for picture
    public PostsModel(String id, String postBy, String postByName, String userPicUrl,
                      String comment, String commentBy, String commentByName, String commentByPicUrl,
                      String pictureUrl, long time,
                      String type, int postFor, long commentsCount, String countryCode,int userAge,String gender) {
        this.id = id;
        this.postBy = postBy;
        this.comment = comment;
        this.commentBy = commentBy;
        this.commentByName = commentByName;
        this.pictureUrl = pictureUrl;
        this.userPicUrl = userPicUrl;
        this.postByName = postByName;
        this.time = time;
        this.type = type;
        this.commentByPicUrl = commentByPicUrl;
        this.postFor = postFor;
        this.commentsCount = commentsCount;
        this.countryCode = countryCode;
        this.userAge = userAge;
        this.gender = gender;
    }

    //for video
    public PostsModel(String id, String postBy, String postByName, String userPicUrl,
                      String comment, String commentBy, String commentByName, String commentByPicUrl,
                      String videoUrl, String videoThumbnailUrl, long time,
                      String type, int postFor, long commentsCount, long videoViews, String countryCode,int userAge,String gender) {
        this.id = id;
        this.postBy = postBy;
        this.comment = comment;
        this.commentBy = commentBy;
        this.commentByName = commentByName;
        this.videoUrl = videoUrl;
        this.videoThumbnailUrl = videoThumbnailUrl;
        this.userPicUrl = userPicUrl;
        this.postByName = postByName;
        this.time = time;
        this.type = type;
        this.commentByPicUrl = commentByPicUrl;
        this.postFor = postFor;
        this.commentsCount = commentsCount;
        this.videoViews = videoViews;
        this.countryCode = countryCode;
        this.userAge = userAge;
        this.gender = gender;
    }

    public boolean isRepost() {
        return repost;
    }

    public void setRepost(boolean repost) {
        this.repost = repost;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getMultiImages() {
        return multiImages;
    }

    public void setMultiImages(List<String> multiImages) {
        this.multiImages = multiImages;
    }

    public long getVideoViews() {
        return videoViews;
    }

    public void setVideoViews(long videoViews) {
        this.videoViews = videoViews;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public Uri getMediaUri() {

        return mediaUri;
    }


    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public void setMediaUri(Uri mediaUri) {
        this.mediaUri = mediaUri;
    }


    public String getCommentByName() {
        return commentByName;
    }

    public void setCommentByName(String commentByName) {
        this.commentByName = commentByName;
    }

    public String getPostByName() {
        return postByName;
    }

    public void setPostByName(String postByName) {
        this.postByName = postByName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getCommentByPicUrl() {
        return commentByPicUrl;
    }

    public void setCommentByPicUrl(String commentByPicUrl) {
        this.commentByPicUrl = commentByPicUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPostFor() {
        return postFor;
    }

    public void setPostFor(int postFor) {
        this.postFor = postFor;
    }
}
