package com.common.network;


public class NetworkUrl {
    public static final String ANDROID_TEST_SERVICE = "http://120.78.217.251/";
    public static final String ANDROID_TEST_SERVICE_GU = "http://10.0.0.68:8080/";//小古
    public static final String ANDROID_TEST_SERVICE_DENG = "http://10.0.0.31:8080/";//小邓
    public static final String ANDROID_TEST_SERVICE_QIU = "http://10.0.0.11:8080/";//小邱
    public static final String ANDROID_TEST_SERVICE_CAI = "http://10.0.0.66:8080/";//小蔡
    public static final String ANDROID_TEST_SERVICE_HAO = "http://yuefenghao.com/";//京东

    private static final String ANDROID_BAIDU_SERVICE = "http://gank.io";

    static String getNetWorkName() {
        return ANDROID_TEST_SERVICE;
//        if (MyApplication.getIsDebug()) {
//        } else {
//            return ANDROID_BAIDU_SERVICE;
//        }
    }
}
