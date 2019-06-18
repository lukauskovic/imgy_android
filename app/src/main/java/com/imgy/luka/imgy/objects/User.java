package com.imgy.luka.imgy.objects;

import java.util.ArrayList;

public class User {

    private String email;
    private String username;
    private String followersCount;
    private String followingCount;
    private String profileImageUrl;
    private String  photosCount;
    private ArrayList<String> followers;
    private ArrayList<String> following;

    public User(String email, String username, String followersCount, String followingCount, String photosCount) {
        this.email = email;
        this.username = username;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.photosCount = photosCount;
    }

    public User(String username, String followersCount, String followingCount, String photosCount) {
        this.username = username;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.photosCount = photosCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(String photosCount) {
        this.photosCount = photosCount;
    }
}
