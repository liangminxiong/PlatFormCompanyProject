package com.yuefeng.features.ui.activity.Lllegalwork;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.StatusBarUtil;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.CarLllegalWorkListAdapter;
import com.yuefeng.features.contract.HistoryLllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.presenter.HistoryLllegalWorkPresenter;
import com.yuefeng.home.ui.modle.MsgDataBean;

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
    @BindView(R.id.space)
    View view;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HistoryLllegalWorkPresenter presenter;

    private List<MsgDataBean> listData = new ArrayList<>();
    private CarLllegalWorkListAdapter adapter;
    private String startTime;
    private String endTime;

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
        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycler();
        getCarList();
        initTime();
    }

    private void initTime() {
        startTime = TimeUtils.getStartTimeofDay();
        endTime = TimeUtils.getCurrentTime();
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
    }

    private void initRecycler() {
        adapter = new CarLllegalWorkListAdapter(R.layout.recyclerview_item_lllegals, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MsgDataBean msgDataBean = listData.get(position);
                Intent intent = new Intent();
                intent.setClass(HistoryLllegalWorkActivity.this, LllegalWorkDetailActivity.class);
                intent.putExtra("DetailInfos", msgDataBean);
                intent.putExtra("type", "car");
                intent.putExtra("isVisible", "0");
                startActivity(intent);
            }
        });

    }

    /*车辆列表*/
    private void getCarList() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
            presenter.getHistoryLllegal(ApiService.LOADVEHICLELIST, pid, userid, "0");
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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeLllegalWorkEvent(LllegalWorkEvent event) {
        switch (event.getWhat()) {
            case Constans.CARLIST_SSUCESS:
//                carListData = (List<CarListInfosMsgBean>) event.getData();
//                if (carListData.size() > 0) {
//                } else {
//                    showSuccessToast("旗下无车辆");
//                }
                break;
            default:
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.tv_search, R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                chooseStartTime();
                break;
            case R.id.tv_end_time:
                chooseEndTime();
                break;
            case R.id.tv_search:
                showAdapterDatasList();
                break;
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
    private void showAdapterDatasList() {
        List<MsgDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            msgDataBean.setImageUrl(R.mipmap.icon_app);
            msgDataBean.setTitle(i + "粤A45654");
            msgDataBean.setTime(TimeUtils.getHourMinute());
            msgDataBean.setDetail(i + "垃圾桶坏了");
            msgDataBean.setCount(i + "广州市天河区新塘大街28号");
            bean.add(msgDataBean);
        }
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
    }
}
