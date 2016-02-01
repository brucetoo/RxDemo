package com.bruce.ghclient;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Bruce too
 * On 2016/2/1
 * At 16:32
 */
public class GHClientApp extends Application {

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Application getAppContext(){
        return mContext;
    }
}
