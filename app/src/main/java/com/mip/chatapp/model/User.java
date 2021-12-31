package com.mip.chatapp.model;

public class User {
    private String ID;
    private String username;
    private String imageURL;
    private String status;
    private String search;

    public User(String ID, String username, String imageURL, String status, String search) {
        this.ID = ID;
        this.username = username;
        this.imageURL = imageURL;
        this.status=status;
        this.search=search;
    }

    public User() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
