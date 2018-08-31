package com.instaclonegram.models;

public class Photo {
    private String photo;
    private String filename;
    private int id;
    private String username;
    private String caption;
    private String url;
    private int like;
    private String timeStamp;
    private int height;
    private int width;
    private String privacy;

    public Photo() {

    }

    public Photo(String photo) {
        super();
        this.photo = photo;
    }

    public Photo(String photo, String filename, int id,  String username, int like, String timeStamp, int width, int height) {
        super();
        this.photo = photo;
        this.filename = filename;
        this.id = id;
        this.username = username;
        this.like = like;
        this.timeStamp = timeStamp;
        this.width = width;
        this.height = height;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
