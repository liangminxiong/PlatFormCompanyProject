package com.yuefeng.home.ui.imActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.contract.GroupQueryContract;
import com.yuefeng.contacts.modle.UserDeatailInfosBean;
import com.yuefeng.contacts.modle.groupanduser.GroupQueryWithUserDataBean;
import com.yuefeng.contacts.presenter.GroupQueryPresenter;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


/*会话界面*/
public class ConversationActivity extends BaseActivity implements GroupQueryContract.View {

    private String mTargetId, title;

    boolean isFromPush = false;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private GroupQueryPresenter mPresenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.conversation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPresenter = new GroupQueryPresenter(this, this);
        Intent intent = getIntent();
        getIntentDate(intent);
        isReconnect(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.USERDETAIL_SUCCESS://获取通讯录成功
                UserDeatailInfosBean.DataBean bean = (UserDeatailInfosBean.DataBean) event.getData();
                showUIDatas(bean);
                break;
            case Constans.USERDETAIL_ERROR:
                setTitle("聊天");
                break;
            case Constans.GROUNPINFOS_SUCCESS:
                GroupQueryWithUserDataBean gBean = (GroupQueryWithUserDataBean) event.getData();
                String name = gBean.getName();
                if (!TextUtils.isEmpty(name)) {
                    setTitle(name);
                } else {
                    setTitle("聊天");
                }
                break;
            case Constans.GROUNPINFOS_ERROR:
                setTitle("聊天");
                break;
        }
    }

    private void showUIDatas(UserDeatailInfosBean.DataBean bean) {
        if (bean != null) {
            String name = bean.getUsername();
            if (!TextUtils.isEmpty(name)) {
                setTitle(name);
            } else {
                setTitle("聊天");
            }
        }
    }


    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId").toString().trim();
        title = intent.getData().getQueryParameter("title").toString().trim();
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        //intent.getData().getLastPathSegment();//获得当前会话类型
        LogUtils.d(title + " == " + mTargetId);

        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        if (mConversationType == Conversation.ConversationType.GROUP) {
            getWhatGroupByNet(mTargetId);
        } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
            getWhatGroupDatasByNet(mTargetId);
        }
//        enterFragment(mConversationType, mTargetId);

    }

    private void getWhatGroupByNet(String targetId) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.groupQuery(targetId);
        }
    }

    private void getWhatGroupDatasByNet(String userId) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.groupQueryWithUser(userId);
        }
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        fragment.setUri(uri);
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {
        Log.e("", "《重连》");

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.e("", "连接失败");
                }

                @Override
                public void onSuccess(String s) {
                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("", "连接失败—————>" + errorCode);
                }
            });
        }
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {
        String token = PreferencesUtils.getString(ConversationActivity.this, Constans.TOKEN);

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
            isFromPush = true;
            Log.e("", "isFromPush");
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        if (!fragment.onBackPressed()) {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && isFromPush) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
