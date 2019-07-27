package com.umetechnologypvt.ume.Models;

public class PostsModel {
    String id, postBy, postByName, userPicUrl, comment, commentBy,commentByName,
            commentByPicUrl, pictureUrl, videoUrl, videoThumbnailUrl, location;
    long time;
    long commentsCount, likesCount;
    String type;

    int postFor;
    long videoViews;


    public PostsModel() {
    }
    //for picture
    public PostsModel(String id, String postBy, String postByName, String userPicUrl,
                      String comment, String commentBy,String commentByName, String commentByPicUrl,
                      String pictureUrl, long time,
                      String type, int postFor,long commentsCount) {
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
    }
    //for video
    public PostsModel(String id, String postBy, String postByName, String userPicUrl,
                      String comment, String commentBy,String commentByName, String commentByPicUrl,
                      String videoUrl, String videoThumbnailUrl, long time,
                      String type, int postFor,long commentsCount,long videoViews) {
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
