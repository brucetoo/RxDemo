package com.bruce.ghclient.views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.bruce.ghclient.BuildConfig;
import com.bruce.ghclient.MainActivity;
import com.bruce.ghclient.R;
import com.bruce.ghclient.network.GithubService;
import com.bruce.ghclient.network.GithubServiceManager;
import com.bruce.ghclient.network.github.GithubApp;
import com.bruce.ghclient.network.github.GithubPreManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 14:31
 */
public class LoginActivity extends FragmentActivity {

    private GithubApp mGithubApp;
    private GithubService mGithubServie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mGithubApp = new GithubApp(this, BuildConfig.client_id, BuildConfig.client_secret, BuildConfig.callback_url);
        mGithubApp.setListener(listener);
        mGithubServie = GithubServiceManager.createGithubService();
    }

    @OnClick(R.id.btn_join)
    void onJoinClick() {
         String url = "https://github.com/join";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    @OnClick(R.id.btn_login)
    void onLoginClick() {

        //https://github.com/thiagolocatelli/android-github-oauth
        if (mGithubApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginActivity.this);
            builder.setMessage("Disconnect from GitHub?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    mGithubApp.resetAccessToken();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mGithubApp.authorize();
        }

    }

    GithubApp.OAuthAuthenticationListener listener = new GithubApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            GithubPreManager.storeLoginState(true);
            Toast.makeText(LoginActivity.this, "Access token successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        @Override
        public void onFail(String error) {
            GithubPreManager.storeLoginState(false);
        }
    };

}
