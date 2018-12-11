package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.AnnouncementListInfosContract;
import com.yuefeng.home.modle.AnnouncementDataBean;
import com.yuefeng.home.modle.AnnouncementDataMsgBean;
import com.yuefeng.home.presenter.AnnouncementListInfosPresenter;
import com.yuefeng.home.ui.adapter.AnnouncementListsInfosAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*公告List*/
@SuppressLint("Registered")
public class AnnouncementListInfosActivtiy extends BaseActivity implements
        AnnouncementListInfosContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.swf_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.iv_showtime)
    ImageView ivShowtime;

    private List<AnnouncementDataMsgBean> listData = new ArrayList<>();
    private AnnouncementListsInfosAdapter adapter;
    private AnnouncementListInfosPresenter mPresenter;

    private static int CURPAGE = 1;
    private boolean isRefresh = false;
    private String mPid;
    private String mStartTime = "";
    private String mEndTime = "";
    private int mCount;
    private TimePickerView timePickerView;

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
        mPresenter = new AnnouncementListInfosPresenter(this, this);
        initUI();
    }

    private void initUI() {
        tv_title.setText("公告消息");
        tv_back.setText(R.string.back);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycleView();
//        getDataByNet();
    }

    @OnClick(R.id.tv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRefresh = false;
        CURPAGE = 1;
        getDataByNet();
    }



    /*获取数据源*/
    private void getDataByNet() {
        if (mPresenter != null) {
            mPid = PreferencesUtils.getString(this, Constans.ORGID, "");
            mPresenter.getAnnouncementByuserid(ApiService.GETDATA, mPid, mStartTime, mEndTime, CURPAGE, Constans.TEN, true);
        }
    }

    @Override
    protected void onStop() {
        listData.clear();
        super.onStop();
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.ANMENT_LIST_SSUCESS:
                AnnouncementDataBean bean = (AnnouncementDataBean) event.getData();
                assert bean != null;
                listData = bean.getData();
                if (listData.size() <= 0) {
                    if (CURPAGE < 2) {
                        showSuccessToast("暂无信息");
                    }
                }
                if (!isRefresh) {
                    adapter.setNewData(listData);
                    isRefresh = true;
                    adapter.setEnableLoadMore(false);
                } else {
                    if (listData != null) {
                        adapter.addData(listData);
                    } else {
                        adapter.loadMoreEnd(true);
                    }
                }
                mCount = bean.getCount();
                break;
            case Constans.ANMENT_LIST_ERROR:
                showSuccessToast("加载失败");
                break;
            case Constans.ANMENT_LIST_ERROR_NULL:
                showSuccessToast("暂无信息");
                break;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initRecycleView() {
        adapter = new AnnouncementListsInfosAdapter(R.layout.recyclerview_item_msgdetail, listData, AnnouncementListInfosActivtiy.this);
        adapter.setOnLoadMoreListener(this, recyclerview);
        adapter.disableLoadMoreIfNotFullPage();
        recyclerview.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (listData.size() > 0) {
                    AnnouncementDataMsgBean msgBean = listData.get(position);
                    toOnlyDetailActivity(msgBean);
                }
            }
        });
    }

    @Override
    protected void setLisenter() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    isRefresh = false;
                    CURPAGE = 1;
                    mEndTime = TimeUtils.getCurrentTime2();
                    adapter.setEnableLoadMore(false);
                    mPresenter.getAnnouncementByuserid(ApiService.GETDATA, mPid, mStartTime, mEndTime, CURPAGE, Constans.TEN, false);
                }
        });
    }

    private void toOnlyDetailActivity(AnnouncementDataMsgBean msgBean) {
        if (msgBean != null) {
            Intent intent = new Intent();
            intent.setClass(AnnouncementListInfosActivtiy.this, AnnouncementDetailInfosActivtiy.class);
            intent.putExtra("msgData", msgBean);
            startActivity(intent);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCount > 10) {
                    ++CURPAGE;
                    mPresenter.getAnnouncementByuserid(ApiService.GETDATA, mPid, mStartTime, mEndTime, CURPAGE, Constans.TEN, false);
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            }
        }, 1000);
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_search, R.id.iv_showtime, R.id.recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                tv_start();
                break;
            case R.id.tv_end_time:
                tv_end();
                break;
            case R.id.recyclerview:
                break;
            case R.id.tv_search:
                rl_search();
                break;
            case R.id.iv_showtime:
                ivShowtime.setVisibility(View.INVISIBLE);
                rlTime.setVisibility(View.VISIBLE);
                break;
        }
    }

    /*查询*/
    private void rl_search() {
        if (!TextUtils.isEmpty(mPid)) {
            rlTime.setVisibility(View.INVISIBLE);
            ivShowtime.setVisibility(View.VISIBLE);
            isRefresh = false;
            CURPAGE = 1;
//            ApiRetrofit.changeApiBaseUrl(NetworkUrl.ANDROID_TEST_SERVICE_DI);
            mPresenter.getAnnouncementByuserid(ApiService.GETDATA, mPid, tvStartTime.getText().toString().trim()
                    , tvEndTime.getText().toString().trim(), CURPAGE, Constans.TEN, true);

        }
    }


    private void tv_start() {

        if (timePickerView == null) {
            timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        // 时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mStartTime = TimeUtils.getTimeHourMin(date);
                if (!TextUtils.isEmpty(tvEndTime.getText().toString().trim())) {
                    boolean boolenStartEndTime = TimeUtils.getBoolenStartEndTime(mStartTime, mEndTime);
                    if (boolenStartEndTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tvStartTime.setText(mStartTime);
            }
        });
        timePickerView.show();
    }

    private void tv_end() {
        if (timePickerView == null) {
            timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        // 时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mEndTime = TimeUtils.getTimeHourMin(date);
                if (!TextUtils.isEmpty(tvStartTime.getText().toString().trim())) {
                    boolean startEndTime = TimeUtils.getBoolenStartEndTime(tvStartTime.getText().toString().trim(),
                            tvEndTime.getText().toString().trim());
                    if (startEndTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tvEndTime.setText(mEndTime);
            }
        });
        timePickerView.show();
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
