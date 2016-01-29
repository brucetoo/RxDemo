package com.bruce.rxdemo.utils;

import android.app.Activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 14:39
 */
public class AppActivityManager {

    private static Map<String, Activity> mActivityStack;
    private static AppActivityManager mInstance;

    private AppActivityManager() {}

    public static AppActivityManager getInstance() {

        if(mInstance == null) {
            synchronized (AppActivityManager.class) {
                if (mInstance == null) {
                    mInstance = new AppActivityManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(String className, Activity activity) {

        if (mActivityStack == null) {
            mActivityStack = new HashMap<String, Activity>();
        }
        mActivityStack.put(className, activity);
    }

    /**
     * 判断Activity是否在堆栈中
     */
    public boolean getActivity(String className) {

        if (mActivityStack != null) {
            return mActivityStack.containsKey(className);
        }

        return false;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(String className) {

        if (mActivityStack != null) {
            Activity activity = mActivityStack.get(className);
            activity.finish();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        if (mActivityStack != null) {
            Iterator<Activity> iterator = mActivityStack.values().iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                activity.finish();
            }
            mActivityStack.clear();
        }
    }


    public void removeActivity(String className) {

        if (mActivityStack != null)
            mActivityStack.remove(className);
    }

    public void clear() {

        if (mActivityStack != null)
            mActivityStack.clear();
    }
}
