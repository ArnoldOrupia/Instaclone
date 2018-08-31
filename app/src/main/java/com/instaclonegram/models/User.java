package com.instaclonegram.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String picture;
    private String username;
    private String name;
    private String description;
    private String link;
    private String email;
    private String privacy;
    private int followersCnt;
    private int followingCnt;
    private int postCnt;

    public User() {

    }

    public User(String id, String picture, String username, String name, String description, String link, String email,
                String privacy, int followersCnt, int followingCnt, int postCnt) {
        this.id = id;
        this.picture = picture;
        this.username = username;
        this.name = name;
        this.description = description;
        this.link = link;
        this.email = email;
        this.privacy = privacy;
        this.followersCnt = followersCnt;
        this.followingCnt = followingCnt;
        this.postCnt = postCnt;
    }

    public String getPicture() { return picture; }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getFollowersCnt() {
        return followersCnt;
    }

    public void setFollowersCnt(int followersCnt) {
        this.followersCnt = followersCnt;
    }

    public int getFollowingCnt() {
        return followingCnt;
    }

    public void setFollowingCnt(int followingCnt) {
        this.followingCnt = followingCnt;
    }

    public int getPostCnt() {
        return postCnt;
    }

    public void setPostCnt(int postCnt) {
        this.postCnt = postCnt;
    }

}
