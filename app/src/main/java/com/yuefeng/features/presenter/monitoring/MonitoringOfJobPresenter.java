package com.yuefeng.features.presenter.monitoring;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.common.utils.StringUtils;
import com.yuefeng.features.contract.MonitoringOfJobContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.GetMonitoringPlanCountBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.monitoring.MonitoringofJobActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 作业监察
 */

public class MonitoringOfJobPresenter extends BasePresenterImpl<MonitoringOfJobContract.View,
        MonitoringofJobActivity> implements MonitoringOfJobContract.Presenter {

    private String hourStr;
    private String minuteStr;
    private String secondStr;
    private String lnglat;

    public MonitoringOfJobPresenter(MonitoringOfJobContract.View view, MonitoringofJobActivity activity) {
        super(view, activity);
    }


    /*信息采集*/
    @Override
    public void uploadJianCcha(String function, String userid, String pid, String timestart,
                               String timeend, String timesum, String lnglat,String startAddress,String endAddress) {
        HttpObservable.getObservable(apiRetrofit.uploadJianCcha(function, userid, pid, timestart,
                timeend, timesum, lnglat,startAddress,endAddress))
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("上传中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.MSGCOLECTION_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }
                });
    }

    @Override
    public void getJianChaCount(String function, String userid, String timestart, String timeend) {
        HttpObservable.getObservable(apiRetrofit.getJianChaCount(function, userid, timestart, timeend))
                .subscribe(new HttpResultObserver<GetMonitoringPlanCountBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetMonitoringPlanCountBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.CHANGEPWD_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.CHANGEPWD_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.CHANGEPWD_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.CHANGEPWD_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new PositionAcquisitionEvent(Constans.CHANGEPWD_ERROR, ""));
                    }
                });
    }


    /*结束展示时间*/
    @SuppressLint("SetTextI18n")
    public String showHowLongTime(String timeLong, String distance, int type) {
        String time = "";
        String hour = "";
        String miniteSecond = "";
        String typeWhat = "";
        if (!TextUtils.isEmpty(timeLong)) {
            int length = timeLong.length();
            if (length > 5) {//小时
                hourStr = timeLong.substring(0, 2);
                minuteStr = timeLong.substring(3, 5);
                secondStr = timeLong.substring(6, 8);
                hourStr = StringUtils.getTimeNoZero(hourStr);
                /* android:text="本次采集持续1分钟，距离0.1公里"*/
                hour = hourStr + "小时";
            } else {
                minuteStr = timeLong.substring(0, 2);
                secondStr = timeLong.substring(3, 5);
            }
            minuteStr = StringUtils.getTimeNoZero(minuteStr);
            secondStr = StringUtils.getTimeNoZero(secondStr);
            miniteSecond = minuteStr + "分" + secondStr + "秒,";
            if (type == 0) {
                if (distance.equals("NaN")) {
                    typeWhat = "无效采集数据";
                } else {
                    typeWhat = "面积约" + distance + "平方米";
                }
            } else {
                typeWhat = "距离" + distance + "公里";
            }

            time = "本次采集持续" + hour + miniteSecond + typeWhat;
        }
        return time;
    }


    public String getLnglatStr(List<LatLng> points) {
        lnglat = "";
        int size = points.size();
        for (int i = 0; i < size; i++) {
            LatLng latLng = points.get(i);
            if (i == 0) {
                lnglat = String.valueOf(latLng.longitude) + "," + latLng.latitude;
            } else {
//                if (i == (size - 1)) {
//                    lnglat = latLng.longitude + "," + latLng.latitude;
//                } else {
//                }
                lnglat = lnglat + ";" + latLng.longitude + "," + latLng.latitude;
            }
        }
        return lnglat;
    }
}
