package com.yuefeng.rongIm;


import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.yuefeng.login_splash.event.LoginEvent;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/*融云工具类*/
public class RongIMUtils implements RongIM.ConversationListBehaviorListener,
        RongIM.UserInfoProvider,
        RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener,
        RongIM.IGroupMembersProvider {

    private static RongIMUtils mRongCloudInstance;

    public RongIMUtils(Context context) {
        initRongIM(context);
        initListener();
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {
            synchronized (RongIMUtils.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongIMUtils(context);
                }
            }
        }

    }

    private void initRongIM(Context context) {
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


    /**
     * init 后就能设置的监听
     */

    public void initListener() {
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setConnectionStatusListener(this);
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
        RongIM.getInstance().setGroupMembersProvider(this);
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

    /*保存融云用户信息*/
    public static void initGroup(final Group croup) {
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {
                return croup;
            }
        }, true);
    }

    public static UserInfo getRongIMUserInfo(String userId) {
        UserInfo mine = RongUserInfoManager.getInstance().getUserInfo(userId);

        return mine;
    }

    /**
     * //初始化群组信息提供者
     */
    public static void initGroupListener(RongIM.GroupInfoProvider groupInfoProvider) {
        RongIM.setGroupInfoProvider(groupInfoProvider, true);
    }

    /**
     * //初始化单聊信息提供者
     */
    public static void initUserInfoListener(RongIM.UserInfoProvider userInfoProvider) {
        RongIM.setUserInfoProvider(userInfoProvider, true);
    }

    /**
     * 刷新用户缓存数据。
     *
     * @param userInfo 需要更新的用户缓存数据。
     */
    public static void refreshUserInfoCache(UserInfo userInfo) {
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }

    /**
     * 刷新群组缓存数据。
     *
     * @param group 需要更新的群组缓存数据。
     */
    public static void refreshGroupInfoCache(Group group) {
        RongIM.getInstance().refreshGroupInfoCache(group);
    }


    /*连接融云*/
    public static void connectToken(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String userid) {
                LogUtils.d("======" + userid);
                EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_SUCCESS_NET, ""));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_ERROR, ""));
            }
        });
    }

    private void getConversationListInfos() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
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
//        init(userId, userName, "http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg");
//        UserInfo userInfo = new UserInfo(userId, name, Uri.parse(portraitUrl));
        RongIM.getInstance().startPrivateChat(context, userId, userName);
    }

    /*群聊*/
    public static void startGroupChat(Context context, String targetGroupId, String title) {
        //跳转到融云群聊天界面
        RongIM.getInstance().startConversation(context, Conversation.ConversationType.GROUP, targetGroupId, title);
//        RongIM.getInstance().startGroupChat(context, targetGroupId, title);
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

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public Group getGroupInfo(String s) {
        return null;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(String s, String s1) {
        return null;
    }

    @Override
    public void getGroupMembers(String s, RongIM.IGroupMemberCallback iGroupMemberCallback) {

    }

    @Override
    public UserInfo getUserInfo(String s) {
        return null;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}
