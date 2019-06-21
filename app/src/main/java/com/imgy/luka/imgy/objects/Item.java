package com.imgy.luka.imgy.objects;

import java.util.ArrayList;

public class Item {

    private String id;
    private String username;
    private String imageUrl;
    private String description;
    private String userId;
    private Integer likesCount;
    private ArrayList<String> likes;

    public Item(String id, String username, String imageUrl, String description, String userId, Integer likesCount, ArrayList<String> likes) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
        this.likesCount = likesCount;
        this.likes = likes;
        this.id = id;
    }

    public Item(String id, String username, String imageUrl, String description, Integer likesCount, ArrayList<String> likes) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.description = description;
        this.likesCount = likesCount;
        this.likes = likes;
        this.id = id;
    }

    public boolean isLiked(String userId){
        if(likes.contains(userId)) return true;
        else return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
