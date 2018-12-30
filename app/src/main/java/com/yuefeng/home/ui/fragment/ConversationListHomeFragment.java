package com.yuefeng.home.ui.fragment;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.groupanduser.GroupQueryWithUserDataBean;
import com.yuefeng.contacts.modle.groupanduser.GrouplistBean;
import com.yuefeng.login_splash.contract.SignInContract;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.login_splash.presenter.SignInPresenter;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class ConversationListHomeFragment extends BaseFragment implements SignInContract.View, RongIM.GroupInfoProvider, RongIM.UserInfoProvider {


    private SignInPresenter presenter;
    private List<GrouplistBean> mList = new ArrayList<>();

    private int mCount;
    private GroupQueryWithUserDataBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_conversationlist_fragment;
    }

    @Override
    protected void initView() {
        super.initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new SignInPresenter(this, (MainActivity) getActivity());
        RongIMUtils.initUserInfoListener(this);
        RongIMUtils.initGroupListener(this);
        mCount = 0;
        initRongUI();
    }

    private void initRongUI() {
        ConversationListFragment conversationListFragment = new ConversationListFragment();

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.rong_container, conversationListFragment);
        transaction.commit();
        conversationListFragment.setUri(uri);
    }

    @Override
    protected void fetchData() {

    }

    @Override
    protected void initData() {
        getInfo();
    }

    private void getInfo() {
        if (presenter != null) {
            String userid = PreferencesUtils.getString(getContext(), Constans.ID);
            presenter.groupQueryWithUser(userid);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSignInEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.GROUPINFOS_SUCCESS:
                mBean = (GroupQueryWithUserDataBean) event.getData();
                if (mBean != null) {
                    mList = mBean.getGrouplist();
                }
                RongIMUtils.initGroupListener(this);
                initRongUI();
                break;

            case Constans.GROUPINFOS_ERROR:
                if (mCount < 8) {
                    getInfo();
                }
                mCount++;
                initRongUI();
                break;
        }
    }


    @Override
    public Group getGroupInfo(String s) {
        //首先对群组非空判断 然后增强for循环遍历数据
        LogUtils.d("======3=s==== " + s);
        if (mList != null) {
            for (GrouplistBean bean : mList) {
                LogUtils.d("======2=s==" + bean.getId() + "== " + s);
                if (bean.getId().equals(s)) {
                    Group group = new Group(bean.getId(), bean.getName(), Uri.parse(""));
                    RongIMUtils.initGroup(group);
                    RongIMUtils.refreshGroupInfoCache(group);
                    return group;//从bean中获取参数返回给融云
                }
            }
        }
        return null;
    }

    @Override
    public UserInfo getUserInfo(String s) {
        if (mBean == null) {
            return null;
        }
        String userid = mBean.getId();
        String name = mBean.getName();
//        "http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg"
        Uri parse = Uri.parse(mBean.getIcon());
        UserInfo info = new UserInfo(userid, name, parse);
        RongIMUtils.init(userid, name, mBean.getIcon());
        RongIMUtils.refreshUserInfoCache(info);
        return info;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
