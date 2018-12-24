package com.yuefeng.home.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.HomeContract;
import com.yuefeng.home.modle.NewMsgListDataBean;
import com.yuefeng.home.presenter.NewRemindNorPresenter;
import com.yuefeng.home.ui.adapter.HomeMsgInfosAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*消息，报警界面*/
public class NewRemindNorActivity extends BaseActivity implements HomeContract.View {


    @BindView(R.id.tv_search_txt)
    TextInputEditText tvSearchTxt;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HomeMsgInfosAdapter adapter;
    private List<NewMsgListDataBean> listData = new ArrayList<>();
    private NewRemindNorPresenter mPresenter;

    private String mStartTime = "";
    private String mEndTime = "";
    private boolean isFirstGetData = true;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_newremindnor;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setTitle("消息列表");
        mPresenter = new NewRemindNorPresenter(this, this);
        tvSearchTxt.clearFocus();
        tvSearchTxt.setCursorVisible(false);
        initRecycleView();
        isFirstGetData = true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        getNetDatas(isFirstGetData);
        super.onStart();
    }

    private void getNetDatas(boolean isFirstGetData) {
        if (mPresenter != null) {
            String pid = PreferencesUtils.getString(NewRemindNorActivity.this, Constans.ORGID, "");
            mPresenter.getAnnouncementByuserid(ApiService.GETANNOUNCEMENTBYUSERID, pid, mStartTime, mEndTime,isFirstGetData);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    private void initRecycleView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
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
                    intent.setClass(NewRemindNorActivity.this, AnnouncementListInfosActivtiy.class);
                } else if (genre.equals("2")) {
                    intent.setClass(NewRemindNorActivity.this, MsgListInfosActivtiy.class);
                } else if (genre.equals("4")) {
                    intent.setClass(NewRemindNorActivity.this, NewRemindNorListInfosActivtiy.class);
                } else {
                    intent.setClass(NewRemindNorActivity.this, HistoryAppVersionActivtiy.class);
                }
                isFirstGetData = false;
                startActivity(intent);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.REMINDLIST_SUCCESS://展示最新消息
                List<NewMsgListDataBean> list = (List<NewMsgListDataBean>) event.getData();
                if (list.size() > 0) {
                    showAdapterDatasList(list);
                } else {
                    showSuccessToast("无最新消息");
                }
                break;

            case Constans.REMINDLIST_ERROR:
                showSureGetAgainDataDialog("获取数据失败，是否重新加载?");
                break;
        }
    }


    @Override
    public void getDatasAgain() {
        getNetDatas(true);
        super.getDatasAgain();
    }

    private void addNativeDatas() {
        List<NewMsgListDataBean> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            NewMsgListDataBean bean = new NewMsgListDataBean();
            if (i == 3) {
                i = 4;
            }
            bean.setGenre(String.valueOf(i));
            bean.setContent("无");
            bean.setIsread("1");
            bean.setIssuedate("无");
            bean.setOrganname("无");
            bean.setSubject("无");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
