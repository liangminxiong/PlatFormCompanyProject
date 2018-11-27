package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.AnnouncementListInfosContract;
import com.yuefeng.home.presenter.AnnouncementListInfosPresenter;
import com.yuefeng.home.ui.adapter.AnnouncementListsInfosAdapter;
import com.yuefeng.home.ui.modle.AnnouncementDataMsgBean;
import com.yuefeng.home.ui.modle.MsgListDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*公告List*/
@SuppressLint("Registered")
public class AnnouncementListInfosActivtiy extends BaseActivity implements AnnouncementListInfosContract.View{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<AnnouncementDataMsgBean> listData = new ArrayList<>();
    private AnnouncementListsInfosAdapter adapter;
    private AnnouncementListInfosPresenter mPresenter;

    private static int CURPAGE = 1;
    private static int PAGE_COUNT = 1;
    private boolean isRefresh = false;
    private String mPid;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_announcementlistinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPresenter = new AnnouncementListInfosPresenter(this, this);
        initUI();
    }

    private void initUI() {
        tv_title.setText(R.string.msg);
        tv_back.setText(R.string.back);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycleView();
        getDatas();
    }

    @OnClick(R.id.tv_back)
    public void onClick() {
        finish();
    }

    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        int size = (int) bundle.get("tempPosition");
        getDataByNet();
    }

    /*获取数据源*/
    private void getDataByNet() {
        if (mPresenter != null) {
            mPid = PreferencesUtils.getString(this, Constans.ORGID, "");
            mPid = "dg1954";
            String startTime = "2018-01-01 00:12:20";
            String endTime = TimeUtils.getCurrentTime2();

            mPresenter.getAnnouncementByuserid(mPid,startTime,endTime,"1");
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.ANMENT_LIST_SSUCESS:
                List<AnnouncementDataMsgBean> list = (List<AnnouncementDataMsgBean>) event.getData();
                if (list.size() > 0) {
                    showAdapterDatasList(list);
//                } else {
//                    showSuccessToast("暂无更新信息");
                }
                break;
            case Constans.ANMENT_LIST_ERROR:
                showSuccessToast("加载失败");
                break;
        }
    }


    /*展示数据*/
    private void showAdapterDatasList(List<AnnouncementDataMsgBean> list) {

        if (CURPAGE <= 1) {
            listData.clear();
            listData.addAll(list);
            adapter.setNewData(listData);
            isRefresh = true;
            adapter.setEnableLoadMore(true);
        } else {
            if (list != null) {
                adapter.addData(list);
            } else {
                adapter.loadMoreEnd(true);
            }
        }
        PAGE_COUNT++;
    }

    private void initRecycleView() {
        adapter = new AnnouncementListsInfosAdapter(R.layout.recyclerview_item_msgdetail, listData, AnnouncementListInfosActivtiy.this);
        recyclerview.setAdapter(adapter);

        /*adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                AnnouncementDataMsgBean msgListDataBean = listData.get(position);
                toOnlyDetailActivity(msgListDataBean);
            }
        });*/
    }

    @Override
    protected void setLisenter() {
    }

    private void toOnlyDetailActivity(MsgListDataBean msgDataBean) {
        if (msgDataBean != null) {
            Intent intent = new Intent();
            intent.setClass(AnnouncementListInfosActivtiy.this, OnlyMsgDetailInfosActivtiy.class);
            intent.putExtra("msgData", msgDataBean);
            startActivity(intent);
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
