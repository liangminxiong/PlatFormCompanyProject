package com.yuefeng.features.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.common.base.BaseMvpFragment;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.ui.activity.JobMonitoringActivity;
import com.yuefeng.features.ui.activity.ProblemUpdateActivity;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;
import com.yuefeng.features.ui.activity.WebH5ZuoyeKaoqinActivity;
import com.yuefeng.features.ui.activity.position.PositionAcquisitionActivity;
import com.yuefeng.features.ui.activity.track.HistoryTrackActivity;
import com.yuefeng.features.ui.activity.video.VideoSytemListActivity;

import butterknife.OnClick;


/**
 * Created by Administrator on 2018/7/11.
 * 应用
 */

public class FeaturesFragment extends BaseMvpFragment {

   /* @BindView(R.id.iv_myproject)
    ImageView ivMyproject;
    @BindView(R.id.iv_myteam)
    ImageView ivMyteam;
    @BindView(R.id.iv_mymanage)
    ImageView ivMymanage;
    @BindView(R.id.iv_mytack)
    ImageView ivMytack;
    @BindView(R.id.iv_msgcollection)
    ImageView ivMsgcollection;
    @BindView(R.id.iv_operationbianji)
    ImageView ivOperationbianji;
    @BindView(R.id.iv_operationkaoqin)
    ImageView ivOperationkaoqin;
    @BindView(R.id.iv_historytrack)
    ImageView ivHistorytrack;
    @BindView(R.id.iv_videomonitor)
    ImageView ivVideomonitor;
    @BindView(R.id.iv_locationinfos)
    ImageView ivLocationinfos;
    @BindView(R.id.iv_problemupload)
    ImageView ivProblemupload;
    @BindView(R.id.iv_qualityxuncha)
    ImageView ivQualityxuncha;
    @BindView(R.id.iv_operationweigui)
    ImageView ivOperationweigui;
    Unbinder unbinder;*/

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_features;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void fetchData() {
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
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_myproject, R.id.iv_myteam, R.id.iv_mymanage, R.id.iv_mytack, R.id.iv_msgcollection, R.id.iv_kaoqin,
            R.id.iv_operationbianji, R.id.iv_operationkaoqin, R.id.iv_historytrack, R.id.iv_videomonitor, R.id.iv_videojian,
            R.id.iv_locationinfos, R.id.iv_problemupload, R.id.iv_qualityxuncha, R.id.iv_operationweigui})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_myproject:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_myteam:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_mymanage:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_mytack:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_videojian:
                toVideoSytemList();//视频监控
                break;
            case R.id.iv_kaoqin://作业考勤aa
                jobwebH5();
                break;
            case R.id.iv_msgcollection:
                msgcollection();//信息采集
                break;
            case R.id.iv_operationbianji:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_operationkaoqin:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_historytrack:
                toHistoryTrack();//历史轨迹
                break;
            case R.id.iv_videomonitor:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_locationinfos:
                showSuccessToast("正在开发...");
                break;
            case R.id.iv_problemupload:
                problemUpload();
                break;
            case R.id.iv_qualityxuncha:
                qualityXuncha();
                break;
            case R.id.iv_operationweigui://作业监控
                jobMonitoring();
                break;
        }
    }

    /*；历史轨迹*/
    private void toHistoryTrack() {
        startActivity(new Intent(getActivity(), HistoryTrackActivity.class));
    }

    /*；视频监控*/
    private void toVideoSytemList() {
        startActivity(new Intent(getActivity(), VideoSytemListActivity.class));
    }

    /*信息采集*/
    private void msgcollection() {
        startActivity(new Intent(getActivity(), PositionAcquisitionActivity.class));
    }

    /*问题上报*/
    private void problemUpload() {
        startActivity(new Intent(getActivity(), ProblemUpdateActivity.class));
    }

    /*质量巡检*/
    private void qualityXuncha() {
        startActivity(new Intent(getActivity(), QualityInspectionActivity.class));
    }

    /*作业监控*/
    private void jobMonitoring() {
        startActivity(new Intent(getActivity(), JobMonitoringActivity.class));
    }


    /*作业考勤*/
    private void jobwebH5() {
        startActivity(new Intent(getActivity(), WebH5ZuoyeKaoqinActivity.class));
    }
}
