package com.yuefeng.ui;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.common.network.ApiRetrofit;
import com.common.utils.RxTool;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.yuefeng.citySelector.db.DBManager;


/**
 * Created  on 2018-01-04.
 * author:seven
 * email:seven2016s@163.com
 */

public class MyApplication extends Application {

    private static Context context;
    private static boolean isDebug = true;//true 玩Android flase 百度
    protected static Handler handler;
    protected static int mainThreadId;
    private DBManager dbHelper;

    public static boolean getIsDebug() {
        return isDebug;
    }

    public static boolean setIsDebug() {
        return false;
    }

    public MyApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        RxTool.init(this);
        ApiRetrofit.getApiRetrofit();
//        MobSDK.init(this, "262adcdbafad0", "c7ee79a0a5971f6fbd0b20f2af152af2");

        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        initThirdParty();
    }

    private void initThirdParty() {
        /*百度地图*/
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        /*短信*/
//        MobSDK.init(this);
        /*语音*/
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b35f250");
        //导入省城市数据库
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
        /*bugly*/
        CrashReport.initCrashReport(getApplicationContext(), "a00c816cfa", false);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
                .denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
                .discCacheFileCount(60)// 缓存文件的最大个数
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }
}
