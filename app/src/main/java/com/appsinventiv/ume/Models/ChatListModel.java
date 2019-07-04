package com.appsinventiv.ume.Models;


import com.appsinventiv.ume.Activities.MainActivity;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

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
