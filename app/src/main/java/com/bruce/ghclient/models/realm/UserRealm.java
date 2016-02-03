package com.bruce.ghclient.models.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Bruce too
 * On 2016/2/3
 * At 10:30
 * Model used by Realm for persis data
 */
public class UserRealm extends RealmObject{

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey
    private long id;

    private String name;
    private String location;
    private String avatar_url;
    private int public_repos;
    private int public_gists;
    private String company;
    private int followers;
    private int following;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public int getPublic_gists() {
        return public_gists;
    }

    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }
}
