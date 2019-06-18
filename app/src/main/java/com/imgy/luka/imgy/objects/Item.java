package com.imgy.luka.imgy.objects;

import java.util.ArrayList;

public class Item {

    private String username;
    private String imageUrl;
    private String description;
    private String userId;

    public Item(String username, String imageUrl, String description, String userId) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
    }

    public Item(String username, String imageUrl, String description) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.description = description;
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
}
