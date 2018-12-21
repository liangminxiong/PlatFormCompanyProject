package com.yuefeng.home.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.HomeContract;
import com.yuefeng.home.modle.NewMsgListDataBean;
import com.yuefeng.home.presenter.HomePresenter;
import com.yuefeng.home.ui.activity.AnnouncementListInfosActivtiy;
import com.yuefeng.home.ui.activity.HistoryAppVersionActivtiy;
import com.yuefeng.home.ui.activity.MsgListInfosActivtiy;
import com.yuefeng.home.ui.adapter.ConversationListAdapterEx;
import com.yuefeng.home.ui.adapter.HomeMsgInfosAdapter;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

//import com.yuefeng.home.ui.imActivity.ConversationListFragment;


/**
 * Created by Administrator on 2018/7/11.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.tv_tab_name)
    TextView tv_tab_name;
    @BindView(R.id.tv_search_txt)
    TextInputEditText tvSearchTxt;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;

    @BindString(R.string.tab_main_name)
    String msg_name;
    Unbinder unbinder;
    private HomeMsgInfosAdapter adapter;
    private List<NewMsgListDataBean> listData = new ArrayList<>();
    private HomePresenter mPresenter;

    private String mStartTime = "";
    private String mEndTime = "";
    private boolean isGetDataAgain = false;

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;



    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, rootView);
        mPresenter = new HomePresenter(this, (MainActivity) getActivity());
        tv_tab_name.setText(msg_name);
        tvSearchTxt.clearFocus();
        tvSearchTxt.setCursorVisible(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        initRecycleView();
        rlSearch.setVisibility(View.VISIBLE);
        isGetDataAgain = false;

    }

    @Override
    public void onStart() {
        if (isGetDataAgain) {
            getNetDatas();
        }
        super.onStart();
    }

    @Override
    protected void fetchData() {
    }


    private void getNetDatas() {
        if (mPresenter != null) {
            String pid = PreferencesUtils.getString(getContext(), Constans.ORGID, "");
            mPresenter.getAnnouncementByuserid(ApiService.GETANNOUNCEMENTBYUSERID, pid, mStartTime, mEndTime);
        }
    }


    private void initRecycleView() {
        adapter = new HomeMsgInfosAdapter(R.layout.recyclerview_item_msginfos, listData);
        recyclerview.setAdapter(adapter);
        addNativeDatas();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                String genre = listData.get(position).getGenre();
                // genre：1就是公告，2就是超哥的信息，3是更新的
                if (genre.equals("1")) {//公告
                    intent.setClass(Objects.requireNonNull(getActivity()), AnnouncementListInfosActivtiy.class);
                } else if (genre.equals("2")) {
                    intent.setClass(Objects.requireNonNull(getActivity()), MsgListInfosActivtiy.class);
                } else {
                    intent.setClass(Objects.requireNonNull(getActivity()), HistoryAppVersionActivtiy.class);
                }
                startActivity(intent);
                isGetDataAgain = true;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.NEW_MSG_SUCCESS://展示最新消息
                List<NewMsgListDataBean> list = (List<NewMsgListDataBean>) event.getData();
                if (list.size() > 0) {
                    showAdapterDatasList(list);
                } else {
                    showSuccessToast("无最新消息");
                }
                break;

            case Constans.NEW_MSG_ERROR:
                addNativeDatas();
                break;
        }
    }

    private void initConversationListUI() {
        //会话列表
        // 加入自定义会话列表
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.chat_content, initConversationList())
                .commit();
    }


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }


    private void addNativeDatas() {
        List<NewMsgListDataBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NewMsgListDataBean bean = new NewMsgListDataBean();
            bean.setGenre(String.valueOf(i));
            bean.setContent("");
            bean.setIsread("1");
            bean.setIssuedate("");
            bean.setOrganname("");
            bean.setSubject("");
            list.add(bean);
        }

        showAdapterDatasList(list);
    }

    /*展示数据*/
    private void showAdapterDatasList(List<NewMsgListDataBean> list) {
        listData.clear();
        listData.addAll(list);
        adapter.setNewData(listData);
    }

    @OnClick(R.id.tv_search_txt)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_txt:
                tvSearchTxt.setCursorVisible(true);
                break;
        }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
