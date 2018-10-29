package com.yuefeng.features.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.common.utils.StringUtils;
import com.yuefeng.features.contract.PositionAcquisitionContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.ui.activity.position.PositionAcquisitionActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 信息采集
 */

public class PositionAcquisitionPresenter extends BasePresenterImpl<PositionAcquisitionContract.View,
        PositionAcquisitionActivity> implements PositionAcquisitionContract.Presenter {

    private String hourStr;
    private String minuteStr;
    private String secondStr;

    public PositionAcquisitionPresenter(PositionAcquisitionContract.View view, PositionAcquisitionActivity activity) {
        super(view, activity);
    }


    /*信息采集*/
    @Override
    public void msgColection(String function, String pid, String userid, String type) {
        HttpObservable.getObservable(apiRetrofit.getEventquestion(function, pid, userid, type))
                .subscribe(new HttpResultObserver<EventQuestionBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(EventQuestionBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                int size = o.getMsg().size();
                                if (size == 0) {
                                    EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, o.getMsg()));
                                } else {
                                    EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_SSUCESS, o.getMsg()));
                                }
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

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    protected void _onNext(EventQuestionBean responseCustom) {
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
                        EventBus.getDefault().postSticky(new PositionAcquisitionEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }

                    @Override
                    public void onNext(EventQuestionBean eventQuestionBean) {
                        super.onNext(eventQuestionBean);
                    }
                });
    }


    /*结束展示时间*/
    @SuppressLint("SetTextI18n")
    public String showHowLongTime(String timeLong) {
        String time = "";
        String hour = "";
        String miniteSecond = "";
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
            time = "本次采集持续" + hour + miniteSecond + "距离0.1公里";
        }
        return time;
    }

}
