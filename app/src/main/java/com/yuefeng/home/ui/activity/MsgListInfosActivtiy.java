package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.MsgListInfosContract;
import com.yuefeng.home.presenter.MsgListInfosPresenter;
import com.yuefeng.home.ui.adapter.MsgListsInfosAdapter;
import com.yuefeng.home.ui.modle.MsgListDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*信息详情List*/
@SuppressLint("Registered")
public class MsgListInfosActivtiy extends BaseActivity implements MsgListInfosContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.swf_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<MsgListDataBean> listData = new ArrayList<>();
    private MsgListsInfosAdapter adapter;
    private MsgListInfosPresenter mPresenter;

    private static int CURPAGE = 1;
    private static int PAGE_COUNT = 1;
    private boolean isRefresh = false;
    private String mPid;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_msgdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPresenter = new MsgListInfosPresenter(this, this);
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
//            pid = "dg1168";
            mPresenter.getAnMentDataList(mPid, CURPAGE, Constans.FOUR, true);
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.MSG_LIST_SSUCESS:
                List<MsgListDataBean> list = (List<MsgListDataBean>) event.getData();
                if (list.size() > 0) {
                    showAdapterDatasList(list);
//                } else {
//                    showSuccessToast("暂无更新信息");
                }
                break;
            case Constans.MSG_LIST_ERROR:
                showSuccessToast("加载失败");
                break;

        }
        swipeRefreshLayout.setRefreshing(false);
    }


    /*展示数据*/
    private void showAdapterDatasList(List<MsgListDataBean> list) {

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
        adapter = new MsgListsInfosAdapter(R.layout.recyclerview_item_msgdetail, listData, MsgListInfosActivtiy.this);
        adapter.setOnLoadMoreListener(this, recyclerview);
        adapter.disableLoadMoreIfNotFullPage();
        recyclerview.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                MsgListDataBean msgListDataBean = listData.get(position);
                toOnlyDetailActivity(msgListDataBean);
//                switch (id) {
//                    case R.id.tv_item_name:
//                        break;
//                    case R.id.tv_item_content:
//                        toOnlyDetailActivity(msgListDataBean);
//                        break;
//                    case R.id.tv_item_detail:
//                        toOnlyDetailActivity(msgListDataBean);
//                        break;
//                }
            }
        });
    }

    @Override
    protected void setLisenter() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CURPAGE = 1;
                mPresenter.getAnMentDataList(mPid, CURPAGE, Constans.TEN, false);
                adapter.setEnableLoadMore(false);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CURPAGE <= PAGE_COUNT) {
                    CURPAGE++;
                    mPresenter.getAnMentDataList(mPid, CURPAGE, Constans.TEN, false);
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            }
        }, 1000);
    }

    private void toOnlyDetailActivity(MsgListDataBean msgDataBean) {
        if (msgDataBean != null) {
            Intent intent = new Intent();
            intent.setClass(MsgListInfosActivtiy.this, OnlyMsgDetailInfosActivtiy.class);
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
