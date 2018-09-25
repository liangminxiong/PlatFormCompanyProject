package com.common.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */

public class ApiRetrofit {
    private static volatile ApiRetrofit apiRetrofit;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory adapterFactory = RxJava2CallAdapterFactory.create();
    private static final int CONNECT_TIME_OUT = 25;
    private static final int READ_TIME_OUT = 25;
    private static String baseUrl = NetworkUrl.getNetWorkName();
    private static ApiService apiServise;
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public ApiService getApiServis() {
        return apiServise;
    }

    public static ApiRetrofit getApiRetrofit() {
        if (apiRetrofit == null) {
            synchronized (ApiRetrofit.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    private ApiRetrofit() {
        /*读写链接超时*///打印log日志
//失败重连
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)/*读写链接超时*/
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)//打印log日志
                .addInterceptor(new AddCookieInterceptor())
                .addInterceptor(new SaveCookieInterceptor())
                .retryOnConnectionFailure(true)//失败重连
                .build();

        getRetrofit(baseUrl);
        apiServise = retrofit.create(ApiService.class);
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;
        getRetrofit(baseUrl);
    }

    private static void getRetrofit(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(adapterFactory)
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
    }

    /*
     * 打印日志拦截器
     * */
    private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
}
