package com.bruce.ghclient.network.github;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

import com.bruce.ghclient.GHClientApp;

/**
 * 授权成功后保存 access token
 */
public class GithubPreManager {

	private static SharedPreferences sharedPref;
	private static Editor editor;

	private static final String SHARED = "Github_Preferences";
	private static final String PREF_ACCESS_TOKEN = "access_token";
	private static final String PREF_USER_NAME = "user_name";
	private static final String PREF_LOGIN_STATE = "login_state";

    static {
        sharedPref = GHClientApp.getAppContext().getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

	public static void storeAccessToken(String accessToken) {
		editor.putString(PREF_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

    public static void storeUserName(String userName) {
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

	/**
	 * Reset access token and user name
	 */
	public static void resetAccessToken() {
		editor.putString(PREF_ACCESS_TOKEN, null);
		editor.commit();
	}

	/**
	 * Get access token
	 * 
	 * @return Access token
	 */
	public static String getAccessToken() {
		return sharedPref.getString(PREF_ACCESS_TOKEN, null);
	}

    public static String getUserName() {
		return sharedPref.getString(PREF_USER_NAME, null);
	}

    public static void storeLoginState(boolean login) {
        editor.putBoolean(PREF_LOGIN_STATE, login);
        editor.commit();
    }

    public static boolean getLoginState(){
        return sharedPref.getBoolean(PREF_LOGIN_STATE, false);
    }

}