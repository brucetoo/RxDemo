package com.bruce.rxdemo.views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bruce.rxdemo.R;
import com.bruce.rxdemo.network.github.GithubApp;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

        mApp = new GithubApp(this, "brucetoo","ty8922436","http://example.com/path");
        mApp.setListener(listener);
    }
    GithubApp mApp;

    @OnClick(R.id.btn_login)
    void loginClick() {

        work();
    }

    private void work() {

        //https://github.com/thiagolocatelli/android-github-oauth

        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginActivity.this);
            builder.setMessage("Disconnect from GitHub?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    mApp.resetAccessToken();
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
            mApp.authorize();
        }

        if (mApp.hasAccessToken()) {
           Log.e("result ","name--"+mApp.getUserName());
        }

    }


 GithubApp.OAuthAuthenticationListener listener = new GithubApp.OAuthAuthenticationListener() {

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail(String error) {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
    }
};

}
