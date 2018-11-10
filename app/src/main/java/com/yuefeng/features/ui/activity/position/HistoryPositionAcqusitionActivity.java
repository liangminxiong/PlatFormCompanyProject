package com.yuefeng.features.ui.activity.position;

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
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.HistoryPositionAdapter;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.LllegalworMsgBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*信息采集历史*/
public class HistoryPositionAcqusitionActivity extends BaseActivity {

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
    private HistoryPositionAdapter adapter;

    private List<LllegalworMsgBean> listData = new ArrayList<>();
    private String startTime;
    private String endTime;


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

        initUI();
    }

    private void initUI() {
        tvTitle.setText("历史采集");
        tvBack.setText("返回");
        initRecycler();
        initTime();
    }

    private void initTime() {
        startTime = TimeUtils.getYesterdayStartTime();
        endTime = TimeUtils.getCurrentTime();
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
//        getCarList(type, id, startTime, endTime);
        initRecyclerDatas(8);
    }

    private void initRecycler() {
        listData.clear();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryPositionAdapter(R.layout.recyclerview_item_historypositon, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LllegalworMsgBean msgBean = listData.get(position);
                Intent intent = new Intent();
                intent.setClass(HistoryPositionAcqusitionActivity.this, PositionAcquisitionDetailActivity.class);
                intent.putExtra("msgBean", msgBean);
                startActivity(intent);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposePositionAcquisitionEvent(PositionAcquisitionEvent event) {
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:
                showSuccessDialog("上传成功，是否退出当前界面?");
                break;
            case Constans.MSGCOLECTION_ERROR://上传失败

                break;
            case Constans.GETCAIJI_SSUCESS://获采集类型成功

                break;
            case Constans.GETCAIJI_ERROR://获采集类型失败
//                showSuccessToast("发布失败");
                break;
            default:

                break;
        }
    }

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList(List<LllegalworMsgBean> beanList) {
        listData.clear();
        listData.addAll(beanList);
        adapter.setNewData(listData);
    }

    private void initRecyclerDatas(int size) {
        List<LllegalworMsgBean> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < size; i++) {
            LllegalworMsgBean bean = new LllegalworMsgBean();
            bean.setName("垃圾点 " + i);
            bean.setTime(TimeUtils.getCurrentTime2());
            bean.setContents("作业区域");
            bean.setAddress("生活垃圾收集点" + i);
            list.add(bean);
        }
        showAdapterDatasList(list);
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
        }

    }

    private void search() {
        startTime = tvStartTime.getText().toString().trim();
        endTime = tvEndTime.getText().toString().trim();
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
//            getCarList(type, id, startTime, endTime);
            showSuccessToast("待完成");
        } else {
            showSuccessToast("请选择时间");
        }
    }

    /*选择结束时间*/
    private void chooseEndTime() {
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvEndTime.setText(TimeUtils.getTimeHourMin(date));
            }
        });
        timePickerView.show();
    }

    /*选择开始时间*/
    private void chooseStartTime() {
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        timePickerView.setTitle("时间选择");
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {

                tvStartTime.setText(TimeUtils.getTimeHourMin(date));
            }
        });
        timePickerView.show();
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
