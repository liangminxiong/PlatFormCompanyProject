package com.yuefeng.features.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.BaseMvpFragment;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.FeaturesMsgAdapter;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.features.ui.activity.JobMonitoringActivity;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkActivity;
import com.yuefeng.features.ui.activity.ProblemUpdateActivity;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;
import com.yuefeng.features.ui.activity.WebH5ZuoyeKaoqinActivity;
import com.yuefeng.features.ui.activity.position.PositionAcquisitionActivity;
import com.yuefeng.features.ui.activity.track.HistoryTrackActivity;
import com.yuefeng.features.ui.activity.video.VideoCameraActivity;
import com.yuefeng.home.ui.activity.MsgDetailInfosActivtiy;
import com.yuefeng.home.ui.modle.MsgDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2018/7/11.
 * 应用
 */

public class FeaturesFragment extends BaseMvpFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_bg_logo)
    TextView tvBgLogo;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    Unbinder unbinder;

    private FeaturesMsgAdapter adapter;
    private List<MsgDataBean> listData = new ArrayList<>();
    private int tempPosition = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_features;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        initRecycleView();
        recyclerview.setFocusable(false);

        initViewHorW();
    }

    private void initViewHorW() {
        int hight = (int) AppUtils.mScreenHeight / 4;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewUtils.setTVHightOrWidth(tvBgLogo, hight, width);
    }

    @Override
    protected void fetchData() {
    }

    @Override
    protected void initData() {

    }


    private void initRecycleView() {
        adapter = new FeaturesMsgAdapter(R.layout.recyclerview_item_msginfos, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    tempPosition = 1;
                } else if (position == 1) {
                    tempPosition = 2;
                } else {
                    tempPosition = 3;
                }
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()), MsgDetailInfosActivtiy.class);
                intent.putExtra("msgList", (Serializable) listData);
                intent.putExtra("tempPosition", tempPosition);
                startActivity(intent);
            }
        });
        showAdapterDatasList();
    }

    /*展示数据*/
    private void showAdapterDatasList() {
        List<MsgDataBean> bean = new ArrayList<>();
        String name = "";
        String detail = "";
        for (int i = 0; i < 3; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            if (i == 0) {
                name = "侨银环保科技股份有限公司";
                detail = "[审批]今天还有一个审批单待你处理，请尽快处理";
                msgDataBean.setImageUrl(R.drawable.work);
            } else if (i == 1) {
                name = "升级提醒";
                detail = "1.0.2版本新功能介绍";
                msgDataBean.setImageUrl(R.drawable.upgrade);
            } else {
                name = "项目通知:池州一体化项目进展情况";
                detail = "[执行]今天还有2个任务待你处理，请尽快完成";
                msgDataBean.setImageUrl(R.drawable.item);
            }
            msgDataBean.setName(name);
            msgDataBean.setTitle(name);
            msgDataBean.setTime(TimeUtils.getHourMinute());
            msgDataBean.setDetail(detail);
            msgDataBean.setCount((i + 1) + "");
            bean.add(msgDataBean);
        }
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCarListEvent(CarListEvent event) {
        switch (event.getWhat()) {
            case Constans.TRACK_SSUCESS://展示
                break;

            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }
    }


    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick({R.id.rl_kaoqin, R.id.rl_operationbianji, R.id.rl_historytrack, R.id.rl_videojian,
            R.id.rl_problemupload, R.id.rl_qualityxuncha, R.id.rl_operationweigui, R.id.rl_msgcollection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_videojian:
                toVideoSytemList();//视频监控
                break;
            case R.id.rl_kaoqin://作业考勤aa
                jobwebH5();
                break;
            case R.id.rl_msgcollection:
                msgcollection();//信息采集
                break;
            case R.id.rl_operationbianji://违规作业
                toIllegalWork();
                break;
            case R.id.rl_historytrack:
                toHistoryTrack();//历史轨迹
                break;
            case R.id.rl_problemupload:
                problemUpload();
                break;
            case R.id.rl_qualityxuncha:
                qualityXuncha();
                break;
            case R.id.rl_operationweigui://作业监控
                jobMonitoring();
                break;
        }
    }

    /*；违规作业*/
    private void toIllegalWork() {
        startActivity(new Intent(getActivity(), LllegalWorkActivity.class));
    }

    /*；历史轨迹*/
    private void toHistoryTrack() {
        startActivity(new Intent(getActivity(), HistoryTrackActivity.class));
    }

    /*；视频监控*/
    private void toVideoSytemList() {
//        startActivity(new Intent(getActivity(), VideoSytemListActivity.class));
        startActivity(new Intent(getActivity(), VideoCameraActivity.class));
    }

    /*信息采集*/
    private void msgcollection() {
        startActivity(new Intent(getActivity(), PositionAcquisitionActivity.class));
    }

    /*作业监察*/
    private void problemUpload() {
        startActivity(new Intent(getActivity(), ProblemUpdateActivity.class));
    }

    /*问题处理*/
    private void qualityXuncha() {
        startActivity(new Intent(getActivity(), QualityInspectionActivity.class));
    }

    /*定位信息*/
    private void jobMonitoring() {
        startActivity(new Intent(getActivity(), JobMonitoringActivity.class));
    }


    /*作业考勤*/
    private void jobwebH5() {
        startActivity(new Intent(getActivity(), WebH5ZuoyeKaoqinActivity.class));
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
