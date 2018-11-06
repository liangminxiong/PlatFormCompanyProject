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
import com.yuefeng.features.contract.PositionAcquisitionContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.modle.video.GetCaijiTypeBean;
import com.yuefeng.features.ui.activity.monitoring.MonitoringofJobActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 作业监察
 */

public class MonitoringOfJobPresenter extends BasePresenterImpl<MonitoringOfJobContract.View,
        MonitoringofJobActivity> implements PositionAcquisitionContract.Presenter {

    private String hourStr;
    private String minuteStr;
    private String secondStr;
    private String lnglat;

    public MonitoringOfJobPresenter(MonitoringOfJobContract.View view, MonitoringofJobActivity activity) {
        super(view, activity);
    }


    /*信息采集*/
    @Override
    public void upLoadmapInfo(String function, String pid, String userid, String typeid,
                              String typename, String name, String lnglat, String area, String imageArrays) {
        HttpObservable.getObservable(apiRetrofit.upLoadmapInfo(function, pid, userid, typeid,
                typename, name, lnglat, area, imageArrays))
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
                                EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }
                });
    }

    /*上传获取采集类型*/
    @Override
    public void getCaijiType(String function) {
        HttpObservable.getObservable(apiRetrofit.getCaijiType(function))
                .subscribe(new HttpResultObserver<GetCaijiTypeBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetCaijiTypeBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                int size = o.getMsg().size();
                                if (size == 0) {
                                    EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_ERROR, o.getMsg()));
                                } else {
                                    EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_SSUCESS, o.getMsg()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.GETCAIJI_ERROR, ""));
                    }

                    @Override
                    protected void _onNext(GetCaijiTypeBean responseCustom) {
                        super._onNext(responseCustom);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void onStart(Disposable d) {
                        super.onStart(d);
                        dismissLoadingDialog();
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
