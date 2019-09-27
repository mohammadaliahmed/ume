package com.umetechnologypvt.ume.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.Stories.StoriesPickedModel;
import com.umetechnologypvt.ume.Stories.StoryModel;
import com.umetechnologypvt.ume.Stories.StoryViewsModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }


    public static void setMessagesList(String username, ArrayList<ChatModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter(username + "messages", json);
    }

    public static ArrayList getMessagesList(String username) {
        Gson gson = new Gson();
        ArrayList<ChatModel> playersList = (ArrayList<ChatModel>) gson.fromJson(preferenceGetter(username + "messages"),
                new TypeToken<ArrayList<ChatModel>>() {
                }.getType());
        return playersList;
    }


    public static void setMultiPickedImg(List<String> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("imgs", json);
    }

    public static ArrayList getMultiImgs() {
        Gson gson = new Gson();
        ArrayList<String> playersList = (ArrayList<String>) gson.fromJson(preferenceGetter("imgs"),
                new TypeToken<ArrayList<String>>() {
                }.getType());
        return playersList;
    }


    public static void setFriendsList(ArrayList<UserModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("friends", json);
    }

    public static ArrayList getFriendsList() {
        Gson gson = new Gson();
        ArrayList<UserModel> playersList = (ArrayList<UserModel>) gson.fromJson(preferenceGetter("friends"),
                new TypeToken<ArrayList<UserModel>>() {
                }.getType());
        return playersList;
    }

    public static void setPickedList(ArrayList<StoriesPickedModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("storiesPicked", json);
    }

    public static ArrayList getPickedList() {
        Gson gson = new Gson();
        ArrayList<StoriesPickedModel> playersList = (ArrayList<StoriesPickedModel>) gson.fromJson(preferenceGetter("storiesPicked"),
                new TypeToken<ArrayList<StoriesPickedModel>>() {
                }.getType());
        return playersList;
    }

    public static void setMutedList(List<String> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("mutes", json);
    }

    public static ArrayList getMutedList() {
        Gson gson = new Gson();
        ArrayList<String> playersList = (ArrayList<String>) gson.fromJson(preferenceGetter("mutes"),
                new TypeToken<ArrayList<String>>() {
                }.getType());
        return playersList == null ? new ArrayList() : playersList;
    }


    public static void setHeadNotificationCount(String id, String count) {
        preferenceSetter(id, count);
    }

    public static String getHeadNotificationCount(String id) {
        return preferenceGetter(id);
    }


    public static void setChatList(ArrayList<ChatListModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("ChatList", json);
    }

    public static ArrayList getChatList() {
        Gson gson = new Gson();
        ArrayList<ChatListModel> playersList = (ArrayList<ChatListModel>) gson.fromJson(preferenceGetter("ChatList"),
                new TypeToken<ArrayList<ChatListModel>>() {
                }.getType());
        return playersList;
    }


    public static void setPosts(ArrayList<PostsModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("PostsModel", json);
    }

    public static ArrayList<PostsModel> getPosts() {
        Gson gson = new Gson();
        ArrayList<PostsModel> playersList = (ArrayList<PostsModel>) gson.fromJson(preferenceGetter("PostsModel"),
                new TypeToken<ArrayList<PostsModel>>() {
                }.getType());
        return playersList;
    }

    public static void setHomePosts(ArrayList<PostsModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("homePosts", json);
    }

    public static ArrayList<PostsModel> getHomePosts() {
        Gson gson = new Gson();
        ArrayList<PostsModel> playersList = (ArrayList<PostsModel>) gson.fromJson(preferenceGetter("homePosts"),
                new TypeToken<ArrayList<PostsModel>>() {
                }.getType());
        return playersList == null ? new ArrayList<>() : playersList;
    }


    public static void setHomeStories(ArrayList<ArrayList<StoryModel>> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("getHomeStories", json);
    }

    public static ArrayList<ArrayList<StoryModel>> getHomeStories() {
        Gson gson = new Gson();
        ArrayList<ArrayList<StoryModel>> playersList = (ArrayList<ArrayList<StoryModel>>) gson.fromJson(preferenceGetter("getHomeStories"),
                new TypeToken<ArrayList<ArrayList<StoryModel>>>() {
                }.getType());

        return playersList == null ? new ArrayList<>() : playersList;
    }

    public static void setSavedPosts(ArrayList<PostsModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("savedPosts", json);
    }

    public static ArrayList<PostsModel> getSavedPosts() {
        Gson gson = new Gson();
        ArrayList<PostsModel> playersList = (ArrayList<PostsModel>) gson.fromJson(preferenceGetter("savedPosts"),
                new TypeToken<ArrayList<PostsModel>>() {
                }.getType());
        return playersList;
    }

    public static void setLikesList(ArrayList<String> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("getLikesList", json);
    }

    public static ArrayList getLikesList() {
        Gson gson = new Gson();
        ArrayList<String> playersList = (ArrayList<String>) gson.fromJson(preferenceGetter("getLikesList"),
                new TypeToken<ArrayList<String>>() {
                }.getType());
        return playersList;
    }


    public static void setParticipantModel(String userId, UserModel model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter(userId, json);
    }

    public static UserModel getParticipantModel(String userId) {
        Gson gson = new Gson();
        UserModel model = gson.fromJson(preferenceGetter(userId), UserModel.class);
        return model;
    }


    public static void setUserModel(UserModel model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("UserModel", json);
    }

    public static UserModel getUserModel() {
        Gson gson = new Gson();
        UserModel model = gson.fromJson(preferenceGetter("UserModel"), UserModel.class);
        return model;
    }


    public static void setSettingDone(String fcmKey) {
        preferenceSetter("setting", fcmKey);
    }

    public static String getSettingDone() {
        return preferenceGetter("setting");
    }

    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }

    public static void setPhoneNumber(String value) {
        preferenceSetter("phone", value);
    }

    public static String getPhoneNumber() {
        return preferenceGetter("phone");
    }

    public static void setCountryCode(String value) {
        preferenceSetter("getCountryCode", value);
    }

    public static String getCountryCode() {
        return preferenceGetter("getCountryCode");
    }

    public static void setMuted(String value) {
        preferenceSetter("muted", value);
    }

    public static String getMuted() {
        return preferenceGetter("muted");
    }


    public static void setIsLoggedIn(boolean value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("loggedIn", value);
        editor.apply();
    }

    public static boolean getIsLoggedIn() {
        SharedPreferences pref;
        boolean value;
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getBoolean("loggedIn", false);
        return value;
    }

    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }

    public static void setNotificationCount(String count) {
        preferenceSetter("setChatCount", count);
    }

    public static String getNotificationCount() {
        return preferenceGetter("setChatCount");
    }

    public static void setChatCount(String count) {
        preferenceSetter("chatCount", count);
    }

    public static String getChatCount() {
        return preferenceGetter("chatCount");
    }

    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
