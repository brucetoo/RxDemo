package com.bruce.ghclient;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

        configRealm();
    }

    private void configRealm() {
        //首先在初次数据库配置中声明 version
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("GHdatabase")
                .schemaVersion(0)  //初始化版本的version = 0
                .build();
        Realm.setDefaultConfiguration(config);
        //如果版本迭代了多次 需要自定义RealmMigration 执行版本数据库迭代
    }

    public static Application getAppContext(){
        return mContext;
    }
}
