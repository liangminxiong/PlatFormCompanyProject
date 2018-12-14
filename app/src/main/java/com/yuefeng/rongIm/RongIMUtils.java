package com.yuefeng.rongIm;


import android.content.Context;
import android.net.Uri;

import com.yuefeng.ui.MyApplication;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/*融云工具类*/
public class RongIMUtils {

    public static void initRongIM(Context context) {
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (context.getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(context)) ||
                "io.rong.push".equals(MyApplication.getCurProcessName(context))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(context);
        }
    }

    /*保存融云用户信息*/
    public static void init(final String userId, final String name, final String portraitUrl) {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {//用户信息提供者的S会改变，所以从服务器拿，参考微商猎手
                UserInfo userInfo = new UserInfo(userId, name, Uri.parse(portraitUrl));
                return userInfo;
            }
        }, true);
    }

    /*连接融云*/
    public static void connectToken(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /*会话聚合*/
    public static void toConversationListActivity(Context context) {
        //        startActivity(new Intent(this, ConversationListActivity.class));
        Map<String, Boolean> map = new HashMap<>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示
        RongIM.getInstance().startConversationList(context, map);
    }

    /*二人会话*/
    public static void startPrivateChat(Context context, String userId, String userName) {
        RongIM.getInstance().startPrivateChat(context, userId, userName);
    }

    /*群聊*/
    public static void startGroupChat(Context context, String targetGroupId, String title) {
        RongIM.getInstance().startGroupChat(context, targetGroupId, title);
    }

    /*注销融云*/
    public static void destroyRongIM() {
        RongIM.getInstance().disconnect();//不设置收不到推送
    }


    /*上传图片*/
    public static void sendImageMessage() {
        /**
         * 发送图片消息方法。
         */

//选择手机端本地图片，例如：
        String localImagePath = "/sdcard/test.jpg";
//构造图片消息对象，两个参数传同一个图片路径即可。
        ImageMessage imgMsg = ImageMessage.obtain(Uri.parse("file://" + localImagePath), Uri.parse("file://" + localImagePath));

        //使用 sendImageMessage 发出。
        RongIM.getInstance().getRongIMClient().sendImageMessage(Conversation.ConversationType.PRIVATE, "9527",
                imgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        //保存数据库成功
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //发送失败
                    }

                    @Override
                    public void onSuccess(Message message) {
                        //发送成功
                    }

                    @Override
                    public void onProgress(Message message, int i) {
                        //发送进度
                    }
                });
    }
}
