package com.bruce.rxdemo.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.bruce.rxdemo.utils.AppActivityManager;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 14:31
 */
public class BaseActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppActivityManager.getInstance().addActivity(getClass().getName(),this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppActivityManager.getInstance().removeActivity(getClass().getName());
    }
}
