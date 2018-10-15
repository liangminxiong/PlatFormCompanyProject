package com.JPush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.navisdk.util.common.LogUtil;
import com.common.utils.AppUtils;
import com.common.utils.PreferencesUtils;
import com.yuefeng.ui.MyApplication;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/*极光推送管理类*/
public class JPushManager {

    private String TAG = "JPushManager";
    private final String KEY = "JpushConfig";
    public static JPushManager instance;
    private Context context;

    public JPushManager() {
    }

    public static JPushManager getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new JPushManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化极光，一般可以放到程序的启动Activity或是Application的onCreate方法中调用
     */
    public void initJPush(Context context) {
        JPushInterface.setDebugMode(true);
        // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context);
        this.context = context;
        // 初始化 JPush
    }

    /**
     * 退出极光，一般是程序退出登录时候，具体还是需要看项目的实际需求
     */
    public void stopJPush() {
        JPushInterface.stopPush(context);
        //setAliasAndTags("", "");//通过清空别名来停止极光
    }

    /**
     * 极光推送恢复正常工作
     */
    public void resumeJPush() {
        JPushInterface.resumePush(context);
    }

    /**
     * 设置AliasAndTag,设置多组tag,如果不需要设置tag的化，直接将此参数设为null;（这个方法设置别名，tag传null没有问题）   *
     * 一般在程序登录成功，注册成功等地方调用。别名一般是用户的唯一标识，如userId等   *   * @param alias   * @param tags
     */
    public void setAliasAndTags(final String alias, Set<String> tags) {
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(context, "别名为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 调用 Handler 来异步设置别名
        AliasAndTagsInfo aliasAndTagsInfo = new AliasAndTagsInfo();
        aliasAndTagsInfo.setAlias(alias);
        aliasAndTagsInfo.setTag(tags);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, aliasAndTagsInfo));
    }

    /**
     * //     * 设置AliasAndTag,设置一组tag,如果不需要设置tag的化，直接将此参数设为null;
     * (这个方法设置别名，不设置tag,tag传null会走6002，tag参数错误）//
     *///
    public void setAliasAndTags(final String alias, String tag) {
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(context, "别名为空", Toast.LENGTH_SHORT).show();
            return;
        }
        /// /        // 调用 Handler 来异步设置别名//
        AliasAndTagsInfo aliasAndTagsInfo = new AliasAndTagsInfo();
        aliasAndTagsInfo.setAlias(alias);
        Set<String> tags = new HashSet<String>();
        tags.add(tag);
        aliasAndTagsInfo.setTag(tags);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, aliasAndTagsInfo));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    saveAlias(alias);
                    break;
                case 6002:
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 保存别名到属性文件。   *   * @param alias
     */
    private void saveAlias(String alias) {
        PreferencesUtils.putString(AppUtils.getContext(), KEY, alias);
    }

    /**
     * 从属性文件取得别名   *   * @param userName   * @return
     */
    private String getAlias(String userName) {
        return PreferencesUtils.getString(AppUtils.getContext(), KEY);
    }

    private static final int MSG_SET_ALIAS = 1001;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            AliasAndTagsInfo aliasAndTagsInfo = (AliasAndTagsInfo) msg.obj;
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtil.e("ym", aliasAndTagsInfo.getAlias());
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context, aliasAndTagsInfo.getAlias(), aliasAndTagsInfo.getTag(), mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };

    public class AliasAndTagsInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String alias;
        private Set<String> tag;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Set<String> getTag() {
            return tag;
        }

        public void setTag(Set<String> tag) {
            this.tag = tag;
        }

    }
}

