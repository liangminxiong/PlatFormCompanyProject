package com.JPush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.navisdk.util.common.LogUtil;
import com.common.utils.LogUtils;
import com.common.utils.ToastUtils;
import com.yuefeng.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ym on 2017/5/27.
 * 自定义极光推送的广播接受者
 */

public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        + printBundle(bundle)
        LogUtil.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: ");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtil.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtil.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notificationId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.e(TAG, "[MyReceiver] 用户点击打开了通知");
            //解析json
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//json串
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);//json串
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);//json串
            ToastUtils.showToast(title + " ++ " + alert);
            try {
                JSONObject jsonObject = new JSONObject(string);
//                String type = jsonObject.getString("type");
                String type = "1";
                LogUtil.e(TAG, "type:" + type);
                switch (type) {
                    case "1"://打开应用
                        if (isBackground(context)) {
                            Intent i = new Intent(context, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        }
                        break;
//                    case "2"://打开创建订单页
//                        String sourse_id = jsonObject.getString("sourse_id");
//                        if (!TextUtils.isEmpty(sourse_id)) {
//                            Intent intentOrder = new Intent(context, ACT_PlaceOrder.class);
//                            intentOrder.putExtra("id", Integer.parseInt(sourse_id));
//                            intentOrder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intentOrder);
//                        }
//                        break;
//                    case "3"://打开品牌
//                        String brand_id = jsonObject.getString("brand_id");
//                        String bra_name = jsonObject.getString("bra_name");
//                        if (!TextUtils.isEmpty(brand_id)){
//                            Intent intentBrand = new Intent(context, ACT_BrandCarList.class);
//                            intentBrand.putExtra("id", brand_id);
//                            intentBrand.putExtra("title", bra_name);
//                            intentBrand.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intentBrand);
//                        }
//                        break;
//                    case "4"://打开指定页面
//                        String http_url = jsonObject.getString("http_url");
//                        if (!TextUtils.isEmpty(http_url)){
//                            Intent intentWeb = new Intent(context, ACT_Web.class);
//                            intentWeb.putExtra("title", "");
//                            intentWeb.putExtra("url", http_url);
//                            intentWeb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intentWeb);
//                        }
//                        break;
//                    case "5"://打开议价详情
//                        String bargainid = jsonObject.getString("bargainid");
//                        if (!TextUtils.equals(bargainid, "0") && !TextUtils.isEmpty(bargainid)) {//bargainid=0是议价已取消，不跳转
//                            Intent bargainIntent = new Intent(context, ACT_BargainingDetail.class);
//                            bargainIntent.putExtra("id", bargainid);
//                            bargainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(bargainIntent);
//                        }
//                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtil.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtil.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtil.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtil.e(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtil.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 判断进程是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                LogUtils.d("tag++ " + appProcess.processName + "前台");
                return false;
            } else {
                LogUtils.d("tag++ " + appProcess.processName + "后台");
                return true;
            }
        }
        return false;
    }
}

