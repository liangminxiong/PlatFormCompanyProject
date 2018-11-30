package com.yuefeng.features.ui.activity.monitoring;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.HistoryMonitoringSngnInAdapter;
import com.yuefeng.features.contract.HistorySngnInContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.HistorySngnInListDataBean;
import com.yuefeng.features.presenter.monitoring.HistorySngnInPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*历史签到*/
public class HistoryMonitoringSngnInActivity extends BaseActivity implements HistorySngnInContract.View {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private HistoryMonitoringSngnInAdapter adapter;

    private List<HistorySngnInListDataBean> listData = new ArrayList<>();
    private String startTime;
    private String endTime;
    private HistorySngnInPresenter mPresenter;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_historypositionacqusition;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        mPresenter = new HistorySngnInPresenter(this, this);
        initUI();
    }

    private void initUI() {
        tvTitle.setText("历史签到");
        tvBack.setText(R.string.back);
        initRecycler();
        initTime();
    }

    @SuppressLint("SetTextI18n")
    private void initTime() {
        Bundle bundle = getIntent().getExtras();
        startTime = (String) bundle.get(Constans.STARTTIME);
        endTime = (String) bundle.get(Constans.ENDTIME);
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
        getNetDatas(startTime, endTime);
    }

    private void getNetDatas(String startTime, String endTime) {
        if (mPresenter != null) {
            String userid = PreferencesUtils.getString(this, Constans.ID);
            mPresenter.getAppWorkSign(ApiService.GETAPPWORKSIGN, userid, startTime, endTime);
        }
    }

    private void initRecycler() {
        try {
            listData.clear();
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            adapter = new HistoryMonitoringSngnInAdapter(R.layout.recyclerview_item_historylllegals, listData);
            recyclerview.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    HistorySngnInListDataBean dataBean = listData.get(position);
                    Intent intent = new Intent();
                    intent.setClass(HistoryMonitoringSngnInActivity.this, MonitoringSngnInDetailActivity.class);
                    intent.putExtra("dataBean", dataBean);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        switch (event.getWhat()) {
            case Constans.MONITORINGSIGNINHIS_SSUCESS://获取数据成功:
                List<HistorySngnInListDataBean> listData = (List<HistorySngnInListDataBean>) event.getData();
                if (listData.size() > 0) {
                    showAdapterDatasList(listData);
                } else {
                    showSuccessToast("暂无信息");
                }
                break;
            case Constans.MONITORINGSIGNINHIS_ERROR://失败
                showSuccessToast("加载失败，请重试!");
                break;
        }
    }

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList(List<HistorySngnInListDataBean> beanList) {
        listData.clear();
        listData.addAll(beanList);
        adapter.setNewData(listData);
    }


    @OnClick({R.id.tv_search, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                chooseStartTime();
                break;
            case R.id.tv_end_time:
                chooseEndTime();
                break;
            case R.id.tv_search:
                search();
                break;
            case R.id.tv_back:
                finish();
                break;
        }

    }

    private void search() {
        startTime = tvStartTime.getText().toString().trim();
        endTime = tvEndTime.getText().toString().trim();
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            getNetDatas(startTime, endTime);
        } else {
            showSuccessToast("请选择时间");
        }
    }

    /*选择结束时间*/
    private void chooseEndTime() {
        try {
            TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
            timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSelect(Date date) {
                    tvEndTime.setText(TimeUtils.getTimeHourMin(date));
                }
            });
            timePickerView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*选择开始时间*/
    private void chooseStartTime() {
        try {
            TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
            timePickerView.setTitle("时间选择");
            timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSelect(Date date) {
                    tvStartTime.setText(TimeUtils.getTimeHourMin(date));
                }
            });
            timePickerView.show();
        } catch (Exception e) {
            e.printStackTrace();
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
