package com.bruce.rxdemo.network.github;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubApp {
    private GithubSession mSession;
    private GithubDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;

    /**
     * Callback url, 在setting中自定义的
     * (https://github.com/settings/developers/)
     */

    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize?";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token?";
    private static final String API_URL = "https://api.github.com";

    private static final String TAG = "GitHubAPI";

    /**
     * https://github.com/settings/developers 在此注册自己app的信息 获取clientId 和 cliendSecret
     *
     * @param context      上下文
     * @param clientId     app client id
     * @param clientSecret app client secret
     * @param callbackUrl  自定义回调的url  (需要是在创建app信息的时候的callback url的子路径)
     */
    public GithubApp(Context context, String clientId, String clientSecret,
                     String callbackUrl) {

        mSession = new GithubSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl;
        mAuthUrl = AUTH_URL + "client_id=" + clientId + "&redirect_uri="
                + mCallbackUrl;

        mDialog = new GithubDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
    }


    GithubDialog.OAuthDialogListener listener = new GithubDialog.OAuthDialogListener() {
        @Override
        public void onComplete(String code) {
            //授权成功 获取 access token
            getAccessToken(code);
        }

        @Override
        public void onError(String error) {
            mListener.onFail("Authorization failed");
        }
    };

    /**
     * 获取access token
     *
     * @param code
     */
    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = 0;

                try {
                    URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, "response " + response);
                    mAccessToken = response.substring(
                            response.indexOf("access_token=") + 13,
                            response.indexOf("&token_type"));
                    Log.i(TAG, "Got access token: " + mAccessToken.substring(0, mAccessToken.indexOf("&scope=")));
                    mSession.storeAccessToken(mAccessToken.substring(0, mAccessToken.indexOf("&scope=")));
                } catch (Exception ex) {
                    what = 1;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                mProgress.dismiss();
                mListener.onSuccess();
            } else {//获取token失败
                mProgress.dismiss();
                mListener.onFail("Failed to get access token");
            }
            return true;
        }
    });

    public boolean hasAccessToken() {
        return mAccessToken != null;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getAccessToken() {
        return mSession.getAccessToken();
    }

    public void authorize() {
        mDialog.show();
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
          void onSuccess();

          void onFail(String error);
    }
}