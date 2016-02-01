package com.bruce.ghclient.views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bruce.ghclient.BuildConfig;
import com.bruce.ghclient.R;
import com.bruce.ghclient.models.User;
import com.bruce.ghclient.network.GithubService;
import com.bruce.ghclient.network.GithubServiceManager;
import com.bruce.ghclient.network.github.GithubApp;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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

    @OnClick(R.id.btn_get_profile)
    void getProfile() {
        mGithubServie.getOwnProfile(mGithubApp.getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Timber.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "user - error");
                    }

                    @Override
                    public void onNext(User user) {
                        Timber.e("Fetch successfully user.name = %s", user.name);
                    }
                });
    }


    @OnClick(R.id.btn_login)
    void loginClick() {
        work();
    }

    private void work() {

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

        if (mGithubApp.hasAccessToken()) {
            Log.e("result ", "token--" + mGithubApp.getAccessToken());
        }

    }


    GithubApp.OAuthAuthenticationListener listener = new GithubApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            Toast.makeText(LoginActivity.this, "Access token successful", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail(String error) {
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };

}
