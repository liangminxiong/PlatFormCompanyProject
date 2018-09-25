package com.common.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.common.utils.Constans;
import com.yuefeng.ui.MyApplication;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public class SaveCookieInterceptor implements Interceptor {
    public static String PREFERENCE_NAME = "Config";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Headers headers = response.headers();
        String date = headers.get("Date");
        List<String> strings = response.headers("set-cookie");
        if (!strings.isEmpty()) {
            String cookie = encodeCookie(strings);
            Log.d("SaveCookieInterceptor", cookie);
            saveCookie(request.url().toString(), request.url().host(), cookie);

        }else {
            saveCookie(request.url().toString(), request.url().host(), date);
        }
        return response;
    }

    private void saveCookie(String url, String host, String cookie) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        } else {
//            editor.putString(url, cookie);
            editor.putString(Constans.COOKIE_PREF, cookie);
        }
        if (!TextUtils.isEmpty(host)) {
//            editor.putString(host, cookie);
            editor.putString(Constans.COOKIE_PREF, cookie);
        }
        editor.apply();
    }

    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) continue;
                set.add(s);
            }
        }
        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            String cookie = ite.next();
            sb.append(cookie).append(";");
        }
        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }
        return sb.toString();
    }
}
