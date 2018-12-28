package com.yuefeng.rongIm;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.ToastUtils;
import com.yuefeng.login_splash.event.LoginEvent;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.GroupNotificationMessageData;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.PrivateConversationProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/*融云工具类*/
public class RongIMUtils implements RongIM.ConversationListBehaviorListener,
//        RongIMClient.OnReceiveMessageListener,
        RongIM.UserInfoProvider,
        RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener,
        RongIM.IGroupMembersProvider {

    private static RongIMUtils mRongCloudInstance;

    public RongIMUtils(Context context) {
        initRongIM(context);

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
            initUserInfo();
            initListener();
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
//        RongIM.getInstance().setOnReceiveMessageListener(this);
//        RongIM.getInstance().registerConversationTemplate(new MyPrivateConversationProvider());//自定义会话

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
    public static void initUserInfo() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {//用户信息提供者的S会改变，所以从服务器拿，参考微商猎手
                return findUser(s);
            }
        }, true);
    }

    private static UserInfo findUser(String userId) {
        String id = PreferencesUtils.getString(AppUtils.getContext(), Constans.ID, "");
        if (userId.equals(id)) {
            String name = PreferencesUtils.getString(AppUtils.getContext(), Constans.USERNAME_N, "");
            UserInfo userInfo = new UserInfo(userId, name, Uri.parse(Constans.USER_LOGO));
            return userInfo;
        }
        return null;
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
    public static void connectToken(String token, final String userId, final String name, final String portraitUrl) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            ToastUtils.showToast("无网络");
        }

        if (TextUtils.isEmpty(token)) {
            return;
        }
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String userid) {
                UserInfo info = new UserInfo(userId, name, Uri.parse(portraitUrl));
                RongIM.getInstance().setCurrentUserInfo(info);
                refreshUserInfoCache(info);
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

//    /*接收到消息*/
//    @Override
//    public boolean onReceived(Message message, int i) {
//        MessageContent messageContent = message.getContent();
//        if (messageContent instanceof ContactNotificationMessage) {
//
//            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
//
//            /* if (contactNotificationMessage.getOperation().equals("Request")) {
//                //对方发来好友邀请
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
//            } else if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
//                //对方同意我的好友请求
//                ContactNotificationMessageData contactNotificationMessageData;
//                try {
//                    contactNotificationMessageData = JsonMananger.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
//                } catch (HttpException e) {
//                    e.printStackTrace();
//                    return false;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//                if (contactNotificationMessageData != null) {
//                    if (SealUserInfoManager.getInstance().isFriendsRelationship(contactNotificationMessage.getSourceUserId())) {
//                        return false;
//                    }
//                    SealUserInfoManager.getInstance().addFriend(
//                            new Friend(contactNotificationMessage.getSourceUserId(),
//                                    contactNotificationMessageData.getSourceUserNickname(),
//                                    null, null, null, null,
//                                    null, null,
//                                    CharacterParser.getInstance().getSpelling(contactNotificationMessageData.getSourceUserNickname()),
//                                    null));
//                }
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_FRIEND);
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
//            }
//            // 发广播通知更新好友列表
//            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
//        }
//    } else*/
//            if (messageContent instanceof GroupNotificationMessage) {
//                GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) messageContent;
//                String groupID = message.getTargetId();
//                GroupNotificationMessageData data = null;
//                try {
//                    String currentID = RongIM.getInstance().getCurrentUserId();
//                    try {
//                        data = jsonToBean(groupNotificationMessage.getData());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (groupNotificationMessage.getOperation().equals("Create")) {
//                        //创建群组
////                        SealUserInfoManager.getInstance().getGroups(groupID);
////                        SealUserInfoManager.getInstance().getGroupMember(groupID);
//                    } else if (groupNotificationMessage.getOperation().equals("Dismiss")) {
//                        //解散群组
////                        hangUpWhenQuitGroup();      //挂断电话
////                        handleGroupDismiss(groupID);
//                    } else if (groupNotificationMessage.getOperation().equals("Kicked")) {
//                        //群组踢人
//                        if (data != null) {
//                            List<String> memberIdList = data.getTargetUserIds();
//                            if (memberIdList != null) {
//                                for (String userId : memberIdList) {
//                                    if (currentID.equals(userId)) {
////                                        hangUpWhenQuitGroup();
//                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, message.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
//                                            @Override
//                                            public void onSuccess(Boolean aBoolean) {
//                                                Log.e("SealAppContext", "Conversation remove successfully.");
//                                            }
//
//                                            @Override
//                                            public void onError(RongIMClient.ErrorCode e) {
//
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//
//                            List<String> kickedUserIDs = data.getTargetUserIds();
////                            SealUserInfoManager.getInstance().deleteGroupMembers(groupID, kickedUserIDs);
////                            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
//                        }
//                    } else if (groupNotificationMessage.getOperation().equals("Add")) {
//                        //群组添加人员
////                        SealUserInfoManager.getInstance().getGroups(groupID);
////                        SealUserInfoManager.getInstance().getGroupMember(groupID);
////                        BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
//                    } else if (groupNotificationMessage.getOperation().equals("Quit")) {
//                        //退出群组
////                        if (data != null) {
////                            List<String> quitUserIDs = data.getTargetUserIds();
////                            if (quitUserIDs.contains(currentID)) {
////                                hangUpWhenQuitGroup();
////                            }
////                            SealUserInfoManager.getInstance().deleteGroupMembers(groupID, quitUserIDs);
////                            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
////                        }
//                    } else if (groupNotificationMessage.getOperation().equals("Rename")) {
//                        //群组重命名
////                        if (data != null) {
////                            String targetGroupName = data.getTargetGroupName();
////                            SealUserInfoManager.getInstance().updateGroupsName(groupID, targetGroupName);
////                            List<String> groupNameList = new ArrayList<>();
////                            groupNameList.add(groupID);
////                            groupNameList.add(data.getTargetGroupName());
////                            groupNameList.add(data.getOperatorNickname());
////                            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_NAME, groupNameList);
////                            Group oldGroup = SealUserInfoManager.getInstance().getGroupsByID(groupID);
////                            if (oldGroup != null) {
////                                Group group = new Group(groupID, data.getTargetGroupName(), Uri.parse(""));
////                                RongIM.getInstance().refreshGroupInfoCache(group);
////                            }
////                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return true;
//            } else if (messageContent instanceof ImageMessage) {
//                //ImageMessage imageMessage = (ImageMessage) messageContent;
//            }
//        }
//
//        return false;
//    }

    private GroupNotificationMessageData jsonToBean(String data) {
        GroupNotificationMessageData dataEntity = new GroupNotificationMessageData();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("operatorNickname")) {
                dataEntity.setOperatorNickname(jsonObject.getString("operatorNickname"));
            }
            if (jsonObject.has("targetGroupName")) {
                dataEntity.setTargetGroupName(jsonObject.getString("targetGroupName"));
            }
            if (jsonObject.has("timestamp")) {
                dataEntity.setTimestamp(jsonObject.getLong("timestamp"));
            }
            if (jsonObject.has("targetUserIds")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserIds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserIds().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("targetUserDisplayNames")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserDisplayNames");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserDisplayNames().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("oldCreatorId")) {
                dataEntity.setOldCreatorId(jsonObject.getString("oldCreatorId"));
            }
            if (jsonObject.has("oldCreatorName")) {
                dataEntity.setOldCreatorName(jsonObject.getString("oldCreatorName"));
            }
            if (jsonObject.has("newCreatorId")) {
                dataEntity.setNewCreatorId(jsonObject.getString("newCreatorId"));
            }
            if (jsonObject.has("newCreatorName")) {
                dataEntity.setNewCreatorName(jsonObject.getString("newCreatorName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataEntity;
    }


    /**
     * 融云消息监听
     */
    static public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
         */
        @Override
        public boolean onReceived(Message message, int left) {
            switch (message.getConversationType()) {
                case PRIVATE:           //单聊
                    Log.d("MyReceiveMessage", "--单聊");
                    break;
                case GROUP:             //群组
                    Log.d("MyReceiveMessage", "--群组");
                    break;
                case DISCUSSION:        //讨论组
                    Log.d("MyReceiveMessage", "--讨论组");
                    break;
                case CHATROOM:          //聊天室
                    Log.d("MyReceiveMessage", "--聊天室");
                    break;
                case CUSTOMER_SERVICE:  //客服
                    Log.d("MyReceiveMessage", "--客服");
                    break;
                case SYSTEM:            //系统会话
                    Log.d("MyReceiveMessage", "--系统会话");
                    break;
                default:
                    break;
            }

            return false;
        }
    }

    /**
     * 获取融云所需用户信息
     */
    @Override
    public UserInfo getUserInfo(String userId) {
//        if (FriendInfoList.getInstance() == null) {
        return null;
//        }
//        return FriendInfoList.getInstance().getUserInfoByUserId(userId);
    }

    /**
     * 自定义聊天会话的模型类
     */
    @ConversationProviderTag(conversationType = "private", portraitPosition = 1)
    static public class MyPrivateConversationProvider extends PrivateConversationProvider {
        @Override
        public View newView(Context context, ViewGroup group) {
            return super.newView(context, group);
        }

        @Override
        public void bindView(View v, int position, UIConversation data) {
            if (data.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);

                //设置会话发送者ID、会话标题、会话头像URL
                String userID = data.getConversationSenderId();
                String userId = PreferencesUtils.getString(AppUtils.getContext(), Constans.ID, "");
                String name = PreferencesUtils.getString(AppUtils.getContext(), Constans.USERNAME_N, "");
                String portraitUrl = "";
//                UserInfo info = FriendInfoList.getInstance().getUserInfoByUserId(userID);
//                data.setIconUrl(info.getPortraitUri());
//                data.setUIConversationTitle(info.getName());
//                refreshUserInfoCache(userID);;
            }

            super.bindView(v, position, data);
        }
    }

}
