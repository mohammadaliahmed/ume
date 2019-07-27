package com.umetechnologypvt.ume.Activities.Comments;

public class CommentsModel {
    String commentId,commentText,commentBy,commentByName,commentByPicUrl;
    long time;

    public CommentsModel(String commentId, String commentText, String commentBy, String commentByName, String commentByPicUrl, long time) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.commentBy = commentBy;
        this.commentByName = commentByName;
        this.commentByPicUrl = commentByPicUrl;
        this.time = time;
    }

    public CommentsModel() {
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getCommentByName() {
        return commentByName;
    }

    public void setCommentByName(String commentByName) {
        this.commentByName = commentByName;
    }

    public String getCommentByPicUrl() {
        return commentByPicUrl;
    }

    public void setCommentByPicUrl(String commentByPicUrl) {
        this.commentByPicUrl = commentByPicUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
