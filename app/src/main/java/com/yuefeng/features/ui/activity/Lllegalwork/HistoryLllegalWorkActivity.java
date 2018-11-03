package com.yuefeng.features.ui.activity.Lllegalwork;

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
import com.yuefeng.features.adapter.CarHistoryLllegalWorkListAdapter;
import com.yuefeng.features.contract.HistoryLllegalWorkContract;
import com.yuefeng.features.modle.LllegalworMsgBean;
import com.yuefeng.features.presenter.HistoryLllegalWorkPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*查看历史违规作业*/
public class HistoryLllegalWorkActivity extends BaseActivity implements HistoryLllegalWorkContract.View {

    private static final String TAG = "tag";
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

    private HistoryLllegalWorkPresenter presenter;

    private List<LllegalworMsgBean> listData = new ArrayList<>();
    private CarHistoryLllegalWorkListAdapter adapter;
    private String startTime;
    private String endTime;
    private String type;
    private String id;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_historylllegalwork;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        tvTitle.setText(R.string.history_lllegal);
        presenter = new HistoryLllegalWorkPresenter(this, this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initIntent();
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        type = (String) bundle.get("type");
        id = (String) bundle.get("id");
        initRecycler(type);
        initTime(type, id);

    }

    private void initTime(String type, String vid) {
        startTime = TimeUtils.getYesterdayStartTime();
        startTime = "2018-10-30 10:00:00";
        endTime = TimeUtils.getCurrentTime();
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
        getCarList(type, id, startTime, endTime);
    }

    private void initRecycler(final String type) {
        adapter = new CarHistoryLllegalWorkListAdapter(R.layout.recyclerview_item_historylllegals, listData, type);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LllegalworMsgBean msgDataBean = listData.get(position);
                Intent intent = new Intent();
                intent.setClass(HistoryLllegalWorkActivity.this, LllegalWorkDetailActivity.class);
                intent.putExtra("DetailInfos", msgDataBean);
                intent.putExtra("type", type);
                intent.putExtra("isVisible", "0");
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    /*违规获取*/
    private void getCarList(String type, String vid, String startTime, String endTime) {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            if (type.equals("car")) {
                type = Constans.TYPE_ZERO;
            } else {
                type = Constans.TYPE_ONE;
            }
            presenter.getWeigui(ApiService.GETWEIGUI, pid, startTime, endTime, vid, type, 3);
        }
    }

    @Override
    public void initData() {
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.CARLIST_SSUCESS:
                List<LllegalworMsgBean> beanList = (List<LllegalworMsgBean>) event.getData();
                if (beanList.size() > 0) {
                    showAdapterDatasList(beanList);
                }
                break;
            default:
                showSuccessToast("查询失败，请重试");
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            getCarList(type, id, startTime, endTime);
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

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList(List<LllegalworMsgBean> beanList) {
        listData.clear();
        listData.addAll(beanList);
        adapter.setNewData(listData);
    }
}
