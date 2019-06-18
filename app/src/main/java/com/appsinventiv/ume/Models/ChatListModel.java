package com.appsinventiv.ume.Models;


import java.util.Objects;

public class ChatListModel {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatListModel product = (ChatListModel) o;
        if (username != null && product.username != null) {
            if (username.equalsIgnoreCase(product.username)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;

        }
//        return id != null ? !id.equals(product.id) : product.id != null;
    }

}
