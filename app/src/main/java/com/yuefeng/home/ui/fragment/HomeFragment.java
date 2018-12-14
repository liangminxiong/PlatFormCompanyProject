package com.yuefeng.home.ui.fragment;

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
import com.yuefeng.home.modle.MsgListDataBean;
import com.yuefeng.home.modle.NewMsgListDataBean;
import com.yuefeng.home.presenter.HomePresenter;
import com.yuefeng.home.ui.activity.AnnouncementListInfosActivtiy;
import com.yuefeng.home.ui.activity.HistoryAppVersionActivtiy;
import com.yuefeng.home.ui.activity.MsgListInfosActivtiy;
import com.yuefeng.home.ui.adapter.ConversationListAdapterEx;
import com.yuefeng.home.ui.adapter.HomeMsgInfosAdapter;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.rongIm.RongIMUtils;
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

    /*测试融云*/
    public static String libaiToken = "aHHKNr+jpANSk+uWQRY39gM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cCCo35Kr7Jm6letTWyBnuJKhz4P1/QEycI9EHoqNufxmiRbEGBi6ETk=";
    public static String dufuToken = "RYCau/8zuqnDOmQTWPRG1QM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cDJY62ny6gUtsHqBkVNTaVu99jIf/dRs6EZ/XPYM5WYb2aUb9QjRZ7s=";
    public static String wangxizhiToken = "DAH3ZJtjSLdz3oaJJPaMYgM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cAQ4syW7Urv0jMLVuVYI85XcyUNRn2uYB2yGD+SXX0kF";
    public static String libaiName = "李白";
    public static String dufuName = "杜甫";
    public static String wangxizhiName = "王羲之";
    public static String libaiUserId = "zxcvbnmasdfghjkl";
    public static String dufuUserId = "asdfghjklzxcvbnm";
    public static String wangxizhiUserId = "qwertyuiop";
    private static String portraitUrl = "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";

    //会话的聊天窗口
    ConversationListFragment mListFragment;
    FragmentManager mManager;
    private boolean isDebug=true;
    private Conversation.ConversationType[] mConversationsTypes;

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

        RongIMUtils.init(libaiUserId, libaiName, portraitUrl);
        RongIMUtils.connectToken(libaiToken);
        //会话列表
        mListFragment = (ConversationListFragment) initConversationList();
        mManager = getChildFragmentManager();
        mManager.beginTransaction().replace(R.id.chat_content,mListFragment).commit();

    }

    private Fragment initConversationList() {
        if (mListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
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
                uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
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
            mListFragment = listFragment;
            return listFragment;
        } else {
            return mListFragment;
        }
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void disposeCarListEvent(CarListEvent event) {
//        switch (event.getWhat()) {
//            case Constans.NEW_MSG_SUCCESS://展示最新消息
//                List<NewMsgListDataBean> list = (List<NewMsgListDataBean>) event.getData();
//                if (list.size() > 0) {
//                    showAdapterDatasList(list);
//                } else {
//                    showSuccessToast("无最新消息");
//                }
//                break;
//            case Constans.NEW_MSG_ERROR:
//                addNativeDatas();
//                break;
//
//        }
//    }

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

    /*展示数据*/
    private void showAdapterDatasList() {
        String name = "";
        String detail = "";
        List<MsgListDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MsgListDataBean msgDataBean = new MsgListDataBean();
            if (i == 0) {
                name = "侨银环保科技股份有限公司";
                detail = "[审批]今天还有一个审批单待你处理，请尽快处理";
                msgDataBean.setImageId(R.drawable.work);
            } else if (i == 1) {
                name = "升级提醒";
                detail = "1.0.2版本新功能介绍";
                msgDataBean.setImageId(R.drawable.upgrade);
            } else {
                name = "项目通知:池州一体化项目进展情况";
                detail = "[执行]今天还有2个任务待你处理，请尽快完成";
                msgDataBean.setImageId(R.drawable.item);
            }
            msgDataBean.setOrg(name);
            msgDataBean.setReviewtitle(name);
            msgDataBean.setReviewcontent(detail);
            bean.add(msgDataBean);
        }
        listData.clear();
//        listData.addAll(bean);
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
