package com.imgy.luka.imgy.objects;

import java.util.ArrayList;

public class FeedItem {

    private String username;
    private String imageUrl;
    private String description;

    public FeedItem(String username, String imageUrl, String description) {
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

    public static ArrayList<FeedItem> initItems() {
        ArrayList<FeedItem> items = new ArrayList<FeedItem>();
        items.add(0, new FeedItem("marin123", "pdkmaspdm", "desc1"));
        items.add(1, new FeedItem("marko_brat", "pdkmaspdm", "desc2"));
        items.add(2, new FeedItem("burke", "pdkmaspdm", "desc3"));
        items.add(3, new FeedItem("likcina131", "pdkmaspdm", "desc4"));
        items.add(4, new FeedItem("brat555", "pdkmaspdm", "desc5"));
        return items;
    }


}
