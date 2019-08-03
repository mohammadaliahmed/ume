package com.umetechnologypvt.ume.Interface;

import com.umetechnologypvt.ume.Models.PostsModel;

public interface PostAdaptersCallbacks {
    public void takeUserToMyUserProfile(String userId);

    public void takeUserToOtherUserProfile(String userId);

    public void takeUserToLikesScreen(String postId);

    public void onLikedPost(PostsModel model);

    public void onUnlikedPost(PostsModel model);

    public void onFileDownload(String filename);

    public void onDelete(PostsModel model, int position);

    public void onMutePost(PostsModel model);
    public void onUnMutePost(PostsModel model);
}
