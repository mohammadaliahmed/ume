package com.appsinventiv.ume.Models;

public class ChatModel {

    String id, messageText, messageBy, imageUrl, audioUrl, videoUrl, documentUrl;
    String username, name, picUrl;
    String messageType, mediaType;
    String roomId;
    long time;
    long mediaTime;
    String stickerUrl;
    String videoImgUrl;
    boolean videoUploaded;
    String messageStatus;
    boolean audioUploaded;
    String documentFileName;
    String translatedText, language,originalText;


    public ChatModel(String id, String messageText, String messageBy,
                     String username, String name, String picUrl, String messageType,
                     long time, String messageStatus, String translatedText,String originalText, String language

    ) {
        this.id = id;
        this.messageText = messageText;
        this.messageBy = messageBy;
        this.username = username;
        this.name = name;
        this.picUrl = picUrl;
        this.messageType = messageType;
        this.time = time;
        this.messageStatus = messageStatus;
        this.translatedText = translatedText;
        this.originalText = originalText;
        this.language = language;
    }

    public ChatModel(String id, String messageText, String messageBy, String imageUrl, String audioUrl, String videoUrl, String documentUrl,
                     String stickerUrl,
                     String username, String name, String picUrl, String messageType, String mediaType, String roomId,
                     long time, long mediaTime, String messageStatus

    ) {
        this.id = id;
        this.messageText = messageText;
        this.messageBy = messageBy;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.videoUrl = videoUrl;
        this.documentUrl = documentUrl;
        this.username = username;
        this.name = name;
        this.picUrl = picUrl;
        this.messageType = messageType;
        this.mediaType = mediaType;
        this.roomId = roomId;
        this.time = time;
        this.mediaTime = mediaTime;
        this.stickerUrl = stickerUrl;
        this.messageStatus = messageStatus;
    }

    public ChatModel() {
    }


    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public boolean isAudioUploaded() {
        return audioUploaded;
    }

    public void setAudioUploaded(boolean audioUploaded) {
        this.audioUploaded = audioUploaded;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public boolean isVideoUploaded() {
        return videoUploaded;
    }

    public void setVideoUploaded(boolean videoUploaded) {
        this.videoUploaded = videoUploaded;
    }

    public String getVideoImgUrl() {
        return videoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        this.videoImgUrl = videoImgUrl;
    }

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageBy() {
        return messageBy;
    }

    public void setMessageBy(String messageBy) {
        this.messageBy = messageBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMediaTime() {
        return mediaTime;
    }

    public void setMediaTime(long mediaTime) {
        this.mediaTime = mediaTime;
    }
}
