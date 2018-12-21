package com.yuefeng.home.ui.imActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.common.base.codereview.BaseActivity;
import com.yuefeng.commondemo.R;

import java.util.Locale;

import io.rong.imlib.model.Conversation;


/*会话聚合*/
public class SubConversationListActivtiy extends BaseActivity {
    private String mTargetId, title;

    boolean isFromPush = false;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;


    @Override
    protected int getContentViewResId() {
        return R.layout.subconversationlist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        getIntentDate(intent);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
//        Toast.makeText(this, title+"<<<会话ID>>>>>>>>>" + mTargetId, Toast.LENGTH_SHORT).show();
        //intent.getData().getLastPathSegment();//获得当前会话类型
//        ToastUtils.showToast(title + " == " + mTargetId);
        setTitle(title);
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

//        enterFragment(mConversationType, mTargetId);
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }
}
