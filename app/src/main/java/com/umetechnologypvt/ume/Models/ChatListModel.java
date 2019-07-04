package com.umetechnologypvt.ume.Models;


public class ChatListModel implements Comparable{
    String username;
    ChatModel message;

    public ChatListModel(String username, ChatModel message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ChatModel getMessage() {
        return message;
    }

    public void setMessage(ChatModel message) {
        this.message = message;
    }

    @Override
    public int compareTo(Object obj) {
        obj=(ChatListModel) obj;
        return Long.compare(this.message.getTime(),((((ChatListModel) obj).getMessage().getTime())));
    }

}
