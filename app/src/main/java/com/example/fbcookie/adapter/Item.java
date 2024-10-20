package com.example.fbcookie.adapter;

import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String url;
    private String userAgent; // Example additional data

    public Item(String title, String url, String userAgent) {
        this.title = title;
        this.url = url;
        this.userAgent = userAgent;
    }

    // Getters and setters for the fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}