package com.common.network;


public class NetworkUrl {
    public static final String ANDROID_TEST_SERVICE = "http://120.78.217.251/";
//        public static final String ANDROID_TEST_SERVICE = "http://10.0.0.68:8080/";//小古
    private static final String ANDROID_BAIDU_SERVICE = "http://gank.io";

    static String getNetWorkName() {
        return ANDROID_TEST_SERVICE;
//        if (MyApplication.getIsDebug()) {
//        } else {
//            return ANDROID_BAIDU_SERVICE;
//        }
    }
}
