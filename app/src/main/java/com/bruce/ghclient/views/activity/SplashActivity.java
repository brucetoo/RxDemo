package com.bruce.ghclient.views.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bruce.ghclient.MainActivity;
import com.bruce.ghclient.R;
import com.bruce.ghclient.network.github.GithubPreManager;

/**
 * Created by Bruce too
 * On 2016/2/2
 * At 10:53
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(GithubPreManager.getLoginState()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },3000);
    }
}
