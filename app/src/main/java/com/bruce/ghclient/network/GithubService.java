package com.bruce.ghclient.network;

import com.bruce.ghclient.models.User;
import com.bruce.ghclient.models.realm.UserRealm;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 15:12
 */
public interface GithubService {

    @GET("/user/{access_token}")
    Observable<User> getOwnProfile(@Path("access_token") String token);

    @GET("/users/{user}")
    Observable<UserRealm> getUserProfile(@Path("user") String userName);
}
