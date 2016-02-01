package com.bruce.ghclient.network.github;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

/**
 * 授权成功后保存 access token
 */
public class GithubSession {

	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String SHARED = "Github_Preferences";
	private static final String API_ACCESS_TOKEN = "access_token";
	private static final String API_USER_NAME = "user_name";

	public GithubSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	public void storeAccessToken(String accessToken) {
		editor.putString(API_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

    public void storeUserName(String userName) {
        editor.putString(API_USER_NAME, userName);
        editor.commit();
    }

	/**
	 * Reset access token and user name
	 */
	public void resetAccessToken() {
		editor.putString(API_ACCESS_TOKEN, null);
		editor.commit();
	}

	/**
	 * Get access token
	 * 
	 * @return Access token
	 */
	public String getAccessToken() {
		return sharedPref.getString(API_ACCESS_TOKEN, null);
	}

    public String getUserName() {
		return sharedPref.getString(API_USER_NAME, null);
	}


}