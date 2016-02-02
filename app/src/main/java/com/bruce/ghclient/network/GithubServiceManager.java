package com.bruce.ghclient.network;

import android.text.TextUtils;
import android.widget.Toast;

import com.bruce.ghclient.GHClientApp;
import com.bruce.ghclient.network.github.GithubPreManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import timber.log.Timber;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 15:08
 */
public class GithubServiceManager {

    private GithubServiceManager(){}

    public static GithubService createGithubService(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())// 返回数据解析gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  // 使用 rxjava
                .baseUrl("https://api.github.com");

        String githubToken = GithubPreManager.getAccessToken();
        if(githubToken != null) {
            //下面代码可以做请求拦截
            if (!TextUtils.isEmpty(githubToken)) {

                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        //此处也可打印每次请求的url  request.url()
                        Request newReq = request.newBuilder()
                                .addHeader("Authorization", String.format("token %s", githubToken))
                                .build();
                        Timber.e("Send Url -> %s",request.url());
                        Timber.e("Send Token -> %s",githubToken);
                        return chain.proceed(newReq);
                    }
                }).build();

                builder.client(client);
            }
        }else {
            Toast.makeText(GHClientApp.getAppContext(),"No Access token",Toast.LENGTH_SHORT).show();
        }

        return builder.build().create(GithubService.class);
    }
}
