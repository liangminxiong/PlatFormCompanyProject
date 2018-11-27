package com.yuefeng.features.ui.activity;

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
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.HistoryProblemAdapter;
import com.yuefeng.features.contract.HistoryProblemUpContract;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.features.presenter.HistoryProblemUpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*历史问题上报*/
public class HistoryProblemUpdataActivity extends BaseActivity implements HistoryProblemUpContract.View {

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
    private HistoryProblemAdapter adapter;

    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private String startTime;
    private String endTime;
    private HistoryProblemUpPresenter mPresenter;


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
        mPresenter = new HistoryProblemUpPresenter(this, this);
        initUI();
    }

    private void initUI() {
        tvTitle.setText("历史上报");
        tvBack.setText(R.string.back);
        initRecycler();
        initTime();
    }

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
            mPresenter.getEveQuestionByuserid(ApiService.GETEVEQUESTIONBYUSERID, userid, startTime, endTime);
        }
    }

    private void initRecycler() {
        try {
            listData.clear();
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            adapter = new HistoryProblemAdapter(R.layout.recycler_history_problem, listData);
            recyclerview.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    String problemid = listData.get(position).getId();
                    String name = listData.get(position).getUploadpeoplename();
                    String state = listData.get(position).getState();
                    Intent intent = new Intent();
                    intent.setClass(HistoryProblemUpdataActivity.this, QualityInspectionDetailActivity.class);
                    intent.putExtra("PROBLEMID", problemid);
                    intent.putExtra("NAME", name);
                    intent.putExtra("STATE", state);
                    intent.putExtra("NOREPLY", "No");
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.UPLOAD_HISTORY_SUCESS://获取数据成功:
                List<EventQuestionMsgBean> listData = (List<EventQuestionMsgBean>) event.getData();
                if (listData.size() > 0) {
                    showAdapterDatasList(listData);
                } else {
                    showSuccessToast("暂无信息");
                }
                break;
            case Constans.UPLOAD_HISTORY_ERROR://失败
                showSuccessToast("加载失败，请重试!");
                break;
        }
    }

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList(List<EventQuestionMsgBean> beanList) {
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
