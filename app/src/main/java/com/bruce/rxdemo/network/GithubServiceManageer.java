package com.bruce.rxdemo.network;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Bruce too
 * On 2016/1/29
 * At 15:08
 */
public class GithubServiceManageer {

    private GithubServiceManageer(){}

    public static GithubService createGithubService(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())// 返回数据解析gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  // 使用 rxjava
                .baseUrl("https://api.github.com");


          //下面代码可以做请求拦截
//        if (!TextUtils.isEmpty(githubToken)) {
//
//            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                @Override public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    //此处也可打印每次请求的url  request.url()
//                    Request newReq = request.newBuilder()
//                            .addHeader("Authorization", format("token %s", githubToken))
//                            .build();
//                    return chain.proceed(newReq);
//                }
//            }).build();
//
//            builder.client(client);
//        }
//
        return builder.build().create(GithubService.class);
    }
}
