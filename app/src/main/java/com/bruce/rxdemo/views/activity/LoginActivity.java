package com.bruce.rxdemo.views.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bruce.rxdemo.R;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 14:31
 */
public class LoginActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_login)
    void loginClick() {

        OAuth20Service service = new ServiceBuilder()
                .apiKey("brucetoo")
                .apiSecret("ty8922436")
                .callback("http://www.example.com/oauth_callback/")
                .build(GitHubApi.instance());
        service.getConfig().getApiKey();

        Log.e("accessURl",  service.getAuthorizationUrl());

//        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.xml");
//        service.signRequest(accessToken, request); // the access token from step 4
//        Response response = request.send();
//        System.out.println(response.getBody());

        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onCompleted", "ok");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", "----" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String string) {

                    }
                });
    }

}
